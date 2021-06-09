package cst438.controller;

import java.sql.Date;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import cst438.domain.Flight;
import cst438.domain.Reservation;
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
      @RequestParam("isFirstClass") int isFirstClass) {

    System.out.println(
        "Rest: Get Seats " + "Flight ID: " + flightId + " Is First Class: " + isFirstClass);

    ArrayList<Seat> seats = airlineService.getSeatsByFlightId(flightId, isFirstClass);

    return seats;

  }

  // Example call (will put in readme)
  // http://localhost:8080/api/makeReservation?flightId=7&userId=9&seatId=890&passengerFirstName=Lindsey&passengerLastName=Reynolds
  @GetMapping("/makeReservation")
  public Reservation makeReservation(@RequestParam("flightId") int flightId,
      @RequestParam("userId") int userId, @RequestParam("seatId") int seatId,
      @RequestParam("passengerFirstName") String passengerFirstName,
      @RequestParam("passengerLastName") String passengerLastName) {

    // Check if this seat is available to book
    if (!airlineService.isSeatAvailable(seatId)) {
      // The seat is not available
      throw new ResponseStatusException(HttpStatus.OK,
          "MESSAGE: This seat is not available. Seat ID: " + seatId);
    }


    Reservation reservation = airlineService.makeReservation(flightId, userId, seatId,
        passengerFirstName, passengerLastName);

    // Check that the reservation was created correctly
    if (reservation == null) {
      // At least one ID was incorrect
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "MESSAGE: Request contains incorrect flight ID, user ID or seat ID.");
    }

    return reservation;
  }


}
