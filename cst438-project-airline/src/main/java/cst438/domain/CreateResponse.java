package cst438.domain;

public class CreateResponse {
  private String status;
  private Reservation data;

  public CreateResponse() {}


  public CreateResponse(String status, Reservation data) {
    super();
    this.status = status;
    this.data = data;
  }

  public String getStatus() {
    return status;
  }


  public void setStatus(String status) {
    this.status = status;
  }


  public Reservation getData() {
    return data;
  }


  public void setData(Reservation data) {
    this.data = data;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CreateResponse other = (CreateResponse) obj;
    if (data == null) {
      if (other.data != null)
        return false;
    } else if (!data.equals(other.data))
      return false;
    if (status == null) {
      if (other.status != null)
        return false;
    } else if (!status.equals(other.status))
      return false;
    return true;
  }



}
