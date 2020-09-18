package com.order.track.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
    private int ordering;
    private String refernceType;
    private String refernceNumber;
    private boolean completed;
    @JsonFormat(pattern = "EEEE MMMMM dd hh:mm a", timezone = "UTC")
    private Date date;
    @JsonInclude(Include.NON_NULL)
    private String trackingUrl;

    public String getRefernceType() {
	return refernceType;
    }

    public String getRefernceNumber() {
	return refernceNumber;
    }
}
