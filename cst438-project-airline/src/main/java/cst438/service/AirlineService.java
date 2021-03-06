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

  private static final int FIRST_CLASS_MULTIPLIER = 2;

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

  public ArrayList<Seat> getSeatsByFlightId(int flightId, boolean isFirstClass) {
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
    if (user == null || flight == null || seat == null || passengerFirstName == ""
        || passengerLastName == "") {
      return null;
    }

    // Set seat to unavailable
    seatRepository.setSeatToUnavailable(seatId);

    int price = getSeatPrice(seat, flight);

    Reservation reservation =
        new Reservation(user, passengerFirstName, passengerLastName, flight, seat, price);

    reservationRepository.save(reservation);

    Reservation reservationFromDb =
        reservationRepository.findByReservationId(reservation.getReservationId());

    return reservationFromDb;
  }

  /**
   * Creates a reservation for multiple passengers. Designed to be used with the web application and
   * AirlineController.
   */
  public ArrayList<Reservation> makeReservation(int userId, int flightId,
      ArrayList<Integer> seatIds, ArrayList<String> passengerFirstNames,
      ArrayList<String> passengerLastNames) {

    final int numPassengers = seatIds.size();
    ArrayList<Reservation> reservations = new ArrayList<>();

    final User user = userRepository.findByUserId(userId);
    final Flight flight = flightRepository.findByFlightId(flightId);
    int price;
    Seat seat;
    String firstName;
    String lastName;
    Reservation reservation;

    // creates a reservation for each passenger
    for (int x = 0; x < numPassengers; x++) {
      firstName = passengerFirstNames.get(x);
      lastName = passengerLastNames.get(x);
      seat = seatRepository.findBySeatId(seatIds.get(x));

      // Check if any of the entered IDs are invalid
      if (user == null || flight == null || seat == null || firstName == "" || lastName == "") {
        return null;
      }

      price = getSeatPrice(seat, flight);

      if (!isSeatAvailable(seat.getSeatId())) {
        return null;
      }

      seatRepository.setSeatToUnavailable(seat.getSeatId());
      reservation = new Reservation(user, firstName, lastName, flight, seat, price);
      reservations.add(reservation);
    }
    reservationRepository.saveAll(reservations);
    return reservations;
  }



  /**
   * Provides the price for a seat, considering its class and associated flight
   */
  private int getSeatPrice(Seat seat, Flight flight) {
    int price = flight.getPrice();
    if (seat.getIsFirstClass()) {
      price = price * FIRST_CLASS_MULTIPLIER;
    }
    return price;
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
    boolean isAvailable = seat.getIsAvailable();
    if (!isAvailable) {
      return false;
    }
    return true;
  }

  /**
   * Checks if this reservation is valid and that the user ID and reservation ID match. Returns a
   * boolean value.
   */
  public boolean isValidReservation(int reservationId, int userId) {
    Reservation reservation =
        reservationRepository.findByReservationIdAndUserId(reservationId, userId);

    if (reservation == null) {
      return false;
    }
    return true;
  }

  /**
   * Removes this reservation from the database and changes the associated seat to available again.
   * Returns a boolean that indicates whether the reservation was cancelled successfully or not.
   */
  public boolean cancelReservation(int reservationId) {
    Reservation reservation = reservationRepository.findByReservationId(reservationId);

    // This should never happen, if isValidReservation() is called before this method
    if (reservation == null)
      return false;

    // Change this seat to be available now
    int seatId = reservation.getSeat().getSeatId();
    seatRepository.setSeatToAvailable(seatId);

    // Delete this reservation from the database
    reservationRepository.cancelByReservationId(reservationId);

    return true;
  }

  /**
   * Gets all the reservations associated with a user ID. Takes the user ID and its corresponding
   * password. Returns an empty array if no reservations are found.
   */
  public ArrayList<Reservation> getAllReservationsForUser(int userId) {
    User user = userRepository.findUserByIdAndPassword(userId);

    // Check if this user ID matches with a record in the database
    if (user == null) {
      // No record found
      return new ArrayList<Reservation>();
    }

    return reservationRepository.findAllReservationsWithUserId(userId);

  }

}
