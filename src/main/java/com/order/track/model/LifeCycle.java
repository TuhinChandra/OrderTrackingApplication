package com.order.track.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
}
