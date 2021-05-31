package cst438.service;

import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cst438.domain.Flight;
import cst438.domain.FlightRepository;
import cst438.domain.PassengerRepository;
import cst438.domain.ReservationRepository;
import cst438.domain.Seat;
import cst438.domain.SeatRepository;
import cst438.domain.UserRepository;

@Service
public class AirlineService {

  @Autowired
  FlightRepository flightRepository;
  @Autowired
  PassengerRepository passengerRepository;
  @Autowired
  ReservationRepository reservationRepository;
  @Autowired
  SeatRepository seatRepository;
  @Autowired
  UserRepository userRepository;

  public AirlineService() {}

  public AirlineService(FlightRepository flightRepository, PassengerRepository passengerRepository,
      ReservationRepository reservationRepository, SeatRepository seatRepository,
      UserRepository userRepository) {
    super();
    this.flightRepository = flightRepository;
    this.passengerRepository = passengerRepository;
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

  public ArrayList<Date> getAvailableDates(String originCity, String destinationCity) {
    return flightRepository.findAvailableDates(originCity, destinationCity);
  }

  public ArrayList<Date> getAvailableTimes(String originCity, String destinationCity, String date) {
    return flightRepository.findAvailableTimes(originCity, destinationCity, date);
  }

  public ArrayList<Flight> getFlightsByRoute(String originCity, String destinationCity) {
    return flightRepository.findFlightsByRoute(originCity, destinationCity);
  }

  public ArrayList<Seat> getSeatsByFlightId(int flightId, int isFirstClass) {
    return seatRepository.findSeatsByFlightID(flightId, isFirstClass);
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
