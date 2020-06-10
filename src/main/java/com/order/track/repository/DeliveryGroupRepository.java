package com.order.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.order.track.entity.DeliveryGroup;

@Repository
public interface DeliveryGroupRepository extends JpaRepository<DeliveryGroup, Long> {

}
