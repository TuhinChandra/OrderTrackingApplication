package com.order.track.adapter;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public TrackOrder loadOrder(final String orderNumber, final String invokationType)
	    throws JsonParseException, JsonMappingException, IOException {

	final Order order = orderTrackingService.loadOrder(Long.parseLong(orderNumber));
	TrackOrder result = null;
	if (order != null) {
	    /*
	     * if ("external".equalsIgnoreCase(invokationType)) { result =
	     * orderTrackingTransformer.transformToTrackOrderExternal(order); } else {
	     * result = orderTrackingTransformer.transformToTrackOrderInternal(order); }
	     */

	    result = orderTrackingTransformer.transformToTrackOrder(order, invokationType);

	} else {
	    result = orderTrackingTransformer.buildOrderNotFoundResponse();
	}

	return result;
    }

    public TrackOrder fulfilOrder(final String orderId, final String lineNo, final String status, final String quantity,
	    final String refernceNumber, final String refernceType, final String fulfilmentSourceType,
	    final String deliveryGroupCode, final Date date, String productName, String ean, Date deliveryDate)
	    throws IOException {

	return orderTrackingTransformer.transformToTrackOrderInternal(
		orderTrackingService.fulfilOrder(orderId, lineNo, status, quantity, refernceNumber, refernceType,
			fulfilmentSourceType, deliveryGroupCode, date, productName, ean, deliveryDate));
    }

}
