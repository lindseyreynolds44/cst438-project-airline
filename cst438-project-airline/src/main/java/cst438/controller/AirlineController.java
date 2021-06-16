package cst438.controller;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import cst438.domain.Flight;
import cst438.domain.Reservation;
import cst438.domain.Seat;
import cst438.domain.User;
import cst438.domain.UserRepository;
import cst438.service.AirlineService;

@Controller
public class AirlineController {

  @Autowired
  private AirlineService airlineService;

  @Autowired
  private UserRepository userRepository;

  /**
   * Displays the landing page
   */
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

  /**
   * 
   */
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
      @RequestParam("selectedSeats[]") ArrayList<String> selectedSeatIds, Model model,
      HttpServletRequest request) {

    System.out.println("Search Flights Passengers:\n" + " FlightID: " + flightId
        + " Number Of Passengers: " + numberOfPassengers + " selectedSeatIds " + selectedSeatIds);

    // gets user from authentication
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) auth.getPrincipal();
    User user = userRepository.findByUserName(userDetails.getUsername());
    // adds userId to session
    request.getSession().setAttribute("userId", user.getUserId());
    System.out.println("USER ID: [" + (int) request.getSession().getAttribute("userId") + "]");

    model.addAttribute("flightId", flightId);
    model.addAttribute("numberOfPassengers", numberOfPassengers);
    model.addAttribute("seats", selectedSeatIds);
    model.addAttribute("userId", user.getUserId());

    System.out.println("Search Flights Passengers Sending Model:\n" + model);

    return "passenger_booking";
  }


  @PostMapping("/bookFlight")
  public String bookFlight(@RequestParam("user") int userId, @RequestParam("flightId") int flightId,
      @RequestParam("seats[]") ArrayList<Integer> seatIds,
      @RequestParam("firstNames[]") ArrayList<String> firstNames,
      @RequestParam("lastNames[]") ArrayList<String> lastNames, Model model) {

    System.out.println("Book Flight Incoming:\n User " + userId + " FlightId " + flightId
        + " Seats " + seatIds + " First Names " + firstNames + " Last Names " + lastNames);


    ArrayList<Reservation> reservations =
        airlineService.makeReservation(userId, flightId, seatIds, firstNames, lastNames);

    model.addAttribute("reservations", reservations);

    System.out.println("Book Flight Sending:\n" + model);

    return "flight_confirmation";
  }

  @GetMapping("/reservations")
  public String reservations(HttpServletRequest request, Model model) {

    // gets user from authentication
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) auth.getPrincipal();
    User user = userRepository.findByUserName(userDetails.getUsername());

    ArrayList<Reservation> reservations =
        airlineService.getAllReservationsForUser(user.getUserId());

    model.addAttribute("userId", user.getUserId());
    model.addAttribute("reservations", reservations);
    System.out.println("Reservations Sending:\n" + model);

    return "reservations";
  }

}
