package com.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.UserRepair;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRepairMapper extends BaseMapper<UserRepair> {

    @Select("<script>" +
            "SELECT ur.*, lu.real_name as user_name FROM user_repair ur " +
            "LEFT JOIN live_user lu ON ur.user_id = lu.id " +
            "WHERE ur.deleted = 0" +
            "<if test='userId != null'> AND ur.user_id = #{userId}</if>" +
            "<if test='status != null'> AND ur.status = #{status}</if>" +
            " ORDER BY ur.id DESC" +
            "</script>")
    IPage<UserRepair> selectPageWithUser(Page<UserRepair> page,
                                         @Param("userId") Long userId,
                                         @Param("status") Integer status);
}
