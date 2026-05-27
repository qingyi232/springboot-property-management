package com.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("parking_lot")
public class ParkingLot {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String spotNumber;
    private String area;
    private BigDecimal monthlyFee;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
