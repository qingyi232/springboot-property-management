package com.property.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserRoleDto {
    private Long userId;
    private List<Long> roleIds;
}
