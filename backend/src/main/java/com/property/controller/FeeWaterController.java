package com.property.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.FeeWater;
import com.property.mapper.FeeWaterMapper;
import com.property.security.LoginUser;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/fee/water")
public class FeeWaterController {

    @Autowired
    private FeeWaterMapper feeWaterMapper;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('fee:water:list')")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) Integer status,
                          @RequestParam(required = false) String feeMonth) {
        Page<FeeWater> page = new Page<>(current, size);
        return Result.ok(feeWaterMapper.selectPageWithDetail(page, null, status, feeMonth));
    }

    @GetMapping("/my")
    public Result<?> myList(@RequestParam(defaultValue = "1") Integer current,
                            @RequestParam(defaultValue = "10") Integer size,
                            @RequestParam(required = false) Integer status) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<FeeWater> page = new Page<>(current, size);
        return Result.ok(feeWaterMapper.selectPageWithDetail(page, loginUser.getUserId(), status, null));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('fee:water:add')")
    public Result<?> add(@RequestBody FeeWater fee) {
        feeWaterMapper.insert(fee);
        return Result.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('fee:water:edit')")
    public Result<?> update(@RequestBody FeeWater fee) {
        feeWaterMapper.updateById(fee);
        return Result.ok();
    }

    @PutMapping("/pay/{id}")
    public Result<?> pay(@PathVariable Long id) {
        FeeWater fee = feeWaterMapper.selectById(id);
        if (fee == null) return Result.fail("记录不存在");
        if (fee.getStatus() == 1) return Result.fail("已缴费，无需重复操作");
        fee.setStatus(1);
        fee.setPayTime(LocalDateTime.now());
        feeWaterMapper.updateById(fee);
        return Result.ok("缴费成功");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('fee:water:delete')")
    public Result<?> delete(@PathVariable Long id) {
        FeeWater fee = feeWaterMapper.selectById(id);
        if (fee != null && fee.getStatus() == 0) {
            return Result.fail("未缴费的记录不能删除");
        }
        feeWaterMapper.deleteById(id);
        return Result.ok();
    }
}
