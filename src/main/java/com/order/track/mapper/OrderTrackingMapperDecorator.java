package com.order.track.mapper;

import com.order.track.entity.Order;
import com.order.track.model.TrackOrder;

public class OrderTrackingMapperDecorator implements OrderTrackingMapper{

    private final OrderTrackingMapper orderTrackingMapper;

    public OrderTrackingMapperDecorator(final OrderTrackingMapper orderTrackingMapper) {
	this.orderTrackingMapper = orderTrackingMapper;
    }

    @Override
    public TrackOrder mapOrderToTrackOrder(Order order) {
	return orderTrackingMapper.mapOrderToTrackOrder(order);
    }



}
