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
@Table(name = "LineFulfillmentEvent")
public class LineFulfillmentEvent {
	@GeneratedValue
	@Id
	@JsonIgnore
	private Long sequenceNumber;
	private int quantity;
	private String status;
	private String refernceType;
	private String refernceNumber;
	private boolean completed = true;
	private int ordering;
	private LocalDateTime date;
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Line line;

	public LineFulfillmentEvent(final String status, final boolean completed, final int ordering,
			final LocalDateTime date, final int quantity, final Line line,final String refernceNumber, final String refernceType) {
		this.status = status;
		this.completed = completed;
		this.ordering = ordering;
		this.date = date;
		this.quantity = quantity;
		this.line = line;
		this.refernceNumber = refernceNumber;
		this.refernceType = refernceType;
	}

}
