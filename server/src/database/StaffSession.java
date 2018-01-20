package database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "STAFF_SESSION")
public class StaffSession {

  @Id
  private String staffSessionId;

  @ManyToOne
  @JoinColumn(name = "employeeNumber")
  private Staff staff;

  public StaffSession() {
    // Empty Body
  }

  public StaffSession(String staffSessionId, Staff staff) {
    this.staffSessionId = staffSessionId;
    this.staff = staff;
  }

  public Staff getStaff() {
    return staff;
  }

  public void setStaff(Staff staff) {
    this.staff = staff;
  }

  public String getStaffSessionId() {
    return staffSessionId;
  }

  public void setStaffSessionId(String staffSessionId) {
    this.staffSessionId = staffSessionId;
  }
}
