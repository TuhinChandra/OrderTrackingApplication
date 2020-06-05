package com.order.track.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
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
	private String itemCategory;

	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<FulfillmentEvent> fulfillmentEvents;

	public Line(final Long lineNo, final String currentStatus, final Order order,String itemCategory,
			final List<FulfillmentEvent> fulfillmentEvents) {
		super();
		this.lineNo = lineNo;
		this.currentStatus = currentStatus;
		this.order = order;
		this.fulfillmentEvents = fulfillmentEvents;
		this.itemCategory=itemCategory;
	}

}
