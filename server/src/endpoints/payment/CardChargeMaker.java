package endpoints.payment;

import static endpoints.transaction.Transactions.getCurrentTransaction;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import database.DatabaseManager;
import database.tables.TableSession;
import database.tables.TableStatus;
import database.tables.Transaction;
import endpoints.notification.NotificationEndpoint;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;
import util.JsonUtil;

/**
 * This class is responsible for creating Stripe charges & charging the customer's card.
 *
 * @author Andrew Nicolalde
 */
public class CardChargeMaker {

  /**
   * This method is called when a request is sent to /api/authTable/createCardCharge. It invokes
   * Stripe to charge the customer's card the total (in GBP) for
   *
   * @param request a Spark request
   * @param response a Spark response
   * @return "success" in the case of success, and "failure" in the case of failure
   */
  public static String createCharge(Request request, Response response) {
    Stripe.apiKey = "sk_test_nKon8YMF1HyqAvNgvFpFHGbi";
    CardChargeMakerParams cm = JsonUtil.getInstance().fromJson(request.body(), CardChargeMakerParams.class);

    // Get charge meta information
    String token = cm.getId();
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    TableSession session = em
        .find(TableSession.class, request.session().attribute("TableSessionKey"));
    Transaction transaction = getCurrentTransaction(session.getRestaurantTable());
    int total = (int) (transaction.getTotal() * 100);

    // Create params
    Map<String, Object> params = new HashMap<>();
    params.put("amount", total);
    params.put("currency", "gbp");
    params.put("description", "Your meal at Oaxaca");
    params.put("source", token);

    try {
      // Charge customer card
      Charge charge = Charge.create(params);
      // Change status in database
      em.getTransaction().begin();

      Transaction temp = em.find(Transaction.class, transaction.getTransactionId());

      temp.setDatetimePaid(new Timestamp(System.currentTimeMillis()));
      temp.setIsPaid(true);
      session.getRestaurantTable().setStatus(TableStatus.NEEDS_CLEANING);
      em.getTransaction().commit();
      NotificationEndpoint.startNotificationService(temp);
      return "success";
    } catch (AuthenticationException | InvalidRequestException | CardException | APIConnectionException | APIException e) {
      e.printStackTrace();
      return "failure";
    } finally {
      em.close();
    }
  }

}
