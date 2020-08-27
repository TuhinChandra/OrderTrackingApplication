package com.order.track.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.track.adapter.OrderTrackingAdapter;
import com.order.track.model.GetOrderResponse;
import com.order.track.model.IncomingFulfillmentInfo;
import com.order.track.model.TrackOrder;
import com.order.track.service.OrderFulfillmentService;
import com.order.track.util.CsvUtils;
import com.order.track.util.Util;

@CrossOrigin
@RestController
public class OrderTrackingController {

	@Autowired
	private OrderTrackingAdapter orderTrackingAdapter;
	@Autowired
	private OrderFulfillmentService orderFulfillmentService;

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

		return OrderTrackingController.mObjectMapper.readValue(
				Util.loadInputStreamAsByteArrayOutputStream(getClass().getResourceAsStream("/test/order/order.json"))
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
			@RequestParam(value = "deliveryDate", required = false) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SS'Z'", timezone = "UTC") final Date deliveryDate,
			@RequestParam(value = "productName", required = false) final String productName,
			@RequestParam(value = "ean", required = false) final String ean) throws IOException {
		return orderTrackingAdapter.fulfilOrder(orderNumber, lineNumber, status, quantity, refernceNumber, refernceType,
				fulfilmentSourceType, deliveryGroupCode, date, productName, ean, deliveryDate);

	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json", consumes = "multipart/form-data")
	@ResponseBody
	public void fulfilOrderInBulk(@RequestParam("file") final MultipartFile file) throws IOException {

		final List<IncomingFulfillmentInfo> incomingFulfillmentInfos = CsvUtils.read(IncomingFulfillmentInfo.class,
				file.getInputStream());
		if (null != incomingFulfillmentInfos && !incomingFulfillmentInfos.isEmpty()) {
			for (final IncomingFulfillmentInfo incomingFulfillmentInfo : incomingFulfillmentInfos) {
				orderFulfillmentService.fulfilOrder(incomingFulfillmentInfo.getOrderNumber(),
						incomingFulfillmentInfo.getLineNumber(), incomingFulfillmentInfo.getStatus(),
						incomingFulfillmentInfo.getQuantity(), incomingFulfillmentInfo.getRefernceNumber(),
						incomingFulfillmentInfo.getRefernceType(), incomingFulfillmentInfo.getFulfilmentSourceType(),
						incomingFulfillmentInfo.getDeliveryGroupCode(), incomingFulfillmentInfo.getFulfillmentDate(),
						incomingFulfillmentInfo.getProductName(), incomingFulfillmentInfo.getEan(),
						incomingFulfillmentInfo.getDeliveryDate());
			}
		}

	}

	@DeleteMapping(value = "/order/{orderNumber}")
	public void deleteOrder(@PathVariable final Long orderNumber) {
		orderFulfillmentService.deleteOrder(orderNumber);
	}

}
