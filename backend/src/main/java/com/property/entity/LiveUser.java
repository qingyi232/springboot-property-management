package com.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("live_user")
public class LiveUser {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String realName;
    private Integer gender;
    private String phone;
    private String idCard;
    private String avatar;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
