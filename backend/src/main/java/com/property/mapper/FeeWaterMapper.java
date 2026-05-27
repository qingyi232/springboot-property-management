package com.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.FeeWater;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FeeWaterMapper extends BaseMapper<FeeWater> {

    @Select("<script>" +
            "SELECT fw.*, hr.room_number, lu.real_name as user_name " +
            "FROM fee_water fw " +
            "LEFT JOIN house_room hr ON fw.house_id = hr.id " +
            "LEFT JOIN live_user lu ON fw.user_id = lu.id " +
            "WHERE fw.deleted = 0" +
            "<if test='userId != null'> AND fw.user_id = #{userId}</if>" +
            "<if test='status != null'> AND fw.status = #{status}</if>" +
            "<if test='feeMonth != null and feeMonth != \"\"'> AND fw.fee_month = #{feeMonth}</if>" +
            " ORDER BY fw.id DESC" +
            "</script>")
    IPage<FeeWater> selectPageWithDetail(Page<FeeWater> page,
                                         @Param("userId") Long userId,
                                         @Param("status") Integer status,
                                         @Param("feeMonth") String feeMonth);
}
