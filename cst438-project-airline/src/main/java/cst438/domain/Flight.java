package cst438.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "flight")
public class Flight {

  @Id
  private int flightId;
  private String airlineName;
  private Date departureDateTime;
  private int numberOfStops;
  private String originCity;
  private String destinationCity;

  public Flight(int flightId, String airlineName, Date departureDateTime, int numberOfStops,
      String originCity, String destinationCity) {
    super();
    this.flightId = flightId;
    this.airlineName = airlineName;
    this.departureDateTime = departureDateTime;
    this.numberOfStops = numberOfStops;
    this.originCity = originCity;
    this.destinationCity = destinationCity;
  }

  public int getFlightId() {
    return flightId;
  }

  public void setFlightId(int flightId) {
    this.flightId = flightId;
  }

  public String getAirlineName() {
    return airlineName;
  }

  public void setAirlineName(String airlineName) {
    this.airlineName = airlineName;
  }

  public Date getDepartureDateTime() {
    return departureDateTime;
  }

  public void setDepartureDateTime(Date departureDateTime) {
    this.departureDateTime = departureDateTime;
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
    if (departureDateTime == null) {
      if (other.departureDateTime != null)
        return false;
    } else if (!departureDateTime.equals(other.departureDateTime))
      return false;
    if (destinationCity == null) {
      if (other.destinationCity != null)
        return false;
    } else if (!destinationCity.equals(other.destinationCity))
      return false;
    if (flightId != other.flightId)
      return false;
    if (numberOfStops != other.numberOfStops)
      return false;
    if (originCity == null) {
      if (other.originCity != null)
        return false;
    } else if (!originCity.equals(other.originCity))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Flight [flightId=" + flightId + ", airlineName=" + airlineName + ", departureDateTime="
        + departureDateTime + ", numberOfStops=" + numberOfStops + ", originCity=" + originCity
        + ", destinationCity=" + destinationCity + "]";
  }

}
