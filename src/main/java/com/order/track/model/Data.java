package com.order.track.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    private String type = "order";
    private Long id;
    private String customerId;
    private List<Attribute> attributes;
}
