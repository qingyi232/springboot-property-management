package com.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.property.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("SELECT DISTINCT m.permission FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.permission IS NOT NULL AND m.permission != ''")
    List<String> selectPermissionsByUserId(Long userId);

    @Select("SELECT r.id FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<Long> selectRoleIdsByUserId(Long userId);
}
