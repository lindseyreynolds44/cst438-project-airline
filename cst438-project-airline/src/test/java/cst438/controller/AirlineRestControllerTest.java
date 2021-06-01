package cst438.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
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

  @BeforeEach
  public void setupEach() {
    MockitoAnnotations.initMocks(this);
    JacksonTester.initFields(this, new ObjectMapper());
  }

  @Test
  public void TestGetFlights() throws Exception {
    Date deptDate = Date.valueOf("2021-12-08");
    Time deptTime = Time.valueOf("12:12:12");



    Flight dogeFlight =
        new Flight(99, "doge airlines", deptDate, deptTime, 0, "dogeville", "dogeland", 300);
    ArrayList<Flight> Flights = new ArrayList<Flight>(Arrays.asList(dogeFlight));

    given(airlineService.getFlightsByRoute("dogeville", "dogeland")).willReturn(Flights);

    MockHttpServletResponse response =
        mvc.perform(get("/api/getFlights?originCity=dogeville&destinationCity=dogeland"))
            .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    ArrayList<Flight> resultFlights = jsonFlightAttempt.parseObject(response.getContentAsString());
    assertEquals(1, resultFlights.size());

    Flight resultFlight = resultFlights.get(0);
    Flight expectedFlight =
        new Flight(99, "doge airlines", deptDate, deptTime, 0, "dogeville", "dogeland", 300);

    System.out.println("*****************************************************"
        + resultFlight.getDepartureDate().toString()); // this works you just have to look in the
                                                       // console tab!

    assertThat(resultFlight).isEqualTo(expectedFlight);
    assertEquals(300, resultFlight.getPrice());


  }

}
