package com.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.HouseUnit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HouseUnitMapper extends BaseMapper<HouseUnit> {

    @Select("<script>" +
            "SELECT u.*, b.name as building_name FROM house_unit u " +
            "LEFT JOIN house_building b ON u.building_id = b.id " +
            "WHERE u.deleted = 0" +
            "<if test='buildingId != null'> AND u.building_id = #{buildingId}</if>" +
            "<if test='name != null and name != \"\"'> AND u.name LIKE CONCAT('%',#{name},'%')</if>" +
            " ORDER BY u.id DESC" +
            "</script>")
    IPage<HouseUnit> selectPageWithBuilding(Page<HouseUnit> page, @Param("buildingId") Long buildingId, @Param("name") String name);
}
