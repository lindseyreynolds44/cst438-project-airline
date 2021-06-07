package cst438.service;

import java.sql.Date;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cst438.domain.Flight;
import cst438.domain.FlightRepository;
import cst438.domain.Reservation;
import cst438.domain.ReservationRepository;
import cst438.domain.Seat;
import cst438.domain.SeatRepository;
import cst438.domain.User;
import cst438.domain.UserRepository;

@Service
public class AirlineService {

  @Autowired
  FlightRepository flightRepository;
  @Autowired
  ReservationRepository reservationRepository;
  @Autowired
  SeatRepository seatRepository;
  @Autowired
  UserRepository userRepository;

  public AirlineService() {}

  public AirlineService(FlightRepository flightRepository,
      ReservationRepository reservationRepository, SeatRepository seatRepository,
      UserRepository userRepository) {
    super();
    this.flightRepository = flightRepository;
    this.reservationRepository = reservationRepository;
    this.seatRepository = seatRepository;
    this.userRepository = userRepository;
  }

  public Iterable<Flight> getFlights() {
    return flightRepository.findAllByAirline();
  }

  public ArrayList<String> getOriginCities() {
    return flightRepository.findOriginCities();
  }

  public ArrayList<String> getDestinationCities() {
    return flightRepository.findDestinationCities();
  }

  public ArrayList<Date> getDatesForRoute(String originCity, String destinationCity) {
    return flightRepository.findDatesForRoute(originCity, destinationCity);
  }

  public ArrayList<Flight> getFlightsByRouteAndDate(String originCity, String destinationCity,
      String date) {
    return flightRepository.findFlightsByRouteAndDate(originCity, destinationCity, date);
  }

  public ArrayList<Flight> getFlightsByRoute(String originCity, String destinationCity) {
    return flightRepository.findFlightsByRoute(originCity, destinationCity);
  }

  public ArrayList<Seat> getSeatsByFlightId(int flightId, int isFirstClass) {
    return seatRepository.findSeatsByFlightID(flightId, isFirstClass);
  }

  public ArrayList<String> getAllRoutes() {
    return flightRepository.findAllRoutes();
  }

  public ArrayList<Flight> getAllFlights() {
    return flightRepository.findAll();
  }

  /**
   * Creates a new reservation using the flight ID, user ID, seat ID, first name and last name
   * specified. Returns the newly created reservation object or null if any of the IDs entered were
   * incorrect.
   */
  public Reservation makeReservation(int flightId, int userId, int seatId,
      String passengerFirstName, String passengerLastName) {
    User user = userRepository.findByUserId(userId);
    Flight flight = flightRepository.findByFlightId(flightId);
    Seat seat = seatRepository.findBySeatId(seatId);

    // Check if any of the entered IDs are invalid
    if (user == null || flight == null || seat == null) {
      return null;
    }

    // Set seat to unavailable
    seatRepository.setSeatToUnavailable(seatId);

    Reservation reservation = new Reservation(user, passengerFirstName, passengerLastName, flight,
        seat, flight.getPrice());

    reservationRepository.save(reservation);

    return reservation;
  }

  /**
   * Checks if this seat is in the database and available to book. Returns a boolean value.
   */
  public boolean isSeatAvailable(int seatId) {

    // Check if this seat exists in the database
    Seat seat = seatRepository.findBySeatId(seatId);
    if (seat == null) {
      return false;
    }

    // Check if this seat is available to book
    int isAvailable = seatRepository.isSeatAvailable(seatId);
    if (isAvailable == 0) {
      return false;
    }
    return true;
  }


  // We need the following methods:

  // A method to return available flights (Note From Dan: I will implement this)
  // A method to return seats for a given origin city and destination (Note from Dan: This should be
  // by flight ID, yeah?)
  // if all seats are used up, then return "no flights"
  // if there are no flights then we return "no flights"

  // A method to book a reservation

  // A method to change a reservation

  // A method to cancel a reservation



}
