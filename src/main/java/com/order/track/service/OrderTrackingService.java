package com.order.track.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.javatuples.Pair;
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
public class OrderTrackingService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private GlobalConfiguration globalConfiguration;

    public Order loadOrder(final Long orderNumber) {
	return orderRepository.findById(orderNumber).orElse(null);
    }

    public Order fulfilOrder(final String orderId, final String lineNo, final String status, final String lineQuantity,
	    final String refernceNumber, final String refernceType, final String fulfilmentSourceType,
	    final String deliveryGroupCode, final Date date, String productName, String ean, Date deliveryDate) {
	final Long orderNumber = Long.parseLong(orderId);
	final Long lineNumber = Long.parseLong(lineNo);
	Order order = loadOrder(orderNumber);
	final int quantity = Integer.parseInt(lineQuantity);
	Line line = null;
	DeliveryGroup deliveryGroup = null;
	Set<DeliveryGroup> deliveryGroups = new HashSet<>();
	if (null != order) {
	    deliveryGroups = order.getDeliveryGroups();
	    if (null == deliveryGroups) {
		deliveryGroups = new HashSet<>();
	    }
	    final Set<Line> lines = new HashSet<>();
	    final Pair<Line, DeliveryGroup> lineToDelivryGroup = findLineFromOrder(fulfilmentSourceType,
		    deliveryGroupCode, lineNumber, order);

	    if (null == lineToDelivryGroup) {
		deliveryGroup = getDeliveryGroup(order, fulfilmentSourceType, deliveryGroupCode);
		if (null == deliveryGroup) {
		    deliveryGroup = createNewDeliveryGroup(order, fulfilmentSourceType, deliveryGroupCode,
			    deliveryDate);
		}
		deliveryGroups.add(deliveryGroup);
		line = new Line(lineNumber, status, deliveryGroup, null, productName, ean);
		lines.add(line);
		deliveryGroup.setLines(lines);
	    } else {
		line = lineToDelivryGroup.getValue0();
		deliveryGroup = line.getDeliveryGroup();
		line.setCurrentStatus(status);
	    }
	    final LineFulfillmentEvent lineFulfillmentEvent = new LineFulfillmentEvent(status, true,
		    fetchOrdering(line.getDeliveryGroup().getFulfilmentSourceType(), status), date, quantity, line,
		    refernceNumber, refernceType);

	    addLineFulfillmentEvent(line, lineFulfillmentEvent);
	    deliveryGroup.setCurrentStatus(status);

	    final GroupFulfillmentEvent groupFulfillmentEvent = new GroupFulfillmentEvent(status, true,
		    fetchOrdering(line.getDeliveryGroup().getFulfilmentSourceType(), status), date, deliveryGroup,
		    refernceNumber, refernceType);
	    addGroupFulfillmentEvent(deliveryGroup, groupFulfillmentEvent);

	} else {
	    order = new Order();
	    order.setOrderId(orderNumber);

	    deliveryGroup = createNewDeliveryGroup(order, fulfilmentSourceType, deliveryGroupCode, deliveryDate);

	    final LineFulfillmentEvent lineFulfillmentEvent = new LineFulfillmentEvent(status, true,
		    fetchOrdering(fulfilmentSourceType, status), date, quantity, null, refernceNumber, refernceType);

	    line = new Line(lineNumber, status, deliveryGroup, new HashSet<>(Arrays.asList(lineFulfillmentEvent)),
		    productName, ean);
	    lineFulfillmentEvent.setLine(line);
	    addLineFulfillmentEvent(line, lineFulfillmentEvent);

	    final Set<Line> lines = new HashSet<>();
	    lines.add(line);
	    deliveryGroup.setLines(lines);

	    final GroupFulfillmentEvent groupFulfillmentEvent = new GroupFulfillmentEvent(status, true,
		    fetchOrdering(line.getDeliveryGroup().getFulfilmentSourceType(), status), date, deliveryGroup,
		    refernceNumber, refernceType);
	    addGroupFulfillmentEvent(deliveryGroup, groupFulfillmentEvent);
	    deliveryGroup.setCurrentStatus(status);

	}
	deliveryGroups.add(deliveryGroup);

	order.setDeliveryGroups(deliveryGroups);
	return orderRepository.save(order);
    }

    private void addGroupFulfillmentEvent(final DeliveryGroup deliveryGroup,
	    final GroupFulfillmentEvent groupFulfillmentEvent) {
	Set<GroupFulfillmentEvent> groupFulfillmentEvents = deliveryGroup.getFulfillmentEvents();
	if (null == groupFulfillmentEvents) {
	    groupFulfillmentEvents = new HashSet<>();
	}
	groupFulfillmentEvents.add(groupFulfillmentEvent);
	deliveryGroup.setFulfillmentEvents(groupFulfillmentEvents);
    }

    private void addLineFulfillmentEvent(final Line line, final LineFulfillmentEvent lineFulfillmentEvent) {
	Set<LineFulfillmentEvent> lineFulfillmentEvents = line.getFulfillmentEvents();
	if (null == lineFulfillmentEvents) {
	    lineFulfillmentEvents = new HashSet<>();
	}
	lineFulfillmentEvents.add(lineFulfillmentEvent);
	line.setFulfillmentEvents(lineFulfillmentEvents);
    }

    private DeliveryGroup createNewDeliveryGroup(final Order order, final String fulfilmentSourceType,
	    final String deliveryGroupCode, Date deliveryDate) {
	return new DeliveryGroup(fulfilmentSourceType, deliveryGroupCode, order, deliveryDate);
    }

    private DeliveryGroup getDeliveryGroup(final Order order, final String fulfilmentSourceType,
	    final String deliveryGroupCode) {
	DeliveryGroup deliveryGroup = null;
	for (final DeliveryGroup dg : order.getDeliveryGroups()) {
	    if (dg.getFulfilmentSourceType().equalsIgnoreCase(fulfilmentSourceType)
		    && dg.getDeliveryGroupCode().equalsIgnoreCase(deliveryGroupCode)) {
		deliveryGroup = dg;
	    }
	}
	return deliveryGroup;
    }

    private Pair<Line, DeliveryGroup> findLineFromOrder(final String fulfilmentSourceType,
	    final String deliveryGroupCode, final Long lineNumber, final Order order) {
	Line targetLine;
	Pair<Line, DeliveryGroup> lineToDelivryGroup = null;
	for (final DeliveryGroup dg : order.getDeliveryGroups()) {
	    if (dg.getFulfilmentSourceType().equalsIgnoreCase(fulfilmentSourceType)
		    && dg.getDeliveryGroupCode().equalsIgnoreCase(deliveryGroupCode)) {
		for (final Line line : dg.getLines()) {
		    if (line.getLineNo() == lineNumber) {
			targetLine = line;
			lineToDelivryGroup = new Pair<>(targetLine, dg);
			break;
		    }
		}

	    } else {
		for (final Line line : dg.getLines()) {
		    if (line.getLineNo() == lineNumber) {
			targetLine = line;
			lineToDelivryGroup = new Pair<>(targetLine, dg);
			break;
		    }
		}
	    }
	}
	return lineToDelivryGroup;
    }

    private int fetchOrdering(final String itemCategory, final String status) {
	return Integer.parseInt(globalConfiguration.fetchStatusMetrix().get(itemCategory).get(status));

    }
}
