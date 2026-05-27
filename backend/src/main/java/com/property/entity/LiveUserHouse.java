package com.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("live_user_house")
public class LiveUserHouse {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long houseId;
    private LocalDateTime bindTime;
}
