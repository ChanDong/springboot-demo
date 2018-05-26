package com.example.springbootdemo.dao;

import com.example.springbootdemo.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

/**
 * Created by shixi03 on 2018/5/23.
 */
@Mapper
public interface UserDAO {

    @Select("select * from user where id = #{id}")
    User getUserById(@Param("id") int id);
}
