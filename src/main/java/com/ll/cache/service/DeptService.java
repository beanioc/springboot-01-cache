package com.ll.cache.service;

import com.ll.cache.bean.Department;
import com.ll.cache.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

/**
 * 2020/9/6 - 11:13
 */
//@CacheConfig(cacheManager = "RedisCacheManager")
@Service
public class DeptService {
    @Autowired
    DepartmentMapper departmentMapper;

    @Qualifier("LLRedisCacheManager")
    @Autowired
    RedisCacheManager redisCacheManager;

//    @Cacheable(cacheNames = "dept")
//    public Department getDeptById(Integer id){
//        System.out.println("DeptService=>getDeptById,查询部门: " + id);
//       Department department =  departmentMapper.getDeptByid(id);
//       return department;
//    }

    //使用缓存管理器得到缓存,进行api调用
//    @Cacheable(cacheNames = "dept")
    public Department getDeptById(Integer id){
        System.out.println("DeptService=>getDeptById,查询部门: " + id);
        Department department =  departmentMapper.getDeptByid(id);

//        获取某个缓存,手动添加缓存到redis
        Cache dept = redisCacheManager.getCache("dept");
        dept.put("dept:1",department);

        return department;
    }
}
