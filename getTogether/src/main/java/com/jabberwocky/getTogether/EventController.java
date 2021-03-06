package com.jabberwocky.getTogether;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
public class EventController {	
	@Autowired 
	private EventRepo repo;

	@Autowired 
	private UserRepo userRepo;

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
			ResponseEntity<User> res = userCont.getUserById(newEvent.getHostID());		
			if (res.getStatusCode() == HttpStatus.OK) { // Makes sure that the Host ID is an existing user
				if(newEvent.getTitle() != null) { // and that the title isn't null					

					if(newEvent.getStartTime() != null) { // Check for Constructor 1
						// Ensures that startTime is before endTime, the location isn't null (null tag list is initialized by constructor)
						if(!newEvent.getStartTime().isAfter(newEvent.getEndTime()) && newEvent.getLocation() != null) {
							ArrayList<User> invited = newEvent.getInvited();
							for(User user : invited) {
								ResponseEntity<User> userRes = userCont.getUserById(user.getId());
								if(userRes.getStatusCode() == HttpStatus.NOT_FOUND) {
									return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
								}	
							}
							// Assuming the application has checked the entire invite list with no errors, the new event will be saved
							repo.save(newEvent);

							// Get the host of the Event and add the event to the User's event list
							User user = res.getBody();
							ArrayList<Event> events = user.getEvents();
							events.add(newEvent);
							userRepo.save(user);
							return new ResponseEntity<Event>(newEvent, HttpStatus.CREATED);	
						}	
					}

					// the invited list isn't null , and that there's a duration longer than 0 minutes
					else if(newEvent.getDuration() > 0) {
						ArrayList<User> invited = newEvent.getInvited();
						for(User user : invited) {
							ResponseEntity<User> userRes = userCont.getUserById(user.getId());
							if(userRes.getStatusCode() == HttpStatus.NOT_FOUND) {
								return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
							}		
						}
						repo.save(newEvent);
						return new ResponseEntity<Event>(newEvent, HttpStatus.CREATED);
					}

					else { return new ResponseEntity<Event> (HttpStatus.NOT_ACCEPTABLE); } //406
				}
			}
			else { return new ResponseEntity<Event> (HttpStatus.NOT_ACCEPTABLE); } //406
		}
		return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);	// 404
	}

	@PutMapping("/events/{id}")
	public @ResponseBody ResponseEntity<Event> updateEventById (@PathVariable String id, @RequestBody Event updatedEvent){

		// Event stored in db that will be updated

		ResponseEntity<Event> eventRes = this.getEventById(id);	
		if (eventRes.getStatusCode() != HttpStatus.OK) {
			return new ResponseEntity<Event> (eventRes.getStatusCode()); // 404
		}
		Event event = eventRes.getBody();

		if(updatedEvent.getHostID() != null) {
			ResponseEntity<User> res = userCont.getUserById(updatedEvent.getHostID());		
			if (res.getStatusCode() == HttpStatus.OK) {
				if(updatedEvent.getTitle() != null) {

					// Checks for Constructor 1
					if(updatedEvent.getStartTime() != null) {

						// Ensures that startTime is before endTime, the location isn't null, and that the tag list isn't null
						if(updatedEvent.getStartTime().isBefore(updatedEvent.getEndTime()) && updatedEvent.getLocation() != null) {


							// Ensures that every user in the invite list is inside the user db
							ArrayList<User> invited = updatedEvent.getInvited();
							ArrayList<String> invitedIds = new ArrayList<>();
							if(invited != null) {

								// Goes through an event's invited list and verifies that all users exist in the User repo
								for(User user : invited) {
									ResponseEntity<User> userRes = userCont.getUserById(user.getId());
									if(userRes.getStatusCode() == HttpStatus.NOT_FOUND) {
										return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
									}
									else {
										invitedIds.add(user.getId());
									}
								}
							}
							
							// Updating the event in the db with the updated event's information		
							event.setHostID(updatedEvent.getHostID());
							event.setTitle(updatedEvent.getTitle());
							event.setLocation(updatedEvent.getLocation());
							if(updatedEvent.getInvited() != null) { event.setInvited(updatedEvent.getInvited());};
							event.setStartTime(updatedEvent.getStartTime());
							event.setEndTime(updatedEvent.getEndTime());

							// Assuming the application has checked the entire rsvp list with no errors, the new event will be saved
							repo.save(event);

							// Go through users who are free at event time and add to their event list
							for(int i = 0; i < invitedIds.size(); i++) {
								User user = userCont.getUserById(invitedIds.get(i)).getBody();
								ArrayList<Event> events = user.getEvents();
								events.add(event);
								user.setEvents(events);

								userRepo.save(user);
							}
							return ResponseEntity.ok(event);
						}
					}

					// Checks for Constructor 2
					// Ensures that startPossible is before endPossible, the invited list isn't null , and that there's a duration longer than 0 minutes
					if(updatedEvent.getDuration() > 0) {

						HashSet<User> attendeeStatus = new HashSet<User>();



						int index = 0;

						// Ensures that every user in the invite list is inside the user db
						ArrayList<User> invited = updatedEvent.getInvited();

						if(invited != null) {
							for(User user : invited) {
								ResponseEntity<User> userRes = userCont.getUserById(user.getId());
								if(userRes.getStatusCode() == HttpStatus.NOT_FOUND) {
									return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
								}else{

									// If a user was in the attendeeStatus list, they've moved from invited to rsvp
									if(!attendeeStatus.add(user)) {
										invited.remove(index);
									}else {
										return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
									}	
								}	
								index++;		
							}
						}

						// Updating the event in the db with the updated event's information		
						event.setHostID(updatedEvent.getHostID());
						event.setTitle(updatedEvent.getTitle());
						event.setLocation(updatedEvent.getLocation());
						//						event.setTags(updatedEvent.getTags());
						event.setDuration(updatedEvent.getDuration());
						if(updatedEvent.getInvited() != null) { event.setInvited(updatedEvent.getInvited());};

						repo.save(event);
						return ResponseEntity.ok(event);
					}	
				}
			}
		}

		return new ResponseEntity<Event>(HttpStatus.BAD_REQUEST);
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

	@PutMapping("/events/{id}/invited/{userID}")
	public @ResponseBody ResponseEntity<Event> updateInvite(@PathVariable String id, @PathVariable String userID){
		ResponseEntity<Event> res = getEventById(id);
		ResponseEntity<User> res2 = userCont.getUserById(userID);
		if (res.getStatusCode() == HttpStatus.OK && res2.getStatusCode() == HttpStatus.OK) {
			Event e = res.getBody();
			User u = res2.getBody();
			if (e.getInvited().contains(u)) {
				e.removeAttendee(u);
			}
			else {
				e.addAttendee(u);
			}
			repo.save(e);
			return ResponseEntity.ok(e);
		}
		else { return new ResponseEntity<Event>(HttpStatus.BAD_REQUEST); }
	}

	/*
	 * /events/{id}/bestTime
	 * /events{id}/bestTime?startPossible=2020-04-12&endPossible=2020-04-19
	 */

	@GetMapping("/events/{id}/bestTime")
	public @ResponseBody ResponseEntity<List<Event>> findBestTimes(@PathVariable String id, @RequestParam String startPossible, @RequestParam String endPossible){
		LocalDate startDate = LocalDate.parse(startPossible);
		LocalDate endDate = LocalDate.parse(endPossible);
		ResponseEntity<Event> res = getEventById(id);
		if (res.getStatusCode() == HttpStatus.OK) {
			Event e = res.getBody();
			return ResponseEntity.ok(e.findTimes(startDate, endDate));
		}
		else { return new ResponseEntity<>(res.getStatusCode()); }
	}
}