package com.sky.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 阿里云oss配置类
 * @author tyq
 * @date 2026年8月26日
 * @project_name sky-server
 * @package_name com.sky.config
 * @file_name OssConfiguration.java
 * @classname OssConfiguration
 * @version 2026年8月26日 下午6:15:55
 */
@Configuration
@Slf4j
public class OssConfiguration {
	
	/**
	 * 创建阿里云工具类
	 * @param aliOssProperties
	 * @return
	 */
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
    	log.info("开始创建阿里云OSS工具类");
    	return new AliOssUtil(aliOssProperties.getEndpoint(),
    			aliOssProperties.getAccessKeyId(),
    			aliOssProperties.getAccessKeySecret(),
    			aliOssProperties.getBucketName());
    }
}
