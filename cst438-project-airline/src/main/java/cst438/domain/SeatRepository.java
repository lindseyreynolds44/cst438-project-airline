package cst438.domain;

import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
  // We'll need a query of available seats for a given flight and whether it's first class or not.
  // select * from seat where flight_id = ? and available = ? and is_first_class = ?;

  ArrayList<Seat> findBySeatId(Integer seatId);

  @Query(value = "SELECT * from seat s WHERE s.flight_id = ?1 AND s.is_first_class = ?2",
      nativeQuery = true)
  ArrayList<Seat> findSeatsByFlightID(int flightId, int isFirstClass);

}
