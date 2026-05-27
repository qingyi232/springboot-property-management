package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.HouseBuilding;
import com.property.mapper.HouseBuildingMapper;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/building")
public class HouseBuildingController {

    @Autowired
    private HouseBuildingMapper buildingMapper;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('house:building:list')")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) String name) {
        Page<HouseBuilding> page = new Page<>(current, size);
        LambdaQueryWrapper<HouseBuilding> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(HouseBuilding::getName, name);
        }
        wrapper.orderByDesc(HouseBuilding::getId);
        return Result.ok(buildingMapper.selectPage(page, wrapper));
    }

    @GetMapping("/all")
    public Result<?> all() {
        return Result.ok(buildingMapper.selectList(null));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('house:building:add')")
    public Result<?> add(@RequestBody HouseBuilding building) {
        buildingMapper.insert(building);
        return Result.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('house:building:edit')")
    public Result<?> update(@RequestBody HouseBuilding building) {
        buildingMapper.updateById(building);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('house:building:delete')")
    public Result<?> delete(@PathVariable Long id) {
        buildingMapper.deleteById(id);
        return Result.ok();
    }
}
