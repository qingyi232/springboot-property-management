package com.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fee_water")
public class FeeWater {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long houseId;
    private Long userId;
    private BigDecimal amount;
    private BigDecimal usageAmount;
    private String feeMonth;
    private Integer status;
    private LocalDateTime payTime;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private String roomNumber;
    @TableField(exist = false)
    private String userName;
}
