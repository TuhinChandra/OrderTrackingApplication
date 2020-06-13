package com.order.track.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FulfilmentGroup {
    private String shippingMethodType;
    private String fulfilmentSource;
    private String deliveryDate;
    private List<BasketLine> basketLines;
}
