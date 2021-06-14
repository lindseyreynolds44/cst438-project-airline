package cst438.domain;

import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  Reservation findByReservationId(int reservationId);

  @Query(value = "SELECT * FROM reservation WHERE reservation_id = ?1 AND user_id = ?2",
      nativeQuery = true)
  Reservation findByReservationIdAndUserId(int reservationId, int userId);


  @Transactional
  @Modifying
  @Query(value = "DELETE FROM reservation r WHERE r.reservation_id = :id", nativeQuery = true)
  void cancelByReservationId(@Param(value = "id") int id);

  @Query(value = "SELECT * FROM reservation WHERE user_id = ?1 ORDER BY date_created",
      nativeQuery = true)
  ArrayList<Reservation> findAllReservationsWithUserId(int userId);

}
