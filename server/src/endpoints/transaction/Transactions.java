package endpoints.transaction;

import database.DatabaseManager;
import database.tables.RestaurantTable;
import database.tables.RestaurantTableStaff;
import database.tables.TableSession;
import database.tables.Transaction;
import endpoints.order.TransactionIdData;
import java.util.List;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;
import util.JsonUtil;

public class Transactions {

  public static Transaction getCurrentTransaction(RestaurantTable table) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    Transaction transaction;
    List<Transaction> transactions = em.createQuery("from Transaction transaction WHERE transaction.restaurantTableStaff.restaurantTable.tableNumber = :tableNumber AND transaction.isPaid = FALSE", Transaction.class).setParameter("tableNumber", table.getTableNumber()).getResultList();
    // If there isn't an unpaid transaction for the current table, create a new one.
    if (transactions.size() == 0) {
      em.getTransaction().begin();
      List<RestaurantTableStaff> servers = em
          .createQuery("from RestaurantTableStaff tableStaff "
              + "where tableStaff.restaurantTable = :table", RestaurantTableStaff.class)
          .setParameter(
              "table", table).getResultList();

      RestaurantTableStaff temp;
      if (servers.size() == 0) {
        // If there are no waiters assigned to serve this table, then we have an issue...
        return null;
      } else {
        temp = servers.get(0);
      }
      transaction = new Transaction(false, 0.0, null, false, temp);
      em.persist(transaction);
      em.getTransaction().commit();
    } else {
      transaction = transactions.get(0);
    }
    em.close();
    return transaction;
  }

  /**
   * This method gets the current transaction for a table. If one doesn't exist it creates a new
   * one.
   *
   * @param request A HTML request.
   * @param response A HTML response.
   * @return A transactionId in the form of a string.
   */
  public static String getTransactionId(Request request, Response response) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    TableSession tableSession = entityManager.find(TableSession.class,
        request.session().attribute("TableSessionKey"));

    Transaction transaction = getCurrentTransaction(tableSession.getRestaurantTable());

    entityManager.close();

    TransactionIdData transactionIdData = new TransactionIdData(transaction.getTransactionId());
    return JsonUtil.getInstance().toJson(transactionIdData);
  }

}
