package com.test.personservice.application.usecase;

import com.test.personservice.domain.model.Person;
import com.test.personservice.domain.port.in.CreatePersonUseCase;
import com.test.personservice.domain.port.out.PersonRepositoryOut;
import com.test.personservice.util.ValidationUtil;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class CreatePersonUseCaseImpl implements CreatePersonUseCase {

  private final PersonRepositoryOut personRepositoryOut;

  public CreatePersonUseCaseImpl(PersonRepositoryOut personRepositoryOut) {
    this.personRepositoryOut = personRepositoryOut;
  }

  @Override
  public Mono<UUID> create(Person person) {
    ValidationUtil.validateParamOrThrow(person, "PERSON_IS_MANDATORY",
        "The person attribute cannot be null");
    return Mono.just(person.toBuilder()
            .id(UUID.randomUUID())
            .build())
        .flatMap(this.personRepositoryOut::save)
        .map(Person::id);
  }

}
