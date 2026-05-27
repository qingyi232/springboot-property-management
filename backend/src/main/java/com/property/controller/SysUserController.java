package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.dto.UserRoleDto;
import com.property.entity.SysUser;
import com.property.mapper.SysUserMapper;
import com.property.mapper.SysUserRoleMapper;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sysuser")
public class SysUserController {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) String realName,
                          @RequestParam(required = false) String phone) {
        Page<SysUser> page = new Page<>(current, size);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(realName)) {
            wrapper.like(SysUser::getRealName, realName);
        }
        if (StringUtils.hasText(phone)) {
            wrapper.like(SysUser::getPhone, phone);
        }
        wrapper.orderByDesc(SysUser::getId);
        Page<SysUser> result = sysUserMapper.selectPage(page, wrapper);
        result.getRecords().forEach(u -> {
            u.setPassword(null);
            List<Long> roleIds = sysUserMapper.selectRoleIdsByUserId(u.getId());
            u.setRoles(null);
            u.setPermissions(null);
        });
        return Result.ok(result);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:add')")
    public Result<?> add(@RequestBody SysUser user) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, user.getUsername());
        if (sysUserMapper.selectCount(wrapper) > 0) {
            return Result.fail("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        sysUserMapper.insert(user);
        return Result.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('sys:user:edit')")
    public Result<?> update(@RequestBody SysUser user) {
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        sysUserMapper.updateById(user);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result<?> delete(@PathVariable Long id) {
        sysUserMapper.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/roles/{userId}")
    public Result<?> getUserRoles(@PathVariable Long userId) {
        return Result.ok(sysUserMapper.selectRoleIdsByUserId(userId));
    }

    @PostMapping("/role")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    public Result<?> assignRoles(@RequestBody UserRoleDto dto) {
        sysUserRoleMapper.deleteByUserId(dto.getUserId());
        if (dto.getRoleIds() != null) {
            for (Long roleId : dto.getRoleIds()) {
                sysUserRoleMapper.insertUserRole(dto.getUserId(), roleId);
            }
        }
        return Result.ok();
    }

    @PutMapping("/resetPwd/{id}")
    @PreAuthorize("hasAuthority('sys:user:edit')")
    public Result<?> resetPassword(@PathVariable Long id) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(passwordEncoder.encode("123456"));
        sysUserMapper.updateById(user);
        return Result.ok("密码已重置为123456");
    }
}
