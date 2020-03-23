package com.jabberwocky.getTogether;

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
	private UUID eventID;
	private User host;
	private String title;
	private LocalDateTime startTime;
	private int duration;
	private String location;
	private ArrayList<String> tags;
	private ArrayList<User> rsvp;
	
	public Event(User host, String title, LocalDateTime startTime, int duration, String location, ArrayList<String> tags) {
		this.host = host;
		this.title = title;
		this.startTime = startTime;
		this.duration = duration;
		this.location = location;
		this.tags = tags;
		this.rsvp = new ArrayList<>();
	}
}
