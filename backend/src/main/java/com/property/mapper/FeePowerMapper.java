package com.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.FeePower;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FeePowerMapper extends BaseMapper<FeePower> {

    @Select("<script>" +
            "SELECT fp.*, hr.room_number, lu.real_name as user_name " +
            "FROM fee_power fp " +
            "LEFT JOIN house_room hr ON fp.house_id = hr.id " +
            "LEFT JOIN live_user lu ON fp.user_id = lu.id " +
            "WHERE fp.deleted = 0" +
            "<if test='userId != null'> AND fp.user_id = #{userId}</if>" +
            "<if test='status != null'> AND fp.status = #{status}</if>" +
            "<if test='feeMonth != null and feeMonth != \"\"'> AND fp.fee_month = #{feeMonth}</if>" +
            " ORDER BY fp.id DESC" +
            "</script>")
    IPage<FeePower> selectPageWithDetail(Page<FeePower> page,
                                         @Param("userId") Long userId,
                                         @Param("status") Integer status,
                                         @Param("feeMonth") String feeMonth);
}
