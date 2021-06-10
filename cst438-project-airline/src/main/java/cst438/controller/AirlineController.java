package cst438.controller;

import java.sql.Date;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import cst438.domain.Flight;
import cst438.domain.Reservation;
import cst438.domain.Seat;
import cst438.service.AirlineService;

@Controller
public class AirlineController {

  @Autowired
  private AirlineService airlineService;

  @GetMapping("/")
  public String getTester(Model model) {
    ArrayList<String> originCities = airlineService.getOriginCities();
    ArrayList<String> destinationCities = airlineService.getDestinationCities();

    model.addAttribute("originCities", originCities);
    model.addAttribute("destinationCities", destinationCities);

    return "index";
  }

  /**
   * Searches for and returns flights, given a route and a date
   */
  @PostMapping("/searchFlights")
  public String searchFlights(@RequestParam(value = "originCity") String origin,
      @RequestParam(value = "destinationCity") String destination,
      @RequestParam(value = "departureDate") String date,
      @RequestParam(value = "numberOfPassengers") String numberOfPassengers, Model model) {

    System.out.println("Search Flights: " + "Origin: " + origin + " Destination: " + destination
        + " Date: " + date + " Number of passengers: " + numberOfPassengers);

    ArrayList<Flight> flights = airlineService.getFlightsByRouteAndDate(origin, destination, date);

    model.addAttribute("numberOfPassengers", numberOfPassengers);
    model.addAttribute("flights", flights);
    return "display_flights";
  }

  @PostMapping("/searchFlights/seats")
  public String pickSeats(@RequestParam("flightId") int flightId,
      @RequestParam(value = "numberOfPassengers") int numberOfPassengers, Model model) {

    System.out.println("Search Flights Seats: " + " FlightID: " + flightId
        + " Number Of Passengers: " + numberOfPassengers);

    ArrayList<Seat> firstClassSeats = airlineService.getSeatsByFlightId(flightId, true);
    ArrayList<Seat> coachSeats = airlineService.getSeatsByFlightId(flightId, false);

    model.addAttribute("flightId", flightId);
    model.addAttribute("numberOfPassengers", numberOfPassengers);
    model.addAttribute("firstClassSeats", firstClassSeats);
    model.addAttribute("coachSeats", coachSeats);

    return "pick_seats";

  }

  @PostMapping("/searchFlights/passengers")
  public String passengers(@RequestParam("flightId") String flightId,
      @RequestParam("numberOfPassengers") int numberOfPassengers,
      @RequestParam("selectedSeats[]") ArrayList<String> selectedSeatIds, Model model) {

    System.out.println("Search Flights Passengers " + " FlightID: " + flightId
        + " Number Of Passengers: " + numberOfPassengers + " selectedSeatIds " + selectedSeatIds);

    ArrayList<Reservation> reservations = new ArrayList<>(numberOfPassengers);
    // System.out.println(reservations.size());

    model.addAttribute("flightId", flightId);
    model.addAttribute("numberOfPassengers", numberOfPassengers);
    // model.addAttribute("reservations", reservations);

    System.out.println("Sending Model: " + model);

    return "passenger_booking";
  }


  @PostMapping("/bookFlight")
  public String bookFlight(@RequestParam("reservations[]") ArrayList<Reservation> reservations,
      Model model) {

    for (Reservation r : reservations) {
      System.out.println(r.toString());
    }

    return "flight_confirmation";
  }



  @GetMapping("/testing")
  public String showTest(Model model) {
    ArrayList<String> originCities = airlineService.getOriginCities();
    System.out.println("\nOrigin cities: " + originCities + "\n");

    ArrayList<String> destCities = airlineService.getDestinationCities();
    System.out.println("Destination cities: " + destCities + "\n");

    String originCity = "seattle";
    String destinationCity = "san diego";

    ArrayList<Date> availDates = airlineService.getDatesForRoute(originCity, destinationCity);
    System.out.println("Dates: " + availDates + "\n");

    String date = "2021-07-12";
    ArrayList<Flight> availFlights =
        airlineService.getFlightsByRouteAndDate(originCity, destinationCity, date);
    System.out.println("Times: " + availFlights + "\n");

    return "tester";
  }



}
