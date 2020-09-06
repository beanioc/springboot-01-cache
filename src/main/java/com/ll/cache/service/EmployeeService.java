package com.ll.cache.service;

import com.ll.cache.bean.Employee;
import com.ll.cache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

/**
 * 2020/8/27 - 0:21
 */
@CacheConfig(cacheNames = "emp") //抽取缓存的公共配置
@Service
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * 将方法的运行结果进行缓存,以后再要相同的数据,,不用调用方法
     *直接从缓存中获取
     * CacheManager管理多个Cache组建的, 对缓存的真正的CRUD操作在Cache组件中,每一个缓存组件有自己唯一一个名字
     * 几个属性:
     *          cacheNames/value: 指定缓存组件的名字:
     *          key: 缓存数据使用的key:可以用它来指定,默认是使用方法参数的值 1-方法的返回值
     *                  编写SPEL; #id:参数id的值 #a0 #p0 #root.args[0]
     *          keyGenerator: key的生成器,可以自己指定key的生成器的组件id
     *                  key/keyGenerator: 二选一使用
     *          CacheManager: 指定缓存管理器;或者cacheResolver指定获取解析器
     *          condition: 指定符合条件的情况下才缓存 condition = "#id>0"
     *          unless: 否定缓存,当unless指定的条件为true, 方法的返回值就不会缓存,可以获取到结果进行判断 unless = "#result == null"
     *          sync: 是否使用异步模式
     * 原理:
     *  1.自动配置类: CacheAutoconfiguration
     *  2.缓存的配置类:
     *  0 = "org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration"
     *  1 = "org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration"
     *  2 = "org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration"
     *  3 = "org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration"
     *  4 = "org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration"
     *  5 = "org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration"
     *  6 = "org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration"
     *  7 = "org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration"
     *  8 = "org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration"
     *  9 = "org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration"
     *  3.哪些配置类默认生效: SimpleCacheConfiguration;
     *
     *  4.给容器中注册了一个CacheManager: SimpleCacheManager
     *
     *  运行流程:
     *  @Cacheable标注的方法执行之前先来检查缓存中有没有这个数据,默认按照参数的值作为key去查询缓存,
     *  如果没有就运行方法并将结果放入缓存;
     *
     *  几个属性:
     *  cacheNames/value: 指定缓存组件的名字,将方法的返回结果放在哪个缓存中,是数组的方式,可以指定多个缓存
     *  key: 缓存数据使用的key:可以用它来指定,默认是使用方法参数的值 1-方法的返回值
     *  编写SPEL; #id:参数id的值 #a0 #p0 #root.args[0]
     *  keyGenerator: key的生成器,可以自己指定key的生成器的组件id
     *      key/keyGenerator: 二选一使用
     *  CacheManager: 指定缓存管理器;或者cacheResolver指定获取解析器
     *  condition: 指定符合条件的情况下才缓存 condition = "#id>0"
     *  unless: 否定缓存,当unless指定的条件为true, 方法的返回值就不会缓存,可以获取到结果进行判断 unless = "#result == null"
     *  sync: 是否使用异步模式
     *
     * @param id
     * @return
     */
    @Cacheable(/*cacheNames = {"emp"}*//*, keyGenerator = "mykeyGenerator", condition = "#a0>1", unless = "#a0 == 2"*/)
    public Employee getEmp(Integer id){
        System.out.println("查询" + id + "号员工");
        Employee empById = employeeMapper.getEmpById(id);
        return empById;
    }

    /**
     * @CachePut: 既调用方法,又更新缓存数据: 同步更新缓存
     * 修改了数据库的某个数据,同时更新缓存
     * 运行时机:
     *  1.先调用目标方法
     *  2.将目标方法的结果缓存起来
     *
     * 测试步骤:
     *  1.查询1号员工;查询到的结果会放在缓存中
     *      key: 1 value: lastName: 张三
     *
     *  2.以后查询还是之前的结果
     *  3.更新1号员工;lastName=zhangsan, email=null, gender=0, dId=null
     *      将方法的返回值也放进缓存了
     *      key: 传入的employee 值: 返回的employee对象
     *  4.查询1号员工
     *      应该是更新后的员工;
     *          key = "#employee.id":使用传入的员工的id
     *          key = key = "#result.id" 使用返回后的id
     *          @Cacheable的key是不能用#result的
     *      为什么是没更新前的?[1号员工没有在缓存中更新]
     */
    @CachePut(/*value = "emp",*/ key = "#result.id")
    public Employee updateEmp(Employee employee){
        System.out.println("updateEmp" + employee);
        employeeMapper.updateEmp(employee);
        return employee;
    }

    /**
     * @CacheEvict: 缓存清除
     * key: 指定要清除的数据
     * allEntries = true: 指定清除这个缓存中所有的数据
     * beforeInvocation = false: 缓存的清除是否在方法之前执行
     *      默认代表缓存清除操作是在方法执行之后执行,如果出现异常,缓存清除不会执行
     * beforeInvocation = true:
     *      代表清除缓存操作是在方法运行之前执行,无论方法是否出现异常,缓存都清除
     *
     */
    @CacheEvict(/*value = "emp",*/ beforeInvocation = true)
    public void deleteEmp(Integer id){
        System.out.println("deleteEmp" + id);
//        employeeMapper.deleteEmpById(id);
    }

    //定义复杂的缓存规格
    @Caching(
            //因为有CachePut注解,所以即使有@cacheable注解,查询方法还是会执行
            cacheable = {
                    @Cacheable(/*value = "emp",*/ key = "#lastName")
            },
            put = {
                    @CachePut(/*value = "emp",*/key = "#result.id"),
                    @CachePut(/*value = "emp",*/key="#result.email")
            }
    )
    public Employee getEmpByLastName(String lastName){
        System.out.println("EmployeeService=>getEmpByLastName: " + lastName);
       return employeeMapper.getEmpByLastName(lastName);
    }

}
