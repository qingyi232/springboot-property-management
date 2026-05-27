package com.property.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.UserComplaint;
import com.property.mapper.UserComplaintMapper;
import com.property.security.LoginUser;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/complaint")
public class UserComplaintController {

    @Autowired
    private UserComplaintMapper complaintMapper;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('feedback:complaint:list')")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) Integer status) {
        Page<UserComplaint> page = new Page<>(current, size);
        return Result.ok(complaintMapper.selectPageWithUser(page, null, status));
    }

    @GetMapping("/my")
    public Result<?> myList(@RequestParam(defaultValue = "1") Integer current,
                            @RequestParam(defaultValue = "10") Integer size) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<UserComplaint> page = new Page<>(current, size);
        return Result.ok(complaintMapper.selectPageWithUser(page, loginUser.getUserId(), null));
    }

    @PostMapping
    public Result<?> add(@RequestBody UserComplaint complaint) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        complaint.setUserId(loginUser.getUserId());
        complaint.setStatus(0);
        complaintMapper.insert(complaint);
        return Result.ok();
    }

    @PutMapping("/handle")
    @PreAuthorize("hasAuthority('feedback:complaint:handle')")
    public Result<?> handle(@RequestBody UserComplaint complaint) {
        UserComplaint update = new UserComplaint();
        update.setId(complaint.getId());
        update.setStatus(1);
        update.setReply(complaint.getReply());
        complaintMapper.updateById(update);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('feedback:complaint:delete')")
    public Result<?> delete(@PathVariable Long id) {
        complaintMapper.deleteById(id);
        return Result.ok();
    }
}
