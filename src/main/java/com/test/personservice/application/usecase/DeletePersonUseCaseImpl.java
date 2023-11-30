package com.test.personservice.application.usecase;

import com.test.personservice.domain.model.Person;
import com.test.personservice.domain.port.in.DeletePersonUseCase;
import com.test.personservice.domain.port.in.FindPersonUseCase;
import com.test.personservice.domain.port.out.PersonCacheOut;
import com.test.personservice.domain.port.out.PersonRepositoryOut;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class DeletePersonUseCaseImpl implements DeletePersonUseCase {

  private final FindPersonUseCase findPersonUseCase;
  private final PersonCacheOut personCacheOut;
  private final PersonRepositoryOut personRepositoryOut;

  public DeletePersonUseCaseImpl(FindPersonUseCase findPersonUseCase, PersonCacheOut personCacheOut,
      PersonRepositoryOut personRepositoryOut) {
    this.findPersonUseCase = findPersonUseCase;
    this.personCacheOut = personCacheOut;
    this.personRepositoryOut = personRepositoryOut;
  }

  @Override
  public Mono<Void> delete(UUID id) {
    return this.findPersonUseCase.findById(id)
        .map(Person::id)
        .flatMap((UUID personId) -> this.personRepositoryOut.deleteById(personId)
            .then(this.personCacheOut.deleteByKey(personId)))
        .then();
  }

}
