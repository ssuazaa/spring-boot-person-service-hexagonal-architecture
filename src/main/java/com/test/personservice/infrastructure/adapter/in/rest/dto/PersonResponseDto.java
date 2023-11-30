package com.test.personservice.infrastructure.adapter.in.rest.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record PersonResponseDto(UUID id, String firstName, String lastName) {

}
