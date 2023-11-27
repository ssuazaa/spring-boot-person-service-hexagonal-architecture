package com.test.personservice.infrastructure.adapter.out.mongodb.mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.personservice.domain.model.Person;
import com.test.personservice.infrastructure.adapter.out.mongodb.entity.PersonEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface PersonEntityMapper {

  Person toDomain(PersonEntity personEntity);

  @InheritInverseConfiguration
  PersonEntity toEntity(Person person);

}

