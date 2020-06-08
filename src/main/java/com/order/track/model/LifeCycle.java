package com.order.track.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class LifeCycle {
	@NonNull
	private String status;
	private int quantity;
	@NonNull
	private int ordering;
	private String refernceType;
	private String refernceNumber;
	@NonNull
	private boolean completed;
	private LocalDateTime date;
}
