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
import cst438.domain.Seat;
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
  public void TestGetFlights() throws Exception {
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
    Flight expectedFlight =
        new Flight(99, "doge airlines", deptDate, deptTime, 0, "dogeville", "dogeland", 300);

    // // needed to force java to print date in GMT timezone.
    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));


    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertEquals(1, resultFlights.size());
    assertEquals("2021-06-01", sdf.format(resultFlight.getDepartureDate()));
    assertEquals(300, resultFlight.getPrice());

  }

  @Test
  public void TestGetFlightsShouldReturnEmptyIfNotFound() throws Exception {
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
  public void TestGetFlightsShouldReturnEmptyIfWrongVarType() throws Exception {
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
  public void TestGetSeatShouldReturnSeats() throws Exception {
    Seat seat = new Seat(254, 23, 4, "A", 1, 1);
    ArrayList<Seat> seats = new ArrayList<Seat>(Arrays.asList(seat));

    given(airlineService.getSeatsByFlightId(254, 1)).willReturn(seats);
    MockHttpServletResponse response =
        mvc.perform(get("/api/getSeats?flightId=254&isFirstClass=1")).andReturn().getResponse();

    ArrayList<Seat> results = jsonSeatAttempt.parseObject(response.getContentAsString());

    Seat expected = new Seat(254, 23, 4, "A", 1, 1);
    Seat result = results.get(0);

    assertEquals(expected, result);

  }

  @Test
  public void TestGetSeatsReturnsEmptyIfNotFound() throws Exception {
    Seat seat = new Seat(254, 23, 4, "A", 1, 1);
    ArrayList<Seat> seats = new ArrayList<Seat>(Arrays.asList(seat));

    given(airlineService.getSeatsByFlightId(254, 1)).willReturn(seats);
    MockHttpServletResponse response =
        mvc.perform(get("/api/getSeats?flightId=0&isFirstClass=1")).andReturn().getResponse();

    ArrayList<Seat> results = jsonSeatAttempt.parseObject(response.getContentAsString());

    assertEquals(0, results.size());
  }


  @Test
  public void TestGetDatesShouldReturnDates() throws Exception {
    ArrayList<Date> dates = new ArrayList<>();
    dates.add(Date.valueOf("2022-01-23"));
    dates.add(Date.valueOf("2022-05-23"));
    dates.add(Date.valueOf("2022-12-25"));
    dates.add(Date.valueOf("2022-2-28"));

    given(airlineService.getDatesForRoute("dogeville", "dogeland")).willReturn(dates);

    MockHttpServletResponse response =
        mvc.perform(get("/api/getFlightDate?originCity=dogeville&destinationCity=dogeland"))
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
  public void TestGetDatesShouldReturnEmptyIfNotFound() throws Exception {

    ArrayList<Date> dates = new ArrayList<>();
    dates.add(Date.valueOf("2022-01-23"));
    dates.add(Date.valueOf("2022-05-23"));
    dates.add(Date.valueOf("2022-12-25"));
    dates.add(Date.valueOf("2022-2-28"));

    given(airlineService.getDatesForRoute("dogeville", "dogeland")).willReturn(dates);

    MockHttpServletResponse response =
        mvc.perform(get("/api/getFlightDate?originCity=danville&destinationCity=hogwarts"))
            .andReturn().getResponse();
    ArrayList<Date> results = jsonDateAttempt.parseObject(response.getContentAsString());

    assertEquals(0, results.size());
  }

}
