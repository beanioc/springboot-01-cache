package com.ll.cache.mapper;

import com.ll.cache.bean.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

/**
 * 2020/8/26 - 23:44
 */
@Mapper
public interface DepartmentMapper {
    @Select("select * from department where id = #{id}")
    Department getDeptByid(Integer id);
}
