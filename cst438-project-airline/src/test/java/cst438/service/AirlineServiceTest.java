package cst438.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
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
  private CustomUserDetailsService uds;

  @SuppressWarnings("deprecation")
  @BeforeEach
  public void setUpBefore() {
    MockitoAnnotations.initMocks(this);

    as = new AirlineService(flightRepository, reservationRepository, seatRepository,
        userRepository);
  }

  @Test
  public void testIsSeatAvailableTrue() {

    // Data for creating stubs
    int seatId = 12;
    Seat seat = new Seat(12, 10, 20, "B", true, false);

    // Create stubs for the MOCK database
    given(seatRepository.findBySeatId(seatId)).willReturn(seat);

    // Test the isSeatAvailable method
    boolean result = as.isSeatAvailable(seatId);

    assertEquals(true, result);
  }

  @Test
  public void testIsSeatAvailableFalse() {
    // Data for creating stubs
    int seatId = 198;
    Seat seat = new Seat(198, 7, 30, "A", false, false);

    // Create stubs for the MOCK database
    given(seatRepository.findBySeatId(seatId)).willReturn(seat);

    // Test the isSeatAvailable method
    boolean result = as.isSeatAvailable(seatId);

    assertEquals(false, result);
  }

  @Test
  public void testIsSeatAvailableSeatDoesNotExist() {
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
  public void testMakeReservationWithValidInfoEconomySeat() {
    // Data for creating stubs
    int flightId = 12;
    String flightNumber = "TS123";
    int userId = 10;
    int seatId = 198;
    int numStops = 0;
    int price = 200;

    User user = new User(userId, "Test", "Person", "user");
    Flight flight = new Flight(flightId, flightNumber, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);
    Seat seat = new Seat(seatId, flightId, 30, "A", true, false);
    Reservation reservation = new Reservation(0, user, "Test", "Person", flight, seat, null, price);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(user);
    given(flightRepository.findByFlightId(flightId)).willReturn(flight);
    given(seatRepository.findBySeatId(seatId)).willReturn(seat);
    given(reservationRepository.findByReservationId(0)).willReturn(reservation);

    // Test the makeReservation method
    Reservation actual = as.makeReservation(flightId, userId, seatId, "Test", "Person");

    Reservation expected = new Reservation(0, user, "Test", "Person", flight, seat, null, price);

    assertEquals(expected, actual);
  }

  @Test
  public void testMakeReservationWithValidInfoFirstClassSeat() {
    // Data for creating stubs
    int flightId = 12;
    String flightNumber = "TS123";
    int userId = 10;
    int seatId = 25;
    int numStops = 0;
    int price = 200;
    int firstClassPrice = price * 2;
    User user = new User(userId, "Test", "Person", "user");
    Flight flight = new Flight(flightId, flightNumber, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);
    Seat seat = new Seat(seatId, flightId, 2, "A", true, true);
    Reservation reservation =
        new Reservation(0, user, "Test", "Person", flight, seat, null, firstClassPrice);


    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(user);
    given(flightRepository.findByFlightId(flightId)).willReturn(flight);
    given(seatRepository.findBySeatId(seatId)).willReturn(seat);
    given(reservationRepository.findByReservationId(0)).willReturn(reservation);

    // Test the makeReservation method
    Reservation actual = as.makeReservation(flightId, userId, seatId, "Test", "Person");

    Reservation expected =
        new Reservation(0, user, "Test", "Person", flight, seat, null, firstClassPrice);

    assertEquals(expected, actual);
  }

  @Test
  public void testMakeReservationWithInvalidFlightId() {
    // Data for creating stubs
    int flightId = 12;
    int userId = 10;
    int seatId = 198;
    User user = new User(userId, "Test", "Person", "user");
    Seat seat = new Seat(seatId, flightId, 30, "A", true, false);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(user);
    given(flightRepository.findByFlightId(flightId)).willReturn(null);
    given(seatRepository.findBySeatId(seatId)).willReturn(seat);

    // Test the makeReservation method
    Reservation actual = as.makeReservation(flightId, userId, seatId, "Test", "Person");

    Reservation expected = null;

    assertEquals(expected, actual);
  }

  @Test
  public void testMakeReservationWithInvalidUserId() {
    // Data for creating stubs
    int flightId = 12;
    String flightNumber = "TS123";
    int userId = 10;
    int seatId = 198;
    int numStops = 0;
    int price = 200;
    Flight flight = new Flight(flightId, flightNumber, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);
    Seat seat = new Seat(seatId, flightId, 30, "A", true, false);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(null);
    given(flightRepository.findByFlightId(flightId)).willReturn(flight);
    given(seatRepository.findBySeatId(seatId)).willReturn(seat);

    // Test the makeReservation method
    Reservation actual = as.makeReservation(flightId, userId, seatId, "Test", "Person");

    Reservation expected = null;

    assertEquals(expected, actual);
  }

  @Test
  public void testMakeReservationWithInvalidSeatId() {
    // Data for creating stubs
    int flightId = 12;
    String flightNumber = "TS123";
    int userId = 10;
    int seatId = 198;
    int numStops = 0;
    int price = 200;
    User user = new User(userId, "Test", "Person", "user");
    Flight flight = new Flight(flightId, flightNumber, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(user);
    given(flightRepository.findByFlightId(flightId)).willReturn(flight);
    given(seatRepository.findBySeatId(seatId)).willReturn(null);

    // Test the makeReservation method
    Reservation actual = as.makeReservation(flightId, userId, seatId, "Test", "Person");

    Reservation expected = null;

    assertEquals(expected, actual);
  }

  @Test
  public void testIsValidReservationTrue() {
    // Data for creating stub
    int flightId = 12;
    String flightNumber = "TS123";
    int userId = 10;
    int seatId = 25;
    int numStops = 0;
    int price = 200;
    int reservationId = 15;
    User user = new User(userId, "Test", "Person", "user");
    Flight flight = new Flight(flightId, flightNumber, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);
    Seat seat = new Seat(seatId, flightId, 2, "A", true, true);
    Reservation reservation = new Reservation(0, user, "Test", "Person", flight, seat, null, price);


    // Create stubs for the MOCK databases
    given(reservationRepository.findByReservationIdAndUserId(reservationId, userId)).willReturn(
        reservation);

    // Test the isValidReservation method
    boolean result = as.isValidReservation(reservationId, userId);

    assertEquals(true, result);

  }

  @Test
  public void testIsValidReservationFalse() {
    // Data for creating stub
    int userId = 10;
    int reservationId = 15;

    // Create stubs for the MOCK databases
    given(reservationRepository.findByReservationIdAndUserId(reservationId, userId)).willReturn(
        null);

    // Test the isValidReservation method
    boolean result = as.isValidReservation(reservationId, userId);

    assertEquals(false, result);
  }

  @Test
  public void testCancelReservationSuccess() {
    // Data for creating stub
    int flightId = 12;
    String flightNumber = "TS123";
    int userId = 10;
    int seatId = 25;
    int numStops = 0;
    int price = 200;
    int reservationId = 15;
    User user = new User(userId, "Test", "Person", "user");
    Flight flight = new Flight(flightId, flightNumber, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);
    Seat seat = new Seat(seatId, flightId, 2, "A", true, true);
    Reservation reservation = new Reservation(0, user, "Test", "Person", flight, seat, null, price);

    // Create stubs for the MOCK databases
    given(reservationRepository.findByReservationId(reservationId)).willReturn(reservation);

    // Test the cancelReservation method
    boolean result = as.cancelReservation(reservationId);

    assertEquals(true, result);
  }

  @Test
  public void testCancelReservationFailure() {
    // Data for creating stub
    int reservationId = 15;

    // Create stubs for the MOCK databases
    given(reservationRepository.findByReservationId(reservationId)).willReturn(null);

    // Test the cancelReservation method
    boolean result = as.cancelReservation(reservationId);

    assertEquals(false, result);
  }

  @Test
  public void testFrontEndMakeReservationWithEconomySeatShouldReturnArrayOfReservations() {

    // Data for creating stubs
    int flightId = 12;
    String flightNumber = "TS123";
    int userId = 10;
    int numStops = 0;
    int price = 200;

    User user = new User(userId, "Test", "Person", "user");

    Flight flight = new Flight(flightId, flightNumber, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);

    ArrayList<Integer> seatIds = new ArrayList<>(Arrays.asList(198, 199, 200));
    Seat seat1 = new Seat(seatIds.get(0), flightId, 23, "A", true, false);
    Seat seat2 = new Seat(seatIds.get(1), flightId, 23, "B", true, false);
    Seat seat3 = new Seat(seatIds.get(2), flightId, 23, "C", true, false);

    ArrayList<String> firstNames = new ArrayList<>(Arrays.asList("Geralt", "Master", "The"));
    ArrayList<String> lastNames = new ArrayList<>(Arrays.asList("Of Rivia", "Chief", "Doctor"));

    Reservation r1 = new Reservation(user, "Geralt", "Of Rivia", flight, seat1, price);
    Reservation r2 = new Reservation(user, "Master", "Chief", flight, seat2, price);
    Reservation r3 = new Reservation(user, "The", "Doctor", flight, seat3, price);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(user);
    given(flightRepository.findByFlightId(flightId)).willReturn(flight);
    given(seatRepository.findBySeatId(seatIds.get(0))).willReturn(seat1);
    given(seatRepository.findBySeatId(seatIds.get(1))).willReturn(seat2);
    given(seatRepository.findBySeatId(seatIds.get(2))).willReturn(seat3);

    // Test the makeReservation method
    ArrayList<Reservation> actual =
        as.makeReservation(userId, flightId, seatIds, firstNames, lastNames);

    ArrayList<Reservation> expected = new ArrayList<>(Arrays.asList(r1, r2, r3));

    assertEquals(expected, actual);

  }

  @Test
  public void testFrontEndMakeReservationWithFirstClassSeatShouldReturnArrayOfReservations() {

    // Data for creating stubs
    int flightId = 12;
    String flightNumber = "TS123";
    int userId = 10;
    int numStops = 0;
    int price = 200;
    int firstClassPrice = price * 2;

    User user = new User(userId, "Test", "Person", "user");

    Flight flight = new Flight(flightId, flightNumber, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);

    ArrayList<Integer> seatIds = new ArrayList<>(Arrays.asList(198, 199, 200));
    Seat seat1 = new Seat(seatIds.get(0), flightId, 2, "A", true, true);
    Seat seat2 = new Seat(seatIds.get(1), flightId, 2, "B", true, true);
    Seat seat3 = new Seat(seatIds.get(2), flightId, 2, "C", true, true);

    ArrayList<String> firstNames = new ArrayList<>(Arrays.asList("Geralt", "Master", "The"));
    ArrayList<String> lastNames = new ArrayList<>(Arrays.asList("Of Rivia", "Chief", "Doctor"));

    Reservation r1 = new Reservation(user, "Geralt", "Of Rivia", flight, seat1, firstClassPrice);
    Reservation r2 = new Reservation(user, "Master", "Chief", flight, seat2, firstClassPrice);
    Reservation r3 = new Reservation(user, "The", "Doctor", flight, seat3, firstClassPrice);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(user);
    given(flightRepository.findByFlightId(flightId)).willReturn(flight);
    given(seatRepository.findBySeatId(seatIds.get(0))).willReturn(seat1);
    given(seatRepository.findBySeatId(seatIds.get(1))).willReturn(seat2);
    given(seatRepository.findBySeatId(seatIds.get(2))).willReturn(seat3);

    // Test the makeReservation method
    ArrayList<Reservation> actual =
        as.makeReservation(userId, flightId, seatIds, firstNames, lastNames);

    ArrayList<Reservation> expected = new ArrayList<>(Arrays.asList(r1, r2, r3));

    assertEquals(expected, actual);

  }


  @Test
  public void testFrontEndMakeReservationWithInvalidUserShouldFail() {
    // Data for creating stubs
    int flightId = 12;
    String flightNumber = "TS123";
    int userId = 10;
    int numStops = 0;
    int price = 200;
    int firstClassPrice = price * 2;

    User user = null;

    Flight flight = new Flight(flightId, flightNumber, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);

    ArrayList<Integer> seatIds = new ArrayList<>(Arrays.asList(198, 199, 200));
    Seat seat1 = new Seat(seatIds.get(0), flightId, 2, "A", true, true);
    Seat seat2 = new Seat(seatIds.get(1), flightId, 2, "B", true, true);
    Seat seat3 = new Seat(seatIds.get(2), flightId, 2, "C", true, true);

    ArrayList<String> firstNames = new ArrayList<>(Arrays.asList("Geralt", "Master", "The"));
    ArrayList<String> lastNames = new ArrayList<>(Arrays.asList("Of Rivia", "Chief", "Doctor"));

    Reservation r1 = new Reservation(user, "Geralt", "Of Rivia", flight, seat1, firstClassPrice);
    Reservation r2 = new Reservation(user, "Master", "Chief", flight, seat2, firstClassPrice);
    Reservation r3 = new Reservation(user, "The", "Doctor", flight, seat3, firstClassPrice);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(user);
    given(flightRepository.findByFlightId(flightId)).willReturn(flight);
    given(seatRepository.findBySeatId(seatIds.get(0))).willReturn(seat1);
    given(seatRepository.findBySeatId(seatIds.get(1))).willReturn(seat2);
    given(seatRepository.findBySeatId(seatIds.get(2))).willReturn(seat3);

    // Test the makeReservation method
    ArrayList<Reservation> actual =
        as.makeReservation(userId, flightId, seatIds, firstNames, lastNames);

    ArrayList<Reservation> expected = null;

    assertEquals(expected, actual);
  }

  @Test
  public void testFrontEndMakeReservationWithInvalidFlightIdShouldFail() {
    // Data for creating stubs
    int flightId = 12;
    int userId = 10;
    int price = 200;
    int firstClassPrice = price * 2;

    User user = new User(userId, "Test", "Person", "user");

    Flight flight = null;

    ArrayList<Integer> seatIds = new ArrayList<>(Arrays.asList(198, 199, 200));
    Seat seat1 = new Seat(seatIds.get(0), flightId, 2, "A", true, true);
    Seat seat2 = new Seat(seatIds.get(1), flightId, 2, "B", true, true);
    Seat seat3 = new Seat(seatIds.get(2), flightId, 2, "C", true, true);

    ArrayList<String> firstNames = new ArrayList<>(Arrays.asList("Geralt", "Master", "The"));
    ArrayList<String> lastNames = new ArrayList<>(Arrays.asList("Of Rivia", "Chief", "Doctor"));

    Reservation r1 = new Reservation(user, "Geralt", "Of Rivia", flight, seat1, firstClassPrice);
    Reservation r2 = new Reservation(user, "Master", "Chief", flight, seat2, firstClassPrice);
    Reservation r3 = new Reservation(user, "The", "Doctor", flight, seat3, firstClassPrice);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(user);
    given(flightRepository.findByFlightId(flightId)).willReturn(flight);
    given(seatRepository.findBySeatId(seatIds.get(0))).willReturn(seat1);
    given(seatRepository.findBySeatId(seatIds.get(1))).willReturn(seat2);
    given(seatRepository.findBySeatId(seatIds.get(2))).willReturn(seat3);

    // Test the makeReservation method
    ArrayList<Reservation> actual =
        as.makeReservation(userId, flightId, seatIds, firstNames, lastNames);

    ArrayList<Reservation> expected = null;

    assertEquals(expected, actual);

  }

  @Test
  public void testFrontEndMakeReservationWithInvalidSeatIdShouldFail() {
    // Data for creating stubs
    int flightId = 12;
    String flightNumber = "TS123";
    int userId = 10;
    int numStops = 0;
    int price = 200;
    int firstClassPrice = price * 2;

    User user = new User(userId, "Test", "Person", "user");

    Flight flight = new Flight(flightId, flightNumber, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);

    ArrayList<Integer> seatIds = new ArrayList<>(Arrays.asList(198, 199, 200));
    Seat seat1 = null;
    Seat seat2 = new Seat(seatIds.get(1), flightId, 2, "B", true, true);
    Seat seat3 = new Seat(seatIds.get(2), flightId, 2, "C", true, true);

    ArrayList<String> firstNames = new ArrayList<>(Arrays.asList("Geralt", "Master", "The"));
    ArrayList<String> lastNames = new ArrayList<>(Arrays.asList("Of Rivia", "Chief", "Doctor"));

    Reservation r1 = new Reservation(user, "Geralt", "Of Rivia", flight, seat1, firstClassPrice);
    Reservation r2 = new Reservation(user, "Master", "Chief", flight, seat2, firstClassPrice);
    Reservation r3 = new Reservation(user, "The", "Doctor", flight, seat3, firstClassPrice);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(user);
    given(flightRepository.findByFlightId(flightId)).willReturn(flight);
    given(seatRepository.findBySeatId(seatIds.get(0))).willReturn(seat1);
    given(seatRepository.findBySeatId(seatIds.get(1))).willReturn(seat2);
    given(seatRepository.findBySeatId(seatIds.get(2))).willReturn(seat3);

    // Test the makeReservation method
    ArrayList<Reservation> actual =
        as.makeReservation(userId, flightId, seatIds, firstNames, lastNames);

    ArrayList<Reservation> expected = null;

    assertEquals(expected, actual);

  }

  @Test
  public void testFrontEndMakeReservationWithInvalidFirstNameShouldFail() {
    // Data for creating stubs
    int flightId = 12;
    String flightNumber = "TS123";
    int userId = 10;
    int numStops = 0;
    int price = 200;
    int firstClassPrice = price * 2;

    User user = new User(userId, "Test", "Person", "user");

    Flight flight = new Flight(flightId, flightNumber, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);

    ArrayList<Integer> seatIds = new ArrayList<>(Arrays.asList(198, 199, 200));
    Seat seat1 = new Seat(seatIds.get(0), flightId, 2, "A", true, true);
    Seat seat2 = new Seat(seatIds.get(1), flightId, 2, "B", true, true);
    Seat seat3 = new Seat(seatIds.get(2), flightId, 2, "C", true, true);

    ArrayList<String> firstNames = new ArrayList<>(Arrays.asList("", "Master", "The"));
    ArrayList<String> lastNames = new ArrayList<>(Arrays.asList("Of Rivia", "Chief", "Doctor"));

    Reservation r1 = new Reservation(user, "Geralt", "Of Rivia", flight, seat1, firstClassPrice);
    Reservation r2 = new Reservation(user, "Master", "Chief", flight, seat2, firstClassPrice);
    Reservation r3 = new Reservation(user, "The", "Doctor", flight, seat3, firstClassPrice);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(user);
    given(flightRepository.findByFlightId(flightId)).willReturn(flight);
    given(seatRepository.findBySeatId(seatIds.get(0))).willReturn(seat1);
    given(seatRepository.findBySeatId(seatIds.get(1))).willReturn(seat2);
    given(seatRepository.findBySeatId(seatIds.get(2))).willReturn(seat3);

    // Test the makeReservation method
    ArrayList<Reservation> actual =
        as.makeReservation(userId, flightId, seatIds, firstNames, lastNames);

    ArrayList<Reservation> expected = null;

    assertEquals(expected, actual);

  }

  @Test
  public void testFrontEndMakeReservationWithInvalidLastNameShouldFail() {
    // Data for creating stubs
    int flightId = 12;
    String flightNumber = "TS123";
    int userId = 10;
    int numStops = 0;
    int price = 200;
    int firstClassPrice = price * 2;

    User user = new User(userId, "Test", "Person", "user");

    Flight flight = new Flight(flightId, flightNumber, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), numStops, "Lala Land", "Over the Rainbow", price);

    ArrayList<Integer> seatIds = new ArrayList<>(Arrays.asList(198, 199, 200));
    Seat seat1 = new Seat(seatIds.get(0), flightId, 2, "A", true, true);
    Seat seat2 = new Seat(seatIds.get(1), flightId, 2, "B", true, true);
    Seat seat3 = new Seat(seatIds.get(2), flightId, 2, "C", true, true);

    ArrayList<String> firstNames = new ArrayList<>(Arrays.asList("Geralt", "Master", "The"));
    ArrayList<String> lastNames = new ArrayList<>(Arrays.asList("", "Chief", "Doctor"));

    Reservation r1 = new Reservation(user, "Geralt", "Of Rivia", flight, seat1, firstClassPrice);
    Reservation r2 = new Reservation(user, "Master", "Chief", flight, seat2, firstClassPrice);
    Reservation r3 = new Reservation(user, "The", "Doctor", flight, seat3, firstClassPrice);

    // Create stubs for the MOCK databases
    given(userRepository.findByUserId(userId)).willReturn(user);
    given(flightRepository.findByFlightId(flightId)).willReturn(flight);
    given(seatRepository.findBySeatId(seatIds.get(0))).willReturn(seat1);
    given(seatRepository.findBySeatId(seatIds.get(1))).willReturn(seat2);
    given(seatRepository.findBySeatId(seatIds.get(2))).willReturn(seat3);

    // Test the makeReservation method
    ArrayList<Reservation> actual =
        as.makeReservation(userId, flightId, seatIds, firstNames, lastNames);

    ArrayList<Reservation> expected = null;

    assertEquals(expected, actual);
  }

  @Test
  public void testGetAllReservationsForUser() {

    // Data for creating stubs
    int userId = 15;
    String password = "password";
    User user = new User(userId, "Test", "Person");

    // Create an Array of Reservation objects
    int flightId = 12;
    String flightNumber = "TS123";
    int seatId = 300;
    int price = 200;
    int reservationId = 1;
    Seat seat = new Seat(seatId, flightId, 2, "A", true, false);
    Flight flight = new Flight(flightId, flightNumber, "unicorn", Date.valueOf("2021-06-01"),
        Time.valueOf("12:12:12"), 0, "Lala Land", "Over the Rainbow", price);
    Reservation reservation =
        new Reservation(reservationId, user, "Test", "Person", flight, seat, null, price);

    ArrayList<Reservation> reservations = new ArrayList<Reservation>(Arrays.asList(reservation));

    // Create stubs for the MOCK databases
    given(userRepository.findUserByIdAndPassword(userId, password)).willReturn(user);
    given(reservationRepository.findAllReservationsWithUserId(userId)).willReturn(reservations);

    // Test the getAllReservationsForUser method
    ArrayList<Reservation> actual = as.getAllReservationsForUser(userId, password);

    assertEquals(reservations, actual);
  }

  @Test
  public void testGetAllReservationsForUserWithInvalidUserShouldReturnEmptyArray() {

    // Data for creating stubs
    int userId = 15;
    String password = "password";

    // Create stubs for the MOCK databases
    given(userRepository.findUserByIdAndPassword(userId, password)).willReturn(null);

    // Test the getAllReservationsForUser method
    ArrayList<Reservation> actual = as.getAllReservationsForUser(userId, password);

    ArrayList<Reservation> expected = new ArrayList<Reservation>();

    assertEquals(expected, actual);
  }

  @Test
  public void testGetAllReservationsForUserNoReservationsShouldReturnEmptyArray() {

    // Data for creating stubs
    int userId = 15;
    String password = "password";
    User user = new User(userId, "Test", "Person");

    // Create stubs for the MOCK databases
    given(userRepository.findUserByIdAndPassword(userId, password)).willReturn(user);
    given(reservationRepository.findAllReservationsWithUserId(userId))
        .willReturn(new ArrayList<Reservation>());

    // Test the getAllReservationsForUser method
    ArrayList<Reservation> actual = as.getAllReservationsForUser(userId, password);

    ArrayList<Reservation> expected = new ArrayList<Reservation>();

    assertEquals(expected, actual);
  }



}
