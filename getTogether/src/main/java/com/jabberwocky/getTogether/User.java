package com.jabberwocky.getTogether;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private String hash;
	private int password;
	private String username;
	private String pw;
	
	public User(String fullName, String email, String username, String pw) {
		this.fullName = fullName;
		this.email = email;
		//interests = new ArrayList<>();
		contacts = new ArrayList<>();
		events = new ArrayList<>();
		//subbedBusinesses = new ArrayList<>();
		/*create random number hash, store hash + hashed pw*/
		hash = randomHash();
		this.password = (new String(pw + hash)).hashCode();
		this.username = username;
	}

	public boolean isFreeAt(LocalDateTime startTime, int duration) {
		LocalDateTime endTime = startTime.plusMinutes(duration);
		for (Event e:events) {
			if (e.hasOverlapWith(startTime, endTime)) {
				return false;
			}
		}
		return true;
	}
	
	
	// This Stack Overflow link helped me: https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
	public String randomHash() { 
  
        // chose a Character random from this String 
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder hash = new StringBuilder(4); 
  
        for (int i = 0; i < 4; i++) { 
 
            int index = (int)(alphaNumericString.length() * Math.random()); 
            hash.append(alphaNumericString.charAt(index)); 
        } 
  
        return hash.toString(); 
    } 
	
	
	// This link allowed for remove of pw from db: https://www.baeldung.com/jackson-field-serializable-deserializable-or-not
	@JsonIgnore
	public String getPw() {
	    return pw;
	}
}
