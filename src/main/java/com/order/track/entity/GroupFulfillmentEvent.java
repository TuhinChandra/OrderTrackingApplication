package com.order.track.entity;

import java.util.Comparator;
import java.util.Date;

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
@Table(name = "GroupFulfillmentEvent")
public class GroupFulfillmentEvent implements Comparator<GroupFulfillmentEvent> {
	@GeneratedValue
	@Id
	@JsonIgnore
	private Long sequenceNumber;
	private String status;
	private String refernceType;
	private String refernceNumber;
	private boolean completed = true;
	private int ordering;
	private Date date;
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private DeliveryGroup deliveryGroup;

	public GroupFulfillmentEvent(final String status, final boolean completed, final int ordering, final Date date,
			final DeliveryGroup deliveryGroup, final String refernceNumber, final String refernceType) {
		this.status = status;
		this.completed = completed;
		this.ordering = ordering;
		this.date = date;
		this.deliveryGroup = deliveryGroup;
		this.refernceNumber = refernceNumber;
		this.refernceType = refernceType;
	}

	@Override
	public int compare(final GroupFulfillmentEvent arg0, final GroupFulfillmentEvent arg1) {
		return arg0.getOrdering() - arg1.getOrdering();
	}

}
