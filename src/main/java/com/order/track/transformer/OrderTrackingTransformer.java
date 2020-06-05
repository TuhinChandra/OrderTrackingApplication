package com.order.track.transformer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dozermapper.core.Mapper;
import com.order.track.entity.Order;
import com.order.track.model.TrackOrder;

@Service
public class OrderTrackingTransformer {

    @Autowired
    private Mapper mapper;

    /*
     * public TrackOrder transformToTrackOrder(Order order) {
     *
     * return OrderTrackingMapper.INSTANCE.mapOrderToTrackOrder(order);
     *
     * }
     */

    public TrackOrder transformToTrackOrder(Order order) throws IOException {

	final TrackOrder trackOrder = new TrackOrder();
	mapper.map(order, trackOrder);
	return trackOrder;

    }

}
