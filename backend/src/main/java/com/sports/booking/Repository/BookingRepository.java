package com.sports.booking.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sports.booking.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Check overlapping bookings while adding
    List<Booking> findByGroundIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
            Long groundId,
            LocalDate bookingDate,
            LocalTime endTime,
            LocalTime startTime
    );

    // Check overlapping bookings while updating
    List<Booking> findByGroundIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(
            Long groundId,
            LocalDate bookingDate,
            LocalTime endTime,
            LocalTime startTime,
            Long id
    );

    // Get bookings by date
    List<Booking> findByBookingDate(LocalDate bookingDate);
}