package com.order.track.adapter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.order.track.entity.Order;
import com.order.track.model.TrackOrder;
import com.order.track.service.OrderTrackingService;
import com.order.track.transformer.OrderTrackingTransformer;

@Service
public class OrderTrackingAdapter {

    @Autowired
    private OrderTrackingService orderTrackingService;
    @Autowired
    private OrderTrackingTransformer orderTrackingTransformer;

    public TrackOrder loadOrder(@PathVariable final String orderNumber)
	    throws JsonParseException, JsonMappingException, IOException {

	final Order order = orderTrackingService.loadOrder(orderNumber);

	return orderTrackingTransformer.transformToTrackOrder(order);

    }

    public TrackOrder fulfilOrder(final String orderId, final String lineNo, final String status, final String quantity,
	    final String refernceNumber, String itemCategory) throws IOException {

	return orderTrackingTransformer.transformToTrackOrder(
		orderTrackingService.fulfilOrder(orderId, lineNo, status, quantity, refernceNumber, itemCategory));
    }

}
