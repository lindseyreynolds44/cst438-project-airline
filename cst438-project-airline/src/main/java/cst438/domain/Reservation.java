package cst438.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@IdClass(ReservationId.class)
@Table(name = "reservation")
public class Reservation {

  @Id
  @Column(name = "reservation_id")
  private int reservationId;
  @Id
  @Column(name = "passenger_id")
  private int passengerId;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private User user;
  @ManyToOne
  @JoinColumn(name = "flight_id", referencedColumnName = "flight_id")
  private Flight flight;
  @OneToOne
  @JoinColumn(name = "seat_id", referencedColumnName = "seat_id")
  private Seat seat;
  private String dateCreated;

  public Reservation(int reservationId, int passengerId, User user, Flight flight, Seat seat,
      String dateCreated) {
    super();
    this.reservationId = reservationId;
    this.passengerId = passengerId;
    this.user = user;
    this.flight = flight;
    this.seat = seat;
    this.dateCreated = dateCreated;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Reservation other = (Reservation) obj;
    if (dateCreated == null) {
      if (other.dateCreated != null)
        return false;
    } else if (!dateCreated.equals(other.dateCreated))
      return false;
    if (flight == null) {
      if (other.flight != null)
        return false;
    } else if (!flight.equals(other.flight))
      return false;
    if (passengerId != other.passengerId)
      return false;
    if (reservationId != other.reservationId)
      return false;
    if (seat == null) {
      if (other.seat != null)
        return false;
    } else if (!seat.equals(other.seat))
      return false;
    if (user == null) {
      if (other.user != null)
        return false;
    } else if (!user.equals(other.user))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Reservation [reservationId=" + reservationId + ", passengerId=" + passengerId
        + ", user=" + user + ", flight=" + flight + ", seat=" + seat + ", dateCreated="
        + dateCreated + "]";
  }


}
