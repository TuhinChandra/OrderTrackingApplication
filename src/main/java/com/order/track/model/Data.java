package com.order.track.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Data {
	private String type;
	private String id;
	private List<Attributes> attributes;
	
}
