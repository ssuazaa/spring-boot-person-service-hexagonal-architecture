package com.test.personservice.infrastructure.adapter.out.mongodb.config;

import com.test.personservice.infrastructure.adapter.out.mongodb.entity.PersonEntity;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PersonRepositoryMongoDB extends ReactiveMongoRepository<PersonEntity, UUID> {

}
