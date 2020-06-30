package com.order.track.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private int ordering;
    private String refernceType;
    private String refernceNumber;
    private boolean completed;
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm a")
    private Date date;

    public String getRefernceType() {
	return refernceType ;
    }

    public String getRefernceNumber() {
	return refernceNumber;
    }
}
