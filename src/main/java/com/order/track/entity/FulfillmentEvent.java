package com.order.track.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "FulfillmentEvent")
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
	private boolean completed=true;
	private int ordering;
	private LocalDateTime date;

	public FulfillmentEvent(final String status, final int quantity, final Line line) {
		this.status = status;
		this.quantity = quantity;
		this.line = line;
	}

	public FulfillmentEvent(final String status, final int quantity, final String refernceType,
			final String refernceNumber, final Line line,final int ordering,LocalDateTime date) {
		this.status = status;
		this.quantity = quantity;
		this.refernceType = refernceType;
		this.refernceNumber = refernceNumber;
		this.line = line;
		this.ordering=ordering;
		this.date = date;
		
	}

}
