package com.test.personservice.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record PersonRequestDto(
    @NotBlank(message = "FirstName is required and cannot be empty") String firstName,
    @NotBlank(message = "LastName is required and cannot be empty") String lastName) {

}
