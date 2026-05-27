package com.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("live_user_parking")
public class LiveUserParking {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long parkingId;
    private LocalDateTime bindTime;
}
