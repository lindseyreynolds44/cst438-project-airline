package cst438.domain;

import java.io.Serializable;

/**
 * This is the composite primary key for the Reservation class
 */

public class ReservationId implements Serializable {

  private int reservationId;
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
