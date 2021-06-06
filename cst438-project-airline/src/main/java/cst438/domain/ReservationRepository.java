package cst438.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
  // We'll need a query to select all the reservations a user name grouping it by date.
  // We'll need a query to update a reservation
  // We'll need a query to delete a reservation
  // Maybe we can add an aggregate query to get the total of a cost, but I think we can do that on
  // the service end.
}
