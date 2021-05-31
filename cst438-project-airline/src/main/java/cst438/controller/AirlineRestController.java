package cst438.controller;

import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cst438.service.AirlineService;

@RestController
@RequestMapping("/api")
public class AirlineRestController {

  @Autowired
  private AirlineService airlineService;

  @GetMapping("/getFlightDate")
  public ArrayList<Date> getFlightDate(@RequestParam("originCity") String origin,
      @RequestParam("destinationCity") String destination) {

    ArrayList<Date> dates = airlineService.getAvailableDates(origin, destination);

    return dates;
  }


}
