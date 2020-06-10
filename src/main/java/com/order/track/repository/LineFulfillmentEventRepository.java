package com.order.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.order.track.entity.LineFulfillmentEvent;

@Repository
public interface LineFulfillmentEventRepository extends JpaRepository<LineFulfillmentEvent, Long> {

}
