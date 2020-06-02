package com.order.track.model;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class Attributes {
	@NonNull
	private String lineNo;
	@NonNull
	private String currentStatus;
	private String trackingUrl;
	@NonNull
	private List<LifeCycle> lifeCycles;
	
}
