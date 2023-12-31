package com.test.personservice.application.usecase;

import com.test.personservice.domain.model.Person;
import com.test.personservice.domain.port.in.FindPersonUseCase;
import com.test.personservice.domain.port.in.UpdatePersonUseCase;
import com.test.personservice.domain.port.out.PersonCacheOut;
import com.test.personservice.domain.port.out.PersonRepositoryOut;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class UpdatePersonUseCaseImpl implements UpdatePersonUseCase {

  private final FindPersonUseCase findPersonUseCase;
  private final PersonCacheOut personCacheOut;
  private final PersonRepositoryOut personRepositoryOut;

  public UpdatePersonUseCaseImpl(FindPersonUseCase findPersonUseCase, PersonCacheOut personCacheOut,
      PersonRepositoryOut personRepositoryOut) {
    this.findPersonUseCase = findPersonUseCase;
    this.personCacheOut = personCacheOut;
    this.personRepositoryOut = personRepositoryOut;
  }

  @Override
  public Mono<Void> update(UUID id, Person person) {
    return this.findPersonUseCase.findById(id)
        .map((Person personDb) -> personDb.toBuilder()
            .firstName(person.firstName())
            .lastName(person.lastName())
            .build())
        .flatMap(this.personRepositoryOut::save)
        .flatMap((Person personDb) -> this.personCacheOut.deleteByKey(personDb.id()))
        .then();
  }

}
