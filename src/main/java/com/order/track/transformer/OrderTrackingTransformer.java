package com.order.track.transformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dozermapper.core.Mapper;
import com.order.track.configuration.GlobalConfiguration;
import com.order.track.entity.Order;
import com.order.track.model.Attribute;
import com.order.track.model.DeliveryGroup;
import com.order.track.model.LifeCycle;
import com.order.track.model.Meta;
import com.order.track.model.TrackOrder;
import com.order.track.model.TrackOrderWithWarning;
import com.order.track.model.Warning;

@Service
public class OrderTrackingTransformer {

    @Autowired
    private Mapper mapper;
    @Autowired
    private GlobalConfiguration globalConfiguration;

    /*
     * public TrackOrder transformToTrackOrder(Order order) {
     *
     * return OrderTrackingMapper.INSTANCE.mapOrderToTrackOrder(order);
     *
     * }
     */

    public TrackOrder transformToTrackOrder(final Order order, String type) throws IOException {

	final TrackOrder trackOrder;

	if (null != order) {
	    trackOrder = new TrackOrder();
	    mapper.map(order, trackOrder);

	    for (final Attribute attribute : trackOrder.getData().getAttributes()) {

		for (final DeliveryGroup deliveryGroup : attribute.getDeliveryGroups()) {

		    final String fulfilmentSourceType = deliveryGroup.getFulfilmentSourceType();

		    final List<String> eligibleStatuses = new ArrayList<>(
			    globalConfiguration.fetchStatusMetrix().get(fulfilmentSourceType).keySet());

		    removeCompleteStatus(deliveryGroup, eligibleStatuses);

		    buildIncompleteStatues(deliveryGroup, fulfilmentSourceType, eligibleStatuses);

		    deliveryGroup.setCurrentStatus(
			    globalConfiguration.getStatusConfig(type).get(deliveryGroup.getCurrentStatus()));

		    final Map<String, LifeCycle> lifeCycleMap = new HashMap<>();
		    for (final LifeCycle lifeCycle : deliveryGroup.getLifeCycles()) {
			lifeCycle.setStatus(globalConfiguration.getStatusConfig(type).get(lifeCycle.getStatus()));
			if (lifeCycleMap.containsKey(lifeCycle.getStatus())) {
			    if (lifeCycle.getDate() != null && lifeCycleMap.get(lifeCycle.getStatus()).getDate() != null
				    & lifeCycleMap.get(lifeCycle.getStatus()).getDate().isBefore(lifeCycle.getDate())) {
				lifeCycleMap.put(lifeCycle.getStatus(), lifeCycle);
			    }
			} else {
			    lifeCycleMap.put(lifeCycle.getStatus(), lifeCycle);
			}

			if (lifeCycle.isCompleted() && (lifeCycle.getStatus().equals("On the way")
				|| lifeCycle.getStatus().equals("Items Dispatched"))) {

			    deliveryGroup.setTrackingUrl("https://www.parcelforce.com/track-trace?trackNumber="
				    + lifeCycle.getRefernceNumber());
			}

		    }

		    final List<LifeCycle> lifeCycles = new ArrayList<>(lifeCycleMap.values());
		    Collections.sort(lifeCycles, (d1, d2) -> {
			return d1.getOrdering() - d2.getOrdering();
		    });
		    deliveryGroup.setLifeCycles(lifeCycles);

		}

	    }
	} else {
	    trackOrder = buildOrderNotFoundResponse();
	}
	return trackOrder;

    }

    public TrackOrder transformToTrackOrderInternal(final Order order) throws IOException {

	final TrackOrder trackOrder;
	if (null != order) {
	    trackOrder = new TrackOrder();
	    mapper.map(order, trackOrder);
	    globalConfiguration.getStatusMatrix();

	    final List<Attribute> attributes = trackOrder.getData().getAttributes();

	    for (final Attribute attribute : attributes) {

		for (final DeliveryGroup deliveryGroup : attribute.getDeliveryGroups()) {

		    final String fulfilmentSourceType = deliveryGroup.getFulfilmentSourceType();

		    final List<String> eligibleStatuses = new ArrayList<>(
			    globalConfiguration.fetchStatusMetrix().get(fulfilmentSourceType).keySet());

		    removeCompleteStatus(deliveryGroup, eligibleStatuses);

		    buildIncompleteStatues(deliveryGroup, fulfilmentSourceType, eligibleStatuses);

		}

	    }
	} else {
	    trackOrder = buildOrderNotFoundResponse();
	}
	return trackOrder;

    }

    public TrackOrder transformToTrackOrderExternal(final Order order) throws IOException {

	final TrackOrder trackOrder;

	if (null != order) {
	    trackOrder = new TrackOrder();
	    mapper.map(order, trackOrder);

	    for (final Attribute attribute : trackOrder.getData().getAttributes()) {

		for (final DeliveryGroup deliveryGroup : attribute.getDeliveryGroups()) {

		    final String fulfilmentSourceType = deliveryGroup.getFulfilmentSourceType();

		    final List<String> eligibleStatuses = new ArrayList<>(
			    globalConfiguration.fetchStatusMetrix().get(fulfilmentSourceType).keySet());

		    removeCompleteStatus(deliveryGroup, eligibleStatuses);

		    buildIncompleteStatues(deliveryGroup, fulfilmentSourceType, eligibleStatuses);

		    deliveryGroup.setCurrentStatus(
			    globalConfiguration.getStatusConfig("external").get(deliveryGroup.getCurrentStatus()));

		    final Map<String, LifeCycle> lifeCycleMap = new HashMap<>();
		    for (final LifeCycle lifeCycle : deliveryGroup.getLifeCycles()) {
			lifeCycle.setStatus(globalConfiguration.getStatusConfig("external").get(lifeCycle.getStatus()));
			if (lifeCycleMap.containsKey(lifeCycle.getStatus())) {
			    if (lifeCycle.getDate() != null && lifeCycleMap.get(lifeCycle.getStatus()).getDate() != null
				    & lifeCycleMap.get(lifeCycle.getStatus()).getDate().isBefore(lifeCycle.getDate())) {
				lifeCycleMap.put(lifeCycle.getStatus(), lifeCycle);
			    }
			} else {
			    lifeCycleMap.put(lifeCycle.getStatus(), lifeCycle);
			}
		    }

		    final List<LifeCycle> lifeCycles = new ArrayList<>(lifeCycleMap.values());
		    Collections.sort(lifeCycles, (d1, d2) -> {
			return d1.getOrdering() - d2.getOrdering();
		    });
		    deliveryGroup.setLifeCycles(lifeCycles);
		}
	    }
	} else {
	    trackOrder = buildOrderNotFoundResponse();
	}
	return trackOrder;

    }

    public TrackOrder buildOrderNotFoundResponse() {

	return new TrackOrderWithWarning(new Meta(new Warning("404", "INVALID-ORDER")));
    }

    private void removeCompleteStatus(final DeliveryGroup deliveryGroup, final List<String> eligibleStatuses) {
	if (null != deliveryGroup.getLifeCycles()) {
	    for (final LifeCycle lifeCycle : deliveryGroup.getLifeCycles()) {
		eligibleStatuses.remove(lifeCycle.getStatus());
	    }
	}
    }

    private void buildIncompleteStatues(final DeliveryGroup deliveryGroup, final String itemCategory,
	    final List<String> eligibleStatuses) {

	for (final String status : eligibleStatuses) {

	    final LifeCycle lifeCycle = new LifeCycle();
	    lifeCycle.setStatus(status);
	    lifeCycle.setOrdering(fetchOrdering(itemCategory, status));
	    List<LifeCycle> lifeCycles = deliveryGroup.getLifeCycles();
	    if (null == lifeCycles) {
		lifeCycles = new ArrayList<>();
	    }

	    lifeCycles.add(lifeCycle);

	}
    }

    private int fetchOrdering(final String itemCategory, final String status) {
	return Integer.parseInt(globalConfiguration.fetchStatusMetrix().get(itemCategory).get(status));

    }

}
