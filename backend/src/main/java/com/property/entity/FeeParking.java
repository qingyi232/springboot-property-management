package com.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fee_parking")
public class FeeParking {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long parkingId;
    private Long userId;
    private BigDecimal amount;
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
    private String spotNumber;
    @TableField(exist = false)
    private String userName;
}
