package com.test.personservice.infrastructure.adapter.out.cache.redis.config;

import com.test.personservice.infrastructure.adapter.out.cache.redis.entity.PersonEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisTemplateConfig {

  @Bean
  public ReactiveRedisTemplate<String, PersonEntity> reactiveRedisTemplate(
      ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
    var serializationContext = RedisSerializationContext
        .<String, PersonEntity>newSerializationContext(new GenericJackson2JsonRedisSerializer())
        .build();

    return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
  }

}
