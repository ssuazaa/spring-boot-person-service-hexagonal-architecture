package com.test.personservice.infrastructure.adapter.in.rest.mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.personservice.domain.model.Person;
import com.test.personservice.infrastructure.adapter.in.rest.dto.PersonRequestDto;
import com.test.personservice.infrastructure.adapter.in.rest.dto.PersonResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface PersonRestMapper {

  Person toDomain(PersonRequestDto personRequestDto);

  PersonResponseDto toResponse(Person person);

}

