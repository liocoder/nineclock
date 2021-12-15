package com.itheima.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.sys.dto.CompanyUserDTO;
import com.itheima.sys.entity.CompanyUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 企业员工表  Mapper 接口
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
public interface CompanyUserMapper extends BaseMapper<CompanyUser> {

    @Select("SELECT DISTINCT su.* FROM `sys_company_user` su INNER JOIN " +
            "(SELECT sr.id FROM `sys_role` sr WHERE role_name LIKE #{roleName} AND company_id = #{companyId}) " +
            "temp ON FIND_IN_SET(temp.id, su.role_ids)")
    public List<CompanyUserDTO> queryAdmins(@Param("roleName") String roleName, @Param("companyId") Long companyId);

    @Update("<script>update sys_company_user scu set <if test='plusFlag==true'>scu.integral = scu.integral + #{integral}</if>" +
            "<if test='plusFlag==false'>scu.integral = scu.integral - #{integral}</if> where scu.id = #{userId}</script>")
    void updateIntegral(@Param("userId") Long userId, @Param("plusFlag") Boolean plusFlag, @Param("integral") Integer integral);
}
