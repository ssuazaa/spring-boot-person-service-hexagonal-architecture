package com.test.personservice.infrastructure.adapter.out.persistance.mongodb.config;

import com.test.personservice.infrastructure.adapter.out.persistance.mongodb.entity.PersonEntity;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoDBRepositoryConfig extends ReactiveMongoRepository<PersonEntity, UUID> {

}
