package com.test.personservice.infrastructure.adapter.out.cache.redis.entity;

import java.util.UUID;

public record PersonEntity(UUID id,
                           String firstName,
                           String lastName,
                           Long version) {

}
