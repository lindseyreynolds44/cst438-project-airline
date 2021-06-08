package cst438.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import java.sql.Date;
import java.sql.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import cst438.domain.Flight;
import cst438.domain.FlightRepository;
import cst438.domain.Reservation;
import cst438.domain.ReservationRepository;
import cst438.domain.Seat;
import cst438.domain.SeatRepository;
import cst438.domain.User;
import cst438.domain.UserRepository;

@WebMvcTest(AirlineService.class)
public class AirlineServiceTest {

  @MockBean
  private FlightRepository flightRepository;

  @MockBean
  private ReservationRepository reservationRepository;

  @MockBean
  private SeatRepository seatRepository;

  @MockBean
  private UserRepository userRepository;

  private AirlineService as;

  @SuppressWarnings("deprecation")
  @BeforeEach
  public void setUpBefore() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testIsSeatAvailableTrue() {
    // Service object that will be tested
    as = new AirlineService(flightRepository, reservationRepository, seatRepository,
        userRepository);

    // Data for creating stubs
    int seatId = 12;
    Seat seat = new Seat(12, 10, 20, "B", 1, 0);

    // Create stubs for the MOCK database
    given(seatRepository.findBySeatId(seatId)).willReturn(seat);

    // Test the isSeatAvailable method
    boolean result = as.isSeatAvailable(seatId);

    assertEquals(true, result);
  }

  @Test
  public void testIsSeatAvailableFalse() {
    // Service object that will be tested
    as = new AirlineService(flightRepository, reservationRepository, seatRepository,
        userRepository);

    // Data for creating stubs
    int seatId = 198;
    Seat seat = new Seat(198, 7, 30, "A", 0, 0);

    // Create stubs for the MOCK database
    given(seatRepository.findBySeatId(seatId)).willReturn(seat);

    // Test the isSeatAvailable method
    boolean result = as.isSeatAvailable(seatId);

    assertEquals(false, result);
  }

  @Test
  public void testIsSeatAvailableSeatDoesNotExist() {
    // Service object that will be tested
    as = new AirlineService(flightRepository, reservationRepository, seatRepository,
        userRepository);

    // Data for creating stubs
    int seatId = 3000;
    Seat seat = null;

    // Create stubs for the MOCK database
    given(seatRepository.findBySeatId(seatId)).willReturn(seat);

    // Test the isSeatAvailable method
    boolean result = as.isSeatAvailable(seatId);

    assertEquals(false, result);
  }


  @Test
  public void testMakeReservationWithValidInfo() {
    // Service object that will be tested
    as = new AirlineService(flightRepository, reservationRepository, seatRepository,
        userRepository);

    // Data for creating stubs
    int flightId = 12;
    int userId = 10;
    int seatId = 198;
    int numStops = 0;
    int price = 200;
    User user = new User(userId, "Test", "Person");
    Flight flight = new Flight(flightId, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);
    Seat seat = new Seat(seatId, flightId, 30, "A", 1, 0);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(user);
    given(flightRepository.findByFlightId(flightId)).willReturn(flight);
    given(seatRepository.findBySeatId(seatId)).willReturn(seat);

    Reservation actual = as.makeReservation(flightId, userId, seatId, "Test", "Person");

    Reservation expected = new Reservation(0, user, "Test", "Person", flight, seat, null, price);

    assertEquals(expected, actual);
  }

  @Test
  public void testMakeReservationWithInvalidFlightId() {
    // Service object that will be tested
    as = new AirlineService(flightRepository, reservationRepository, seatRepository,
        userRepository);

    // Data for creating stubs
    int flightId = 12;
    int userId = 10;
    int seatId = 198;
    User user = new User(userId, "Test", "Person");
    Seat seat = new Seat(seatId, flightId, 30, "A", 1, 0);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(user);
    given(flightRepository.findByFlightId(flightId)).willReturn(null);
    given(seatRepository.findBySeatId(seatId)).willReturn(seat);

    Reservation actual = as.makeReservation(flightId, userId, seatId, "Test", "Person");

    Reservation expected = null;

    assertEquals(expected, actual);
  }

  @Test
  public void testMakeReservationWithInvalidUserId() {
    // Service object that will be tested
    as = new AirlineService(flightRepository, reservationRepository, seatRepository,
        userRepository);

    // Data for creating stubs
    int flightId = 12;
    int userId = 10;
    int seatId = 198;
    int numStops = 0;
    int price = 200;
    Flight flight = new Flight(flightId, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);
    Seat seat = new Seat(seatId, flightId, 30, "A", 1, 0);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(null);
    given(flightRepository.findByFlightId(flightId)).willReturn(flight);
    given(seatRepository.findBySeatId(seatId)).willReturn(seat);

    Reservation actual = as.makeReservation(flightId, userId, seatId, "Test", "Person");

    Reservation expected = null;

    assertEquals(expected, actual);
  }

  @Test
  public void testMakeReservationWithInvalidSeatId() {
    // Service object that will be tested
    as = new AirlineService(flightRepository, reservationRepository, seatRepository,
        userRepository);

    // Data for creating stubs
    int flightId = 12;
    int userId = 10;
    int seatId = 198;
    int numStops = 0;
    int price = 200;
    User user = new User(userId, "Test", "Person");
    Flight flight = new Flight(flightId, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(user);
    given(flightRepository.findByFlightId(flightId)).willReturn(flight);
    given(seatRepository.findBySeatId(seatId)).willReturn(null);

    Reservation actual = as.makeReservation(flightId, userId, seatId, "Test", "Person");

    Reservation expected = null;

    assertEquals(expected, actual);
  }

}
