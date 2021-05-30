package cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import cst438.domain.Flight;
import cst438.service.AirlineService;

@Controller
public class AirlineController {

  @Autowired
  private AirlineService airlineService;

  @GetMapping("/index")
  public String getTester(Model model) {

    return "index";
  }

  @GetMapping("/flights")
  public String displayRatings(Model model) {

    Iterable<Flight> flights = airlineService.getFlights();
    model.addAttribute("flights", flights);
    return "display_flights";
  }

}
