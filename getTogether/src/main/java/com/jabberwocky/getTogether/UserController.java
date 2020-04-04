package com.jabberwocky.getTogether;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;


@RestController
@CrossOrigin
public class UserController {	
	@Autowired 
	private UserRepo repo;

	@GetMapping("/users")
	public @ResponseBody ResponseEntity<List<User>> getAllUsers() {
		// This returns a JSON or XML with the users
		return ResponseEntity.ok(repo.findAll());
	}

	@PostMapping("/users")
	public @ResponseBody ResponseEntity<User> addNewUser (@RequestBody User newUser) {
		repo.save(newUser);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
	}

	@GetMapping("/users/{id}")
	public @ResponseBody ResponseEntity<Optional<User>> getUserById (@PathVariable String id){
		return ResponseEntity.ok(repo.findById(id));
	}

	@PutMapping("/users/{id}")
	public @ResponseBody ResponseEntity<User> updateUserById (@PathVariable String id, @RequestBody User updatedUser){
		repo.save(updatedUser);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/users/{id}")
	public @ResponseBody ResponseEntity<User> deleteUserById (@PathVariable String id) {
		repo.deleteById(id);
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}
}