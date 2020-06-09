package com.order.track.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.order.track.configuration.GlobalConfiguration;
import com.order.track.entity.FulfillmentEvent;
import com.order.track.entity.Line;
import com.order.track.entity.Order;
import com.order.track.repository.OrderRepository;
import com.order.track.util.JwtTokenUtil;

@Service
public class OrderTrackingService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private GlobalConfiguration globalConfiguration;
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public Order loadOrder(final String orderNumber, final String customerId) {
		return loadOrder(Long.parseLong(orderNumber));

	}

	public String fetchUserType(final Order order, final String customerId, final String authorization) {

		String userType = null;

		if (customerId != null && customerId.equals(order.getCustomerId())) {

			userType = "External User";

		} else if (StringUtils.isNotEmpty(authorization) && authorization.startsWith("Bearer")
				&& isAuthenticatedInternalUser(authorization)) {

			userType = "Internal User";

		}

		return userType;
	}

	private boolean isAuthenticatedInternalUser(final String authorization) {

		final UserDetails userDetails = jwtUserDetailsService
				.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(authorization.substring(7)));

		if (jwtTokenUtil.validateToken(authorization.substring(7), userDetails)) {

			final Iterator iterator = userDetails.getAuthorities().iterator();

			while (iterator.hasNext()) {

				return ((GrantedAuthority) iterator.next()).getAuthority().equals("Internal User");

			}

		}

		return false;

	}

	private Order loadOrder(final Long orderNumber) {
		return orderRepository.findById(orderNumber).orElse(null);
	}

	public Order fulfilOrder(final String orderId, final String lineNo, final String status, final String quantity,
			final String refernceNumber, final String itemCategory, final String customerId, final LocalDateTime date) {
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

	private int fetchOrdering(final String itemCategory, final String status) {
		return Integer.parseInt(globalConfiguration.fetchStatusMetrix().get(itemCategory).get(status));

	}
}
