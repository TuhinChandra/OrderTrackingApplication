package com.order.track.entity;

import java.util.List;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Order {
	private String sapOrderId;
	private List<Line> lineItems;
}
