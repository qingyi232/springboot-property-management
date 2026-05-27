package com.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_repair")
public class UserRepair {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Integer status;
    private String reply;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private String userName;
}
