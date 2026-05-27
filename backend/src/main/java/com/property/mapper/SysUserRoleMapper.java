package com.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.property.entity.SysUserRole;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Delete("DELETE FROM sys_user_role WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);

    @Insert("INSERT INTO sys_user_role (user_id, role_id) VALUES (#{userId}, #{roleId})")
    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
