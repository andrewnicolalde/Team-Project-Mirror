package endpoints.manager;


import database.DatabaseManager;
import database.tables.Department;
import database.tables.Staff;
import database.tables.StaffSession;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;
import util.JsonUtil;

public class Employee {

  public static String getEmployees(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    StaffSession session = em
        .find(StaffSession.class, request.session().attribute("StaffSessionKey"));
    List<Staff> employees = em.createQuery(
        "FROM Staff staff WHERE staff.franchise = :franchise ORDER BY employeeNumber ASC",
        Staff.class).setParameter("franchise", session.getStaff().getFranchise()).getResultList();
    em.close();
    List<EmployeeData> employeesToSend = new ArrayList<>();
    for (Staff s : employees) {
      employeesToSend.add(new EmployeeData(s));
    }
    return JsonUtil.getInstance().toJson(employeesToSend);
  }

  public static String getWaiters(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    StaffSession session = em
        .find(StaffSession.class, request.session().attribute("StaffSessionKey"));
    List<Staff> employees = em.createQuery(
        "FROM Staff staff WHERE staff.franchise = :franchise and staff.department = :waiter ORDER BY employeeNumber ASC",
        Staff.class).setParameter("franchise", session.getStaff().getFranchise())
        .setParameter("waiter", Department.WAITER).getResultList();
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

  public static String getDepartments(Request request, Response response) {
    return Department.getJsonList();
  }

  public static String addEmployee(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    NewEmployeeData ed = JsonUtil.getInstance().fromJson(request.body(), NewEmployeeData.class);
    em.getTransaction().begin();
    StaffSession session = em
        .find(StaffSession.class, request.session().attribute("StaffSessionKey"));
    Staff staff = new Staff(ed.getFirstName(), ed.getLastName(),
        BCrypt.hashpw(ed.getPassword(), BCrypt.gensalt()),
        Department.fromString(ed.getDepartment()), session.getStaff().getFranchise());
    em.persist(staff);
    em.getTransaction().commit();
    em.close();
    return JsonUtil.getInstance().toJson(new EmployeeData(staff));
  }

  public static String removeEmployee(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    EmployeeIdParams id = JsonUtil.getInstance().fromJson(request.body(), EmployeeIdParams.class);
    em.getTransaction().begin();
    Staff staff = em.find(Staff.class, id.getEmployeeNumber());
    em.remove(staff);
    em.getTransaction().commit();
    em.close();
    return "success";
  }
}
