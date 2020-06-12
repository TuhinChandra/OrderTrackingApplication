package com.order.track;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class OrderTrackingApplication {

    public static void main(final String[] args) {
	SpringApplication.run(OrderTrackingApplication.class, args);
    }

}
