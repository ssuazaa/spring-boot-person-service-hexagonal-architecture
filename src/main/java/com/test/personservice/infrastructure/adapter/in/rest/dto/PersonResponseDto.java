package com.test.personservice.infrastructure.adapter.in.rest.dto;

import java.util.UUID;

public record PersonResponseDto(UUID id, String firstName, String lastName) {

}
