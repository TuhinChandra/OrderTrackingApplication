package com.order.track.service;

import org.springframework.stereotype.Service;

import com.order.track.entity.Order;
import com.order.track.model.Data;

@Service
public class OrderTrackingService {

	public Data buildApiResponse(String orderNumber) {
		Order order=loadOrder(orderNumber);
		return new Data();
		
	}

	private Order loadOrder(String orderNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public Order fulfilOrder(String orderNumber, String lineNumber, String status, String refernceNumber) {
		// TODO Auto-generated method stub
		return null;
	}
}
