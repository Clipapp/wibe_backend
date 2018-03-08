package com.wibe.backend.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.wibe.backend.entities.models.Otp;

@Configuration
public class RedisConfiguration {
 
   @Inject
   private JedisConnectionFactory jedisConnFactory;
	
   @Bean
   public StringRedisSerializer stringRedisSerializer() {
      StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
      return stringRedisSerializer;
   }
    
   @Bean
   public JacksonJsonRedisSerializer<Otp> jacksonJsonRedisJsonSerializer() {
      JacksonJsonRedisSerializer<Otp> jacksonJsonRedisJsonSerializer = new JacksonJsonRedisSerializer<>(Otp.class);
      return jacksonJsonRedisJsonSerializer;
   }
    
   @Bean
   public RedisTemplate<String, Otp> redisTemplate() {
      RedisTemplate<String, Otp> redisTemplate = new RedisTemplate<>();
      redisTemplate.setConnectionFactory(jedisConnFactory);
      redisTemplate.setKeySerializer(stringRedisSerializer());
      redisTemplate.setValueSerializer(jacksonJsonRedisJsonSerializer());
      return redisTemplate;
   }
}