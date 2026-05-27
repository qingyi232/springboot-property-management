package com.property.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.FeePower;
import com.property.mapper.FeePowerMapper;
import com.property.security.LoginUser;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/fee/power")
public class FeePowerController {

    @Autowired
    private FeePowerMapper feePowerMapper;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('fee:power:list')")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) Integer status,
                          @RequestParam(required = false) String feeMonth) {
        Page<FeePower> page = new Page<>(current, size);
        return Result.ok(feePowerMapper.selectPageWithDetail(page, null, status, feeMonth));
    }

    @GetMapping("/my")
    public Result<?> myList(@RequestParam(defaultValue = "1") Integer current,
                            @RequestParam(defaultValue = "10") Integer size,
                            @RequestParam(required = false) Integer status) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<FeePower> page = new Page<>(current, size);
        return Result.ok(feePowerMapper.selectPageWithDetail(page, loginUser.getUserId(), status, null));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('fee:power:add')")
    public Result<?> add(@RequestBody FeePower fee) {
        feePowerMapper.insert(fee);
        return Result.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('fee:power:edit')")
    public Result<?> update(@RequestBody FeePower fee) {
        feePowerMapper.updateById(fee);
        return Result.ok();
    }

    @PutMapping("/pay/{id}")
    public Result<?> pay(@PathVariable Long id) {
        FeePower fee = feePowerMapper.selectById(id);
        if (fee == null) return Result.fail("记录不存在");
        if (fee.getStatus() == 1) return Result.fail("已缴费，无需重复操作");
        fee.setStatus(1);
        fee.setPayTime(LocalDateTime.now());
        feePowerMapper.updateById(fee);
        return Result.ok("缴费成功");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('fee:power:delete')")
    public Result<?> delete(@PathVariable Long id) {
        FeePower fee = feePowerMapper.selectById(id);
        if (fee != null && fee.getStatus() == 0) {
            return Result.fail("未缴费的记录不能删除");
        }
        feePowerMapper.deleteById(id);
        return Result.ok();
    }
}
