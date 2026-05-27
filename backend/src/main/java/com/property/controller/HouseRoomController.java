package com.property.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.HouseRoom;
import com.property.mapper.HouseRoomMapper;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room")
public class HouseRoomController {

    @Autowired
    private HouseRoomMapper roomMapper;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('house:room:list')")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) Long unitId,
                          @RequestParam(required = false) String roomNumber,
                          @RequestParam(required = false) Integer status) {
        Page<HouseRoom> page = new Page<>(current, size);
        return Result.ok(roomMapper.selectPageWithDetail(page, unitId, roomNumber, status));
    }

    @GetMapping("/free")
    public Result<?> freeRooms() {
        return Result.ok(roomMapper.selectFreeRooms());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('house:room:add')")
    public Result<?> add(@RequestBody HouseRoom room) {
        roomMapper.insert(room);
        return Result.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('house:room:edit')")
    public Result<?> update(@RequestBody HouseRoom room) {
        roomMapper.updateById(room);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('house:room:delete')")
    public Result<?> delete(@PathVariable Long id) {
        roomMapper.deleteById(id);
        return Result.ok();
    }
}
