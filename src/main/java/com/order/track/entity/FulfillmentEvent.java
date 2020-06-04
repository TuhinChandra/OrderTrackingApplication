package com.order.track.entity;

//import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Entity
public class FulfillmentEvent {
	private String status;
	private int quantity;
	private String refernceType;
	private String refernceNumber;

}
