package com.test.personservice.infrastructure.adapter.out.cache.redis.adapter;

import com.test.personservice.domain.model.Person;
import com.test.personservice.infrastructure.adapter.out.cache.redis.entity.PersonEntity;
import com.test.personservice.infrastructure.adapter.out.cache.redis.mapper.RedisPersonEntityMapper;
import java.time.Duration;
import java.util.UUID;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PersonCacheRedis {

  private static final String PREFIX_KEY = "PERSONS";
  private final ReactiveRedisTemplate<String, PersonEntity> redisTemplate;
  private final RedisPersonEntityMapper mapper;

  public PersonCacheRedis(ReactiveRedisTemplate<String, PersonEntity> redisTemplate,
      RedisPersonEntityMapper mapper) {
    this.redisTemplate = redisTemplate;
    this.mapper = mapper;
  }

  public Mono<Person> findByKey(UUID key) {
    return this.redisTemplate.opsForValue()
        .get(getComposeKey(key))
        .map(mapper::toDomain);
  }

  public Mono<Void> save(UUID key, Person person) {
    var userEntity = mapper.toEntity(person);
    return this.redisTemplate.opsForValue()
        .set(getComposeKey(key), userEntity, Duration.ofMinutes(30L))
        .then();
  }

  public Mono<Void> deleteByKey(UUID key) {
    return this.redisTemplate.opsForValue()
        .getAndDelete(getComposeKey(key))
        .then();
  }

  private String getComposeKey(UUID key) {
    return String.join(":", PREFIX_KEY, key.toString());
  }

}
