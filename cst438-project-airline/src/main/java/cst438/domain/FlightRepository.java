package cst438.domain;

import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

  ArrayList<Flight> findAll();

  @Query("select m from Flight m order by airlineName, departureDateTime desc")
  ArrayList<Flight> findAllByAirline();

  // Methods TBD
  // We are going to need to add a query that pulls a flight based on origin and destination cities
  // example: find a flight departing from san diego towards boston.
  // code: select * from flight where origin_city = ? and destination_city = ?;
  // We may also have to account for open seats, so if a flight does not have any open seats
  // then we can't display this flight to the customer as available.


}
