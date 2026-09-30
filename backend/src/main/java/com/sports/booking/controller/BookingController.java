package com.sports.booking.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.sports.booking.entity.Booking;
import com.sports.booking.service.BookingService;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Add Booking
    @PostMapping("/bookings")
    public Booking addBooking(@Valid @RequestBody Booking booking) {
        return bookingService.addBooking(booking);
    }

    // Get All Bookings
    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // Get Booking By ID
    @GetMapping("/bookings/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    // Get Bookings By Date
    @GetMapping("/bookings/date")
    public List<Booking> getBookingsByDate(
            @RequestParam LocalDate date) {

        return bookingService.getBookingsByDate(date);
    }

    // Get Booking Price
    @GetMapping("/bookings/{id}/price")
    public double getBookingPrice(@PathVariable Long id) {
        return bookingService.getBookingPrice(id);
    }

    // Update Booking
    @PutMapping("/bookings/{id}")
    public Booking updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody Booking booking) {

        return bookingService.updateBooking(id, booking);
    }

    // Cancel Booking
    @PatchMapping("/bookings/{id}/cancel")
    public Booking cancelBooking(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }

    // Delete Booking
    @DeleteMapping("/bookings/{id}")
    public String deleteBooking(@PathVariable Long id) {
        return bookingService.deleteBooking(id);
    }

    // Check Ground Availability
    @GetMapping("/grounds/{groundId}/availability")
    public boolean checkAvailability(
            @PathVariable Long groundId,
            @RequestParam LocalDate date,
            @RequestParam LocalTime startTime,
            @RequestParam LocalTime endTime) {

        return bookingService.checkAvailability(
                groundId,
                date,
                startTime,
                endTime
        );
    }
}