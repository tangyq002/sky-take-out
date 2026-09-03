package com.sky.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.extern.slf4j.Slf4j;

/**
 * redis配置类
 * @author tyq
 * @date 2026年9月3日
 * @project_name sky-server
 * @package_name com.sky.config
 * @file_name RedisConfiguration.java
 * @classname RedisConfiguration
 * @version 2026年9月3日 下午5:15:51
 */
@Configuration
@Slf4j
public class RedisConfiguration {
	
	/**
	 * 创建RedisTemplate对象
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
		log.info("开始创建redisTemplate:{}",redisConnectionFactory);
		RedisTemplate redisTemplate = new RedisTemplate();
		//设置Key的序列化器，默认为JdkSerializationRedisSerializer
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		//设置redis连接工厂对象
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}
}
