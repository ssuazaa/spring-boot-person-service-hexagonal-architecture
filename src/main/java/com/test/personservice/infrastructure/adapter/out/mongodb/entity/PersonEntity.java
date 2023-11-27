package com.test.personservice.infrastructure.adapter.out.mongodb.entity;

import java.util.UUID;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "persons")
public record PersonEntity(@MongoId UUID id,
                           String firstName,
                           String lastName,
                           @Version Long version) {

}
