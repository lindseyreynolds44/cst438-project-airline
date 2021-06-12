package cst438.domain;

import java.sql.Date;
import java.sql.Time;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "flight")
public class Flight {

  @Id
  @Column(name = "flight_id")
  private int flightId;
  @Column(name = "flight_number")
  private String flightNumber;
  @Column(name = "airline_name")
  private String airlineName;
  @Column(name = "departure_date")
  private Date departureDate;
  @Column(name = "departure_time")
  private Time departureTime;
  @Column(name = "number_of_stops")
  private int numberOfStops;
  @Column(name = "origin_city")
  private String originCity;
  @Column(name = "destination_city")
  private String destinationCity;
  private int price;

  public Flight() {}

  public Flight(int flightId, String flightNumber, String airlineName, Date departureDate,
      Time departureTime, int numberOfStops, String originCity, String destinationCity, int price) {
    super();
    this.flightId = flightId;
    this.flightNumber = flightNumber;
    this.airlineName = airlineName;
    this.departureDate = departureDate;
    this.departureTime = departureTime;
    this.numberOfStops = numberOfStops;
    this.originCity = originCity;
    this.destinationCity = destinationCity;
    this.price = price;
  }

  public int getFlightId() {
    return flightId;
  }

  public void setFlightId(int flightId) {
    this.flightId = flightId;
  }

  public String getFlightNumber() {
    return flightNumber;
  }

  public void setFlightNumber(String flightNumber) {
    this.flightNumber = flightNumber;
  }

  public String getAirlineName() {
    return airlineName;
  }

  public void setAirlineName(String airlineName) {
    this.airlineName = airlineName;
  }

  public Date getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(Date departureDate) {
    this.departureDate = departureDate;
  }

  public Time getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(Time departureTime) {
    this.departureTime = departureTime;
  }

  public int getNumberOfStops() {
    return numberOfStops;
  }

  public void setNumberOfStops(int numberOfStops) {
    this.numberOfStops = numberOfStops;
  }

  public String getOriginCity() {
    return originCity;
  }

  public void setOriginCity(String originCity) {
    this.originCity = originCity;
  }

  public String getDestinationCity() {
    return destinationCity;
  }

  public void setDestinationCity(String destinationCity) {
    this.destinationCity = destinationCity;
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
    Flight other = (Flight) obj;
    if (airlineName == null) {
      if (other.airlineName != null)
        return false;
    } else if (!airlineName.equals(other.airlineName))
      return false;
    if (departureDate == null) {
      if (other.departureDate != null)
        return false;
    } else if (!departureDate.equals(other.departureDate))
      return false;
    if (departureTime == null) {
      if (other.departureTime != null)
        return false;
    } else if (!departureTime.equals(other.departureTime))
      return false;
    if (destinationCity == null) {
      if (other.destinationCity != null)
        return false;
    } else if (!destinationCity.equals(other.destinationCity))
      return false;
    if (flightId != other.flightId)
      return false;
    if (flightNumber == null) {
      if (other.flightNumber != null)
        return false;
    } else if (!flightNumber.equals(other.flightNumber))
      return false;
    if (numberOfStops != other.numberOfStops)
      return false;
    if (originCity == null) {
      if (other.originCity != null)
        return false;
    } else if (!originCity.equals(other.originCity))
      return false;
    if (price != other.price)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Flight [flightId=" + flightId + ", flightNumber=" + flightNumber + ", airlineName="
        + airlineName + ", departureDate=" + departureDate + ", departureTime=" + departureTime
        + ", numberOfStops=" + numberOfStops + ", originCity=" + originCity + ", destinationCity="
        + destinationCity + ", price=" + price + "]";
  }

}
