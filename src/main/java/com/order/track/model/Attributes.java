package com.order.track.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attributes {
	@NonNull
	private String lineNo;
	@NonNull
	private String currentStatus;
	private String trackingUrl;
	@NonNull
	private List<LifeCycle> lifeCycles;

	public String getLineNo() {
		return lineNo;
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getTrackingUrl() {
		return trackingUrl;
	}
	public void setTrackingUrl(String trackingUrl) {
		this.trackingUrl = trackingUrl;
	}
	public List<LifeCycle> getLifeCycles() {
		return lifeCycles;
	}
	public void setLifeCycles(List<LifeCycle> lifeCycles) {
		this.lifeCycles = lifeCycles;
	}

}
