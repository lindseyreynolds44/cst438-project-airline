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

  @PostMapping("/searchFlights/provideDetails")
  public String provideDetails(@RequestParam("flightId") String flightId,
      @RequestParam(value = "numberOfPassengers") String numberOfPassengers, Model model) {

    System.out.println("Search Flights Provide Details " + " FlightID: " + flightId
        + " Number Of Passengers: " + numberOfPassengers);
    model.addAttribute("flightId", flightId);

    return "provide_details";
  }



  // TODO This happens on a button press on the flight table
  // Parameters: flight_id, how many passengers
  @GetMapping("/chooseFlight")
  public String chooseFlight(@RequestParam("flightId") String flightId,
      @RequestParam("numPassengers") String numPassengers, Model model) {

    // add this flight to model

    return "reservation_form";
  }


  @PostMapping("/bookFlight")
  public String bookFlight(@RequestParam("reservations") ArrayList<Reservation> reservations,
      Model model) {
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
