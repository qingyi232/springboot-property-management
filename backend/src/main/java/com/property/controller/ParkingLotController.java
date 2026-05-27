package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.ParkingLot;
import com.property.mapper.ParkingLotMapper;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parking")
public class ParkingLotController {

    @Autowired
    private ParkingLotMapper parkingMapper;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('house:parking:list')")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) String spotNumber,
                          @RequestParam(required = false) Integer status) {
        Page<ParkingLot> page = new Page<>(current, size);
        LambdaQueryWrapper<ParkingLot> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(spotNumber)) {
            wrapper.like(ParkingLot::getSpotNumber, spotNumber);
        }
        if (status != null) {
            wrapper.eq(ParkingLot::getStatus, status);
        }
        wrapper.orderByDesc(ParkingLot::getId);
        return Result.ok(parkingMapper.selectPage(page, wrapper));
    }

    @GetMapping("/free")
    public Result<?> freeParkings() {
        return Result.ok(parkingMapper.selectList(
                new LambdaQueryWrapper<ParkingLot>().eq(ParkingLot::getStatus, 0)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('house:parking:add')")
    public Result<?> add(@RequestBody ParkingLot parking) {
        parkingMapper.insert(parking);
        return Result.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('house:parking:edit')")
    public Result<?> update(@RequestBody ParkingLot parking) {
        parkingMapper.updateById(parking);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('house:parking:delete')")
    public Result<?> delete(@PathVariable Long id) {
        parkingMapper.deleteById(id);
        return Result.ok();
    }
}
