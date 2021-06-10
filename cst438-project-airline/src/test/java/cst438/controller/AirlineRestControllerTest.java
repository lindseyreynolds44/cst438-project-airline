package cst438.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import cst438.domain.Flight;
import cst438.domain.Reservation;
import cst438.domain.Response;
import cst438.domain.Seat;
import cst438.domain.User;
import cst438.service.AirlineService;

@WebMvcTest(AirlineRestController.class)
public class AirlineRestControllerTest {

  @MockBean
  private AirlineService airlineService;

  @Autowired
  private MockMvc mvc;

  private JacksonTester<ArrayList<Flight>> jsonFlightAttempt;
  private JacksonTester<ArrayList<Seat>> jsonSeatAttempt;
  private JacksonTester<ArrayList<Date>> jsonDateAttempt;
  private JacksonTester<ArrayList<String>> jsonRouteAttempt;
  private JacksonTester<Reservation> jsonReservationAttempt;
  private JacksonTester<Response> jsonResponseAttempt;

  SimpleDateFormat sdf;

  @BeforeEach
  public void setupEach() {
    MockitoAnnotations.initMocks(this);
    JacksonTester.initFields(this, new ObjectMapper());

    // needed to force java to print date in GMT timezone.
    sdf = new SimpleDateFormat("yyyy-MM-dd");
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
  }

  @Test
  public void testGetFlights() throws Exception {
    Date deptDate = Date.valueOf("2021-06-01");
    Time deptTime = Time.valueOf("12:12:12");
    Flight dogeFlight =
        new Flight(99, "doge airlines", deptDate, deptTime, 0, "dogeville", "dogeland", 300);

    ArrayList<Flight> Flights = new ArrayList<Flight>(Arrays.asList(dogeFlight));

    given(airlineService.getFlightsByRoute("dogeville", "dogeland")).willReturn(Flights);

    MockHttpServletResponse response =
        mvc.perform(get("/api/getFlights?originCity=dogeville&destinationCity=dogeland"))
            .andReturn().getResponse();

    ArrayList<Flight> resultFlights = jsonFlightAttempt.parseObject(response.getContentAsString());
    Flight resultFlight = resultFlights.get(0);

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertEquals(1, resultFlights.size());
    assertEquals("2021-06-01", sdf.format(resultFlight.getDepartureDate()));
    assertEquals(300, resultFlight.getPrice());

  }

  @Test
  public void testGetFlightsShouldReturnEmptyIfNotFound() throws Exception {
    Date deptDate = Date.valueOf("2021-06-01");
    Time deptTime = Time.valueOf("12:12:12");
    Flight dogeFlight =
        new Flight(99, "doge airlines", deptDate, deptTime, 0, "dogeville", "dogeland", 300);

    ArrayList<Flight> Flights = new ArrayList<Flight>(Arrays.asList(dogeFlight));

    given(airlineService.getFlightsByRoute("dogeville", "dogeland")).willReturn(Flights);

    MockHttpServletResponse response =
        mvc.perform(get("/api/getFlights?originCity=moon&destinationCity=dogeland")).andReturn()
            .getResponse();

    ArrayList<Flight> resultFlights = jsonFlightAttempt.parseObject(response.getContentAsString());

    assertEquals(0, resultFlights.size());

  }

  @Test
  public void testGetFlightsShouldReturnEmptyIfWrongVarType() throws Exception {
    Date deptDate = Date.valueOf("2021-06-01");
    Time deptTime = Time.valueOf("12:12:12");
    Flight dogeFlight =
        new Flight(99, "doge airlines", deptDate, deptTime, 0, "dogeville", "dogeland", 300);

    ArrayList<Flight> Flights = new ArrayList<Flight>(Arrays.asList(dogeFlight));

    given(airlineService.getFlightsByRoute("dogeville", "dogeland")).willReturn(Flights);

    MockHttpServletResponse response =
        mvc.perform(get("/api/getFlights?originCity=25&destinationCity=dogeland")).andReturn()
            .getResponse();

    ArrayList<Flight> resultFlights = jsonFlightAttempt.parseObject(response.getContentAsString());

    assertEquals(0, resultFlights.size());

  }

  @Test
  public void testGetSeatShouldReturnSeats() throws Exception {
    Seat seat = new Seat(254, 23, 4, "A", true, true);
    ArrayList<Seat> seats = new ArrayList<Seat>(Arrays.asList(seat));

    given(airlineService.getSeatsByFlightId(254, true)).willReturn(seats);
    MockHttpServletResponse response =
        mvc.perform(get("/api/getSeats?flightId=254&isFirstClass=1")).andReturn().getResponse();

    ArrayList<Seat> results = jsonSeatAttempt.parseObject(response.getContentAsString());

    Seat expected = new Seat(254, 23, 4, "A", true, true);
    Seat result = results.get(0);

    assertEquals(expected, result);

  }

  @Test
  public void testGetSeatsReturnsEmptyIfNotFound() throws Exception {
    Seat seat = new Seat(254, 23, 4, "A", true, true);
    ArrayList<Seat> seats = new ArrayList<Seat>(Arrays.asList(seat));

    given(airlineService.getSeatsByFlightId(254, true)).willReturn(seats);
    MockHttpServletResponse response =
        mvc.perform(get("/api/getSeats?flightId=0&isFirstClass=1")).andReturn().getResponse();

    ArrayList<Seat> results = jsonSeatAttempt.parseObject(response.getContentAsString());

    assertEquals(0, results.size());
  }


  @Test
  public void testGetDatesShouldReturnDates() throws Exception {
    ArrayList<Date> dates = new ArrayList<>();
    dates.add(Date.valueOf("2022-01-23"));
    dates.add(Date.valueOf("2022-05-23"));
    dates.add(Date.valueOf("2022-12-25"));
    dates.add(Date.valueOf("2022-2-28"));

    given(airlineService.getDatesForRoute("dogeville", "dogeland")).willReturn(dates);

    MockHttpServletResponse response =
        mvc.perform(get("/api/getFlightDates?originCity=dogeville&destinationCity=dogeland"))
            .andReturn().getResponse();

    ArrayList<Date> expected = new ArrayList<>();
    expected.add(Date.valueOf("2022-01-23"));
    expected.add(Date.valueOf("2022-05-23"));
    expected.add(Date.valueOf("2022-12-25"));
    expected.add(Date.valueOf("2022-2-28"));
    ArrayList<Date> results = jsonDateAttempt.parseObject(response.getContentAsString());

    assertEquals(expected.size(), results.size());
    assertEquals("2022-01-23", sdf.format(results.get(0)));
  }

  @Test
  public void testGetDatesShouldReturnEmptyIfNotFound() throws Exception {

    ArrayList<Date> dates = new ArrayList<>();
    dates.add(Date.valueOf("2022-01-23"));
    dates.add(Date.valueOf("2022-05-23"));
    dates.add(Date.valueOf("2022-12-25"));
    dates.add(Date.valueOf("2022-2-28"));

    given(airlineService.getDatesForRoute("dogeville", "dogeland")).willReturn(dates);

    MockHttpServletResponse response =
        mvc.perform(get("/api/getFlightDates?originCity=danville&destinationCity=hogwarts"))
            .andReturn().getResponse();
    ArrayList<Date> results = jsonDateAttempt.parseObject(response.getContentAsString());

    assertEquals(0, results.size());
  }

  @Test
  public void testGetAllFlights() throws Exception {
    Date deptDate1 = Date.valueOf("2021-06-01");
    Time deptTime1 = Time.valueOf("12:12:12");
    Flight flight1 =
        new Flight(99, "doge airlines", deptDate1, deptTime1, 0, "dogeville", "dogeland", 300);
    Date deptDate2 = Date.valueOf("2021-07-11");
    Time deptTime2 = Time.valueOf("02:15:00");
    Flight flight2 =
        new Flight(100, "unicorn airlines", deptDate2, deptTime2, 2, "uni", "corn", 555);

    ArrayList<Flight> Flights = new ArrayList<Flight>(Arrays.asList(flight1, flight2));

    given(airlineService.getAllFlights()).willReturn(Flights);

    MockHttpServletResponse response =
        mvc.perform(get("/api/getAllFlights")).andReturn().getResponse();

    ArrayList<Flight> resultFlights = jsonFlightAttempt.parseObject(response.getContentAsString());
    Flight resultFlight1 = resultFlights.get(0);
    Flight resultFlight2 = resultFlights.get(1);

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertEquals(2, resultFlights.size());

    assertEquals("2021-06-01", sdf.format(resultFlight1.getDepartureDate()));
    assertEquals(300, resultFlight1.getPrice());
    assertEquals("2021-07-11", sdf.format(resultFlight2.getDepartureDate()));
    assertEquals(555, resultFlight2.getPrice());

  }

  @Test
  public void testGetAllFlightsWithEmptyDB() throws Exception {
    ArrayList<Flight> Flights = new ArrayList<Flight>();

    given(airlineService.getAllFlights()).willReturn(Flights);

    MockHttpServletResponse response =
        mvc.perform(get("/api/getAllFlights")).andReturn().getResponse();

    ArrayList<Flight> resultFlights = jsonFlightAttempt.parseObject(response.getContentAsString());

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertEquals(0, resultFlights.size());

  }

  @Test
  public void testGetRoutes() throws Exception {
    ArrayList<String> routes = new ArrayList<String>(
        Arrays.asList("seattle,san diego", "san francisco,boston", "new york,boston",
            "boston,san francisco", "washington d.c.,new york", "washington d.c.,san diego",
            "san diego,seattle", "san francisco,seattle", "san diego,washington d.c.",
            "san francisco,washington d.c.", "san diego,san francisco", "boston,washington d.c.",
            "san diego,boston", "new york,san diego", "seattle,new york"));

    given(airlineService.getAllRoutes()).willReturn(routes);

    MockHttpServletResponse response = mvc.perform(get("/api/getRoutes")).andReturn().getResponse();

    ArrayList<String> resultRoutes = jsonRouteAttempt.parseObject(response.getContentAsString());
    String firstResult = resultRoutes.get(0);
    String lastResult = resultRoutes.get(14);

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertEquals(15, resultRoutes.size());

    assertEquals("seattle,san diego", firstResult);
    assertEquals("seattle,new york", lastResult);
  }

  @Test
  public void TestGetRoutesWithEmptyDB() throws Exception {
    ArrayList<String> routes = new ArrayList<>();

    given(airlineService.getAllRoutes()).willReturn(routes);

    MockHttpServletResponse response = mvc.perform(get("/api/getRoutes")).andReturn().getResponse();

    ArrayList<String> resultRoutes = jsonRouteAttempt.parseObject(response.getContentAsString());

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertEquals(0, resultRoutes.size());

  }

  @Test
  public void testMakeReservationWithValidInfoFirstClassSeat() throws Exception {

    int flightId = 10;
    int userId = 9;
    int seatId = 350;
    int price = 400;
    int firstClassPrice = price * 2;
    int numStops = 0;

    Date deptDate = Date.valueOf("2021-06-15");
    Time deptTime = Time.valueOf("12:12:12");
    String firstName = "Fake";
    String lastName = "Data";
    String originCity = "Doge";
    String destCity = "Unicorn";

    User user = new User(userId, firstName, lastName);
    Flight flight =
        new Flight(flightId, "United", deptDate, deptTime, numStops, originCity, destCity, price);
    Seat seat = new Seat(seatId, flightId, 2, "B", true, true);
    Reservation reservation =
        new Reservation(0, user, firstName, lastName, flight, seat, null, firstClassPrice);

    given(airlineService.isSeatAvailable(seatId)).willReturn(true);
    given(airlineService.makeReservation(flightId, userId, seatId, firstName, lastName))
        .willReturn(reservation);

    // Perform simulated HTTP call
    MockHttpServletResponse response = mvc
        .perform(get("/api/makeReservation?flightId=" + flightId + "&userId=" + userId + "&seatId="
            + seatId + "&passengerFirstName=" + firstName + "&passengerLastName=" + lastName))
        .andReturn().getResponse();

    // Verify that the status code is as expected
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    // Convert returned data from JSON string format to Reservation object
    Reservation actual = jsonReservationAttempt.parseObject(response.getContentAsString());

    // Create the expected Reservation object
    Reservation expected =
        new Reservation(0, user, firstName, lastName, flight, seat, null, firstClassPrice);

    // Compare expected data to actual data
    assertEquals(expected.getReservationId(), actual.getReservationId());
    assertEquals(expected.getFlight().getFlightId(), actual.getFlight().getFlightId());
    assertEquals(expected.getUser(), actual.getUser());
    assertEquals(expected.getSeat(), actual.getSeat());
    assertEquals(expected.getFirstName(), actual.getFirstName());
    assertEquals(expected.getLastName(), actual.getLastName());
    assertEquals(expected.getPrice(), actual.getPrice());
  }

  @Test
  public void testMakeReservationWithValidInfoEconomySeat() throws Exception {

    int flightId = 10;
    int userId = 9;
    int seatId = 350;
    int price = 400;
    int numStops = 0;

    Date deptDate = Date.valueOf("2021-06-15");
    Time deptTime = Time.valueOf("12:12:12");
    String firstName = "Fake";
    String lastName = "Data";
    String originCity = "Doge";
    String destCity = "Unicorn";

    User user = new User(userId, firstName, lastName);
    Flight flight =
        new Flight(flightId, "United", deptDate, deptTime, numStops, originCity, destCity, price);
    Seat seat = new Seat(seatId, flightId, 20, "B", true, false);
    Reservation reservation =
        new Reservation(0, user, firstName, lastName, flight, seat, null, price);

    given(airlineService.isSeatAvailable(seatId)).willReturn(true);
    given(airlineService.makeReservation(flightId, userId, seatId, firstName, lastName))
        .willReturn(reservation);

    // Perform simulated HTTP call
    MockHttpServletResponse response = mvc
        .perform(get("/api/makeReservation?flightId=" + flightId + "&userId=" + userId + "&seatId="
            + seatId + "&passengerFirstName=" + firstName + "&passengerLastName=" + lastName))
        .andReturn().getResponse();

    // Verify that the status code is as expected
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    // Convert returned data from JSON string format to Reservation object
    Reservation actual = jsonReservationAttempt.parseObject(response.getContentAsString());

    // Create the expected Reservation object
    Reservation expected = new Reservation(0, user, firstName, lastName, flight, seat, null, price);

    // Compare expected data to actual data
    assertEquals(expected.getReservationId(), actual.getReservationId());
    assertEquals(expected.getFlight().getFlightId(), actual.getFlight().getFlightId());
    assertEquals(expected.getUser(), actual.getUser());
    assertEquals(expected.getSeat(), actual.getSeat());
    assertEquals(expected.getFirstName(), actual.getFirstName());
    assertEquals(expected.getLastName(), actual.getLastName());
    assertEquals(expected.getPrice(), actual.getPrice());
  }

  @Test
  public void testMakeReservationWithInvalidId() throws Exception {
    int flightId = 10;
    int userId = 120;
    int seatId = 350;

    String firstName = "Fake";
    String lastName = "Data";

    given(airlineService.isSeatAvailable(seatId)).willReturn(true);
    given(airlineService.makeReservation(flightId, userId, seatId, firstName, lastName))
        .willReturn(null);

    // Perform simulated HTTP call
    MockHttpServletResponse response = mvc
        .perform(get("/api/makeReservation?flightId=" + flightId + "&userId=" + userId + "&seatId="
            + seatId + "&passengerFirstName=" + firstName + "&passengerLastName=" + lastName))
        .andReturn().getResponse();

    // Verify that the status code is as expected
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }

  @Test
  public void testMakeReservationWithUnavailableSeat() throws Exception {
    int flightId = 10;
    int userId = 9;
    int seatId = 350;
    int price = 400;
    int numStops = 0;

    Date deptDate = Date.valueOf("2021-06-15");
    Time deptTime = Time.valueOf("12:12:12");
    String firstName = "Fake";
    String lastName = "Data";
    String originCity = "Doge";
    String destCity = "Unicorn";

    User user = new User(userId, firstName, lastName);
    Flight flight =
        new Flight(flightId, "United", deptDate, deptTime, numStops, originCity, destCity, price);
    Seat seat = new Seat(seatId, flightId, 20, "B", false, false);
    Reservation reservation =
        new Reservation(0, user, firstName, lastName, flight, seat, null, price);

    given(airlineService.isSeatAvailable(seatId)).willReturn(false);
    given(airlineService.makeReservation(flightId, userId, seatId, firstName, lastName))
        .willReturn(reservation);

    // Perform simulated HTTP call
    MockHttpServletResponse response = mvc
        .perform(get("/api/makeReservation?flightId=" + flightId + "&userId=" + userId + "&seatId="
            + seatId + "&passengerFirstName=" + firstName + "&passengerLastName=" + lastName))
        .andReturn().getResponse();

    // Verify that the status code is as expected
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    // Compare expected result to actual result
    assertEquals("", response.getContentAsString()); // Nothing should be returned
  }

  @Test
  public void testCancelReservationSuccess() throws Exception {
    int reservationId = 10;
    int userId = 12;

    given(airlineService.isValidReservation(userId, reservationId)).willReturn(true);
    given(airlineService.cancelReservation(reservationId)).willReturn(true);


    // MvcResult result =
    // mvc.perform(post("/api/cancelReservation?&reservationId=19&userId=2").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content("json").andExpect(status().isOk()).andReturn();

    // String content = result.getResponse().getContentAsString();

    // Perform simulated HTTP call
    // Find out how to test a post request!!!


    // Verify that the status code is as expected
    // assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }



}
