package cst438.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(AirlineService.class)
public class AirlineServiceTest {

  // @MockBean
  // private FlightRepository flightRepository;

  // @MockBean
  // private PassengerRepository passengerRepository;

  // @MockBean
  // private ReservationRepository reservationRepository;

  // @MockBean
  // private SeatRepository seatRepository;

  // @MockBean
  // private UserRepository userRepository;

  private AirlineService as;

  @SuppressWarnings("deprecation")
  @BeforeEach
  public void setUpBefore() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void test_getAirlineNames() {
    // We will test this CityService object
    as = new AirlineService();


    // Create MOCKs in order to test the getCityInfo method
    // given(weatherService.getTempAndTime("New York")).willReturn(tempTime);


    // Test
    ArrayList<String> actual = as.getOriginCities();

    // Create Expected results
    ArrayList<String> expected = new ArrayList<>();
    expected.add("seattle");
    expected.add("san francisco");
    expected.add("new york");
    expected.add("boston");
    expected.add("washington d.c.");
    expected.add("san diego");


    // Check that expected matches actual
    assertEquals(expected, actual);
  }

}
