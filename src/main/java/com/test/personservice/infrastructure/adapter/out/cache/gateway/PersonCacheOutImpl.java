package com.test.personservice.infrastructure.adapter.out.cache.gateway;

import com.test.personservice.domain.model.Person;
import com.test.personservice.domain.port.out.PersonCacheOut;
import com.test.personservice.infrastructure.adapter.out.cache.redis.adapter.PersonCacheRedis;
import java.util.UUID;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PersonCacheOutImpl implements PersonCacheOut {

  private final PersonCacheRedis repository;

  public PersonCacheOutImpl(PersonCacheRedis repository) {
    this.repository = repository;
  }

  @Override
  public Mono<Person> findByKey(UUID key) {
    return this.repository.findByKey(key);
  }

  @Override
  public Mono<Void> save(UUID key, Person person) {
    return this.repository.save(key, person);
  }

  @Override
  public Mono<Void> deleteByKey(UUID key) {
    return this.repository.deleteByKey(key);
  }

}
