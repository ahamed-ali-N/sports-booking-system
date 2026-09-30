package com.sports.booking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sports.booking.entity.Ground;

public interface GroundRepository extends JpaRepository<Ground, Long> {

    List<Ground> findBySportIgnoreCase(String sport);
}