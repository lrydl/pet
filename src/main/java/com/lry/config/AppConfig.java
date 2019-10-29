package com.lry.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource("classpath:db.properties")
@ComponentScan(value = "com.lry",excludeFilters = {@ComponentScan.Filter(value = Controller.class)})
public class AppConfig {
 
	/**
	 * 获取数据源
	 * 
	 * @param driver
	 * @param url
	 * @param name
	 * @param password
	 * @return
	 */
	@Bean("dataSource")
	public DruidDataSource getDruidDataSource(@Value("${driver}") String driver, @Value("${url}") String url,
											  @Value("${name}") String name, @Value("${password}") String password) {
 
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(name);
		dataSource.setPassword(password);
 
		return dataSource;
	}
 
	/**
	 * 创建sqlSessionFactory对象
	 * 
	 * @param dataSource
	 * @return
	 */
	@Bean("sqlSessionFactory")
	public SqlSessionFactoryBean getSqlSessionFactory(@Autowired DruidDataSource dataSource) {
 
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.addMappers("com.lry.mapper");
		sessionFactoryBean.setConfiguration(configuration);
 
		return sessionFactoryBean;
	}
 
	/**
	 * 扫描dao包
	 * 
	 * @return
	 */
	@Bean("mapperScannerConfigurer")
	public MapperScannerConfigurer getMapperScannerConfigurer() {
 
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("com.lry.mapper");
		configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		return configurer;
	}


	@Bean("redisConnectionFactory")
	public RedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory fac = new JedisConnectionFactory();
		fac.setHostName("192.168.235.100");
		fac.setPort(6379);
		return fac;
	}

	@Bean("redisTemplate")
	public RedisTemplate<String, Object> getRedisTemplate(@Autowired RedisConnectionFactory redisConnectionFactory){
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(@Autowired RedisConnectionFactory factory) {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(factory);
		return stringRedisTemplate;
	}
}
