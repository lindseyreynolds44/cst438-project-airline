package cst438.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
  // We'll need a query of available seats for a given flight and whether it's first class or not.
  // select * from seat where flight_id = ? and available = ? and is_first_class = ?;

}
