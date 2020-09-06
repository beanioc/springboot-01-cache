package com.ll.cache.controller;

import com.ll.cache.bean.Department;
import com.ll.cache.service.DeptService;
import com.ll.cache.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2020/9/6 - 11:18
 */
@RestController
public class DeptController {

    @Autowired
    DeptService deptService;

    @GetMapping("/dept/{id}")
    public Department getDeptById(@PathVariable("id") Integer id){
        Department deptById = deptService.getDeptById(id);
        return deptById;
    }
}
