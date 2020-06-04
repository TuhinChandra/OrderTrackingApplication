package com.order.track.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.order.track.entity.Order;
import com.order.track.service.OrderTrackingService;

@RestController
public class OrderTrackingController {
	@Autowired
	private OrderTrackingService orderTrackingService;

	@RequestMapping(value = "/trackOrder/{orderNumber}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Order trackOrder(@PathVariable final String orderNumber) {
		return orderTrackingService.buildApiResponse(orderNumber);

	}

	@RequestMapping(value = "/fulfilOrder", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Order fulfilOrder(@RequestParam(value = "order", required = false) final String orderNumber,
			@RequestParam(value = "line", required = false) final String lineNumber,
			@RequestParam(value = "status", required = false) final String status,
			@RequestParam(value = "quantity", required = false) final String quantity,
			@RequestParam(value = "refernceNumber", required = false) final String refernceNumber) {
		return orderTrackingService.fulfilOrder(orderNumber, lineNumber, status, quantity, refernceNumber);

	}
}
