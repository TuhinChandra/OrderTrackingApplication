package com.order.track.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.order.track.configuration.GlobalConfiguration;
import com.order.track.entity.FulfillmentEvent;
import com.order.track.entity.Line;
import com.order.track.entity.Order;
import com.order.track.repository.OrderRepository;

@Service
public class OrderTrackingService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private GlobalConfiguration globalConfiguration;

    public Order loadOrder(final String orderNumber, String customerId) {
	return loadOrder(Long.parseLong(orderNumber));

    }

    public String fetchUserType(Order order, String customerId) {

	String userType = null;

	if (customerId != null && customerId.equals(order.getCustomerId())) {

	    userType = "External User";

	} else if (isAuthenticatedUser()) {

	    userType = "Internal User";

	}

	return userType;
    }

    @PreAuthorize("hasAuthority('Internal User')")
    private boolean isAuthenticatedUser() {
	return true;
    }

    private Order loadOrder(final Long orderNumber) {
	return orderRepository.findById(orderNumber).orElse(null);
    }

    public Order fulfilOrder(final String orderId, final String lineNo, final String status, final String quantity,
	    final String refernceNumber, String itemCategory, String customerId, LocalDateTime date) {
	final Long orderNumber = Long.parseLong(orderId);
	final Long lineNumber = Long.parseLong(lineNo);
	Order order = loadOrder(orderNumber);
	if (null != order) {
	    final FulfillmentEvent fulfillmentEvent = new FulfillmentEvent(status, Integer.parseInt(quantity), null,
		    refernceNumber, null, fetchOrdering(itemCategory, status), date);
	    final Set<Line> lines = order.getLineItems();
	    Line line = order.getLineItems().stream().filter(e -> e.getLineNo() == lineNumber).findFirst().orElse(null);
	    if (null == line) {
		line = new Line(lineNumber, status, order, itemCategory, Arrays.asList(fulfillmentEvent));
		lines.add(line);
		// order.setLineItems(lines);
	    } else {
		line.setCurrentStatus(status);
		line.getFulfillmentEvents().add(fulfillmentEvent);
	    }
	    fulfillmentEvent.setLine(line);

	} else {
	    order = new Order();
	    order.setOrderId(orderNumber);
	    order.setCustomerId(customerId);
	    final FulfillmentEvent fulfillmentEvent = new FulfillmentEvent(status, Integer.parseInt(quantity), null,
		    refernceNumber, null, fetchOrdering(itemCategory, status), date);
	    final Line line = new Line(lineNumber, status, order, itemCategory, Arrays.asList(fulfillmentEvent));
	    final Set<Line> lines = new HashSet<>();
	    lines.add(line);
	    order.setLineItems(lines);
	    fulfillmentEvent.setLine(line);
	}
	return orderRepository.save(order);
    }

    private int fetchOrdering(String itemCategory, String status) {
	return Integer.parseInt(globalConfiguration.fetchStatusMetrix().get(itemCategory).get(status));

    }
}
