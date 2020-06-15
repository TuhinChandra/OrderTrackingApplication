package com.order.track.dozer.customconverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.collection.internal.PersistentSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.dozermapper.core.CustomConverter;
import com.github.dozermapper.core.Mapper;
import com.order.track.configuration.GlobalConfiguration;
import com.order.track.entity.DeliveryGroup;
import com.order.track.model.Attribute;
import com.order.track.model.DeliveryAddress;

@Component
public class DeliveryGroupsToAttributesConverter implements CustomConverter {

    private static GlobalConfiguration globalConfiguration;

    private static Mapper mapper;

    @Autowired // Spring will pass appConfig to constructor
    public DeliveryGroupsToAttributesConverter(GlobalConfiguration globalConfiguration, Mapper mapper) {
	this();
	DeliveryGroupsToAttributesConverter.globalConfiguration = globalConfiguration;
	DeliveryGroupsToAttributesConverter.mapper = mapper;
    }

    public DeliveryGroupsToAttributesConverter() {
	// TODO Auto-generated constructor stub
    }

    @Override
    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass,
	    Class<?> sourceClass) {
	final Map<String, Attribute> attributes = new HashMap<>();

	for (final DeliveryGroup deliveryGroup : (List<DeliveryGroup>) new ArrayList<>(
		(PersistentSet) sourceFieldValue)) {

	    final String fulfillmentType = globalConfiguration.getFulfillmentTypes()
		    .get(deliveryGroup.getFulfilmentSourceType());

	    final Attribute attribute = attributes.containsKey(fulfillmentType) ? attributes.get(fulfillmentType)
		    : new Attribute();
	    attribute.setFulfillmentType(fulfillmentType);

	    final com.order.track.model.DeliveryGroup delGrp = new com.order.track.model.DeliveryGroup();

	    mapper.map(deliveryGroup, delGrp);

	    attribute.getDeliveryGroups().add(delGrp);
	    if ("Home Delivery".equals(fulfillmentType)) {
		attribute.setDeliveryAddress(new DeliveryAddress("B&Q House", "Chestnut Ave", "Chandler's Ford",
			"Eastleigh", "Hampshire", "GB", "SO53 3LE"));
	    }
	    attributes.put(fulfillmentType, attribute);

	}

	return attributes.values().stream().collect(Collectors.toList());
    }

}
