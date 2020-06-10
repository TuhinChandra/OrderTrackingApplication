package com.order.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.order.track.entity.GroupFulfillmentEvent;

@Repository
public interface GroupFulfillmentEventRepository extends JpaRepository<GroupFulfillmentEvent, Long> {

}
