package com.test.personservice.domain.port.in;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DeletePersonUseCase {

  Mono<Void> delete(UUID id);

}
