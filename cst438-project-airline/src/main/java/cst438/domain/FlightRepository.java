package cst438.domain;

import java.sql.Date;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
  Flight findByFlightId(int flightId);

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

  @Query(value = "SELECT DISTINCT origin_city, destination_city FROM flight", nativeQuery = true)
  ArrayList<String> findAllRoutes();


}
