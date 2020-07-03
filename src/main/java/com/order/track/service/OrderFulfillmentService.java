package com.order.track.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.track.configuration.GlobalConfiguration;
import com.order.track.entity.DeliveryGroup;
import com.order.track.entity.GroupFulfillmentEvent;
import com.order.track.entity.Line;
import com.order.track.entity.LineFulfillmentEvent;
import com.order.track.entity.Order;
import com.order.track.repository.OrderRepository;

@Service
public class OrderFulfillmentService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private GlobalConfiguration globalConfiguration;

	public Order loadOrder(final Long orderNumber) {
		return orderRepository.findById(orderNumber).orElse(null);
	}

	public Order fulfilOrder(final Long orderNumber, final Long lineNo, final String status,
			final int fulfillmentQuantity, final String refernceNumber, final String refernceType,
			final String fulfilmentSourceType, final String deliveryGroupCode, final Date fulfillmentDate,
			final String productName, final String ean, final Date deliveryDate) {

		final Order order = createOrderIfNotExists(orderNumber);
		final String incomingStatus = status;
		final Line line = retriveOrCreateLineIfNotExists(order, lineNo, productName, ean, fulfillmentQuantity);
		if (line.isCancelled()) {
			System.err.println("Line::" + lineNo + " is already cancelled. Hence not fulfilling further");
			return order;
		}

		final DeliveryGroup deliveryGroup = retriveOrCreateGroupIfNotExists(order, fulfilmentSourceType,
				deliveryGroupCode, line, deliveryDate);

		if ("SO_CHANGED".equalsIgnoreCase(incomingStatus)) {
			handleLineQuantityChange(fulfillmentQuantity, line, deliveryGroup);
		} else {
			generateLineFulfillmentEvent(line, incomingStatus, fulfillmentQuantity, refernceNumber, refernceType,
					fulfillmentDate);
			generateDelievryFulfillmentEvent(deliveryGroup, incomingStatus, refernceNumber, refernceType,
					fulfillmentDate);
		}

		deriveOrderStatus(order);
		return orderRepository.save(order);
	}

	private void handleLineQuantityChange(final int fulfillmentQuantity, final Line line,
			final DeliveryGroup deliveryGroup) {
		line.setQuantity(fulfillmentQuantity);
		line.setCompleted(true);
		if (fulfillmentQuantity == 0) {
			line.setCancelled(true);
		}
		int latestStateOrdering = 0;
		LineFulfillmentEvent latestEvent = null;
		for (final LineFulfillmentEvent event : line.getFulfillmentEvents()) {
			event.setCompleted(true);
			event.setQuantity(fulfillmentQuantity);
			if (event.getOrdering() > latestStateOrdering) {
				latestStateOrdering = event.getOrdering();
				latestEvent = event;
			}
		}
		if (null != latestEvent) {
			line.setCurrentStatus(latestEvent.getStatus());
			generateDelievryFulfillmentEvent(deliveryGroup, latestEvent.getStatus(), latestEvent.getRefernceNumber(),
					latestEvent.getRefernceType(), latestEvent.getDate());
		}
	}

	private Order createOrderIfNotExists(final Long orderNumber) {
		return orderRepository.findById(orderNumber).orElse(buildNewOrder(orderNumber));
	}

	private Order buildNewOrder(final Long orderNumber) {
		final Order newOrder = new Order(orderNumber);
		return newOrder;
	}

	private Line retriveOrCreateLineIfNotExists(final Order order, final Long lineNo, final String productName,
			final String ean, final int fulfillmentQuantity) {
		final Set<Line> orderLines = order.getLines();
		for (final Line line : orderLines) {
			if (line.getLineNo() == lineNo) {
				return line;
			}
		}
		return createNewLine(order, lineNo, productName, ean, fulfillmentQuantity);
	}

	private Line createNewLine(final Order order, final Long lineNo, final String productName, final String ean,
			final int fulfillmentQuantity) {
		final Line line = new Line(lineNo, null, order, fulfillmentQuantity, productName, ean);
		order.getLines().add(line);
		return line;
	}

	private DeliveryGroup retriveOrCreateGroupIfNotExists(final Order order, final String fulfilmentSourceType,
			final String deliveryGroupCode, final Line line, final Date deliveryDate) {
		DeliveryGroup deliveryGroup = order.getDeliveryGroups().stream()
				.filter(dg -> dg.getFulfilmentSourceType().equalsIgnoreCase(fulfilmentSourceType)
						&& dg.getDeliveryGroupCode().equalsIgnoreCase(deliveryGroupCode))
				.findFirst().orElse(line.getDeliveryGroup());
		if (null == deliveryGroup) {
			deliveryGroup = createNewDeliveryGroup(order, fulfilmentSourceType, deliveryGroupCode, deliveryDate);
		}

		deliveryGroup.getLines().add(line);
		line.setDeliveryGroup(deliveryGroup);
		return deliveryGroup;
	}

	private DeliveryGroup createNewDeliveryGroup(final Order order, final String fulfilmentSourceType,
			final String deliveryGroupCode, final Date deliveryDate) {
		final DeliveryGroup deliveryGroup = new DeliveryGroup(fulfilmentSourceType, deliveryGroupCode, order,
				deliveryDate);
		order.getDeliveryGroups().add(deliveryGroup);
		return deliveryGroup;

	}

	private LineFulfillmentEvent generateLineFulfillmentEvent(final Line line, final String status,
			final int fulfillmentQuantity, final String refernceNumber, final String refernceType,
			final Date fulfillmentDate) {
		final int existingLineStatusOrdering = fetchOrdering(line.getDeliveryGroup().getFulfilmentSourceType(),
				line.getCurrentStatus());
		final int incomingLineStatusOrdering = fetchOrdering(line.getDeliveryGroup().getFulfilmentSourceType(), status);
		LineFulfillmentEvent lineFulfillmentEvent = null;
		if (incomingLineStatusOrdering > existingLineStatusOrdering) {
			lineFulfillmentEvent = createLineFulfillmentEvent(line, status, fulfillmentQuantity,
					incomingLineStatusOrdering, refernceNumber, refernceType, fulfillmentDate);
			if (line.isCompleted()) {
				line.setCurrentStatus(status);
			}
		} else {
			// reject the incoming status
			System.err.println("Rejecting incoming state as ::incomingLineStatusOrdering::" + status + "["
					+ incomingLineStatusOrdering + "]" + "might be lower than existingLineStatusOrdering::"
					+ line.getCurrentStatus() + "[" + existingLineStatusOrdering + "]");
		}
		return lineFulfillmentEvent;
	}

	private LineFulfillmentEvent createLineFulfillmentEvent(final Line line, final String status,
			final int fulfillmentQuantity, final int incomingLineStatusOrdering, final String refernceNumber,
			final String refernceType, final Date fulfillmentDate) {
		final LineFulfillmentEvent lineFulfillmentEvent = new LineFulfillmentEvent(status,
				fulfillmentQuantity >= line.getQuantity(), incomingLineStatusOrdering, fulfillmentDate,
				fulfillmentQuantity, line, refernceNumber, refernceType);
		line.getFulfillmentEvents().add(lineFulfillmentEvent);
		line.setCompleted(fulfillmentQuantity >= line.getQuantity());
		return lineFulfillmentEvent;
	}

	private GroupFulfillmentEvent generateDelievryFulfillmentEvent(final DeliveryGroup deliveryGroup,
			final String status, final String refernceNumber, final String refernceType, final Date fulfillmentDate) {

		final int existingLineStatusOrdering = fetchOrdering(deliveryGroup.getFulfilmentSourceType(),
				deliveryGroup.getCurrentStatus());
		final int derivedDeliveryGroupStatusBasedOnLines = deriveDeliveryGroupStatusBasedOnLines(deliveryGroup);
		GroupFulfillmentEvent groupFulfillmentEvent = null;
		if (derivedDeliveryGroupStatusBasedOnLines > existingLineStatusOrdering) {
			groupFulfillmentEvent = createDeliveryGroupFulfillmentEvent(deliveryGroup, status, refernceNumber,
					refernceType, fulfillmentDate, derivedDeliveryGroupStatusBasedOnLines);
			deliveryGroup.setCurrentStatus(status);
		}
		return groupFulfillmentEvent;

	}

	private GroupFulfillmentEvent createDeliveryGroupFulfillmentEvent(final DeliveryGroup deliveryGroup,
			final String status, final String refernceNumber, final String refernceType, final Date fulfillmentDate,
			final int derivedDeliveryGroupStatusBasedOnLines) {
		final GroupFulfillmentEvent groupFulfillmentEvent = new GroupFulfillmentEvent(status, true,
				derivedDeliveryGroupStatusBasedOnLines, fulfillmentDate, deliveryGroup, refernceNumber, refernceType);
		deliveryGroup.getFulfillmentEvents().add(groupFulfillmentEvent);
		return groupFulfillmentEvent;
	}

	private int deriveDeliveryGroupStatusBasedOnLines(final DeliveryGroup deliveryGroup) {
		final Set<String> lineStauses = deliveryGroup.getLines().stream().filter(line -> !line.isCancelled())
				.map(Line::getCurrentStatus).collect(Collectors.toSet());
		final Set<Integer> ordering = new HashSet<>();
		for (final String status : lineStauses) {
			ordering.add(fetchOrdering(deliveryGroup.getFulfilmentSourceType(), status));
		}
		return ordering.isEmpty() ? 0 : Collections.min(ordering);
	}

	private void deriveOrderStatus(final Order order) {
		final Set<Integer> ordering = new HashSet<>();
		order.getDeliveryGroups().stream().map(DeliveryGroup::getCurrentStatus).collect(Collectors.toSet())
				.forEach(e -> {
					ordering.add(fetchOrderingByStatus(e));
				});
		order.setCurrentStatus(globalConfiguration.getOrderStateMap().get(Collections.min(ordering)));

	}

	private int fetchOrdering(final String fulfillmentSourceType, final String status) {
		final String ordering = globalConfiguration.fetchStatusMetrix().get(fulfillmentSourceType).get(status);
		return null != ordering ? Integer.parseInt(ordering) : 0;

	}

	private int fetchOrderingByStatus(final String status) {
		return Optional.ofNullable(globalConfiguration.getOrderStatusMatrix().get(status)).orElse(1);

	}
}
