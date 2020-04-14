package com.jabberwocky.getTogether;

import java.util.ArrayList;
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
import org.json.*;

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
	
	@PostMapping("/login")
	public @ResponseBody ResponseEntity<User> loginUser (@RequestBody JSONObject userInfo) {
		System.out.println(userInfo);
		
		//Optional<User> opt = repo.findById(id);
//		String username = userInfo.substring(3, userInfo.indexOf(',') - 1);
//		String password = userInfo.substring(userInfo.indexOf(',') + 4, userInfo.length() - 3);


		String password = null;
		String username = null;
		try {
			password = userInfo.getString("password");
			username = userInfo.getString("username");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		System.out.println(username);
		System.out.println(password);

		ArrayList<User> users = repo.findByUsername(username);
		System.out.println(users);
		if (users.size() > 0) {

			for(User user: users) {
				System.out.println((password + user.getHash()).hashCode());
				System.out.println(user.getPassword());

				System.out.println(user.getId());

				if(user.getPassword() == (password + user.getHash()).hashCode()) {
					ResponseEntity<User> returnUser = this.getUserById(user.getId());
					return returnUser;
				}
			}
			return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
		}
		else 
			return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/users/{id}")
	public @ResponseBody ResponseEntity<User> getUserById (@PathVariable String id){
		Optional<User> opt = repo.findById(id);
		if (opt.isPresent()) {
			return ResponseEntity.ok(opt.get());
		}
		else 
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
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