package com.test.personservice.infrastructure.adapter.out.cache.redis.mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.personservice.domain.model.Person;
import com.test.personservice.infrastructure.adapter.out.cache.redis.entity.PersonEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface RedisPersonEntityMapper {

  Person toDomain(PersonEntity personEntity);

  @InheritInverseConfiguration
  PersonEntity toEntity(Person person);

}
