package com.test.personservice.application.usecase;

import com.test.personservice.domain.model.Person;
import com.test.personservice.domain.port.in.FindPersonUseCase;
import com.test.personservice.domain.port.in.UpdatePersonUseCase;
import com.test.personservice.domain.port.out.PersonRepositoryOut;
import com.test.personservice.util.ValidationUtil;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class UpdatePersonUseCaseImpl implements UpdatePersonUseCase {

  private final FindPersonUseCase findPersonUseCase;
  private final PersonRepositoryOut personRepositoryOut;

  public UpdatePersonUseCaseImpl(FindPersonUseCase findPersonUseCase,
      PersonRepositoryOut personRepositoryOut) {
    this.findPersonUseCase = findPersonUseCase;
    this.personRepositoryOut = personRepositoryOut;
  }

  @Override
  public Mono<Void> update(UUID id, Person person) {
    ValidationUtil.validateParamOrThrow(id, "ID_IS_MANDATORY", "The id attribute is mandatory");
    ValidationUtil.validateParamOrThrow(person, "PERSON_IS_MANDATORY",
        "The person attribute cannot be null");
    return this.findPersonUseCase.findById(id)
        .map((Person personDb) -> personDb.toBuilder()
            .firstName(person.firstName())
            .lastName(person.lastName())
            .build())
        .flatMap(this.personRepositoryOut::save)
        .then();
  }

}
