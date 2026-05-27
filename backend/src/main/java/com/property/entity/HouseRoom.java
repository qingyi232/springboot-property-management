package com.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("house_room")
public class HouseRoom {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long unitId;
    private String roomNumber;
    private BigDecimal area;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private String unitName;
    @TableField(exist = false)
    private String buildingName;
    @TableField(exist = false)
    private Long buildingId;
}
