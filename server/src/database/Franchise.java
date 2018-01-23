package database;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
   * This field is the ID of the restaurant, it is also the primary key of the table.
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long franchiseId;
  /**
   * This field is the address for the restaurant.
   */
  private String address;
  /**
   * This field is the name of the restaurant.
   */
  private String name;
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
   * This constrctuor is used by the server to create entries in the table.
   *
   * @param name          The name of the restaurant.
   * @param address       The address of the restaurant.
   * @param contactNumber The contact number for the restaurant.
   */
  public Franchise(String name, String address, String contactNumber) {
    this.name = name;
    this.address = address;
    this.contactNo = contactNumber;
  }

  public Long getFranchiseId() {
    return franchiseId;
  }

  public void setFranchiseId(Long franchiseId) {
    this.franchiseId = franchiseId;
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
}
