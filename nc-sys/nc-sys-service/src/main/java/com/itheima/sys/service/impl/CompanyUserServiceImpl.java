package com.itheima.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.constants.Constant;
import com.itheima.common.constants.NotifyMessageConstant;
import com.itheima.common.constants.RocketMQConstants;
import com.itheima.common.enums.ResponseEnum;
import com.itheima.common.exception.auth.NcException;
import com.itheima.common.threadlocals.UserHolder;
import com.itheima.common.util.BeanHelper;
import com.itheima.common.util.JsonUtils;
import com.itheima.notify.client.NotifyMsgClient;
import com.itheima.notify.dto.NotifyMessage;
import com.itheima.sys.dto.CompanyUserAdminDTO;
import com.itheima.sys.dto.CompanyUserDTO;
import com.itheima.sys.dto.UserJoinCompanyDTO;
import com.itheima.sys.entity.CompanyUser;
import com.itheima.sys.entity.Function;
import com.itheima.sys.entity.Role;
import com.itheima.sys.mapper.CompanyUserMapper;
import com.itheima.sys.service.ICompanyUserService;
import com.itheima.sys.service.IFunctionService;
import com.itheima.sys.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 企业员工表  服务实现类
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
@Slf4j
@Service
public class CompanyUserServiceImpl extends ServiceImpl<CompanyUserMapper, CompanyUser> implements ICompanyUserService {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IFunctionService functionService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private NotifyMsgClient notifyMsgClient;

    @Override
    public CompanyUserDTO querySysUser(String username) {
        LambdaQueryWrapper<CompanyUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CompanyUser::getMobile, username);
        CompanyUser companyUser = this.getOne(wrapper);
        CompanyUserDTO companyUserDTO = BeanHelper.copyProperties(companyUser, CompanyUserDTO.class);
        //入职时间转换
        /*ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = companyUser.getTimeEntry().atZone(zoneId);
        companyUserDTO.setTimeEntry(Date.from(zdt.toInstant()));*/

        //查询用户角色
        String roleIdsStr = companyUser.getRoleIds();
        if (StringUtils.isNotBlank(roleIdsStr)) {
            String[] roleIds = roleIdsStr.split(",");
            Collection<Role> roles = roleService.listByIds(Arrays.asList(roleIds));
            companyUserDTO.setSysRoles((List<Role>) roles);
            //查询角色包含的权限集合
            List<Function> allFunction = new ArrayList<>();
            for (Role role : roles) {
                List<Function> functionList = functionService.findFunctionByRole(role);
                if (!CollectionUtils.isEmpty(functionList)) {
                    allFunction.addAll(functionList);
                }
            }
            allFunction = allFunction.stream().distinct().collect(Collectors.toList());
            companyUserDTO.setSysFunctions(allFunction);
        }
        return companyUserDTO;
    }

    @Override
    public Boolean checkMobile(String mobile) {
        LambdaQueryWrapper<CompanyUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CompanyUser::getMobile, mobile).last("LIMIT 1");
        return this.getOne(wrapper) != null;
    }

    @Override
    public List<CompanyUserDTO> findCompanyAdmins() {
        Long companyId = UserHolder.getCompanyId();
        //查询企业管理员
        List<CompanyUserDTO> companyUserDTOList = getBaseMapper().queryAdmins("ROLE_ADMIN%", companyId);
        for (CompanyUserDTO companyUserDTO : companyUserDTOList) {
            String roleIds = companyUserDTO.getRoleIds();
            if (StringUtils.isNotBlank(roleIds)) {
                String[] split = roleIds.split(",");
                List<String> roleIdList = Arrays.asList(split);
                Collection<Role> roles = roleService.listByIds(roleIdList);
                companyUserDTO.setSysRoles((List<Role>) roles);
            }
        }
        return companyUserDTOList;
    }

    @Override
    public void subAdmin(CompanyUserAdminDTO companyUserAdminDTO) {
        //根据用户id查询用户
        CompanyUser companyUser = this.getById(companyUserAdminDTO.getUserId());
        List<String> roleIds = companyUserAdminDTO.getRoleIds();
        if (!CollectionUtils.isEmpty(roleIds)) {
            companyUser.setRoleIds(org.apache.commons.lang3.StringUtils.join(roleIds,","));
            //根据角色id查询角色列表
            Collection<Role> roles = roleService.listByIds(roleIds);
            String collect = roles.stream().map(Role::getRoleDesc).collect(Collectors.joining(","));
            companyUser.setRoleDesc(collect);
            //根据主键更新数据库
            this.updateById(companyUser);
        }
    }

    @Override
    public Long register(CompanyUser companyUser, String checkcode) {
        //1. 判断验证码是否存在
        String jsonCode = redisTemplate.opsForValue().get(Constant.SMS_REGISTER_KEY_PREFIX + companyUser.getMobile());
        String realCheckCode = (String) JsonUtils.readJsonToMap(jsonCode).get("code");
        if (!checkcode.equals(realCheckCode)) {
            throw new NcException(ResponseEnum.CODE_IMAGE_ERROR);
        }
        //2. 校验手机号是否存在
        Boolean flag = checkMobile(companyUser.getMobile());
        if (flag) {
            throw new NcException(ResponseEnum.USER_MOBILE_EXISTS);
        }
        //3. 设置用户相关属性，用户状态：未认证，密码加密
        companyUser.setEnable(false);
        companyUser.setPassword(passwordEncoder.encode(companyUser.getPassword()));
        //4. 保存数据
        this.save(companyUser);
        //5. 清除redis中的code
        redisTemplate.delete(Constant.SMS_REGISTER_KEY_PREFIX + companyUser.getMobile());
        log.info("用户注册成功，手机号：{}", companyUser.getMobile());
        return companyUser.getId();
    }

    @Override
    public void applyJoinCompany(UserJoinCompanyDTO userJoinCompanyDTO) {
        //1. 判断企业中是否包含该用户
        CompanyUser companyUser = this.getById(userJoinCompanyDTO.getUserId());
        if (companyUser.getEnable()) {
            throw new NcException(400, "您已加入企业！");
        }
        //2. 将入参中的信息更新到用户表，再设置认证状态为false
        companyUser.setEnable(false);
        companyUser.setCompanyId(userJoinCompanyDTO.getCompanyId());
        companyUser.setCompanyName(userJoinCompanyDTO.getCompanyName());
        companyUser.setEmail(userJoinCompanyDTO.getEmail());
        companyUser.setPost(userJoinCompanyDTO.getPost());
        companyUser.setUsername(userJoinCompanyDTO.getUserName());
        //3. 更新用户表
        this.updateById(companyUser);

        //4. 给目标企业管理原推送消息-生产者
        //4.1 根据企业id查询企业管理员，默认只有一个管理员
        List<CompanyUserDTO> companyUserDTOList = this.getBaseMapper().queryAdmins("ROLE_ADMIN_SYS", userJoinCompanyDTO.getCompanyId());
        if (CollectionUtils.isEmpty(companyUserDTOList)) {
            throw new NcException(ResponseEnum.COMPANY_ADMIN_NOT_EXISTS);
        }
        CompanyUserDTO companyAdmin = companyUserDTOList.get(0);
        //4.2 给指定管理员推送消息
        NotifyMessage notifyMessage = new NotifyMessage();
        //4.2.1 设置发送方(用户)
        notifyMessage.setApplyUserId(companyUser.getId());
        notifyMessage.setApplyUsername(companyUser.getUsername());
        //4.2.2 设置接收者信息(企业管理员)
        notifyMessage.setApproveUserId(companyAdmin.getId());
        notifyMessage.setApproveUsername(companyAdmin.getUsername());
        //4.2.4 设置消息内容
        notifyMessage.setTitle("有新员工申请加入企业");
        notifyMessage.setContent(userJoinCompanyDTO.getUserName() + " 申请加入：" + userJoinCompanyDTO.getCompanyName() + ",请及时审批！备注:" + userJoinCompanyDTO.getApplyReason());
        notifyMessage.setTargets(Arrays.asList(companyAdmin.getMobile()));
        notifyMessage.setCreateTime(System.currentTimeMillis());
        notifyMessage.setCompanyId(userJoinCompanyDTO.getCompanyId());
        notifyMessage.setMessageType(NotifyMessageConstant.COMPANY_APPLY);
        rocketMQTemplate.convertAndSend(RocketMQConstants.TOPIC.PUSH_TOPIC_NAME + ":" + RocketMQConstants.TAGS.USER_APPLY_TAGS, notifyMessage);
        log.info("{}，申请加入企业。{}", userJoinCompanyDTO.getUserName(), notifyMessage.toString());
    }

    /**
     * 是否同意加入企业
     *
     * @param applyUserId 申请者用户ID
     * @param approved    是否同意
     * @param remark      备注
     * @param notifyMsgId 推送记录ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allowedJonCompany(Long applyUserId, Boolean approved, String remark, String notifyMsgId) {
        //1. 根据用户的id查询用户的信息，更新状态
        CompanyUser companyUser = this.getById(applyUserId);
        if (companyUser.getEnable()) {
            throw new NcException(400, "该成员已认证通过");
        }
        if (approved) {
            //同意
            companyUser.setEnable(true);
            updateById(companyUser);
        } else {
            //不同意
            companyUser.setCompanyId(null);
            companyUser.setCompanyName(null);
            this.updateById(companyUser);
        }
        //远程调用推送微服务更新推送文档状态
        Integer status = approved ? 2 : 3;
        notifyMsgClient.updateNotifyStatus(notifyMsgId, status);
    }

    @Override
    public List<CompanyUserDTO> queryUsers(List<Long> ids) {
        LambdaQueryWrapper<CompanyUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CompanyUser::getId, ids);
        wrapper.select(CompanyUser::getId, CompanyUser::getUsername, CompanyUser::getEnable, CompanyUser::getCompanyId);
        List<CompanyUser> companyUsers = this.list(wrapper);
        return BeanHelper.copyWithCollection(companyUsers, CompanyUserDTO.class);
    }

    @Override
    public List<CompanyUserDTO> queryAllCompanyUser() {
        LambdaQueryWrapper<CompanyUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CompanyUser::getCompanyId, UserHolder.getCompanyId());
        wrapper.eq(CompanyUser::getEnable, true);
        wrapper.select(CompanyUser::getId, CompanyUser::getUsername, CompanyUser::getEnable, CompanyUser::getCompanyId);
        List<CompanyUser> companyUsers = this.list(wrapper);
        if(companyUsers.isEmpty()){
            throw new NcException(ResponseEnum.USER_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(companyUsers, CompanyUserDTO.class);
    }

    @Override
    public void updateIntegral(Long userId, Boolean plusFlag, Integer integral) {
        this.getBaseMapper().updateIntegral(userId, plusFlag, integral);
    }
}
