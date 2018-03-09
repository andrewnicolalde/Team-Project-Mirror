package endpoints.transaction;

import database.DatabaseManager;
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

    Transaction transaction;

    List<Transaction> transactions = entityManager.createQuery("from Transaction transaction where "
        + "transaction.restaurantTableStaff.restaurantTable.tableNumber = :tableNo AND "
        + "transaction.isPaid = false ", Transaction.class).setParameter("tableNo",
        tableSession.getRestaurantTable().getTableNumber()).getResultList();

    // If there isn't an unpaid transaction for the current table, create a new one.
    if (transactions.size() == 0) {
      entityManager.getTransaction().begin();
      List<RestaurantTableStaff> servers = entityManager
          .createQuery("from RestaurantTableStaff tableStaff "
              + "where tableStaff.restaurantTable = :table", RestaurantTableStaff.class)
          .setParameter(
              "table", tableSession.getRestaurantTable()).getResultList();

      RestaurantTableStaff temp;
      if (servers.size() == 0) {
        // If there are no waiters assigned to serve this table, then we have an issue...
        return "failure";
      } else {
        temp = servers.get(0);
      }
      transaction = new Transaction(false, 0.0, null, false, temp);
      entityManager.persist(transaction);
      entityManager.getTransaction().commit();
    } else {
      transaction = transactions.get(0);
    }

    entityManager.close();

    TransactionIdData transactionIdData = new TransactionIdData(transaction.getTransactionId());
    return JsonUtil.getInstance().toJson(transactionIdData);
  }

  /**
   * Returns the total price of a transaction.
   *
   * JSON Input:
   * transactionId: the ID of the transaction you want the total for
   */
  public static String getTransactionTotal(Request request, Response response) {
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    TransactionIdParams orderIdParams = JsonUtil.getInstance()
        .fromJson(request.body(), TransactionIdParams.class);
    Transaction transaction = em.find(Transaction.class, orderIdParams.getTransactionId());
    return JsonUtil.getInstance().toJson(transaction.getTotal());
  }
}
