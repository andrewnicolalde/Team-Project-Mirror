package authentication;

import database.Staff;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.mindrot.jbcrypt.BCrypt;

public class EmployeeAuthenticator {
  private EntityManagerFactory emf;
  private EntityManager em;

  public EmployeeAuthenticator() {
    emf = Persistence.createEntityManagerFactory("server.database");
    em = emf.createEntityManager();
  }

  public void close() {
    emf.close();
  }

  /**
   * Checks to see if the given credentials match an existing employee.
   * @param employeeNumber The employee number of the employee to check.
   * @param password The plaintext password of the employee to check.
   * @return True if the credentials match what is stored in the database, false otherwise.
   */
  public boolean checkCredentials(Long employeeNumber, String password) {
    // Find the employee with the given employee number
    em.getTransaction().begin();
    Staff employee = em.find(Staff.class, employeeNumber);
    if (employee == null) {
      // Employee does not exist - return false
      return false;
    }

    // Check the password
    return BCrypt.checkpw(password, employee.getPassword());
  }
}
