package cst438.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "seat")
public class Seat {

  @Id
  private int seatId;
  @ManyToOne
  @JoinColumn(name = "flight_id", referencedColumnName = "flight_id")
  private Flight flight;
  private int seatRow;
  private String seatLetter;
  private int available;

  public Seat(int seatId, Flight flight, int seatRow, String seatLetter, int available) {
    super();
    this.seatId = seatId;
    this.flight = flight;
    this.seatRow = seatRow;
    this.seatLetter = seatLetter;
    this.available = available;
  }

  public int getSeatId() {
    return seatId;
  }

  public void setSeatId(int seatId) {
    this.seatId = seatId;
  }

  public Flight getFlight() {
    return flight;
  }

  public void setFlight(Flight flight) {
    this.flight = flight;
  }

  public int getSeatRow() {
    return seatRow;
  }

  public void setSeatRow(int seatRow) {
    this.seatRow = seatRow;
  }

  public String getSeatLetter() {
    return seatLetter;
  }

  public void setSeatLetter(String seatLetter) {
    this.seatLetter = seatLetter;
  }

  public int getAvailable() {
    return available;
  }

  public void setAvailable(int available) {
    this.available = available;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Seat other = (Seat) obj;
    if (available != other.available)
      return false;
    if (flight == null) {
      if (other.flight != null)
        return false;
    } else if (!flight.equals(other.flight))
      return false;
    if (seatId != other.seatId)
      return false;
    if (seatLetter == null) {
      if (other.seatLetter != null)
        return false;
    } else if (!seatLetter.equals(other.seatLetter))
      return false;
    if (seatRow != other.seatRow)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Seat [seatId=" + seatId + ", flight=" + flight + ", seatRow=" + seatRow
        + ", seatLetter=" + seatLetter + ", available=" + available + "]";
  }

}
