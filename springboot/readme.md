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
            <!--fastjson-->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>fastjson</artifactId>
                <version>1.2.73</version>
            </dependency>

            <!-- jsckson依赖-->
            <dependency>
                <groupId>com.fasterxml.jackson.core</groupId>
                <artifactId>jackson-databind</artifactId>
                <version>2.11.2</version>
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

    


