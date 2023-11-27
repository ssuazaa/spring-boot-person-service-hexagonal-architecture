package com.test.personservice.domain.model;

import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record Person(UUID id,
                     String firstName,
                     String lastName,
                     Long version) {

}
