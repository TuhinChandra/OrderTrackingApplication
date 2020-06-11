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
    private String fulfilmentSourceType;
    @NonNull
    private String deliveryGroupCode;
    @NonNull
    private String currentStatus;
    private String trackingUrl;
    @NonNull
    private List<LifeCycle> lifeCycles;

    public String TrackingUrl() {
	return "DISPATCHED".equals(currentStatus) ? "https://www.parcelforce.com/track-trace?trackNumber=SF3778426001"
		: trackingUrl;
    }
}
