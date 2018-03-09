package endpoints.manager;


import database.DatabaseManager;
import database.tables.Department;
import database.tables.Staff;
import database.tables.StaffSession;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;
import util.JsonUtil;

public class Employee {
  public static String getEmployees(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    StaffSession session = em.find(StaffSession.class, request.session().attribute("StaffSessionKey"));
    List<Staff> employees = em.createQuery("FROM Staff staff WHERE staff.franchise = :franchise ORDER BY employeeNumber ASC", Staff.class).setParameter("franchise", session.getStaff().getFranchise()).getResultList();
    em.close();
    List<EmployeeData> employeesToSend = new ArrayList<>();
    for (Staff s : employees) {
      employeesToSend.add(new EmployeeData(s));
    }
    return JsonUtil.getInstance().toJson(employeesToSend);
  }

  public static String editEmployee(Request request, Response response) {
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
