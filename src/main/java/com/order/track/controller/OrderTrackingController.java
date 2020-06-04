package com.order.track.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.order.track.entity.Order;
import com.order.track.model.Data;
import com.order.track.service.OrderTrackingService;

@RestController
public class OrderTrackingController {
	@Autowired
	private OrderTrackingService orderTrackingService;

	@RequestMapping(value = "/trackOrder/{orderNumber}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String trackOrder(@PathVariable String orderNumber) {
//		return orderTrackingService.buildApiResponse(orderNumber);
		return "Order::"+orderNumber;
		
	}
	
	@RequestMapping(value = "/fulfilOrder", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Order fulfilOrder(
			@RequestParam(value = "order", required = false) String orderNumber,
			@RequestParam(value = "line", required = false) String lineNumber,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "refernceNumber", required = false) String refernceNumber
			) {
		return orderTrackingService.fulfilOrder(orderNumber, lineNumber, status, refernceNumber);
		
	}
}
