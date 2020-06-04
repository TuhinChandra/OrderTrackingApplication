package com.order.track.entity;

import java.util.List;

//import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Entity
public class Line {

	private String lineNo;
	private String currentStatus;
	private List<FulfillmentEvent> fulfillmentEvents;

}
