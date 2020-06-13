package com.order.track.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryAddress {

    private String houseName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String county;
    private String country;
    private String postalCode;

}
