package com.order.track.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.order.track.entity.Line;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryGroup {
    
    @NonNull
    private String fulfilmentSourceType;
    @NonNull
    private String deliveryGroupCode;
    @NonNull
    private String currentStatus;
    private String trackingUrl;
    @NonNull
    private List<LifeCycle> lifeCycles;
    private Set<Line> lines;  
    private LocalDateTime deliveryDate;
    private Map<String,String> references;
    private String deliveryGroupMsg;
    

}
