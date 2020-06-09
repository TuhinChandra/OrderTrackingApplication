package com.order.track.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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

	public Order loadOrder(final Long orderNumber) {
		return orderRepository.findById(orderNumber).orElse(null);
	}

	public Order fulfilOrder(final String orderId, final String lineNo, final String status, final String quantity,
			final String refernceNumber, final String itemCategory, final LocalDateTime date) {
		final Long orderNumber = Long.parseLong(orderId);
		final Long lineNumber = Long.parseLong(lineNo);
		Order order = loadOrder(orderNumber);
		if (null != order) {

			final Set<Line> lines = order.getLineItems();
			Line line = order.getLineItems().stream().filter(e -> e.getLineNo() == lineNumber).findFirst().orElse(null);
			if (null == line) {
				line = new Line(lineNumber, status, order, itemCategory, null);
				lines.add(line);
			} else {
				line.setCurrentStatus(status);
			}
			final FulfillmentEvent fulfillmentEvent = new FulfillmentEvent(status, Integer.parseInt(quantity), null,
					refernceNumber, null, fetchOrdering(line.getItemCategory(), status), date);
			line.getFulfillmentEvents().add(fulfillmentEvent);
			fulfillmentEvent.setLine(line);

		} else {
			order = new Order();
			order.setOrderId(orderNumber);
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

	private int fetchOrdering(final String itemCategory, final String status) {
		return Integer.parseInt(globalConfiguration.fetchStatusMetrix().get(itemCategory).get(status));

	}
}
