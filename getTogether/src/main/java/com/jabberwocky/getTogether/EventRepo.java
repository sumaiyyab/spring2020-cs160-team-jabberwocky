package com.jabberwocky.getTogether;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepo extends MongoRepository<Event, String> {
}