package cst438.domain;

import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

  Seat findBySeatId(int seatId);

  @Query(value = "SELECT * from seat s WHERE s.flight_id = ?1 AND s.is_first_class = ?2",
      nativeQuery = true)
  ArrayList<Seat> findSeatsByFlightID(int flightId, boolean isFirstClass);

  @Query(value = "SELECT available FROM seat s WHERE s.seat_id = ?1", nativeQuery = true)
  int isSeatAvailable(int seatId);

  @Transactional
  @Modifying
  @Query(value = "UPDATE seat s SET s.available = 0 WHERE s.seat_id = :id", nativeQuery = true)
  void setSeatToUnavailable(@Param(value = "id") int id);

  @Transactional
  @Modifying
  @Query(value = "UPDATE seat s SET s.available = 1 WHERE s.seat_id = :id", nativeQuery = true)
  void setSeatToAvailable(@Param(value = "id") int id);


}
