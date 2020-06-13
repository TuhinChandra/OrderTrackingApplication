package com.order.track.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.order.track.entity.Line;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attribute {

    private DeliveryAddress deliveryAddress;
    private String fulfillmentType;
    private List<DeliveryGroup> deliveryGroups =  new ArrayList<>();

}
