package cst438.domain;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "reservation")
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_id")
  private int reservationId;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private User user;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @ManyToOne
  @JoinColumn(name = "flight_id", referencedColumnName = "flight_id")
  private Flight flight;

  @OneToOne
  @JoinColumn(name = "seat_id", referencedColumnName = "seat_id")
  private Seat seat;

  @Column(name = "date_created")
  @CreationTimestamp
  private Timestamp dateCreated;

  private int price;

  public Reservation() {}

  public Reservation(User user, String firstName, String lastName, Flight flight, Seat seat,
      int price) {
    super();
    this.user = user;
    this.firstName = firstName;
    this.lastName = lastName;
    this.flight = flight;
    this.seat = seat;
    this.price = price;
  }

  public Reservation(int reservationId, User user, String firstName, String lastName, Flight flight,
      Seat seat, Timestamp dateCreated, int price) {
    super();
    this.reservationId = reservationId;
    this.user = user;
    this.firstName = firstName;
    this.lastName = lastName;
    this.flight = flight;
    this.seat = seat;
    this.dateCreated = dateCreated;
    this.price = price;
  }

  public int getReservationId() {
    return reservationId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Flight getFlight() {
    return flight;
  }

  public void setFlight(Flight flight) {
    this.flight = flight;
  }

  public Seat getSeat() {
    return seat;
  }

  public void setSeat(Seat seat) {
    this.seat = seat;
  }

  public Timestamp getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Timestamp dateCreated) {
    this.dateCreated = dateCreated;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
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
    if (firstName == null) {
      if (other.firstName != null)
        return false;
    } else if (!firstName.equals(other.firstName))
      return false;
    if (flight == null) {
      if (other.flight != null)
        return false;
    } else if (!flight.equals(other.flight))
      return false;
    if (lastName == null) {
      if (other.lastName != null)
        return false;
    } else if (!lastName.equals(other.lastName))
      return false;
    if (price != other.price)
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
    return "Reservation [reservationId=" + reservationId + ", user=" + user + ", firstName="
        + firstName + ", lastName=" + lastName + ", flight=" + flight + ", seat=" + seat
        + ", dateCreated=" + dateCreated + ", price=" + price + "]";
  }

}
