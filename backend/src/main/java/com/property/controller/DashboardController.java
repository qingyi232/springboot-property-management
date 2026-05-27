package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.property.entity.*;
import com.property.mapper.*;
import com.property.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private LiveUserMapper liveUserMapper;
    @Autowired
    private HouseBuildingMapper buildingMapper;
    @Autowired
    private HouseRoomMapper roomMapper;
    @Autowired
    private ParkingLotMapper parkingMapper;
    @Autowired
    private FeeWaterMapper feeWaterMapper;
    @Autowired
    private FeePowerMapper feePowerMapper;
    @Autowired
    private FeeParkingMapper feeParkingMapper;
    @Autowired
    private UserComplaintMapper complaintMapper;
    @Autowired
    private UserRepairMapper repairMapper;
    @Autowired
    private SysNoticeMapper noticeMapper;

    @GetMapping("/stats")
    public Result<?> stats() {
        Map<String, Object> data = new HashMap<>();

        long ownerCount = liveUserMapper.selectCount(null);
        long buildingCount = buildingMapper.selectCount(null);
        long roomCount = roomMapper.selectCount(null);
        long parkingCount = parkingMapper.selectCount(null);

        data.put("ownerCount", ownerCount);
        data.put("buildingCount", buildingCount);
        data.put("roomCount", roomCount);
        data.put("parkingCount", parkingCount);

        long unpaidWater = feeWaterMapper.selectCount(new LambdaQueryWrapper<FeeWater>().eq(FeeWater::getStatus, 0));
        long paidWater = feeWaterMapper.selectCount(new LambdaQueryWrapper<FeeWater>().eq(FeeWater::getStatus, 1));
        long unpaidPower = feePowerMapper.selectCount(new LambdaQueryWrapper<FeePower>().eq(FeePower::getStatus, 0));
        long paidPower = feePowerMapper.selectCount(new LambdaQueryWrapper<FeePower>().eq(FeePower::getStatus, 1));
        long unpaidParking = feeParkingMapper.selectCount(new LambdaQueryWrapper<FeeParking>().eq(FeeParking::getStatus, 0));
        long paidParking = feeParkingMapper.selectCount(new LambdaQueryWrapper<FeeParking>().eq(FeeParking::getStatus, 1));

        data.put("unpaidWater", unpaidWater);
        data.put("paidWater", paidWater);
        data.put("unpaidPower", unpaidPower);
        data.put("paidPower", paidPower);
        data.put("unpaidParking", unpaidParking);
        data.put("paidParking", paidParking);

        long pendingComplaint = complaintMapper.selectCount(new LambdaQueryWrapper<UserComplaint>().eq(UserComplaint::getStatus, 0));
        long doneComplaint = complaintMapper.selectCount(new LambdaQueryWrapper<UserComplaint>().eq(UserComplaint::getStatus, 1));
        long pendingRepair = repairMapper.selectCount(new LambdaQueryWrapper<UserRepair>().eq(UserRepair::getStatus, 0));
        long processingRepair = repairMapper.selectCount(new LambdaQueryWrapper<UserRepair>().eq(UserRepair::getStatus, 1));
        long doneRepair = repairMapper.selectCount(new LambdaQueryWrapper<UserRepair>().eq(UserRepair::getStatus, 2));

        data.put("pendingComplaint", pendingComplaint);
        data.put("doneComplaint", doneComplaint);
        data.put("pendingRepair", pendingRepair);
        data.put("processingRepair", processingRepair);
        data.put("doneRepair", doneRepair);
        data.put("noticeCount", noticeMapper.selectCount(null));

        long freeRoom = roomMapper.selectCount(new LambdaQueryWrapper<HouseRoom>().eq(HouseRoom::getStatus, 0));
        long usedRoom = roomMapper.selectCount(new LambdaQueryWrapper<HouseRoom>().eq(HouseRoom::getStatus, 1));
        long freeParking = parkingMapper.selectCount(new LambdaQueryWrapper<ParkingLot>().eq(ParkingLot::getStatus, 0));
        long usedParking = parkingMapper.selectCount(new LambdaQueryWrapper<ParkingLot>().eq(ParkingLot::getStatus, 1));
        data.put("freeRoom", freeRoom);
        data.put("usedRoom", usedRoom);
        data.put("freeParking", freeParking);
        data.put("usedParking", usedParking);

        // 各月费用汇总
        List<FeeWater> allWater = feeWaterMapper.selectList(null);
        List<FeePower> allPower = feePowerMapper.selectList(null);
        List<FeeParking> allParkFee = feeParkingMapper.selectList(null);

        Set<String> months = new TreeSet<>();
        allWater.forEach(f -> { if (f.getFeeMonth() != null) months.add(f.getFeeMonth()); });
        allPower.forEach(f -> { if (f.getFeeMonth() != null) months.add(f.getFeeMonth()); });
        allParkFee.forEach(f -> { if (f.getFeeMonth() != null) months.add(f.getFeeMonth()); });

        List<String> monthList = new ArrayList<>(months);
        List<BigDecimal> waterAmounts = new ArrayList<>();
        List<BigDecimal> powerAmounts = new ArrayList<>();
        List<BigDecimal> parkAmounts = new ArrayList<>();

        for (String m : monthList) {
            waterAmounts.add(allWater.stream().filter(f -> m.equals(f.getFeeMonth())).map(FeeWater::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            powerAmounts.add(allPower.stream().filter(f -> m.equals(f.getFeeMonth())).map(FeePower::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            parkAmounts.add(allParkFee.stream().filter(f -> m.equals(f.getFeeMonth())).map(FeeParking::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        data.put("feeMonths", monthList);
        data.put("waterAmounts", waterAmounts);
        data.put("powerAmounts", powerAmounts);
        data.put("parkAmounts", parkAmounts);

        return Result.ok(data);
    }
}
