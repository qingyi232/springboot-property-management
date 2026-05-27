package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.dto.RoleMenuDto;
import com.property.entity.SysMenu;
import com.property.entity.SysRole;
import com.property.mapper.SysRoleMapper;
import com.property.service.SysMenuService;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class SysRoleController {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) String roleName) {
        Page<SysRole> page = new Page<>(current, size);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(roleName)) {
            wrapper.like(SysRole::getRoleName, roleName);
        }
        wrapper.orderByDesc(SysRole::getId);
        return Result.ok(sysRoleMapper.selectPage(page, wrapper));
    }

    @GetMapping("/all")
    public Result<?> all() {
        return Result.ok(sysRoleMapper.selectList(null));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('sys:role:add')")
    public Result<?> add(@RequestBody SysRole role) {
        sysRoleMapper.insert(role);
        return Result.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('sys:role:edit')")
    public Result<?> update(@RequestBody SysRole role) {
        sysRoleMapper.updateById(role);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Result<?> delete(@PathVariable Long id) {
        sysRoleMapper.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/menu/tree")
    public Result<?> menuTree() {
        List<SysMenu> tree = sysMenuService.getAllMenuTree();
        return Result.ok(tree);
    }

    @GetMapping("/menu/{roleId}")
    public Result<?> getRoleMenus(@PathVariable Long roleId) {
        return Result.ok(sysMenuService.getRoleMenuIds(roleId));
    }

    @PostMapping("/menu")
    @PreAuthorize("hasAuthority('sys:role:edit')")
    public Result<?> assignMenus(@RequestBody RoleMenuDto dto) {
        sysMenuService.saveRoleMenus(dto.getRoleId(), dto.getMenuIds());
        return Result.ok();
    }
}
