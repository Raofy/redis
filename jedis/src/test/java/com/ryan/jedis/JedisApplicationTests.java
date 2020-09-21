package com.ryan.jedis;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

@SpringBootTest
class JedisApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testPing() {
        Jedis jedis = new Jedis("47.112.240.174", 6379);
        String response = jedis.ping();
        System.out.println(response); // PONG
        jedis.close();
    }

    @Test
    public void testTransaction() {
        Jedis jedis = new Jedis("47.112.240.174", 6379);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "world");
        jsonObject.put("name", "fuyi");
        // 开启事务
        Transaction multi = jedis.multi();
        String result = jsonObject.toJSONString();
        try {
            multi.set("user1", result);
            multi.set("user2", result);
            // 执行事务
            multi.exec();
        }catch (Exception e){
            // 放弃事务
            multi.discard();
        } finally {
            // 关闭连接
            System.out.println(jedis.get("user1"));
            System.out.println(jedis.get("user2"));
            jedis.close();
        }
    }

}
