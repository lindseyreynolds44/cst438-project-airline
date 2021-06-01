package cst438.domain;

import java.sql.Date;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

  ArrayList<Flight> findAll();

  @Query("select m from Flight m order by airlineName, departureDateTime desc")
  ArrayList<Flight> findAllByAirline();

  @Query(value = "SELECT DISTINCT origin_city FROM flight ORDER BY origin_city", nativeQuery = true)
  ArrayList<String> findOriginCities();

  @Query(value = "SELECT DISTINCT destination_city FROM flight ORDER BY destination_city",
      nativeQuery = true)
  ArrayList<String> findDestinationCities();

  @Query(
      value = "SELECT DISTINCT departure_date FROM flight f WHERE f.origin_city = ?1 and f.destination_city=?2",
      nativeQuery = true)
  ArrayList<Date> findDatesForRoute(String originCity, String destinationCity);

  @Query(
      value = "SELECT * FROM flight f WHERE f.origin_city = ?1 and f.destination_city=?2 and departure_date=?3",
      nativeQuery = true)
  ArrayList<Flight> findFlightsByRouteAndDate(String originCity, String destinationCity,
      String date);

  @Query(value = "SELECT * from flight f WHERE f.origin_city = ?1 AND f.destination_city = ?2",
      nativeQuery = true)
  ArrayList<Flight> findFlightsByRoute(String originCity, String destinationCity);


  // Methods TBD
  // DONE: We are going to need to add a query that pulls a flight based on origin and destination
  // cities (Dan was here)
  // example: find a flight departing from san diego towards boston.
  // code: select * from flight where origin_city = ? and destination_city = ?;
  // We may also have to account for open seats, so if a flight does not have any open seats
  // then we can't display this flight to the customer as available.


}
