package com.property.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.SysNotice;
import com.property.mapper.SysNoticeMapper;
import com.property.security.LoginUser;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notice")
public class SysNoticeController {

    @Autowired
    private SysNoticeMapper noticeMapper;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) String title) {
        Page<SysNotice> page = new Page<>(current, size);
        return Result.ok(noticeMapper.selectPageWithCreator(page, title));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('sys:notice:add')")
    public Result<?> add(@RequestBody SysNotice notice) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notice.setCreatorId(loginUser.getUserId());
        noticeMapper.insert(notice);
        return Result.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('sys:notice:edit')")
    public Result<?> update(@RequestBody SysNotice notice) {
        noticeMapper.updateById(notice);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:notice:delete')")
    public Result<?> delete(@PathVariable Long id) {
        noticeMapper.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.ok(noticeMapper.selectById(id));
    }
}
