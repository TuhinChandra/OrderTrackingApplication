package com.order.track.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attribute {
	@NonNull
	private String lineNo;
	@NonNull
	private String currentStatus;
	private String trackingUrl;
	@NonNull
	private List<LifeCycle> lifeCycles;
	@NonNull
	private String itemCategory;
}
