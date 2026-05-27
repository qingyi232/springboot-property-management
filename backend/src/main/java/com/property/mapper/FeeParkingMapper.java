package com.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.FeeParking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FeeParkingMapper extends BaseMapper<FeeParking> {

    @Select("<script>" +
            "SELECT fpk.*, pl.spot_number, lu.real_name as user_name " +
            "FROM fee_parking fpk " +
            "LEFT JOIN parking_lot pl ON fpk.parking_id = pl.id " +
            "LEFT JOIN live_user lu ON fpk.user_id = lu.id " +
            "WHERE fpk.deleted = 0" +
            "<if test='userId != null'> AND fpk.user_id = #{userId}</if>" +
            "<if test='status != null'> AND fpk.status = #{status}</if>" +
            "<if test='feeMonth != null and feeMonth != \"\"'> AND fpk.fee_month = #{feeMonth}</if>" +
            " ORDER BY fpk.id DESC" +
            "</script>")
    IPage<FeeParking> selectPageWithDetail(Page<FeeParking> page,
                                           @Param("userId") Long userId,
                                           @Param("status") Integer status,
                                           @Param("feeMonth") String feeMonth);
}
