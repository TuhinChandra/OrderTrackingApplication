package com.order.track.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class DeliveryAddress {

    private String houseName = "B&Q House";
    private String addressLine1 = "Chestnut Ave";
    private String addressLine2 = "Chandler's Ford";
    private String city = "Eastleigh ";
    private String county = "Hampshire";
    private String country;
    private String postalCode = "SO53 3LE";

    public String getHouseName() {
	return "B&Q House";
    }

    public String getAddressLine1() {
	return "Chestnut Ave";
    }

    public String getAddressLine2() {
	return "Chandler's Ford";
    }

    public String getCity() {
	return "Eastleigh ";
    }

    public String getCounty() {
	return "Hampshire";
    }

    public String getCountry() {
	return "GB";
    }

    public String getPostalCode() {
	return "SO53 3LE";
    }

}
