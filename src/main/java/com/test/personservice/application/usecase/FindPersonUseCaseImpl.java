package com.test.personservice.application.usecase;

import com.test.personservice.domain.model.Person;
import com.test.personservice.domain.port.in.FindPersonUseCase;
import com.test.personservice.domain.port.out.PersonRepositoryOut;
import com.test.personservice.infrastructure.config.exceptions.ObjectNotFoundException;
import com.test.personservice.util.ValidationUtil;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class FindPersonUseCaseImpl implements FindPersonUseCase {

  private final PersonRepositoryOut personRepositoryOut;

  public FindPersonUseCaseImpl(PersonRepositoryOut personRepositoryOut) {
    this.personRepositoryOut = personRepositoryOut;
  }

  @Override
  public Mono<List<Person>> findAll() {
    return this.personRepositoryOut.findAll();
  }

  @Override
  public Mono<Person> findById(UUID id) {
    ValidationUtil.validateParamOrThrow(id, "ID_IS_MANDATORY", "The id attribute is mandatory");
    return this.personRepositoryOut.findById(id)
        .switchIfEmpty(Mono.error(() -> new ObjectNotFoundException("PERSON_NOT_FOUND",
            String.format("Person with id '%s' was not found", id))));
  }

}
