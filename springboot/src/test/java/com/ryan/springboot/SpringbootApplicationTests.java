package com.ryan.springboot;

import com.ryan.springboot.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@SpringBootTest
class SpringbootApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    void contextLoads() {
    }

    @Test
    public void testRedisTemplate() {
        redisTemplate.opsForValue().set("myKey","fuyi学Redis");
        log.info("myKey:" + redisTemplate.opsForValue().get("myKey"));
    }

    @Test
    public void testSerialization() {
        User fuyi = new User("fuyi", 18);
        redisTemplate.opsForValue().set("user",fuyi);
        log.info("user:" + redisTemplate.opsForValue().get("user"));
    }

}
