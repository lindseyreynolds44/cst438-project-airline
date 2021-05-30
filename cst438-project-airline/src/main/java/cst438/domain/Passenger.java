package cst438.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "passenger")
public class Passenger {

  @Id
  @Column(name = "passenger_id")
  private int passengerId;
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private User user;
  @Column(name = "first_name")
  private String firstName;
  @Column(name = "last_name")
  private String lastName;

  public Passenger(int passengerId, User user, String firstName, String lastName) {
    super();
    this.passengerId = passengerId;
    this.user = user;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public int getPassengerId() {
    return passengerId;
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

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Passenger other = (Passenger) obj;
    if (firstName == null) {
      if (other.firstName != null)
        return false;
    } else if (!firstName.equals(other.firstName))
      return false;
    if (lastName == null) {
      if (other.lastName != null)
        return false;
    } else if (!lastName.equals(other.lastName))
      return false;
    if (passengerId != other.passengerId)
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
    return "Passenger [passengerId=" + passengerId + ", user=" + user + ", firstName=" + firstName
        + ", lastName=" + lastName + "]";
  }

}
