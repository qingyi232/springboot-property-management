package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.*;
import com.property.mapper.*;
import com.property.security.LoginUser;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/liveuser")
public class LiveUserController {

    @Autowired
    private LiveUserMapper liveUserMapper;
    @Autowired
    private LiveUserHouseMapper liveUserHouseMapper;
    @Autowired
    private LiveUserParkingMapper liveUserParkingMapper;
    @Autowired
    private HouseRoomMapper houseRoomMapper;
    @Autowired
    private ParkingLotMapper parkingLotMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('owner:list')")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer current,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) String realName,
                          @RequestParam(required = false) String phone) {
        Page<LiveUser> page = new Page<>(current, size);
        LambdaQueryWrapper<LiveUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(realName)) {
            wrapper.like(LiveUser::getRealName, realName);
        }
        if (StringUtils.hasText(phone)) {
            wrapper.like(LiveUser::getPhone, phone);
        }
        wrapper.orderByDesc(LiveUser::getId);
        Page<LiveUser> result = liveUserMapper.selectPage(page, wrapper);
        result.getRecords().forEach(u -> u.setPassword(null));
        return Result.ok(result);
    }

    @GetMapping("/all")
    public Result<?> all() {
        List<LiveUser> list = liveUserMapper.selectList(null);
        list.forEach(u -> u.setPassword(null));
        return Result.ok(list);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('owner:add')")
    public Result<?> add(@RequestBody LiveUser user) {
        LambdaQueryWrapper<LiveUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveUser::getUsername, user.getUsername());
        if (liveUserMapper.selectCount(wrapper) > 0) {
            return Result.fail("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        liveUserMapper.insert(user);
        return Result.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('owner:edit')")
    public Result<?> update(@RequestBody LiveUser user) {
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        liveUserMapper.updateById(user);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('owner:delete')")
    public Result<?> delete(@PathVariable Long id) {
        liveUserMapper.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/bindInfo/{userId}")
    public Result<?> getBindInfo(@PathVariable Long userId) {
        Map<String, Object> data = new HashMap<>();

        List<LiveUserHouse> houses = liveUserHouseMapper.selectList(
                new LambdaQueryWrapper<LiveUserHouse>().eq(LiveUserHouse::getUserId, userId));
        List<Long> houseIds = houses.stream().map(LiveUserHouse::getHouseId).collect(Collectors.toList());
        data.put("houseIds", houseIds);

        List<LiveUserParking> parkings = liveUserParkingMapper.selectList(
                new LambdaQueryWrapper<LiveUserParking>().eq(LiveUserParking::getUserId, userId));
        List<Long> parkingIds = parkings.stream().map(LiveUserParking::getParkingId).collect(Collectors.toList());
        data.put("parkingIds", parkingIds);

        return Result.ok(data);
    }

    @PostMapping("/bindHouse")
    @PreAuthorize("hasAuthority('owner:edit')")
    public Result<?> bindHouse(@RequestParam Long userId, @RequestParam Long houseId) {
        HouseRoom room = houseRoomMapper.selectById(houseId);
        if (room == null || room.getStatus() == 1) {
            return Result.fail("该房屋已被绑定或不存在");
        }
        LiveUserHouse bind = new LiveUserHouse();
        bind.setUserId(userId);
        bind.setHouseId(houseId);
        liveUserHouseMapper.insert(bind);

        room.setStatus(1);
        houseRoomMapper.updateById(room);
        return Result.ok();
    }

    @PostMapping("/unbindHouse")
    @PreAuthorize("hasAuthority('owner:edit')")
    public Result<?> unbindHouse(@RequestParam Long userId, @RequestParam Long houseId) {
        LambdaQueryWrapper<LiveUserHouse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveUserHouse::getUserId, userId).eq(LiveUserHouse::getHouseId, houseId);
        liveUserHouseMapper.delete(wrapper);

        HouseRoom room = houseRoomMapper.selectById(houseId);
        if (room != null) {
            room.setStatus(0);
            houseRoomMapper.updateById(room);
        }
        return Result.ok();
    }

    @PostMapping("/bindParking")
    @PreAuthorize("hasAuthority('owner:edit')")
    public Result<?> bindParking(@RequestParam Long userId, @RequestParam Long parkingId) {
        ParkingLot lot = parkingLotMapper.selectById(parkingId);
        if (lot == null || lot.getStatus() == 1) {
            return Result.fail("该车位已被绑定或不存在");
        }
        LiveUserParking bind = new LiveUserParking();
        bind.setUserId(userId);
        bind.setParkingId(parkingId);
        liveUserParkingMapper.insert(bind);

        lot.setStatus(1);
        parkingLotMapper.updateById(lot);
        return Result.ok();
    }

    @PostMapping("/unbindParking")
    @PreAuthorize("hasAuthority('owner:edit')")
    public Result<?> unbindParking(@RequestParam Long userId, @RequestParam Long parkingId) {
        LambdaQueryWrapper<LiveUserParking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveUserParking::getUserId, userId).eq(LiveUserParking::getParkingId, parkingId);
        liveUserParkingMapper.delete(wrapper);

        ParkingLot lot = parkingLotMapper.selectById(parkingId);
        if (lot != null) {
            lot.setStatus(0);
            parkingLotMapper.updateById(lot);
        }
        return Result.ok();
    }

    @GetMapping("/myInfo")
    public Result<?> myInfo() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LiveUser user = liveUserMapper.selectById(loginUser.getUserId());
        if (user != null) {
            user.setPassword(null);
        }
        return Result.ok(user);
    }

    @PutMapping("/myInfo")
    public Result<?> updateMyInfo(@RequestBody LiveUser user) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setId(loginUser.getUserId());
        user.setUsername(null);
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        liveUserMapper.updateById(user);
        return Result.ok();
    }

    @PutMapping("/resetPwd/{id}")
    @PreAuthorize("hasAuthority('owner:edit')")
    public Result<?> resetPassword(@PathVariable Long id) {
        LiveUser user = new LiveUser();
        user.setId(id);
        user.setPassword(passwordEncoder.encode("123456"));
        liveUserMapper.updateById(user);
        return Result.ok("密码已重置为123456");
    }
}
