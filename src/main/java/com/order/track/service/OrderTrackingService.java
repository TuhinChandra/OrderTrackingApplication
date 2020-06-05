package com.order.track.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.track.entity.FulfillmentEvent;
import com.order.track.entity.Line;
import com.order.track.entity.Order;
import com.order.track.repository.OrderRepository;

@Service
public class OrderTrackingService {

    @Autowired
    private OrderRepository orderRepository;

    public Order loadOrder(final String orderNumber) {
	return loadOrder(Long.parseLong(orderNumber));
    }

    private Order loadOrder(final Long orderNumber) {
	return orderRepository.findById(orderNumber).orElse(null);
    }

    public Order fulfilOrder(final String orderId, final String lineNo, final String status, final String quantity,
	    final String refernceNumber, String itemCategory) {
	final Long orderNumber = Long.parseLong(orderId);
	final Long lineNumber = Long.parseLong(lineNo);
	Order order = loadOrder(orderNumber);
	if (null != order) {
	    final FulfillmentEvent fulfillmentEvent = new FulfillmentEvent(status, Integer.parseInt(quantity), null,
		    refernceNumber, null);
	    final List<Line> lines = order.getLineItems();
	    Line line = order.getLineItems().stream().filter(e -> e.getLineNo() == lineNumber).findFirst().orElse(null);
	    if (null == line) {
		line = new Line(lineNumber, status, order, itemCategory, Arrays.asList(fulfillmentEvent));
		lines.add(line);
		order.setLineItems(lines);
	    } else {
		line.setCurrentStatus(status);
		line.getFulfillmentEvents().add(fulfillmentEvent);
	    }
	    fulfillmentEvent.setLine(line);

	} else {
	    order = new Order();
	    order.setOrderId(orderNumber);
	    final FulfillmentEvent fulfillmentEvent = new FulfillmentEvent(status, Integer.parseInt(quantity), null,
		    refernceNumber, null);
	    final Line line = new Line(lineNumber, status, order, itemCategory, Arrays.asList(fulfillmentEvent));
	    order.setLineItems(Arrays.asList(line));
	    fulfillmentEvent.setLine(line);
	}
	return orderRepository.save(order);
    }
}
