package com.jabberwocky.getTogether;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepo extends MongoRepository<User, String> {
	
	// This Stack Overflow link helped me: https://stackoverflow.com/questions/33494444/mongorepository-findbycreatedatbetween-not-returning-accurate-results
	@Query(value = "{\"username\": ?0 }")
	public ArrayList<User> findByUsername(String username);
}