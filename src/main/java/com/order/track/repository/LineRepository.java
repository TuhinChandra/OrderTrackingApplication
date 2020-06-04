package com.order.track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.order.track.entity.Line;

@Repository
public interface LineRepository extends JpaRepository<Line, Long> {

}
