package com.order.track.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.track.entity.Order;
import com.order.track.model.TrackOrder;
import com.order.track.service.OrderTrackingService;
import com.order.track.util.Util;

@RestController
public class OrderTrackingController {
    @Autowired
    private OrderTrackingService orderTrackingService;

    private static ObjectMapper mObjectMapper = new ObjectMapper();

    @RequestMapping(value = "{type}/trackOrder/{orderNumber}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public TrackOrder trackOrder(@PathVariable final String type,@PathVariable final String orderNumber) throws JsonParseException, JsonMappingException, IOException {

	final Map<String,String> responseMap = new HashMap<>();

	responseMap.put("internal","/test/trackOrder/trackOrderInternal.json");
	responseMap.put("external","/test/trackOrder/trackOrderExternal.json");

	final TrackOrder result = mObjectMapper.readValue(
		Util.loadInputStreamAsByteArrayOutputStream(
			getClass().getResourceAsStream(responseMap.get(type))).toString(),
		TrackOrder.class);
	result.getData().setId(orderNumber);
	return result;

	//return orderTrackingService.buildApiResponse(orderNumber);

    }

    @RequestMapping(value = "/fulfilOrder", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Order fulfilOrder(@RequestParam(value = "order", required = false) final String orderNumber,
	    @RequestParam(value = "line", required = false) final String lineNumber,
	    @RequestParam(value = "status", required = false) final String status,
	    @RequestParam(value = "quantity", required = false) final String quantity,
	    @RequestParam(value = "refernceNumber", required = false) final String refernceNumber) {
	return orderTrackingService.fulfilOrder(orderNumber, lineNumber, status, quantity, refernceNumber);

    }
}
