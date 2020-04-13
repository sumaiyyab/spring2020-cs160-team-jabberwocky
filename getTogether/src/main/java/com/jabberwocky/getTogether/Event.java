package com.jabberwocky.getTogether;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {

	@Id
	private String id;	
	private String hostID;
	public String title;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private int duration; //mins
	private LocalDate startPossible;
	private LocalDate endPossible;
	private String location;
	private ArrayList<String> tags;
	private ArrayList<User> invited;
	private ArrayList<User> rsvp;


	public Event(String userID, String title, String location, ArrayList<String> tags, ArrayList<User> invited, LocalDateTime startTime, LocalDateTime endTime) {
		//the Actual filled out event; the only kind of event that should be saved to repo
		this.hostID = userID;
		this.title = title;
		this.location = location;
		if(tags == null) {
			this.tags = new ArrayList<>();
		}else {
			this.tags = tags;
		}

		this.rsvp = new ArrayList<>();

		// Created invited arrayList for people who haven't rsvpsd
		this.invited = invited;

		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Event(String userID, String title, ArrayList<User> invited, LocalDate startPossible, LocalDate endPossible, int duration) { 
		//used when first creating event, before searching
		this.startPossible = startPossible;
		this.endPossible = endPossible;
		this.duration = duration;
	}
	
	public Event(LocalDateTime startTime, LocalDateTime endTime) { //used when presenting potential events that could fit free time
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public void addAttendee(User user) {
		rsvp.add(user);
	}

	public boolean hasOverlapWith(LocalDateTime start2, LocalDateTime end2) {
		return !this.endTime.isBefore(start2) && !this.startTime.isAfter(end2);
	}

	public ArrayList<Event> findTimes() {
		// go through the invited list and find times within the dates
		LocalDateTime possibleStartTime = startPossible.atStartOfDay();
		LocalDateTime possibleEndTime = possibleStartTime.plusMinutes(duration);
		HashMap<LocalDateTime, Integer> timeslots = new HashMap<LocalDateTime, Integer>();
		while(possibleEndTime.isBefore(endPossible.atTime(11, 59))) { //O(n^2) gross >_<
			int freeUsers = 0;
			for (User i:invited) {
				if(i.isFreeAt(possibleStartTime, duration)) {
					freeUsers++;
				}
			}
			timeslots.put(possibleStartTime, freeUsers);
			possibleStartTime = possibleEndTime;
			possibleEndTime = possibleStartTime.plusMinutes(duration);
		}
		int maxValueInMap=(Collections.max(timeslots.values()));  // This will return max value in the Hashmap
		ArrayList<Event> bestTimes = new ArrayList<>();
        for (LocalDateTime key : timeslots.keySet()) {  // Itrate through hashmap
            if (timeslots.get(key)==maxValueInMap) {
                bestTimes.add(new Event(key, key.plusMinutes(duration)));     // Print the key with max value
            }
        }
		return bestTimes;
	}
}
