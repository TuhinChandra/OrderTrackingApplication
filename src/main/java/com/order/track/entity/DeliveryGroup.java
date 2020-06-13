
package com.order.track.entity;

import java.time.LocalDateTime;
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
@Table(name = "DELIVERY_GROUP")
public class DeliveryGroup {
	@GeneratedValue
	@Id
	@JsonIgnore
	private Long sequenceNumber;
	private String fulfilmentSourceType;
	private String deliveryGroupCode;
	private String currentStatus;
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Order order;

	@OneToMany(mappedBy = "deliveryGroup", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Line> lines;

	@OneToMany(mappedBy = "deliveryGroup", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<GroupFulfillmentEvent> fulfillmentEvents;
	private LocalDateTime deliveryDate;

	public DeliveryGroup(final String fulfilmentSourceType, final String deliveryGroupCode, final String currentStatus,
			final Order order, final Set<Line> lines) {
		super();
		this.fulfilmentSourceType = fulfilmentSourceType;
		this.deliveryGroupCode = deliveryGroupCode;
		this.currentStatus = currentStatus;
		this.order = order;
		this.lines = lines;
	}

	public DeliveryGroup(final String fulfilmentSourceType, final String deliveryGroupCode, final Order order, LocalDateTime deliveryDate) {
		super();
		this.fulfilmentSourceType = fulfilmentSourceType;
		this.deliveryGroupCode = deliveryGroupCode;
		this.order = order;
		this.deliveryDate=deliveryDate;
	}

}
