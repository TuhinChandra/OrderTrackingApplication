package com.order.track.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "LINE")
public class Line {
	@GeneratedValue
	@Id
	@JsonIgnore
	private Long sequenceNumber;
	private Long lineNo;
	private String currentStatus;
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Order order;
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private DeliveryGroup deliveryGroup;
	private String productName;
	private String ean;
	private String info;
	private int quantity;
	private boolean completed = true;
	private boolean cancelled;

	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<LineFulfillmentEvent> fulfillmentEvents = new HashSet<>();

	public Line(final Long lineNo, final String currentStatus, final DeliveryGroup deliveryGroup,
			final Set<LineFulfillmentEvent> fulfillmentEvents, final String productName, final String ean) {
		super();
		this.lineNo = lineNo;
		this.currentStatus = currentStatus;
		this.deliveryGroup = deliveryGroup;
		this.fulfillmentEvents = fulfillmentEvents;
		this.productName = productName;
		this.ean = ean;
	}

	public Line(final Long lineNo, final String currentStatus, final Order order, final int quantity,
			final String productName, final String ean) {
		super();
		this.lineNo = lineNo;
		this.currentStatus = currentStatus;
		this.order = order;
		this.quantity = quantity;
		this.productName = productName;
		this.ean = ean;
	}

	@Override
	public boolean equals(final Object line) {

		if (line == this)
			return true;
		if (!(line instanceof Line)) {
			return false;
		}
		return lineNo == ((Line) line).getLineNo();
	}

	@Override
	public int hashCode() {
		return Objects.hash(lineNo);
	}

}
