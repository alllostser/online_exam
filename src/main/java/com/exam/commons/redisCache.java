package com.exam.commons;

import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

public class redisCache {
    @Resource
    JedisPool jedisPool;
    /**
     * 1.redis的控制器，操作redis
     * */
    @Bean("redisManager")
    public RedisManager redisManager(){

        RedisManager redisManager = new RedisManager();
//        redisManager.setHost("123.56.75.159");
//        redisManager.setPort(6379);
//        redisManager.setTimeout(1000);
        redisManager.setJedisPool(jedisPool);
        return redisManager;
    }

    /**
     *2.sessionDao
     * */
    @Bean("redisSessionDAO")
    public RedisSessionDAO redisSessionDAO(@Qualifier("redisManager") RedisManager redisManager ){
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager);
        return sessionDAO;
    }

    /**
     *3.会话管理器
     * */
//    @Bean
//    public DefaultWebSessionManager sessionManager(){
//        CustomSessionManager sessionManager = new CustomSessionManager();
//        sessionManager.setSessionDAO(redisSessionDAO());
//        //禁用cookie
//        sessionManager.setSessionIdCookieEnabled(false);
//        //禁用url重写 url：jsessionid=id
//        sessionManager.setSessionIdUrlRewritingEnabled(false);
//        return sessionManager;
//    }

    /**
     *4.缓存管理器
     * */
    @Bean("cacheManager")
    public RedisCacheManager cacheManager(@Qualifier("redisManager") RedisManager redisManager){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        return redisCacheManager;
    }
}
