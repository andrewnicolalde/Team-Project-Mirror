/**
 * This class is for the Staff Table in the database. It requires the <code>Staff.hbm.xml</code>
 * file, to map this Java class to the XML file.
 *
 * @author Marcus Messer
 */
public class Staff {

  /**
   * This field stores the employees number as a Long.
   */
  private Long employee_number;
  /**
   * This field stores the employees hashed password as a String.
   */
  private String password;
  /**
   * Thie field stores the department that the employee belongs too.
   */
  private String department;

  /**
   * An empty constructor used by hibernate.
   */
  public Staff() {
    //Empty Body
  }

  /**
   * This constructor enables the application to create new instances of employees and add them to
   * the database.
   * @param password Is the hashed password for the employee.
   * @param department Is the department the employee belongs too.
   */
  public Staff(String password, String department) {
    this.password = password;
    this.department = department;
  }

  public Long getEmployee_number() {
    return employee_number;
  }

  public void setEmployee_number(Long employee_number) {
    this.employee_number = employee_number;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }
}