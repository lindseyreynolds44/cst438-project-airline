package cst438.controller;

import java.util.ArrayList;
import java.util.Date;
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

  // TODO Finish up this method
  @GetMapping("/searchFlights")
  public String searchFlights(@RequestParam("originCity") String origin,
      @RequestParam("destinationCity") String destination, @RequestParam("date") String date,
      Model model) {

    Iterable<Flight> flights = airlineService.getFlights();
    model.addAttribute("flights", flights);
    return "display_flights";
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

    ArrayList<Date> availDates = airlineService.getAvailableDates(originCity, destinationCity);
    System.out.println("Dates: " + availDates + "\n");

    String date = "2021-07-12";
    ArrayList<Date> availTimes =
        airlineService.getAvailableTimes(originCity, destinationCity, date);
    System.out.println("Times: " + availTimes + "\n");

    return "index";
  }



}
