package com.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.SysNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysNoticeMapper extends BaseMapper<SysNotice> {

    @Select("<script>" +
            "SELECT n.*, su.real_name as creator_name FROM sys_notice n " +
            "LEFT JOIN sys_user su ON n.creator_id = su.id " +
            "WHERE n.deleted = 0" +
            "<if test='title != null and title != \"\"'> AND n.title LIKE CONCAT('%',#{title},'%')</if>" +
            " ORDER BY n.id DESC" +
            "</script>")
    IPage<SysNotice> selectPageWithCreator(Page<SysNotice> page, @Param("title") String title);
}
