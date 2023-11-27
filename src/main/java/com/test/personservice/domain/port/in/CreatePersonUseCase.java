package com.test.personservice.domain.port.in;

import com.test.personservice.domain.model.Person;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface CreatePersonUseCase {

  Mono<UUID> create(Person person);

}
