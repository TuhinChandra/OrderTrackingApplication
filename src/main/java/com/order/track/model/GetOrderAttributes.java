package com.order.track.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetOrderAttributes {
    private String orderStatus;
    private String dateTimeCreated;
    private List<FulfilmentGroup> fulfilmentGroups;
    private DeliveryAddress deliveryAddress;

}
