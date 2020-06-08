package com.order.track.model;

import lombok.Getter;

public class JwtResponse {

    @Getter
    private final String jwttoken;

    public JwtResponse(String jwttoken) {
	this.jwttoken = jwttoken;
    }

}
