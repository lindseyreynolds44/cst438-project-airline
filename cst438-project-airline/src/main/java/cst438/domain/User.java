package cst438.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

  @Id
  @Column(name = "user_id")
  private int userId;
  @Column(name = "user_name")
  private String userName;
  private String password;

  public User() {}

  public User(int userId, String userName, String password) {
    super();
    this.userId = userId;
    this.userName = userName;
    this.password = password;
  }

  public int getUserId() {
    return userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    User other = (User) obj;
    if (password == null) {
      if (other.password != null)
        return false;
    } else if (!password.equals(other.password))
      return false;
    if (userId != other.userId)
      return false;
    if (userName == null) {
      if (other.userName != null)
        return false;
    } else if (!userName.equals(other.userName))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "User [userId=" + userId + ", userName=" + userName + ", password=" + password + "]";
  }


}
