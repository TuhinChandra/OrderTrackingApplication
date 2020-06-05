package com.order.track.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.order.track.adapter.OrderTrackingAdapter;
import com.order.track.configuration.GlobalConfiguration;
import com.order.track.model.TrackOrder;

@RestController
public class OrderTrackingController {

    @Autowired
    private OrderTrackingAdapter orderTrackingAdapter;
    @Autowired
    private GlobalConfiguration globalConfiguration;

    @RequestMapping(value = "/trackOrder/{orderNumber}", method = RequestMethod.GET, produces = "application/json")
    public TrackOrder trackOrder(@PathVariable final String orderNumber)
	    throws JsonParseException, JsonMappingException, IOException {
	System.out.println(globalConfiguration.getItemCategory());
	return orderTrackingAdapter.loadOrder(orderNumber);
    }

    @RequestMapping(value = "/fulfilOrder", method = RequestMethod.POST, produces = "application/json")
    public TrackOrder fulfilOrder(@RequestParam(value = "order", required = false) final String orderNumber,
	    @RequestParam(value = "line", required = false) final String lineNumber,
	    @RequestParam(value = "status", required = false) final String status,
	    @RequestParam(value = "quantity", required = false) final String quantity,
	    @RequestParam(value = "refernceNumber", required = false) final String refernceNumber,
	    @RequestParam(value = "itemCategory", required = false) final String itemCategory) throws IOException {
	return orderTrackingAdapter.fulfilOrder(orderNumber, lineNumber, status, quantity, refernceNumber,
		itemCategory);

    }
}
