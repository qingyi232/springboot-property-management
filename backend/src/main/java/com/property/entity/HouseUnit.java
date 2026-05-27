package com.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("house_unit")
public class HouseUnit {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long buildingId;
    private String name;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private String buildingName;
}
