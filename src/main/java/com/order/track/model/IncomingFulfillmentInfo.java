package com.order.track.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class IncomingFulfillmentInfo {

	private long orderNumber;
	private String fulfilmentSourceType;
	private String deliveryGroupCode;
	private long lineNumber;
	private String status;
	private int quantity;
	private String refernceNumber;
	private String refernceType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SS'Z'", timezone = "UTC")
	private Date fulfillmentDate;
	private String productName;
	private String ean;
}
