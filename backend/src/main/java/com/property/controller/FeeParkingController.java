package com.property.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.FeeParking;
import com.property.mapper.FeeParkingMapper;
import com.property.security.LoginUser;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/fee/parking")
public class FeeParkingController {

    @Autowired
    private FeeParkingMapper feeParkingMapper;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('fee:parking:list')")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) Integer status,
                          @RequestParam(required = false) String feeMonth) {
        Page<FeeParking> page = new Page<>(current, size);
        return Result.ok(feeParkingMapper.selectPageWithDetail(page, null, status, feeMonth));
    }

    @GetMapping("/my")
    public Result<?> myList(@RequestParam(defaultValue = "1") Integer current,
                            @RequestParam(defaultValue = "10") Integer size,
                            @RequestParam(required = false) Integer status) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<FeeParking> page = new Page<>(current, size);
        return Result.ok(feeParkingMapper.selectPageWithDetail(page, loginUser.getUserId(), status, null));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('fee:parking:add')")
    public Result<?> add(@RequestBody FeeParking fee) {
        feeParkingMapper.insert(fee);
        return Result.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('fee:parking:edit')")
    public Result<?> update(@RequestBody FeeParking fee) {
        feeParkingMapper.updateById(fee);
        return Result.ok();
    }

    @PutMapping("/pay/{id}")
    public Result<?> pay(@PathVariable Long id) {
        FeeParking fee = feeParkingMapper.selectById(id);
        if (fee == null) return Result.fail("记录不存在");
        if (fee.getStatus() == 1) return Result.fail("已缴费，无需重复操作");
        fee.setStatus(1);
        fee.setPayTime(LocalDateTime.now());
        feeParkingMapper.updateById(fee);
        return Result.ok("缴费成功");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('fee:parking:delete')")
    public Result<?> delete(@PathVariable Long id) {
        FeeParking fee = feeParkingMapper.selectById(id);
        if (fee != null && fee.getStatus() == 0) {
            return Result.fail("未缴费的记录不能删除");
        }
        feeParkingMapper.deleteById(id);
        return Result.ok();
    }
}
