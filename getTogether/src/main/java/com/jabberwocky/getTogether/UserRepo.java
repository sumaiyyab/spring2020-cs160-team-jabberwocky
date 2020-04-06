package com.jabberwocky.getTogether;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, String> {
}