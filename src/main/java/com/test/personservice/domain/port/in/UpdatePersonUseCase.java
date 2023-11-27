package com.test.personservice.domain.port.in;

import com.test.personservice.domain.model.Person;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UpdatePersonUseCase {

  Mono<Void> update(UUID id, Person person);

}
