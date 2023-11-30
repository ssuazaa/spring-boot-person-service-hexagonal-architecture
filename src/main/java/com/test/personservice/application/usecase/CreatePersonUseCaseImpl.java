package com.test.personservice.application.usecase;

import com.test.personservice.domain.model.Person;
import com.test.personservice.domain.port.in.CreatePersonUseCase;
import com.test.personservice.domain.port.out.PersonRepositoryOut;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class CreatePersonUseCaseImpl implements CreatePersonUseCase {

  private final PersonRepositoryOut personRepositoryOut;

  public CreatePersonUseCaseImpl(PersonRepositoryOut personRepositoryOut) {
    this.personRepositoryOut = personRepositoryOut;
  }

  @Override
  public Mono<UUID> create(Person person) {
    return Mono.just(person.toBuilder()
            .id(UUID.randomUUID())
            .build())
        .flatMap(this.personRepositoryOut::save)
        .map(Person::id);
  }

}
