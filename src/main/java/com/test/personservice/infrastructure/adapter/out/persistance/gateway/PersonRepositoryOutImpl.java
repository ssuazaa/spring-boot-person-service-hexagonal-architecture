package com.test.personservice.infrastructure.adapter.out.persistance.gateway;

import com.test.personservice.domain.model.Person;
import com.test.personservice.domain.port.out.PersonRepositoryOut;
import com.test.personservice.infrastructure.adapter.out.persistance.mongodb.adapter.PersonRepositoryMongoDb;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PersonRepositoryOutImpl implements PersonRepositoryOut {

  private final PersonRepositoryMongoDb repository;

  public PersonRepositoryOutImpl(PersonRepositoryMongoDb repository) {
    this.repository = repository;
  }

  @Override
  public Mono<List<Person>> findAll() {
    return this.repository.findAll();
  }

  @Override
  public Mono<Person> findById(UUID id) {
    return this.repository.findById(id);
  }

  @Override
  public Mono<Person> save(Person person) {
    return this.repository.save(person);
  }

  @Override
  public Mono<Void> deleteById(UUID id) {
    return this.repository.deleteById(id);
  }

}
