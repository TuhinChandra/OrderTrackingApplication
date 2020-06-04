package com.order.track.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class FulfillmentEvent {
	@GeneratedValue
	@Id
	@JsonIgnore
	private Long sequenceNumber;
	private String status;
	private int quantity;
	private String refernceType;
	private String refernceNumber;
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Line line;

	public FulfillmentEvent(final String status, final int quantity, final Line line) {
		this.status = status;
		this.quantity = quantity;
		this.line = line;
	}

	public FulfillmentEvent(final String status, final int quantity, final String refernceType,
			final String refernceNumber, final Line line) {
		this.status = status;
		this.quantity = quantity;
		this.refernceType = refernceType;
		this.refernceNumber = refernceNumber;
		this.line = line;
	}

}
