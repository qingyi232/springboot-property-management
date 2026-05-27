package com.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
@TableName("sys_menu")
public class SysMenu {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long parentId;
    private String name;
    private String path;
    private String component;
    private String permission;
    private String icon;
    private Integer sortOrder;
    private Integer menuType;
    private Integer visible;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private List<SysMenu> children;
}
