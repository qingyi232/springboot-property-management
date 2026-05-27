package com.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.HouseRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HouseRoomMapper extends BaseMapper<HouseRoom> {

    @Select("<script>" +
            "SELECT r.*, u.name as unit_name, b.name as building_name, b.id as building_id " +
            "FROM house_room r " +
            "LEFT JOIN house_unit u ON r.unit_id = u.id " +
            "LEFT JOIN house_building b ON u.building_id = b.id " +
            "WHERE r.deleted = 0" +
            "<if test='unitId != null'> AND r.unit_id = #{unitId}</if>" +
            "<if test='roomNumber != null and roomNumber != \"\"'> AND r.room_number LIKE CONCAT('%',#{roomNumber},'%')</if>" +
            "<if test='status != null'> AND r.status = #{status}</if>" +
            " ORDER BY r.id DESC" +
            "</script>")
    IPage<HouseRoom> selectPageWithDetail(Page<HouseRoom> page,
                                          @Param("unitId") Long unitId,
                                          @Param("roomNumber") String roomNumber,
                                          @Param("status") Integer status);

    @Select("SELECT r.*, u.name as unit_name, b.name as building_name " +
            "FROM house_room r " +
            "LEFT JOIN house_unit u ON r.unit_id = u.id " +
            "LEFT JOIN house_building b ON u.building_id = b.id " +
            "WHERE r.deleted = 0 AND r.status = 0")
    List<HouseRoom> selectFreeRooms();
}
