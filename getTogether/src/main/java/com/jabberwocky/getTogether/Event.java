package com.jabberwocky.getTogether;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
	
	@Id
	private String id;	
	private String userID;
	private User host;
	public String title;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDate startDate;
	private LocalDate endDate;
	private String location;
	private ArrayList<String> tags;
	private ArrayList<User> invited;
	private ArrayList<User> rsvp;
	
	
	public Event(String userID, String title, String location, ArrayList<String> tags, ArrayList<User> invited, LocalDate startDate, LocalDate endDate) {
		this.userID = userID;
		this.title = title;
		//this.startTime = startTime;
		//this.endTime = endTime;
		this.location = location;
		if(tags == null) {
			this.tags = new ArrayList<>();
		}else {
			this.tags = tags;
		}
		
		this.rsvp = new ArrayList<>();
		
		// Created invited arrayList for people who haven't rsvpsd
		this.invited = invited;
		
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public void setHost(User person) {
		host = person;
		rsvp.add(person);
	}
	
	public void addAttendee(User user) {
		rsvp.add(user);
	}
	
	public void findTimes() {
		// go through the rsvp list and find times within the dates
	}
}
