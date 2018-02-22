package database.tables;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class maps Hibernate to the Franchise table.
 *
 * @author Marcus Messer
 */
@Entity
@Table(name = "FRANCHISE")
public class Franchise {

  /**
   * This field is the name of the restaurant.
   */
  @Id
  private String name;

  /**
   * This field is the hashed password for the restaurant.
   */
  private String password;
  /**
   * This field is the address for the restaurant.
   */
  private String address;
  /**
   * This field is the contact number of the restaurant.
   */
  private String contactNo;

  /**
   * This empty constructor is used by Hibernate.
   */
  public Franchise() {
    //Empty Body
  }

  /**
   * This constructor is used by the server to create entries in the table.
   *
   * @param name The name of the restaurant.
   * @param address The address of the restaurant.
   * @param contactNumber The contact number for the restaurant.
   * @param password The password for the restaurant.
   */
  public Franchise(String name, String address, String contactNumber, String password) {
    this.name = name;
    this.address = address;
    this.contactNo = contactNumber;
    this.password = password;
  }


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getContactNo() {
    return contactNo;
  }

  public void setContactNo(String contactNo) {
    this.contactNo = contactNo;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "Franchise{" +
        "name='" + name + '\'' +
        ", password='" + password + '\'' +
        ", address='" + address + '\'' +
        ", contactNo='" + contactNo + '\'' +
        '}';
  }
}
