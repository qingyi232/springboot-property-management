package com.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.entity.UserComplaint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserComplaintMapper extends BaseMapper<UserComplaint> {

    @Select("<script>" +
            "SELECT uc.*, lu.real_name as user_name FROM user_complaint uc " +
            "LEFT JOIN live_user lu ON uc.user_id = lu.id " +
            "WHERE uc.deleted = 0" +
            "<if test='userId != null'> AND uc.user_id = #{userId}</if>" +
            "<if test='status != null'> AND uc.status = #{status}</if>" +
            " ORDER BY uc.id DESC" +
            "</script>")
    IPage<UserComplaint> selectPageWithUser(Page<UserComplaint> page,
                                            @Param("userId") Long userId,
                                            @Param("status") Integer status);
}
