package com.order.track.configuration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class GlobalConfiguration {

    @Value("#{${itemCategory}}")
    private Map<String, String> itemCategory;

}
