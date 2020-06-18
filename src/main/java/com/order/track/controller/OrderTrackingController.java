package com.order.track.controller;

import static com.order.track.util.Util.loadInputStreamAsByteArrayOutputStream;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.track.adapter.OrderTrackingAdapter;
import com.order.track.model.GetOrderResponse;
import com.order.track.model.TrackOrder;

@RestController
public class OrderTrackingController {

    @Autowired
    private OrderTrackingAdapter orderTrackingAdapter;

    private static ObjectMapper mObjectMapper = new ObjectMapper();

    @RequestMapping(value = "/internal/trackOrder/{orderNumber}", method = RequestMethod.GET, produces = "application/json")
    public TrackOrder trackOrderInternal(@PathVariable final String orderNumber)
	    throws JsonParseException, JsonMappingException, IOException {
	return orderTrackingAdapter.loadOrder(orderNumber, null);
    }

    @RequestMapping(value = "/trackOrder/{orderNumber}", method = RequestMethod.GET, produces = "application/json")
    public TrackOrder trackOrderExternal(@PathVariable final String orderNumber, @RequestParam final String type)
	    throws JsonParseException, JsonMappingException, IOException {
	return orderTrackingAdapter.loadOrder(orderNumber, type);
    }

    @RequestMapping(value = "/order/{orderNumber}", method = RequestMethod.GET, produces = "application/json")
    public GetOrderResponse getOrder() throws IOException {

	return mObjectMapper.readValue(
		loadInputStreamAsByteArrayOutputStream(getClass().getResourceAsStream("/test/order/order.json"))
			.toString(),
		GetOrderResponse.class);

    }

    /*
     * @RequestMapping(value = "/trackOrder/{orderNumber}", method =
     * RequestMethod.GET, produces = "application/json") public TrackOrder
     * trackOrder(@PathVariable final String orderNumber) throws JsonParseException,
     * JsonMappingException, IOException { return
     * orderTrackingAdapter.loadOrder(orderNumber, null); }
     */

    @RequestMapping(value = "/fulfilOrder", method = RequestMethod.POST, produces = "application/json")
    public TrackOrder fulfilOrder(@RequestParam(value = "order", required = true) final String orderNumber,
	    @RequestParam(value = "fulfilmentSourceType", required = false) final String fulfilmentSourceType,
	    @RequestParam(value = "deliveryGroupCode", required = false) final String deliveryGroupCode,
	    @RequestParam(value = "line", required = true) final String lineNumber,
	    @RequestParam(value = "status", required = true) final String status,
	    @RequestParam(value = "quantity", required = true) final String quantity,
	    @RequestParam(value = "refernceNumber", required = false) final String refernceNumber,
	    @RequestParam(value = "refernceType", required = false) final String refernceType,
	    @RequestParam(value = "fulfillmentDate", required = false) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SS'Z'", timezone = "UTC") final Date date,
	    @RequestParam(value = "productName", required = false) final String productName,
	    @RequestParam(value = "ean", required = false) final String ean)    
	    throws IOException {
	return orderTrackingAdapter.fulfilOrder(orderNumber, lineNumber, status, quantity, refernceNumber, refernceType,
		fulfilmentSourceType, deliveryGroupCode, date, productName, ean, null);

    }
}
