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
public class EventController {	
	@Autowired 
	private EventRepo repo;

	@GetMapping("/events")
	public @ResponseBody ResponseEntity<List<Event>> getAllEvents() {
		// This returns a JSON or XML with the users
		return ResponseEntity.ok(repo.findAll());
	}

	@PostMapping("/events")
	public @ResponseBody ResponseEntity<Event> addNewEvent (@RequestBody Event newEvent) {
		repo.save(newEvent);
		return new ResponseEntity<Event>(newEvent, HttpStatus.CREATED);
	}

	// instead of returning optional, make sure it contains something before returning it or else 404
	@GetMapping("/event/{id}")
	public @ResponseBody ResponseEntity<Optional<Event>> getEventById (@PathVariable String id){
		return ResponseEntity.ok(repo.findById(id));
	}

	@PutMapping("/event/{id}")
	public @ResponseBody ResponseEntity<Event> updateEventById (@PathVariable String id, @RequestBody Event updatedEvent){
		repo.save(updatedEvent);
		return ResponseEntity.ok(updatedEvent);
	}

	@DeleteMapping("/event/{id}")
	public @ResponseBody ResponseEntity<Event> deleteEventById (@PathVariable String id) {
		repo.deleteById(id);
		return new ResponseEntity<Event>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/events/{id}/attendees")
	public @ResponseBody ResponseEntity<List<User>> getAllEventAttendees(String id) {
		// This returns a JSON or XML with the users
		return ResponseEntity.ok(repo.findById(id).get().getRSVP());
	}
	
	@PostMapping("/events/{id}/attendees")
	public @ResponseBody ResponseEntity<List<User>> addAttendee (@RequestBody User user, Event event, String eventID) {
		event.addAttendee(user);
		repo.save(event);
		return ResponseEntity.ok(repo.findById(eventID).get().getRSVP());
	}
	
	
}