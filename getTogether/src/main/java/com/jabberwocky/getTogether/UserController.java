package com.jabberwocky.getTogether;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;


@RestController
public class UserController {	
	@Autowired 
	private UserRepo repo;
	@CrossOrigin
	@GetMapping("/users")
	public @ResponseBody Iterable<User> getAllUsers() {
	    // This returns a JSON or XML with the users
	    return repo.findAll();
	  }
	@CrossOrigin
	@PostMapping("/users/new")
	 public @ResponseBody String addNewUser (@RequestParam String name, @RequestParam String email) {
		System.out.print("hello") ;
		System.out.print(name + email) ;
		User n = new User();
	    n.setName(name);
	    n.setEmail(email);
	    repo.save(n);
	    return "Saved";
	}
}