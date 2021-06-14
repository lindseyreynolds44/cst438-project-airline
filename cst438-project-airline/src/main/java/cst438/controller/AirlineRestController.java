package cst438.controller;

import java.sql.Date;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cst438.domain.CreateResponse;
import cst438.domain.Flight;
import cst438.domain.Reservation;
import cst438.domain.Response;
import cst438.domain.Seat;
import cst438.service.AirlineService;

@RestController
@RequestMapping("/api")
public class AirlineRestController {

  @Autowired
  private AirlineService airlineService;

  @GetMapping("/getFlightDates")
  public ArrayList<Date> getFlightDate(@RequestParam("originCity") String origin,
      @RequestParam("destinationCity") String destination) {

    ArrayList<Date> dates = airlineService.getDatesForRoute(origin, destination);

    return dates;
  }

  /**
   * Mapping that returns all possible routes (origin and destination pairs) that are present in the
   * database
   */
  @GetMapping("/getRoutes")
  public ArrayList<String> getRoutes() {
    ArrayList<String> routes = airlineService.getAllRoutes();
    return routes;
  }

  /**
   * Mapping that returns all available flights
   */
  @GetMapping("/getAllFlights")
  public ArrayList<Flight> getAllFlights() {
    ArrayList<Flight> flights = airlineService.getAllFlights();
    return flights;
  }

  @GetMapping("/getFlights")
  public ArrayList<Flight> getFlights(@RequestParam("originCity") String origin,
      @RequestParam("destinationCity") String destination) {
    // avoids calling the DB.
    if (origin == null || destination == null) {
      return new ArrayList<Flight>();
    }

    ArrayList<Flight> flights = airlineService.getFlightsByRoute(origin, destination);
    return flights;

  }

  @GetMapping("/getSeats")
  public ArrayList<Seat> getSeats(@RequestParam("flightId") int flightId,
      @RequestParam("isFirstClass") boolean isFirstClass) {

    System.out.println(
        "Rest: Get Seats " + "Flight ID: " + flightId + " Is First Class: " + isFirstClass);

    ArrayList<Seat> seats = airlineService.getSeatsByFlightId(flightId, isFirstClass);

    return seats;

  }

  /**
   * Creates a reservation using the flight ID, User ID, seat ID and passenger information. If it
   * was successful, this method will return the newly created reservation, otherwise it will return
   * with an "incorrect ID" exception or a "seat not available" exception.
   */

  @PostMapping(value = "/makeReservation")
  public CreateResponse makeReservation(@RequestParam("flightId") int flightId,
      @RequestParam("userId") int userId, @RequestParam("seatId") int seatId,
      @RequestParam("passengerFirstName") String passengerFirstName,
      @RequestParam("passengerLastName") String passengerLastName) {

    System.out.println("\nAttempting to make a reservation for " + passengerFirstName + " "
        + passengerLastName + "\nReservation Details... \nFlight ID: " + flightId + " | User ID: "
        + userId + " | Seat ID: " + seatId);

    // Check if this seat is available to book
    if (!airlineService.isSeatAvailable(seatId)) {
      // The seat is not available
      return new CreateResponse("Error: Seat ID " + seatId + " is not available.", null);
    }

    Reservation reservation = airlineService.makeReservation(flightId, userId, seatId,
        passengerFirstName, passengerLastName);

    // Check that the reservation was created correctly
    if (reservation == null) {
      // At least one ID was incorrect
      return new CreateResponse("Error: Request contains incorrect flight ID, user ID or seat ID.",
          null);
    }

    System.out.println("-- Reservation Created ID: " + reservation.getReservationId() + " --");
    CreateResponse response = new CreateResponse("Success", reservation);

    return response;
  }

  /**
   * Cancels a reservation using a reservation ID and user ID. If it was successful, this method
   * will return the reservation ID that was just cancelled. If this reservation ID and user ID pair
   * do not have a corresponding reservation, a 404 not found exception will be returned.
   */
  @PostMapping(value = "/cancelReservation")
  public Response cancelReservation(@RequestParam("reservationId") int reservationId,
      @RequestParam("userId") int userId) {

    System.out.println("\nAttempting to cancel reservation with Reservation ID: " + reservationId
        + " and User ID: " + userId);

    // Check that this reservation is valid
    if (!airlineService.isValidReservation(reservationId, userId)) {
      // This is not a valid reservation
      return new Response("Error: Invalid Reservation", null);
    }

    // Cancel the reservation
    if (!airlineService.cancelReservation(reservationId)) {
      // This is not a valid reservation
      return new Response("Error: Invalid Reservation", null);
    }

    System.out.println("-- Cancelled Reservation with ID: " + reservationId + " --");

    return new Response("Success", reservationId);
  }

  /**
   * Provides a list of all the reservations for the specified user ID.
   */
  @GetMapping("/getAllReservations")
  public Response getSeats(@RequestParam("userId") int userId,
      @RequestParam("password") String password) {

    System.out.println("Getting all reservations for user with User ID: " + userId);

    ArrayList<Reservation> reservations =
        airlineService.getAllReservationsForUser(userId, password);

    if (reservations.isEmpty()) {
      return new Response("Error: Could not find any reservations for this User ID and password.",
          null);
    }

    return new Response("Success", reservations);

  }


}
