package com.ll.cache;

import com.ll.cache.bean.Employee;
import com.ll.cache.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class Springboot01CacheApplicationTests {
    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate; //操作字符串

    @Autowired
    RedisTemplate redisTemplate; //k-v都是对象的

    @Autowired
    RedisTemplate<Object, Employee> empRedisTemplate;

    /**
     * Redis常见的五大数据类型
     * String(字符串),List(列表),Set(集合),Hash(哈希),Zset(有序集合)
     * stringRedisTemplate.opsForValue(); 字符串
     * stringRedisTemplate.opsForList() 列表
     * stringRedisTemplate.opsForSet() 集合
     * stringRedisTemplate.opsForHash() 哈希
     * stringRedisTemplate.opsForZSet() 有序集合
     */
    @Test
    public void test01() {
        //给redis中保存数据
//        stringRedisTemplate.opsForValue().append("msg","hello");
//        String msg = stringRedisTemplate.opsForValue().get("msg");
//        System.out.println(msg);

//        stringRedisTemplate.opsForList().leftPush("mylist","1");
//        stringRedisTemplate.opsForList().leftPush("mylist","2");
        stringRedisTemplate.opsForList().leftPop("mylist");
    }

    //测试保存对象
    @Test
    public void test02() {
        Employee employee = employeeMapper.getEmpById(2);
        //默认如果保存对象,使用jdk序列化机制,序列化后的数据保存在redis中
        redisTemplate.opsForValue().set("emp-01",employee);
        //1.将数据以json的方式保存
        //(1)自己将对象转为json
        //(2)redisTemplate默认的序列化规则,改变默认的序列化规则
        empRedisTemplate.opsForValue().set("emp-01",employee);
    }

    @Test
    void contextLoads() {
        Employee empById = employeeMapper.getEmpById(2);
        System.out.println(empById);

    }

//    @Test
//    public void test(){
//        RedisProperties.Jedis jedis = new RedisProperties.Jedis("47.107.162.238", 6379);
//        System.out.println(jedis);
//
//    }
}
