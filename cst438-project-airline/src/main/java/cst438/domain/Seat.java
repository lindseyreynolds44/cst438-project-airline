package cst438.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "seat")
public class Seat {

  @Id
  @Column(name = "seat_id")
  private int seatId;
  @Column(name = "flight_id")
  private int flightId;
  @Column(name = "seat_row")
  private int seatRow;
  @Column(name = "seat_letter")
  private String seatLetter;
  @Column(name = "available")
  private boolean isAvailable;
  @Column(name = "is_first_class")
  private boolean isFirstClass;

  public Seat() {}

  public Seat(int seatId, int flightId, int seatRow, String seatLetter, boolean isAvailable,
      boolean isFirstClass) {
    super();
    this.seatId = seatId;
    this.flightId = flightId;
    this.seatRow = seatRow;
    this.seatLetter = seatLetter;
    this.isAvailable = isAvailable;
    this.isFirstClass = isFirstClass;
  }

  public int getSeatId() {
    return seatId;
  }


  public int getFlightId() {
    return flightId;
  }

  public void setFlightId(int flightId) {
    this.flightId = flightId;
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

  public boolean getIsAvailable() {
    return isAvailable;
  }

  public void setIsAvailable(boolean isAvailable) {
    this.isAvailable = isAvailable;
  }

  public boolean getIsFirstClass() {
    return isFirstClass;
  }

  public void setIsFirstClass(boolean isFirstClass) {
    this.isFirstClass = isFirstClass;
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
    if (isAvailable != other.isAvailable)
      return false;
    if (flightId != other.flightId)
      return false;
    if (isFirstClass != other.isFirstClass)
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
    return "Seat [seatId=" + seatId + ", flightId=" + flightId + ", seatRow=" + seatRow
        + ", seatLetter=" + seatLetter + ", isAvailable=" + isAvailable + ", isFirstClass="
        + isFirstClass + "]";
  }


}
