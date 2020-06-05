package com.order.track.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackOrder {

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	private Data data;

}
