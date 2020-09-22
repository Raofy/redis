# Jedis连接Redis （Java操作Redis的中间件）

   #### 1. 环境搭建
  
   - 添加pom依赖
   
   ```xml
    <!--导入jredis的包-->
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
        <version>3.3.0</version>
    </dependency>
    <!--fastjson-->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.73</version>
    </dependency>
   ```   

   #### 2. 编码测试
   
   - 连接Redis
   
   ```java
    @Test
    public void testPing() {
        Jedis jedis = new Jedis("47.112.240.174", 6379);    //步骤一：连接数据库
        String response = jedis.ping();                     //步骤二：操作数据库（执行Ping命令）
        System.out.println(response); // PONG               
        jedis.close();                                      //步骤三：断开连接
    }
   ```

   - 结果输出
   
   ```text
    2020-09-22 09:55:34.797  INFO 488 --- [           main] com.ryan.jedis.JedisApplicationTests     : Starting JedisApplicationTests on DESKTOP-PEVRKGP with PID 488 (started by 13121 in C:\Users\13121\Desktop\redis\jedis)
    2020-09-22 09:55:34.816  INFO 488 --- [           main] com.ryan.jedis.JedisApplicationTests     : No active profile set, falling back to default profiles: default
    2020-09-22 09:55:35.416  INFO 488 --- [           main] com.ryan.jedis.JedisApplicationTests     : Started JedisApplicationTests in 0.921 seconds (JVM running for 3.453)
    
    PONG
    
    Disconnected from the target VM, address: '127.0.0.1:54177', transport: 'socket'
    
    Process finished with exit code 0

   ```

   - 测试事务
   
   ```java
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
            int i = 1/0;       //代码抛出异常，模拟事务回滚
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
   ```

   - 结果打印
   
   ```text
    2020-09-22 09:59:33.135  INFO 14508 --- [           main] com.ryan.jedis.JedisApplicationTests     : Started JedisApplicationTests in 0.72 seconds (JVM running for 1.583)
    
    {"name":"fuyi","hello":"world"}
    {"name":"fuyi","hello":"world"}
    
    
    Process finished with exit code 0
   ```



   
   
        
   
   