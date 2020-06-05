package com.order.track.model;

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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getOrdering() {
		return ordering;
	}
	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	public String getRefernceType() {
		return refernceType;
	}
	public void setRefernceType(String refernceType) {
		this.refernceType = refernceType;
	}
	public String getRefernceNumber() {
		return refernceNumber;
	}
	public void setRefernceNumber(String refernceNumber) {
		this.refernceNumber = refernceNumber;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	@NonNull
	private boolean completed;
}
