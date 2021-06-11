package cst438.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
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

    System.out.println("Search Flights:\n" + "Origin: " + origin + " Destination: " + destination
        + " Date: " + date + " Number of passengers: " + numberOfPassengers);

    ArrayList<Flight> flights = airlineService.getFlightsByRouteAndDate(origin, destination, date);

    model.addAttribute("numberOfPassengers", numberOfPassengers);
    model.addAttribute("flights", flights);

    System.out.println("Search Flights Sending Model:\n" + model);

    return "display_flights";
  }

  @PostMapping("/searchFlights/seats")
  public String pickSeats(@RequestParam("flightId") int flightId,
      @RequestParam(value = "numberOfPassengers") int numberOfPassengers, Model model) {


    System.out.println("Search Flights Seats Incoming:\n" + "Seats: " + " FlightId: " + flightId
        + " Number Of Passengers: " + numberOfPassengers);

    ArrayList<Seat> firstClassSeats = airlineService.getSeatsByFlightId(flightId, true);
    ArrayList<Seat> coachSeats = airlineService.getSeatsByFlightId(flightId, false);

    model.addAttribute("flightId", flightId);
    model.addAttribute("numberOfPassengers", numberOfPassengers);
    model.addAttribute("firstClassSeats", firstClassSeats);
    model.addAttribute("coachSeats", coachSeats);

    System.out.println("Search Flights Seats Sending Model:\n" + model);

    return "pick_seats";

  }

  @PostMapping("/searchFlights/passengers")
  public String passengers(@RequestParam("flightId") String flightId,
      @RequestParam("numberOfPassengers") int numberOfPassengers,
      @RequestParam("selectedSeats[]") ArrayList<String> selectedSeatIds, Model model) {

    System.out.println("Search Flights Passengers:\n" + " FlightID: " + flightId
        + " Number Of Passengers: " + numberOfPassengers + " selectedSeatIds " + selectedSeatIds);

    ArrayList<Reservation> reservations = new ArrayList<>(numberOfPassengers);
    // System.out.println(reservations.size());

    model.addAttribute("flightId", flightId);
    model.addAttribute("numberOfPassengers", numberOfPassengers);
    model.addAttribute("seats", selectedSeatIds);
    // model.addAttribute("reservations", reservations);

    System.out.println("Search Flights Passengers Sending Model:\n" + model);

    return "passenger_booking";
  }


  @PostMapping("/bookFlight")
  public String bookFlight(@RequestParam("user") int user, @RequestParam("flightId") int flightId,
      @RequestParam("seats[]") ArrayList<Integer> seatIds,
      @RequestParam("firstNames[]") ArrayList<String> firstNames,
      @RequestParam("lastNames[]") ArrayList<String> lastNames, Model model) {

    System.out.println("User " + user + " FlightId " + flightId + " Seats " + seatIds
        + " First Names " + firstNames + " Last Names " + lastNames);

    /**
     * TODO: Need to send these vars to airlineService and have it perform the conversation. It
     * needs to return an object like a Confirmation object that will return the vars needed to
     * match those being added to the model.
     */

    // DEMO VARS BELOW NOT ACTUALL DATA
    model.addAttribute("flightNumber", "FL123");
    model.addAttribute("passengers",
        new ArrayList<String>(Arrays.asList("test person", "tester person", "testee person")));
    model.addAttribute("seats", new ArrayList<String>(Arrays.asList("1A", "1B", "1C")));
    model.addAttribute("totalPrice", "1500");

    System.out.println("Book Flight Sending:\n" + model);

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
