package com.jabberwocky.getTogether;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	@Id
	private String id;

	private String fullName;
	private String email;
	//private ArrayList<String> interests;
	private ArrayList<User> contacts;
	//private ArrayList<Business> subbedBusinesses;
	private ArrayList<Event> events;
	
	public User(String fullName, String email/*, String pw*/) {
		this.fullName = fullName;
		this.email = email;
		//interests = new ArrayList<>();
		contacts = new ArrayList<>();
		//subbedBusinesses = new ArrayList<>();
		/*create random number hash, store hash + hashed pw*/
		events = new ArrayList<>();
	}

}
