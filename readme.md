## 初始Redis

什么是Redis？Redis也叫远程字典服务，它本身是用C语言进行编写的一款支持网络、基于内存亦可持久化的日志型的、支持多种语言API的、存储类型是key-value的非关系型数据库。简言之就是Redis是一款高性能的Key-Value的数据库。它的高性能主要体现在它基于内存进行操作的，在对数据进行读写方面的速度都是非常快的。

上面提到什么是非关系型数据库？非关系型数据库也叫NoSQL（全称Not only SQL），是相对于传统的关系型数据库的统称。传统的软件系统的大体的架构是用户——DAL——数据库系统

而对于并发量大的场景，这种架构是无法承受这么大的访问量，于是在此架构上进一步的进行改动，用户——DAL——缓存——数据库系统

尽管如此，有时候会遇见某个sql 语句需要锁表，导致暂时不能使用读的服务，这样就会影响现有业务，使用主从复制，让主库负责写，从库负责读，这样，即使主库出现了锁表的情景，通过读从库也可以保证业务的正常运作。而Redis里面也是使用到主从复制的

## 关系型数据库

1. 特点
    
    严格的一致性
    
    事务支持使得安全性更高
    
    数据和关系都存储在表中
    
2. 缺点
     
    大量数据的处理
    
    扩展性差，字段增加不便利 
    
### 非关系型数据库 

1. 优点
    
   存储结构是基于Key-value的对应关系，处理速度快

2. 缺点
    
   有限的查询方式
   
## Redis

1. 自身特点
    
    支持每秒十几万的读写操作

    支持集群

    支持分布式

    主从同步

    支持一定的事务能力

2. 应用场景

    - Redis可以存储缓存数据
    
        相对于使用传统的数据库，在用户请求进行读写数据库中的数据时候，数据库会根据相应的SQL语句然后进行磁盘进行查找索引，然后返回数据。这个过程是比较低效的，在请求数量增多的时候，数据库的连接数一定时，整个系统的处理速度就会出现缓慢，甚至宕机。对于这种情况下，Redis的处理流程是，在第一次请求访问数据资源的时候，读取Redis就是失败，因为内存还没有加载任何数据，这时候Redis就会去访问数据库，将数据加载到内存中，然后返回给用户。当下一个请求访问同样的数据的时候，Redis就会直接从内存中提取缓存数据进行返回，因为实在内存中，读取访问速度非常快。如此一来减少了直接对数据库的访问，也大大提高了处理速度。
    
    - 高速读写数据的场景
    
        秒杀，抢购等
        
## Redis数据类型

既然它作为一款数据库软件，那它的所能够存储的数据类型有哪些？Redis并不是简单的key-value存储，实际上他是一个数据结构服务器，支持不同类型的值。也就是说，你不必仅仅把字符串当作键所指向的值。下列这些数据类型都可作为值类型：

- 二进制安全的字符串

- Lists: 按插入顺序排序的字符串元素的集合。他们基本上就是链表（linked lists）。

- Sets: 不重复且无序的字符串元素的集合。

- Sorted sets,类似Sets,但是每个字符串元素都关联到一个叫score浮动数值（floating number value）。里面的元素总是通过score进行着排序，所以不同的是，它是可以检索的一系列元素。（例如你可能会问：给我前面10个或者后面10个元素）。

- Hashes,由field和关联的value组成的map。field和value都是字符串的。这和Ruby、Python的hashes很像。

- Bit arrays (或者说 simply bitmaps): 通过特殊的命令，你可以将 String 值当作一系列 bits 处理：可以设置和清除单独的 bits，数出所有设为 1 的 bits 的数量，找到最前的被设为 1 或 0 的 bit，等等。

- HyperLogLogs: 这是被用于估计一个 set 中元素数量的概率性的数据结构。别害怕，它比看起来的样子要简单…参见本教程的 HyperLogLog 部分


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


# SpringBoot整合Redis

#### 1. 环境搭建

   - 添加依赖
   
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.3.4.RELEASE</version>
            <relativePath/> <!-- lookup parent from repository -->
        </parent>
        <groupId>com.Ryan</groupId>
        <artifactId>springboot</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <name>springboot</name>
        <description>Demo project for Spring Boot</description>
    
        <properties>
            <java.version>1.8</java.version>
        </properties>
    
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter</artifactId>
            </dependency>
    
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
                <exclusions>
                    <exclusion>
                        <groupId>org.junit.vintage</groupId>
                        <artifactId>junit-vintage-engine</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
    
            <!--Redis依赖-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-redis</artifactId>
            </dependency>
    
            <!--配置lombok依赖-->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <optional>true</optional>
            </dependency>
        </dependencies>
    
        <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                </plugin>
            </plugins>
        </build>
    
    </project>

   ```

   - 测试代码
   
   ```java
    @Test
    public void testRedisTemplate() {
        redisTemplate.opsForValue().set("myKey","fuyi学Redis");
        log.info("myKey:" + redisTemplate.opsForValue().get("myKey"));
    }
   ```

   - 运行结果
   
   ```text
    2020-09-22 10:23:03.872  INFO 16100 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 11ms. Found 0 Redis repository interfaces.
    2020-09-22 10:23:04.556  INFO 16100 --- [           main] c.r.s.SpringbootApplicationTests         : Started SpringbootApplicationTests in 1.298 seconds (JVM running for 2.39)
    
    2020-09-22 10:23:05.496  INFO 16100 --- [           main] c.r.s.SpringbootApplicationTests         : myKey:fuyi学Redis
    
    
    Process finished with exit code 0
   ```




#### 2. 配置Redis
   
   - 配置原因
   
   ```java
    public class RedisTemplate<K, V> extends RedisAccessor implements RedisOperations<K, V>, BeanClassLoaderAware {
    
    	private boolean enableTransactionSupport = false;
    	private boolean exposeConnection = false;
    	private boolean initialized = false;
    	private boolean enableDefaultSerializer = true;
    	private @Nullable RedisSerializer<?> defaultSerializer;
    	private @Nullable ClassLoader classLoader;
    
    	@SuppressWarnings("rawtypes") private @Nullable RedisSerializer keySerializer = null;
    	@SuppressWarnings("rawtypes") private @Nullable RedisSerializer valueSerializer = null;
    	@SuppressWarnings("rawtypes") private @Nullable RedisSerializer hashKeySerializer = null;
    	@SuppressWarnings("rawtypes") private @Nullable RedisSerializer hashValueSerializer = null;
    	private RedisSerializer<String> stringSerializer = RedisSerializer.string();
    
    	private @Nullable ScriptExecutor<K> scriptExecutor;
    
    	private final ValueOperations<K, V> valueOps = new DefaultValueOperations<>(this);
    	private final ListOperations<K, V> listOps = new DefaultListOperations<>(this);
    	private final SetOperations<K, V> setOps = new DefaultSetOperations<>(this);
    	private final StreamOperations<K, ?, ?> streamOps = new DefaultStreamOperations<>(this, new ObjectHashMapper());
    	private final ZSetOperations<K, V> zSetOps = new DefaultZSetOperations<>(this);
    	private final GeoOperations<K, V> geoOps = new DefaultGeoOperations<>(this);
    	private final HyperLogLogOperations<K, V> hllOps = new DefaultHyperLogLogOperations<>(this);
    	private final ClusterOperations<K, V> clusterOps = new DefaultClusterOperations<>(this);
    
    	/**
    	 * Constructs a new <code>RedisTemplate</code> instance.
    	 */
    	public RedisTemplate() {}
    
    	/*
    	 * (non-Javadoc)
    	 * @see org.springframework.data.redis.core.RedisAccessor#afterPropertiesSet()
    	 */
    	@Override
    	public void afterPropertiesSet() {
    
    		super.afterPropertiesSet();
    
    		boolean defaultUsed = false;
    
    		if (defaultSerializer == null) {
    
    			defaultSerializer = new JdkSerializationRedisSerializer(
    					classLoader != null ? classLoader : this.getClass().getClassLoader());
    		}
    
    		if (enableDefaultSerializer) {
    
    			if (keySerializer == null) {
    				keySerializer = defaultSerializer;
    				defaultUsed = true;
    			}
    			if (valueSerializer == null) {
    				valueSerializer = defaultSerializer;
    				defaultUsed = true;
    			}
    			if (hashKeySerializer == null) {
    				hashKeySerializer = defaultSerializer;
    				defaultUsed = true;
    			}
    			if (hashValueSerializer == null) {
    				hashValueSerializer = defaultSerializer;
    				defaultUsed = true;
    			}
    		}
    
    		if (enableDefaultSerializer && defaultUsed) {
    			Assert.notNull(defaultSerializer, "default serializer null and not all serializers initialized");
    		}
    
    		if (scriptExecutor == null) {
    			this.scriptExecutor = new DefaultScriptExecutor<>(this);
    		}
    
    		initialized = true;
    	}
    }
   ```

   **在Redis的源码中，对于没有设置序列化，默认是使用Java的JDK进行序列化，在实际应用开发场景中是不满足需求的，更多情况下我们会使用
    将对象进行序列存储到Redis中，例如下面的例子**

   ```java
    @Test
    public void testSerialization() {
        User fuyi = new User("fuyi", 18);
        redisTemplate.opsForValue().set("user",fuyi);
        log.info("user:" + redisTemplate.opsForValue().get("user"));
    }
   ```

  输出结果

```text
org.springframework.data.redis.serializer.SerializationException: Cannot serialize; nested exception is org.springframework.core.serializer.support.SerializationFailedException: Failed to serialize object using DefaultSerializer; nested exception is java.lang.IllegalArgumentException: DefaultSerializer requires a Serializable payload but received an object of type [com.ryan.springboot.entity.User]

	at org.springframework.data.redis.serializer.JdkSerializationRedisSerializer.serialize(JdkSerializationRedisSerializer.java:96)
	at org.springframework.data.redis.core.AbstractOperations.rawValue(AbstractOperations.java:127)
	at org.springframework.data.redis.core.DefaultValueOperations.set(DefaultValueOperations.java:235)
	at com.ryan.springboot.SpringbootApplicationTests.testSerialization(SpringbootApplicationTests.java:31)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:686)
	at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:131)
	at org.junit.jupiter.engine.extension.TimeoutExtension.intercept(TimeoutExtension.java:149)
	at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestableMethod(TimeoutExtension.java:140)
	at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestMethod(TimeoutExtension.java:84)
	at org.junit.jupiter.engine.execution.ExecutableInvoker$ReflectiveInterceptorCall.lambda$ofVoidMethod$0(ExecutableInvoker.java:115)
	at org.junit.jupiter.engine.execution.ExecutableInvoker.lambda$invoke$0(ExecutableInvoker.java:105)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$InterceptedInvocation.proceed(InvocationInterceptorChain.java:106)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.proceed(InvocationInterceptorChain.java:64)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.chainAndInvoke(InvocationInterceptorChain.java:45)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.invoke(InvocationInterceptorChain.java:37)
	at org.junit.jupiter.engine.execution.ExecutableInvoker.invoke(ExecutableInvoker.java:104)
	at org.junit.jupiter.engine.execution.ExecutableInvoker.invoke(ExecutableInvoker.java:98)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.lambda$invokeTestMethod$6(TestMethodTestDescriptor.java:212)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.invokeTestMethod(TestMethodTestDescriptor.java:208)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:137)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:71)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:135)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:125)
	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:135)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:123)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:122)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:80)
	at java.util.ArrayList.forEach(ArrayList.java:1257)
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:38)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:139)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:125)
	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:135)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:123)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:122)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:80)
	at java.util.ArrayList.forEach(ArrayList.java:1257)
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:38)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:139)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:125)
	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:135)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:123)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:122)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:80)
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.submit(SameThreadHierarchicalTestExecutorService.java:32)
	at org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutor.execute(HierarchicalTestExecutor.java:57)
	at org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine.execute(HierarchicalTestEngine.java:51)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:248)
	at org.junit.platform.launcher.core.DefaultLauncher.lambda$execute$5(DefaultLauncher.java:211)
	at org.junit.platform.launcher.core.DefaultLauncher.withInterceptedStreams(DefaultLauncher.java:226)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:199)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:132)
	at com.intellij.junit5.JUnit5IdeaTestRunner.startRunnerWithArgs(JUnit5IdeaTestRunner.java:69)
	at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
Caused by: org.springframework.core.serializer.support.SerializationFailedException: Failed to serialize object using DefaultSerializer; nested exception is java.lang.IllegalArgumentException: DefaultSerializer requires a Serializable payload but received an object of type [com.ryan.springboot.entity.User]
	at org.springframework.core.serializer.support.SerializingConverter.convert(SerializingConverter.java:64)
	at org.springframework.core.serializer.support.SerializingConverter.convert(SerializingConverter.java:33)
	at org.springframework.data.redis.serializer.JdkSerializationRedisSerializer.serialize(JdkSerializationRedisSerializer.java:94)
	... 66 more
Caused by: java.lang.IllegalArgumentException: DefaultSerializer requires a Serializable payload but received an object of type [com.ryan.springboot.entity.User]
	at org.springframework.core.serializer.DefaultSerializer.serialize(DefaultSerializer.java:43)
	at org.springframework.core.serializer.Serializer.serializeToByteArray(Serializer.java:56)
	at org.springframework.core.serializer.support.SerializingConverter.convert(SerializingConverter.java:60)
	... 68 more
```


**报错的原因就是：无法进行序列化，对于基本数据类型和其对应的包装类、String类型是可以进行序列化的，但是一旦是对象就默认的JDK序列化就不能满足我们的需求**

**因此我们需要进行对RedisTemplate进行自定义配置**

- **自定义配置**

    ```java
  package com.ryan.springboot.config;
  
  
  import com.fasterxml.jackson.annotation.JsonAutoDetect;
  import com.fasterxml.jackson.annotation.PropertyAccessor;
  import com.fasterxml.jackson.databind.ObjectMapper;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.data.redis.connection.RedisConnectionFactory;
  import org.springframework.data.redis.core.*;
  import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
  import org.springframework.data.redis.serializer.StringRedisSerializer;
  
  @Configuration
  public class MyRedisConfig {
  
      @Bean
      public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
          // 我们为了自己开发方便，一般直接使用 <String, Object>
          RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
          template.setConnectionFactory(factory);
  
          // Json序列化配置
          Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
          ObjectMapper om = new ObjectMapper();
          om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
          om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
          jackson2JsonRedisSerializer.setObjectMapper(om);
  
          // String 的序列化
          StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
  
          // key采用String的序列化方式
          template.setKeySerializer(stringRedisSerializer);
  
          // hash的key也采用String的序列化方式
          template.setHashKeySerializer(stringRedisSerializer);
  
          // value序列化方式采用jackson
          template.setValueSerializer(jackson2JsonRedisSerializer);
  
          // hash的value序列化方式采用jackson
          template.setHashValueSerializer(jackson2JsonRedisSerializer);
          template.afterPropertiesSet();
  
          return template;
      }
  
  
      @Bean
      public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
          return redisTemplate.opsForHash();
      }
  
      @Bean
      public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
          return redisTemplate.opsForValue();
      }
  
      @Bean
      public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
          return redisTemplate.opsForList();
      }
  
      @Bean
      public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
          return redisTemplate.opsForSet();
      }
  
      @Bean
      public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
          return redisTemplate.opsForZSet();
      }
  
  }

    ```
  
  
此时再运行测试代码，输出结果

```text
2020-09-22 11:01:48.336  INFO 4504 --- [           main] c.r.s.SpringbootApplicationTests         : Started SpringbootApplicationTests in 1.497 seconds (JVM running for 2.347)

2020-09-22 11:01:49.457  INFO 4504 --- [           main] c.r.s.SpringbootApplicationTests         : user:User(username=fuyi, age=18)

Process finished with exit code 0
```


#### 3. Redis.conf配置文件说明

   - 大小写不敏感
   ```text
    # units are case insensitive so 1GB 1Gb 1gB are all the same.
   ```

   - 网络配置
   
   ```text
    bind 127.0.0.1     # 绑定IP
    protected-mode no  # 保护模式
    port 6379          # 端口号
   ```
   **开启远程访问的时候应该把bind注释掉和protected-mode置为No **
   
   - 通用配置
   
   ```text
   daemonize yes #是否以守护线程运行
   pidfile /var/run/redis_6379.pid # 如果以后台的方式运行，我们就需要指定一个 pid 文件！
   ```

   - 日志配置
   
   ```text
    loglevel notice 
    logfile ""           # 日志的文件位置名 
    databases 16         # 数据库的数量，默认是 16 个数据库 
    always-show-logo yes # 是否总是显示LOGO
   ```

   - 持久化配置
   
   ```text
     
    save 900 1                      # 如果900s内，如果至少有一个1 key进行了修改，我们及进行持久化操作
     
    save 300 10                     # 如果300s内，如果至少10 key进行了修改，我们及进行持久化操作
    
    save 60 10000                   # 如果60s内，如果至少10000 key进行了修改，我们及进行持久化操作 
    
    stop-writes-on-bgsave-error yes # 持久化如果出错，是否还需要继续工作！
 
    rdbcompression yes              # 是否压缩 rdb 文件，需要消耗一些cpu资源！ 

    rdbchecksum yes                 # 保存rdb文件的时候，进行错误的检查校验！ 

    dir ./                          # rdb 文件保存的目录！
   ```

   - 密码配置
   
   ```text
    requirepass 123456   # 配置密码，默认没有密码
   ```

   - 连接配置
   
   ```text
    maxclients 10000            # 设置能连接上redis的最大客户端的数量 
    maxmemory <bytes>           # redis 配置最大的内存容量 
    maxmemory-policy noeviction # 内存到达上限之后的处理策略 
        1、volatile-lru：         只对设置了过期时间的key进行LRU（默认值） 
        2、allkeys-lru ：         删除lru算法的key 
        3、volatile-random：      随机删除即将过期key 
        4、allkeys-random：       随机删除 
        5、volatile-ttl ：        删除即将过期的 
        6、noeviction ：          永不过期，返回错误
   ```

   - aof配置
   
   ```text
    appendonly no                   # 默认是不开启aof模式的，默认是使用rdb方式持久化的，在大部分所有的情况下， rdb完全够用！ 
    appendfilename "appendonly.aof" # 持久化的文件的名字 
    # appendfsync always            # 每次修改都会 sync。消耗性能 
    appendfsync everysec            # 每秒执行一次 sync，可能会丢失这1s的数据！ 
    # appendfsync no                # 不执行 sync，这个时候操作系统自己同步数据，速度最快！
   ```


# Redis持久化

#### 1. RDB
在指定时间间隔内进行快照存储。


#### 2. AOF
 
记录对服务器写的操作，AOF命令会以Redis协议追加到每次写操作文件的结尾，当服务器重启的时候执行这些命令进行恢复原始数据。


    






   
   
        
   
   

