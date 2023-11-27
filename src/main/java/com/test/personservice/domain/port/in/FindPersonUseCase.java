package com.test.personservice.domain.port.in;

import com.test.personservice.domain.model.Person;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface FindPersonUseCase {

  Mono<List<Person>> findAll();

  Mono<Person> findById(UUID id);

}
