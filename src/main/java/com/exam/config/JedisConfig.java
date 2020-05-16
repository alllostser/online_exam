package com.exam.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
public class JedisConfig {
    private Integer maxTotal=8;

    private Integer maxIdle=8;

    private Integer minIdle=0;

    private boolean blockWhenExhausted=true;

    private Integer maxWaitMillis=1000;

    private boolean testOnBorrow=true;

    private boolean testOnReturn=true;

    private boolean jmxEnabled=true;


    private String redisHost="123.56.75.159";

    private Integer redisPort=6379;

    private Integer timeout=1000;
    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig(){
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setBlockWhenExhausted(blockWhenExhausted);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setJmxEnabled(jmxEnabled);
        return config;
    }
    /**
     * JedisPool的初始化，交给Ioc管理
     * */
    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(8);
        config.setMaxIdle(8);
        config.setMinIdle(0);
        config.setBlockWhenExhausted(true);
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(true );
        config.setTestOnReturn(true);
        config.setJmxEnabled(true);
        JedisPool jedisPool = new JedisPool(genericObjectPoolConfig(),redisHost,redisPort, timeout);
        return jedisPool;
    }
}