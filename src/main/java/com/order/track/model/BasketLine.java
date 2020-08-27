package com.order.track.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasketLine {

    private String basketLineItemId;
    private String lineNumber;
    private String ean;
    private String lineQty;
    private String name;

}
