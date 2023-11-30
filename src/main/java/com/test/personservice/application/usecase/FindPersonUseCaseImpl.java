package com.test.personservice.application.usecase;

import com.test.personservice.domain.model.Person;
import com.test.personservice.domain.port.in.FindPersonUseCase;
import com.test.personservice.domain.port.out.PersonCacheOut;
import com.test.personservice.domain.port.out.PersonRepositoryOut;
import com.test.personservice.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class FindPersonUseCaseImpl implements FindPersonUseCase {

  private final PersonCacheOut personCacheOut;
  private final PersonRepositoryOut personRepositoryOut;

  public FindPersonUseCaseImpl(PersonCacheOut personCacheOut,
      PersonRepositoryOut personRepositoryOut) {
    this.personCacheOut = personCacheOut;
    this.personRepositoryOut = personRepositoryOut;
  }

  @Override
  public Mono<List<Person>> findAll() {
    return this.personRepositoryOut.findAll();
  }

  @Override
  public Mono<Person> findById(UUID id) {
    return this.personCacheOut.findByKey(id)
        .switchIfEmpty(Mono.defer(() -> this.personRepositoryOut.findById(id))
            .flatMap((Person person) -> this.personCacheOut.save(person.id(), person)
                .then(Mono.just(person))))
        .switchIfEmpty(Mono.error(() -> new ObjectNotFoundException("PERSON_NOT_FOUND",
            String.format("Person with id '%s' was not found", id))));
  }

}
