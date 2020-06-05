package com.order.track.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    private String type;
    private String id;
    private List<Attributes> attributes;
    public String getType() {
	return type;
    }
    public void setType(String type) {
	this.type = type;
    }
    public String getId() {
	return id;
    }
    public void setId(String id) {
	this.id = id;
    }
    public List<Attributes> getAttributes() {
	return attributes;
    }
    public void setAttributes(List<Attributes> attributes) {
	this.attributes = attributes;
    }
}
