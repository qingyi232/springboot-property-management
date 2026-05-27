package com.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.property.entity.SysRoleMenu;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    @Delete("DELETE FROM sys_role_menu WHERE role_id = #{roleId}")
    int deleteByRoleId(Long roleId);

    @Select("SELECT menu_id FROM sys_role_menu WHERE role_id = #{roleId}")
    List<Long> selectMenuIdsByRoleId(Long roleId);

    @Insert("INSERT INTO sys_role_menu (role_id, menu_id) VALUES (#{roleId}, #{menuId})")
    int insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);
}
