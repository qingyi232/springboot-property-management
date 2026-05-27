package com.property.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.UserRepair;
import com.property.mapper.UserRepairMapper;
import com.property.security.LoginUser;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repair")
public class UserRepairController {

    @Autowired
    private UserRepairMapper repairMapper;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('feedback:repair:list')")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) Integer status) {
        Page<UserRepair> page = new Page<>(current, size);
        return Result.ok(repairMapper.selectPageWithUser(page, null, status));
    }

    @GetMapping("/my")
    public Result<?> myList(@RequestParam(defaultValue = "1") Integer current,
                            @RequestParam(defaultValue = "10") Integer size) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<UserRepair> page = new Page<>(current, size);
        return Result.ok(repairMapper.selectPageWithUser(page, loginUser.getUserId(), null));
    }

    @PostMapping
    public Result<?> add(@RequestBody UserRepair repair) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        repair.setUserId(loginUser.getUserId());
        repair.setStatus(0);
        repairMapper.insert(repair);
        return Result.ok();
    }

    @PutMapping("/handle")
    @PreAuthorize("hasAuthority('feedback:repair:handle')")
    public Result<?> handle(@RequestBody UserRepair repair) {
        UserRepair update = new UserRepair();
        update.setId(repair.getId());
        update.setStatus(repair.getStatus());
        update.setReply(repair.getReply());
        repairMapper.updateById(update);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('feedback:repair:delete')")
    public Result<?> delete(@PathVariable Long id) {
        repairMapper.deleteById(id);
        return Result.ok();
    }
}
