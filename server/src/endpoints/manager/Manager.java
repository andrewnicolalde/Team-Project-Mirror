package endpoints.manager;

import database.DatabaseManager;
import database.tables.Department;
import database.tables.Staff;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;
import util.JsonUtil;

public class Manager {

  public static String editStaff(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    EmployeeData ed = JsonUtil.getInstance().fromJson(request.body(), EmployeeData.class);
    em.getTransaction().begin();
    Staff staff = em.find(Staff.class, ed.getEmployeeNumber());
    staff.setFirstName(ed.getFirstName());
    staff.setSurname(ed.getLastName());
    staff.setDepartment(Department.fromString(ed.getDepartment()));
    em.getTransaction().commit();
    em.close();
    return "success";
  }
}
