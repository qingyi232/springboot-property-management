package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.HouseUnit;
import com.property.mapper.HouseUnitMapper;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unit")
public class HouseUnitController {

    @Autowired
    private HouseUnitMapper unitMapper;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('house:unit:list')")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) Long buildingId,
                          @RequestParam(required = false) String name) {
        Page<HouseUnit> page = new Page<>(current, size);
        return Result.ok(unitMapper.selectPageWithBuilding(page, buildingId, name));
    }

    @GetMapping("/byBuilding/{buildingId}")
    public Result<?> getByBuilding(@PathVariable Long buildingId) {
        List<HouseUnit> list = unitMapper.selectList(
                new LambdaQueryWrapper<HouseUnit>().eq(HouseUnit::getBuildingId, buildingId));
        return Result.ok(list);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('house:unit:add')")
    public Result<?> add(@RequestBody HouseUnit unit) {
        unitMapper.insert(unit);
        return Result.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('house:unit:edit')")
    public Result<?> update(@RequestBody HouseUnit unit) {
        unitMapper.updateById(unit);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('house:unit:delete')")
    public Result<?> delete(@PathVariable Long id) {
        unitMapper.deleteById(id);
        return Result.ok();
    }
}
