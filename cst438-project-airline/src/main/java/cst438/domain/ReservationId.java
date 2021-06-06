package cst438.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * This is the composite primary key for the Reservation class
 */

@Embeddable
public class ReservationId implements Serializable {

  @Column(name = "reservation_id")
  private int reservationId;

  @Column(name = "passenger_id")
  private int passengerId;

  public ReservationId() {
    super();
  }

  public ReservationId(int reservationId, int passengerId) {
    super();
    this.reservationId = reservationId;
    this.passengerId = passengerId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + passengerId;
    result = prime * result + reservationId;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ReservationId other = (ReservationId) obj;
    if (passengerId != other.passengerId)
      return false;
    if (reservationId != other.reservationId)
      return false;
    return true;
  }

}
