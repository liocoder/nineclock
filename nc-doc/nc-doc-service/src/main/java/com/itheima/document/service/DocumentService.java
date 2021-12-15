package com.itheima.document.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.enums.ResponseEnum;
import com.itheima.common.exception.auth.NcException;
import com.itheima.common.threadlocals.UserHolder;
import com.itheima.common.util.BeanHelper;
import com.itheima.common.util.CountUtil;
import com.itheima.common.vo.PageResult;
import com.itheima.document.dto.CollaborationsDTO;
import com.itheima.document.dto.DocumentDTO;
import com.itheima.document.dto.UserCollaborationDTO;
import com.itheima.document.entity.Collaborations;
import com.itheima.document.entity.File;
import com.itheima.document.entity.Folder;
import com.itheima.document.enums.DocumentEnum;
import com.itheima.sys.client.SysClient;
import com.itheima.sys.dto.CompanyUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.itheima.document.enums.DocumentEnum.FILE_STATUS_PASS;
import static com.itheima.document.enums.DocumentEnum.FILE_STATUS_WAIT_APPROVE;
import static com.itheima.document.enums.DocumentEnum.PRIVATE_READ_WRITE;

@Service
public class DocumentService {

    @Autowired
    private IFileService fileService;

    @Autowired
    private IFolderService folderService;

    @Autowired
    private ICollaborationsService collaborationsService;

    @Autowired
    private IFileHistoryService fileHistoryService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private SysClient sysClient;

    /**
     * 创建文件夹
     */
    public void insertFolder(Folder folder) {
        //1. 判断同级是否存在重复文件夹
        LambdaQueryWrapper<Folder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Folder::getName, folder.getName());
        wrapper.eq(Folder::getParentId, folder.getParentId());
        int count = folderService.count(wrapper);
        if (count > 0) {
            throw new NcException(400, "该文件夹已存在");
        }
        folder.setCompanyId(UserHolder.getCompanyId());
        folder.setOrder(0);
        folder.setCompanyUserId(UserHolder.getUserId());
        folder.setCreateTime(LocalDateTime.now());
        folder.setStatus(1);
        folderService.save(folder);
    }

    /**
     * 新建文档
     */
    public void insertFile(File file) {
        System.out.println("主线程处理："+Thread.currentThread());
        //设置文档其他属性
        file.setCompanyUserId(UserHolder.getUserId());
        file.setFileSize(CountUtil.getSize(file.getContent().length()));
        file.setAuthority(PRIVATE_READ_WRITE.getVal());
        file.setUpdateUserId(UserHolder.getUserId());
        file.setCompanyId(UserHolder.getCompanyId());
        file.setType(1);
        file.setStatus(FILE_STATUS_WAIT_APPROVE.getVal());
        fileService.save(file);

        //记录编辑历史
        fileHistoryService.saveRecord(file);
    }

    /**
     * 查询文件下的文件夹和文档
     * @param parentFoldId  当前文件夹id
     * @return DocumentDTO
     */
    public DocumentDTO queryAllFolderAndFile(Long parentFoldId) {
        DocumentDTO documentDTO = new DocumentDTO();
        //1. 查询上级文件下的子文件夹
        documentDTO.setFolders(queryFoldersByParentId(parentFoldId));
        //2. 查询上级文件下的用户可见的文件
        documentDTO.setFiles(queryFilesByParentId(parentFoldId));
        return documentDTO;
    }

    //查询当前用户可见的文档列表(大前提：当前企业，文档状态未封禁的，审核通过的)
    private List<File> queryFilesByParentId(Long parentFoldId) {
        List<File> allFileList = new ArrayList<>();
        //1.当前企业文档类型为公共读，公共读写
        List<File> readOrWriteFileList = queryFileByAuthority(parentFoldId,
                DocumentEnum.ALL_READ_WRITE.getVal(), DocumentEnum.ALL_READ.getVal());
        //2. 当前用户是作者的文档列表
        List<File> authorFileList = queryFileByAuthor(parentFoldId);
        //3. 查询当前用户是协作者
        List<File> collocationFileList = queryFileByCollocation(parentFoldId);

        if (!CollectionUtils.isEmpty(readOrWriteFileList)) {
            allFileList.addAll(readOrWriteFileList);
        }
        if (!CollectionUtils.isEmpty(authorFileList)) {
            allFileList.addAll(authorFileList);
        }
        if (!CollectionUtils.isEmpty(collocationFileList)) {
            allFileList.addAll(collocationFileList);
        }

        //4. 将重复文档去重
        allFileList = allFileList.stream().distinct().collect(Collectors.toList());
        return allFileList;

    }

    //当前用户作为协作者
    private List<File> queryFileByCollocation(Long parentFoldId) {
        LambdaQueryWrapper<Collaborations> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collaborations::getCompanyUserId, UserHolder.getUserId());
        List<Collaborations> collaborations = collaborationsService.list(wrapper);
        if (CollectionUtils.isEmpty(collaborations)) {
            return null;
        }
        List<Long> fileIds = collaborations.stream().map(Collaborations::getFileId).collect(Collectors.toList());
        LambdaQueryWrapper<File> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(File::getId, fileIds);
        wrapper1.eq(File::getEnable, 1);
        wrapper1.eq(File::getFolderId, parentFoldId);
        return fileService.list(wrapper1);
    }

    //第一作者
    private List<File> queryFileByAuthor(Long parentFoldId) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(File::getCompanyUserId, UserHolder.getUserId());
        wrapper.eq(File::getFolderId, parentFoldId);
        wrapper.eq(File::getEnable, true);
        return fileService.list(wrapper);
    }

    //公司公共读，公共读写文档
    private List<File> queryFileByAuthority(Long parentFoldId, Integer ... authority) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(File::getCompanyId, UserHolder.getCompanyId());
        wrapper.eq(File::getEnable, true);
        wrapper.eq(File::getFolderId, parentFoldId);
        wrapper.in(File::getAuthority, authority);
        return fileService.list(wrapper);
    }

    private List<Folder> queryFoldersByParentId(Long parentFoldId) {
        LambdaQueryWrapper<Folder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Folder::getParentId, parentFoldId);
        wrapper.eq(Folder::getStatus, true);
        wrapper.eq(Folder::getCompanyId, UserHolder.getCompanyId());
        wrapper.select(Folder::getId, Folder::getName);
        return folderService.list(wrapper);
    }

    /**
     * 通过id获取文档
     * @param id
     * @return
     */
    public File queryFileById(Long id) {
        File File = fileService.getById(id);
        if(File==null){
            throw new NcException(ResponseEnum.DOC_NOT_FOUND);
        }
        return File;
    }

    /**
     * 根据文档id查询协作者
     * @param page
     * @param pageSize
     * @param fileId    文档id
     * @return
     */
    public PageResult<UserCollaborationDTO> pagingCollaborations(Integer page, Integer pageSize, Long fileId) {
        //根据文档查询协作者表
        LambdaQueryWrapper<Collaborations> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collaborations::getFileId, fileId);
        List<Long> userIds = collaborationsService.list(wrapper).stream().map(Collaborations::getCompanyUserId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIds)) {
            return new PageResult<>(0L, 0L, null);
        }
        List<CompanyUserDTO> data = sysClient.queryCompanyUserByIds(userIds).getData();
        List<UserCollaborationDTO> userCollaborations = BeanHelper.copyWithCollection(data, UserCollaborationDTO.class);
        if (CollectionUtils.isEmpty(userCollaborations)) {
            return new PageResult<>(0L, 0L, null);
        }
        long total = userCollaborations.size();
        long totalPage = 0;
        List<UserCollaborationDTO> records;
        if (total < pageSize) {
            page = 1;
            totalPage = 1;
            records = userCollaborations;
        } else {
            totalPage = total / pageSize - 1;
            int start = (page - 1) * pageSize;
            int end = start + pageSize;
            records = userCollaborations.stream().skip(start).limit(10).collect(Collectors.toList());
        }

        return new PageResult<>(1L,10L, userCollaborations);
    }

    public List<UserCollaborationDTO> pagingCollaborationUsers(Long fileId) {
        //1.远程调用系统微服务 获取当前企业所有的员工
        List<CompanyUserDTO> companyUserDTOList = sysClient.queryAllCompanyUser().getData();
        //2. 根据文档id查询文档 -得到创建者用户id
        File file = fileService.getById(fileId);
        //3. 根据文档id获取协作者id
        LambdaQueryWrapper<Collaborations> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collaborations::getFileId, fileId);
        List<Collaborations> collaborationsList = collaborationsService.list(wrapper);
        List<Long> collaborationsUserIds = collaborationsList.stream().map(Collaborations::getCompanyUserId).collect(Collectors.toList());
        //4. 遍历员工表 判断员工类型
        List<UserCollaborationDTO> userCollaborationDTOS = BeanHelper.copyWithCollection(companyUserDTOList, UserCollaborationDTO.class);
        for (UserCollaborationDTO userCollaborationDTO : userCollaborationDTOS) {
            Integer status = 0;
            boolean equals = file.getCompanyUserId().equals(userCollaborationDTO.getId());
            //判断是作者还是其他人
            status = equals ? DocumentEnum.USER_TYPE_AUTHOR.getVal() : DocumentEnum.USER_TYPE_NONE.getVal();
            //判断是否是协作者
            if (collaborationsUserIds.contains(userCollaborationDTO.getId())) {
                status = DocumentEnum.USER_TYPE_COLLABORATION.getVal();
            }
            userCollaborationDTO.setState(status);
        }
        return userCollaborationDTOS;
    }

    /**
     * 新增协作者
     * @param collaborationsDTO
     */
    public void insertCollaboration(CollaborationsDTO collaborationsDTO) {
        //1.只有当前文档作者 才能新增协作者（判断当前文档是否属于当前登录用户）
        this.validDoc(collaborationsDTO);
        //2.新增文档协作者记录
        Collaborations collaborations = BeanHelper.copyProperties(collaborationsDTO, Collaborations.class);
        //TODO 前端提交员工id参数名称 userId
        collaborations.setCompanyUserId(collaborationsDTO.getUserId());
        boolean b = collaborationsService.save(collaborations);
        if(!b){
            throw new NcException(ResponseEnum.INSERT_OPERATION_FAIL);
        }
    }

    private void validDoc(CollaborationsDTO collaborationsDTO) {
        //1.只有当前文档作者 才能新增协作者（判断当前文档是否属于当前登录用户）
        //1.1 获取文档对象
        File file = fileService.getById(collaborationsDTO.getFileId());
        if(file==null){
            throw new NcException(ResponseEnum.DOC_NOT_FOUND);
        }
        //1.2判断文档状态
        if(file.getEnable() != 1){
            throw new NcException(ResponseEnum.DOC_NOT_FOUND);
        }
        //1.2 判断文档拥有者
        Long companyUserId = UserHolder.getUserId();
        if(!file.getCompanyUserId().equals(companyUserId)){
            throw new NcException(ResponseEnum.DOC_NOT_ALLOWED);
        }
    }

    /**
     * 删除协作者
     * @param collaborationsDTO
     */
    public void deleteCollaboration(CollaborationsDTO collaborationsDTO) {
        this.validDoc(collaborationsDTO);
        QueryWrapper<Collaborations> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Collaborations::getCompanyUserId, collaborationsDTO.getUserId());
        queryWrapper.lambda().eq(Collaborations::getFileId, collaborationsDTO.getFileId());
        boolean b = collaborationsService.remove(queryWrapper);
        if(!b){
            throw new NcException(ResponseEnum.DELETE_OPERATION_FAIL);
        }
    }

    /**
     * 查询所有企业可读文档
     * @param page
     * @param pageSize
     * @return
     */
    public PageResult<File> queryByPage(Integer page, Integer pageSize) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(File::getStatus, FILE_STATUS_PASS.getVal());
        wrapper.eq(File::getEnable, true);
        wrapper.in(File::getAuthority, DocumentEnum.ALL_READ.getVal(), DocumentEnum.ALL_READ_WRITE.getVal());
        IPage<File> iPage = new Page<>(page, pageSize);
        IPage<File> fileIPage = fileService.page(iPage, wrapper);
        return new PageResult<>(fileIPage.getTotal(), fileIPage.getPages(), fileIPage.getRecords());
    }
}
