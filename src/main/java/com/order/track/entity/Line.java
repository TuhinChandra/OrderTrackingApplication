package com.order.track.entity;

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
	private DeliveryGroup deliveryGroup;
	private String productName;
	private String ean;
	private String info;

	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<LineFulfillmentEvent> fulfillmentEvents;

	public Line(final Long lineNo, final String currentStatus, final DeliveryGroup deliveryGroup,
			final Set<LineFulfillmentEvent> fulfillmentEvents,String productName,String ean) {
		super();
		this.lineNo = lineNo;
		this.currentStatus = currentStatus;
		this.deliveryGroup = deliveryGroup;
		this.fulfillmentEvents = fulfillmentEvents;
		this.productName=productName;
		this.ean=ean;
	}

}
