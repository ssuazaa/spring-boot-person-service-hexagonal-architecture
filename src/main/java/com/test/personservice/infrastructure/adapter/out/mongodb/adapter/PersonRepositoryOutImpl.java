package com.test.personservice.infrastructure.adapter.out.mongodb.adapter;

import com.test.personservice.domain.model.Person;
import com.test.personservice.domain.port.out.PersonRepositoryOut;
import com.test.personservice.infrastructure.adapter.out.mongodb.config.PersonRepositoryMongoDB;
import com.test.personservice.infrastructure.adapter.out.mongodb.mapper.PersonEntityMapper;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PersonRepositoryOutImpl implements PersonRepositoryOut {

  private final PersonEntityMapper mapper;
  private final PersonRepositoryMongoDB repository;

  public PersonRepositoryOutImpl(PersonEntityMapper mapper, PersonRepositoryMongoDB repository) {
    this.mapper = mapper;
    this.repository = repository;
  }

  @Override
  public Mono<List<Person>> findAll() {
    return repository.findAll()
        .map(mapper::toDomain)
        .collectList();
  }

  @Override
  public Mono<Person> findById(UUID id) {
    return repository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public Mono<Person> save(Person person) {
    return repository.save(mapper.toEntity(person))
        .map(mapper::toDomain);
  }

}
