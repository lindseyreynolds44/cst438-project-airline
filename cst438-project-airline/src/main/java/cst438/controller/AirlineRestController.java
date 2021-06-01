package cst438.controller;

import java.sql.Date;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cst438.domain.Flight;
import cst438.domain.Seat;
import cst438.service.AirlineService;

@RestController
@RequestMapping("/api")
public class AirlineRestController {

  @Autowired
  private AirlineService airlineService;

  @GetMapping("/getFlightDate")
  public ArrayList<Date> getFlightDate(@RequestParam("originCity") String origin,
      @RequestParam("destinationCity") String destination) {

    ArrayList<Date> dates = airlineService.getDatesForRoute(origin, destination);

    return dates;
  }

  @GetMapping("getFlights")
  public ArrayList<Flight> getFlights(@RequestParam("originCity") String origin,
      @RequestParam("destinationCity") String destination) {
    // avoids calling the DB.
    if (origin == null || destination == null) {
      return new ArrayList<Flight>();
    }

    ArrayList<Flight> flights = airlineService.getFlightsByRoute(origin, destination);
    return flights;

  }

  @GetMapping("getSeats")
  public ArrayList<Seat> getSeats(@RequestParam("flightId") int flightId,
      @RequestParam("isFirstClass") int isFirstClass) {

    ArrayList<Seat> seats = airlineService.getSeatsByFlightId(flightId, isFirstClass);

    return seats;

  }

  // AJAX call to get seats based on which class the user selected


}
