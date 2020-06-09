package com.order.track.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.order.track.adapter.OrderTrackingAdapter;
import com.order.track.model.TrackOrder;

@RestController
public class OrderTrackingController {

	@Autowired
	private OrderTrackingAdapter orderTrackingAdapter;

	@RequestMapping(value = "/internal/trackOrder/{orderNumber}", method = RequestMethod.GET, produces = "application/json")
	public TrackOrder trackOrderInternal(@PathVariable final String orderNumber)
			throws JsonParseException, JsonMappingException, IOException {
		return orderTrackingAdapter.loadOrder(orderNumber, null);
	}

	@RequestMapping(value = "/external/trackOrder/{orderNumber}", method = RequestMethod.GET, produces = "application/json")
	public TrackOrder trackOrderExternal(@PathVariable final String orderNumber)
			throws JsonParseException, JsonMappingException, IOException {
		return orderTrackingAdapter.loadOrder(orderNumber, "External");
	}

	@RequestMapping(value = "/trackOrder/{orderNumber}", method = RequestMethod.GET, produces = "application/json")
	public TrackOrder trackOrder(@PathVariable final String orderNumber)
			throws JsonParseException, JsonMappingException, IOException {
		return orderTrackingAdapter.loadOrder(orderNumber, null);
	}

	@RequestMapping(value = "/fulfilOrder", method = RequestMethod.POST, produces = "application/json")
	public TrackOrder fulfilOrder(@RequestParam(value = "order", required = false) final String orderNumber,
			@RequestParam(value = "line", required = false) final String lineNumber,
			@RequestParam(value = "status", required = false) final String status,
			@RequestParam(value = "quantity", required = false) final String quantity,
			@RequestParam(value = "refernceNumber", required = false) final String refernceNumber,
			@RequestParam(value = "itemCategory", required = false) final String itemCategory,
			@RequestParam("datetime") @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") final LocalDateTime date)
			throws IOException {
		return orderTrackingAdapter.fulfilOrder(orderNumber, lineNumber, status, quantity, refernceNumber, itemCategory,
				date);

	}
}
