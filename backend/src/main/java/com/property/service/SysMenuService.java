package com.property.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.property.entity.SysMenu;
import com.property.mapper.SysMenuMapper;
import com.property.mapper.SysRoleMenuMapper;
import com.property.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    public List<SysMenu> getMenuTreeByUserId(Long userId) {
        List<Long> roleIds = sysUserMapper.selectRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<SysMenu> allMenus = new ArrayList<>();
        for (Long roleId : roleIds) {
            allMenus.addAll(sysMenuMapper.selectMenusByRoleId(roleId));
        }
        allMenus = allMenus.stream().distinct().collect(Collectors.toList());
        return buildTree(allMenus, 0L);
    }

    public List<SysMenu> getAllMenuTree() {
        List<SysMenu> all = sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSortOrder));
        return buildTree(all, 0L);
    }

    public List<SysMenu> getAllMenuList() {
        return sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getSortOrder));
    }

    private List<SysMenu> buildTree(List<SysMenu> menus, Long parentId) {
        return menus.stream()
                .filter(m -> parentId.equals(m.getParentId()))
                .peek(m -> m.setChildren(buildTree(menus, m.getId())))
                .collect(Collectors.toList());
    }

    public void saveRoleMenus(Long roleId, List<Long> menuIds) {
        sysRoleMenuMapper.deleteByRoleId(roleId);
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                sysRoleMenuMapper.insertRoleMenu(roleId, menuId);
            }
        }
    }

    public List<Long> getRoleMenuIds(Long roleId) {
        return sysRoleMenuMapper.selectMenuIdsByRoleId(roleId);
    }
}
