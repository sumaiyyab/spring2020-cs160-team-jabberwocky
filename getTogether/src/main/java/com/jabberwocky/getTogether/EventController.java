package com.jabberwocky.getTogether;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
	
	// Always use autowired to make sure it's connected for repos and controllers 
	@Autowired 
	private UserController userCont;

	@GetMapping("/events")
	public @ResponseBody ResponseEntity<List<Event>> getAllEvents() {
		// This returns a JSON or XML with the users
		return ResponseEntity.ok(repo.findAll());
	}
	
	
	// instead of returning optional, make sure it contains something before returning it or else 404
	@GetMapping("/events/{id}")
	public @ResponseBody ResponseEntity<Event> getEventById (@PathVariable String id){
		
		Optional<Event> event = repo.findById(id);
		if (event.isPresent()) {
			return ResponseEntity.ok(event.get());
		}
		else {
			return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
		}
		
	}

	@PostMapping("/events")
	public @ResponseBody ResponseEntity<Event> addNewEvent (@RequestBody Event newEvent) {
		// This returns the response entity - http header and the body of the data (meat of the json)
		// Response entity - creates http package, getBody returns data
		// getUserById returns an option, and an option is holding a user
		

		// Making sure that the Host ID isn't null
		if(newEvent.getHostID() != null) {
			Optional<User> host = userCont.getUserById(newEvent.getHostID()).getBody();		
			
			// Makes sure that the Host ID is an existing user, and that the title isn't null
			if(host.isPresent() && newEvent.getTitle() != null) {
				
				// Check for Constructor 1
				if(newEvent.getStartTime() != null) {
					
					// Ensures that startTime is before endTime, the location isn't null, and that the tag list isn't null
					if(newEvent.getStartTime().isBefore(newEvent.getEndTime()) && newEvent.getLocation() != null && newEvent.getTags() != null) {
						ArrayList<User> invited = newEvent.getInvited();
						for(User user : invited) {
							host = userCont.getUserById(user.getId()).getBody();
							if(!host.isPresent()) {
								return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
							}	
						}
						
						// Assuming the application has checked the entire rsvp list with no errors, the new event will be saved
						repo.save(newEvent);
						return new ResponseEntity<Event>(newEvent, HttpStatus.CREATED);	
					}	
				}
				
				
				// Check for Constructor 2
				else if(newEvent.getStartPossible() != null) {
					
					// Ensures that startPossible is before endPossible, the invited list isn't null , and that there's a duration longer than 0 minutes
					if(newEvent.getStartPossible().isBefore(newEvent.getEndPossible()) && newEvent.getInvited() != null && newEvent.getDuration() > 0) {
						ArrayList<User> invited = newEvent.getInvited();
						for(User user : invited) {
							host = userCont.getUserById(user.getId()).getBody();
							if(!host.isPresent()) {
								return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
							}	
						}
						repo.save(newEvent);
						return new ResponseEntity<Event>(newEvent, HttpStatus.CREATED);
					}	
				}
				
			}else {
				return new ResponseEntity<Event> (HttpStatus.NOT_ACCEPTABLE); // 406
			}
		}
		
		return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);	// 404
	}



	@PutMapping("/events/{id}")
	public @ResponseBody ResponseEntity<Event> updateEventById (@PathVariable String id, @RequestBody Event updatedEvent){
		
		// Event stored in db that will be updated
		Event event = this.getEventById(id).getBody();	
		
		// Verifying that the updatedEvent's host user is stored in the user db
		Optional<User> host = userCont.getUserById(updatedEvent.getHostID()).getBody();	
		if(host.isPresent() && updatedEvent.getTitle() != null) {
			
			// Checks for Constructor 1
			if(updatedEvent.getStartTime() != null) {
				
				// Ensures that startTime is before endTime, the location isn't null, and that the tag list isn't null
				if(updatedEvent.getStartTime().isBefore(updatedEvent.getEndTime()) && updatedEvent.getLocation() != null && updatedEvent.getTags() != null) {
					
					/**
					 * I will use this HashSet to track any changes from invited to rsvp
					 */
					HashSet<User> attendeeStatus = new HashSet<User>();
					
					// Ensures that every user in rsvp is inside the user db
					ArrayList<User> rsvp = updatedEvent.getRsvp();
					for(User user : rsvp) {
						attendeeStatus.add(user);
						host = userCont.getUserById(user.getId()).getBody();
						if(!host.isPresent()) {
							return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
						}	
					}
					
					// Ensures that every user in the invite list is inside the user db
					ArrayList<User> invited = updatedEvent.getInvited();
					int index = 0;
					
					for(User user : invited) {
						host = userCont.getUserById(user.getId()).getBody();
						if(!host.isPresent()) {
							
							// If a user was in the attendeeStatus list, they've moved from invited to rsvp
							if(!attendeeStatus.add(user)) {
								invited.remove(index);
							}else {
								return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
							}	
						}	
						index++;
					}
					
					// Updating the event in the db with the updated event's information		
					event.setHostID(updatedEvent.getHostID());
					event.setTitle(updatedEvent.getTitle());
					event.setLocation(updatedEvent.getLocation());
					event.setTags(updatedEvent.getTags());
					event.setInvited(updatedEvent.getInvited());
					event.setStartTime(updatedEvent.getStartTime());
					event.setEndTime(updatedEvent.getEndTime());
					
					// Set these values to null because the event has been finalized
					event.setEndPossible(null);
					event.setStartPossible(null);
					
					// Assuming the application has checked the entire rsvp list with no errors, the new event will be saved
					repo.save(event);
					return ResponseEntity.ok(event);
				}
			}
					
			// Checks for Constructor 2
			else if(updatedEvent.getStartPossible() != null) {
				
				// Ensures that startPossible is before endPossible, the invited list isn't null , and that there's a duration longer than 0 minutes
				if(updatedEvent.getStartPossible().isBefore(updatedEvent.getEndPossible()) && updatedEvent.getInvited() != null && updatedEvent.getDuration() > 0) {
					
					// Ensures that every user in rsvp is inside the user db
					ArrayList<User> rsvp = updatedEvent.getRsvp();
					for(User user : rsvp) {
						host = userCont.getUserById(user.getId()).getBody();
						if(!host.isPresent()) {
							return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
						}	
					}
					
					// Ensures that every user in the invite list is inside the user db
					rsvp = updatedEvent.getInvited();
					for(User user : rsvp) {
						host = userCont.getUserById(user.getId()).getBody();
						if(!host.isPresent()) {
							return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
						}	
					}
					
					// Updating the event in the db with the updated event's information		
					event.setHostID(updatedEvent.getHostID());
					event.setTitle(updatedEvent.getTitle());
					event.setLocation(updatedEvent.getLocation());
					event.setTags(updatedEvent.getTags());
					event.setInvited(updatedEvent.getInvited());
					event.setStartPossible(updatedEvent.getStartPossible());
					event.setEndPossible(updatedEvent.getEndPossible());
					event.setDuration(updatedEvent.getDuration());
					
					
					repo.save(event);
					return ResponseEntity.ok(event);
				}	
			}
			
		}
		
		return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/events/{id}")
	public @ResponseBody ResponseEntity<Event> deleteEventById (@PathVariable String id) {
		
		// Event stored in db that will be deleted
		Optional<Event> event = repo.findById(id);
		if (event.isPresent()) {
			repo.deleteById(id);
			return new ResponseEntity<Event>(HttpStatus.NO_CONTENT);
		}
		else {
			return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/events/{id}/rsvp")
	public @ResponseBody ResponseEntity<List<User>> getAllEventRSVP(String id) {
		
		// Event stored in db that will be updated
		Optional<Event> event = repo.findById(id);

		if(event.isPresent()) {
			return ResponseEntity.ok(event.get().getRsvp());
		}else {
			return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/events/{id}/invited")
	public @ResponseBody ResponseEntity<List<User>> getAllEventInvited(String id) {
		
		// Event stored in db that will be updated
		Optional<Event> event = repo.findById(id);

		if(event.isPresent()) {
			return ResponseEntity.ok(event.get().getInvited());
		}else {
			return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}
	}
		
	
	// Do we need this if it's handled by updated user??
	@PostMapping("/events/{id}/attendees")
	public @ResponseBody ResponseEntity<List<User>> addAttendee (@RequestBody User user, Event event, String eventID) {
		event.addAttendee(user);
		repo.save(event);
		return ResponseEntity.ok(repo.findById(eventID).get().getRsvp());
	}
	
	
}