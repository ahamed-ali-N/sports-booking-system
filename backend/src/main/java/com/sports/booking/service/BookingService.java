package com.sports.booking.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sports.booking.entity.Booking;
import com.sports.booking.entity.BookingStatus;
import com.sports.booking.Repository.BookingRepository;
import com.sports.booking.Repository.GroundRepository;
import com.sports.booking.entity.Ground;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final GroundRepository groundRepository;

    public BookingService(
            BookingRepository bookingRepository,
            GroundRepository groundRepository) {

        this.bookingRepository = bookingRepository;
        this.groundRepository = groundRepository;
    }

    // Add Booking
    public Booking addBooking(Booking booking) {

        // Check Ground
        boolean groundExists =
                groundRepository.existsById(booking.getGroundId());

        if (!groundExists) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Ground not found."
            );
        }

        // Check past date
        if (booking.getBookingDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Booking date cannot be in the past."
            );
        }

        // Check invalid time
        if (!booking.getStartTime().isBefore(booking.getEndTime())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start time must be before end time."
            );
        }

        // Check overlapping booking
        boolean alreadyBooked =
                !bookingRepository
                        .findByGroundIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
                                booking.getGroundId(),
                                booking.getBookingDate(),
                                booking.getEndTime(),
                                booking.getStartTime()
                        )
                        .isEmpty();

        if (alreadyBooked) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This ground is already booked for the selected date and time."
            );
        }

        // Calculate total price
        double totalPrice = calculatePrice(
                booking.getGroundId(),
                booking.getStartTime(),
                booking.getEndTime()
        );

        booking.setTotalPrice(totalPrice);

        return bookingRepository.save(booking);
    }

    // Get All Bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get Booking By ID
    public Booking getBookingById(Long id) {

        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found."
                ));
    }

    // Get Bookings By Date
    public List<Booking> getBookingsByDate(LocalDate bookingDate) {
        return bookingRepository.findByBookingDate(bookingDate);
    }

    // Update Booking
    public Booking updateBooking(Long id, Booking booking) {

        Booking existingBooking =
                bookingRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Booking not found."
                        ));

        // Check Ground
        boolean groundExists =
                groundRepository.existsById(booking.getGroundId());

        if (!groundExists) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Ground not found."
            );
        }

        // Check past date
        if (booking.getBookingDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Booking date cannot be in the past."
            );
        }

        // Check invalid time
        if (!booking.getStartTime().isBefore(booking.getEndTime())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start time must be before end time."
            );
        }

        // Check overlapping booking except current booking
        boolean alreadyBooked =
                !bookingRepository
                        .findByGroundIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(
                                booking.getGroundId(),
                                booking.getBookingDate(),
                                booking.getEndTime(),
                                booking.getStartTime(),
                                id
                        )
                        .isEmpty();

        if (alreadyBooked) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This ground is already booked for the selected date and time."
            );
        }

        // Calculate new total price
        double totalPrice = calculatePrice(
                booking.getGroundId(),
                booking.getStartTime(),
                booking.getEndTime()
        );

        existingBooking.setCustomerName(booking.getCustomerName());
        existingBooking.setGroundId(booking.getGroundId());
        existingBooking.setBookingDate(booking.getBookingDate());
        existingBooking.setStartTime(booking.getStartTime());
        existingBooking.setEndTime(booking.getEndTime());
        existingBooking.setStatus(booking.getStatus());
        existingBooking.setTotalPrice(totalPrice);

        return bookingRepository.save(existingBooking);
    }

    // Cancel Booking
    public Booking cancelBooking(Long id) {

        Booking booking =
                bookingRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Booking not found."
                        ));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Booking is already cancelled."
            );
        }

        booking.setStatus(BookingStatus.CANCELLED);

        return bookingRepository.save(booking);
    }

    // Delete Booking
    public String deleteBooking(Long id) {

        if (!bookingRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Booking not found."
            );
        }

        bookingRepository.deleteById(id);

        return "Booking deleted successfully";
    }

    // Check Ground Availability
    public boolean checkAvailability(
            Long groundId,
            LocalDate bookingDate,
            LocalTime startTime,
            LocalTime endTime) {

        // Check Ground
        boolean groundExists =
                groundRepository.existsById(groundId);

        if (!groundExists) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Ground not found."
            );
        }

        // Check invalid time
        if (!startTime.isBefore(endTime)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start time must be before end time."
            );
        }

        // Check past date
        if (bookingDate.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Booking date cannot be in the past."
            );
        }

        // Check overlapping booking
        boolean alreadyBooked =
                !bookingRepository
                        .findByGroundIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
                                groundId,
                                bookingDate,
                                endTime,
                                startTime
                        )
                        .isEmpty();

        return !alreadyBooked;
    }

    // Calculate Price
    public double calculatePrice(
            Long groundId,
            LocalTime startTime,
            LocalTime endTime) {

        Ground ground =
                groundRepository.findById(groundId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Ground not found."
                        ));

        long minutes =
                Duration.between(startTime, endTime).toMinutes();

        double hours = minutes / 60.0;

        return ground.getPrice() * hours;
    }

    // Get Price By Booking ID
    public double getBookingPrice(Long id) {

        Booking booking =
                bookingRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Booking not found."
                        ));

        return calculatePrice(
                booking.getGroundId(),
                booking.getStartTime(),
                booking.getEndTime()
        );
    }
}