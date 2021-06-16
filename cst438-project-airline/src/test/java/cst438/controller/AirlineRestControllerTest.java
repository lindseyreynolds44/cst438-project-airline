package cst438.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import cst438.domain.CreateResponse;
import cst438.domain.Flight;
import cst438.domain.Reservation;
import cst438.domain.Response;
import cst438.domain.Seat;
import cst438.domain.User;
import cst438.service.AirlineService;

@WebMvcTest(AirlineRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class AirlineRestControllerTest {

  @MockBean
  private AirlineService airlineService;

  @Autowired
  private MockMvc mvc;

  private JacksonTester<ArrayList<Flight>> jsonFlightAttempt;
  private JacksonTester<ArrayList<Seat>> jsonSeatAttempt;
  private JacksonTester<ArrayList<Date>> jsonDateAttempt;
  private JacksonTester<ArrayList<String>> jsonRouteAttempt;
  private JacksonTester<Response> jsonResponseAttempt;
  private JacksonTester<CreateResponse> jsonCreateResponseAttempt;

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
    String flightNumber = "TS123";
    Flight dogeFlight = new Flight(99, flightNumber, "doge airlines", deptDate, deptTime, 0,
        "dogeville", "dogeland", 300);

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
    String flightNumber = "TS123";
    Flight dogeFlight = new Flight(99, flightNumber, "doge airlines", deptDate, deptTime, 0,
        "dogeville", "dogeland", 300);

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
    String flightNumber = "TS123";
    Flight dogeFlight = new Flight(99, flightNumber, "doge airlines", deptDate, deptTime, 0,
        "dogeville", "dogeland", 300);

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
    String flightNumber1 = "TS123";

    Flight flight1 = new Flight(99, flightNumber1, "doge airlines", deptDate1, deptTime1, 0,
        "dogeville", "dogeland", 300);

    Date deptDate2 = Date.valueOf("2021-07-11");
    Time deptTime2 = Time.valueOf("02:15:00");
    String flightNumber2 = "TS456";
    Flight flight2 = new Flight(100, flightNumber2, "unicorn airlines", deptDate2, deptTime2, 2,
        "uni", "corn", 555);

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
    String flightNumber = "TS123";
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


    User user = new User(userId, firstName, lastName, "user");
    Flight flight = new Flight(flightId, flightNumber, "United", deptDate, deptTime, numStops,
        originCity, destCity, price);
    Seat seat = new Seat(seatId, flightId, 2, "B", true, true);
    Reservation reservation =
        new Reservation(0, user, firstName, lastName, flight, seat, null, firstClassPrice);

    given(airlineService.isSeatAvailable(seatId)).willReturn(true);
    given(airlineService.makeReservation(flightId, userId, seatId, firstName, lastName))
        .willReturn(reservation);

    // Perform simulated HTTP call
    MvcResult response = mvc
        .perform(MockMvcRequestBuilders
            .post("/api/makeReservation?flightId=" + flightId + "&userId=" + userId + "&seatId="
                + seatId + "&passengerFirstName=" + firstName + "&passengerLastName=" + lastName)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();
    CreateResponse actual =
        jsonCreateResponseAttempt.parseObject(response.getResponse().getContentAsString());

    // Create expected result
    Reservation expectedReservation =
        new Reservation(0, user, firstName, lastName, flight, seat, null, firstClassPrice);
    CreateResponse expected = new CreateResponse("Success", expectedReservation);

    // Compare expected result to actual result
    assertEquals(expected.getStatus(), actual.getStatus());
    assertEquals(expected.getData().getFlight().getFlightId(),
        actual.getData().getFlight().getFlightId());
    assertEquals(expected.getData().getReservationId(), actual.getData().getReservationId());
    assertEquals(expected.getData().getPrice(), actual.getData().getPrice());
    assertEquals(expected.getData().getSeat(), actual.getData().getSeat());
    assertEquals(expected.getData().getUser(), actual.getData().getUser());
  }

  @Test
  public void testMakeReservationWithValidInfoEconomySeat() throws Exception {

    int flightId = 10;
    String flightNumber = "TS123";
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

    User user = new User(userId, firstName, lastName, "user");
    Flight flight = new Flight(flightId, flightNumber, "United", deptDate, deptTime, numStops,
        originCity, destCity, price);
    Seat seat = new Seat(seatId, flightId, 20, "B", true, false);
    Reservation reservation =
        new Reservation(0, user, firstName, lastName, flight, seat, null, price);

    given(airlineService.isSeatAvailable(seatId)).willReturn(true);
    given(airlineService.makeReservation(flightId, userId, seatId, firstName, lastName))
        .willReturn(reservation);

    // Perform simulated HTTP call
    MvcResult response = mvc
        .perform(MockMvcRequestBuilders
            .post("/api/makeReservation?flightId=" + flightId + "&userId=" + userId + "&seatId="
                + seatId + "&passengerFirstName=" + firstName + "&passengerLastName=" + lastName)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();
    CreateResponse actual =
        jsonCreateResponseAttempt.parseObject(response.getResponse().getContentAsString());

    // Create expected result
    Reservation expectedReservation =
        new Reservation(0, user, firstName, lastName, flight, seat, null, price);
    CreateResponse expected = new CreateResponse("Success", expectedReservation);

    // Compare expected result to actual result
    assertEquals(expected.getStatus(), actual.getStatus());
    assertEquals(expected.getData().getFlight().getFlightId(),
        actual.getData().getFlight().getFlightId());
    assertEquals(expected.getData().getReservationId(), actual.getData().getReservationId());
    assertEquals(expected.getData().getPrice(), actual.getData().getPrice());
    assertEquals(expected.getData().getSeat(), actual.getData().getSeat());
    assertEquals(expected.getData().getUser(), actual.getData().getUser());

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
    MvcResult response = mvc
        .perform(MockMvcRequestBuilders
            .post("/api/makeReservation?flightId=" + flightId + "&userId=" + userId + "&seatId="
                + seatId + "&passengerFirstName=" + firstName + "&passengerLastName=" + lastName)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();
    CreateResponse actual =
        jsonCreateResponseAttempt.parseObject(response.getResponse().getContentAsString());

    // Create expected result
    CreateResponse expected = new CreateResponse(
        "Error: Request contains incorrect flight ID, user ID or seat ID.", null);

    // Compare expected result to actual result
    assertEquals(expected.getData(), actual.getData());
    assertEquals(expected.getStatus(), actual.getStatus());
  }

  @Test
  public void testMakeReservationWithUnavailableSeat() throws Exception {
    int flightId = 10;
    String flightNumber = "TS123";
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


    User user = new User(userId, firstName, lastName, "user");
    Flight flight = new Flight(flightId, flightNumber, "United", deptDate, deptTime, numStops,
        originCity, destCity, price);
    Seat seat = new Seat(seatId, flightId, 20, "B", false, false);
    Reservation reservation =
        new Reservation(0, user, firstName, lastName, flight, seat, null, price);

    given(airlineService.isSeatAvailable(seatId)).willReturn(false);
    given(airlineService.makeReservation(flightId, userId, seatId, firstName, lastName))
        .willReturn(reservation);

    // Perform simulated HTTP call
    MvcResult response = mvc
        .perform(MockMvcRequestBuilders
            .post("/api/makeReservation?flightId=" + flightId + "&userId=" + userId + "&seatId="
                + seatId + "&passengerFirstName=" + firstName + "&passengerLastName=" + lastName)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();
    CreateResponse actual =
        jsonCreateResponseAttempt.parseObject(response.getResponse().getContentAsString());

    // Create expected result
    CreateResponse expected =
        new CreateResponse("Error: Seat ID " + seatId + " is not available.", null);

    // Compare expected result to actual result
    assertEquals(expected.getData(), actual.getData());
    assertEquals(expected.getStatus(), actual.getStatus());
  }

  @Test
  public void testCancelReservationSuccess() throws Exception {
    int reservationId = 10;
    int userId = 12;

    given(airlineService.isValidReservation(reservationId, userId)).willReturn(true);
    given(airlineService.cancelReservation(reservationId)).willReturn(true);

    // Perform simulated HTTP call
    MvcResult result = mvc
        .perform(MockMvcRequestBuilders.delete("/api/cancelReservation?&reservationId=10&userId=12")
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    // Get the response object
    Response actual = jsonResponseAttempt.parseObject(result.getResponse().getContentAsString());

    // Create the expected response object
    Response expected = new Response("Success", reservationId);

    // Verify that the results are as expected
    assertThat(expected.getStatus()).isEqualTo(actual.getStatus());
    assertThat(expected.getData()).isEqualTo(actual.getData());
  }

  @Test
  public void testCancelReservationFailure() throws Exception {
    int reservationId = 10;
    int userId = 12;

    given(airlineService.isValidReservation(reservationId, userId)).willReturn(false);
    given(airlineService.cancelReservation(reservationId)).willReturn(true);

    // Perform simulated HTTP call
    MvcResult result = mvc
        .perform(MockMvcRequestBuilders.delete("/api/cancelReservation?&reservationId=10&userId=12")
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    // Get the response object
    Response actual = jsonResponseAttempt.parseObject(result.getResponse().getContentAsString());

    // Create the expected response object
    Response expected = new Response("Error: Invalid Reservation", null);

    // Verify that the results are as expected
    assertThat(expected.getStatus()).isEqualTo(actual.getStatus());
    assertThat(expected.getData()).isEqualTo(actual.getData());
  }

  @Test
  public void testGetAllReservationsSuccess() throws Exception {
    int userId = 12;
    User user = new User(userId, "Test", "Person", "user");

    // Create an Array of Reservation objects
    int flightId = 14;
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


    given(airlineService.getAllReservationsForUser(userId)).willReturn(reservations);

    // Perform simulated HTTP call
    MockHttpServletResponse response =
        mvc.perform(get("/api/getAllReservations?userId=" + userId)).andReturn().getResponse();

    // Get the response object
    Response actual = jsonResponseAttempt.parseObject(response.getContentAsString());

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    // Create the expected response object
    Response expected = new Response("Success", reservations);

    // Verify that the results are as expected
    assertThat(expected.getStatus()).isEqualTo(actual.getStatus());
  }

  @Test
  public void testGetAllReservationsWithNoResults() throws Exception {
    int userId = 12;

    given(airlineService.getAllReservationsForUser(userId))
        .willReturn(new ArrayList<Reservation>());

    // Perform simulated HTTP call
    MockHttpServletResponse response =
        mvc.perform(get("/api/getAllReservations?userId=" + userId)).andReturn().getResponse();

    // Get the response object
    Response actual = jsonResponseAttempt.parseObject(response.getContentAsString());

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    // Create the expected response object
    Response expected =
        new Response("Error: Could not find any reservations for this User ID and password.", null);

    // Verify that the results are as expected
    assertThat(expected.getStatus()).isEqualTo(actual.getStatus());
    assertThat(expected.getData()).isEqualTo(actual.getData());
  }



}
