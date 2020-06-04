package com.order.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.order.track.entity.FulfillmentEvent;

@Repository
public interface FulfillmentEventRepository extends JpaRepository<FulfillmentEvent, Long> {

}
