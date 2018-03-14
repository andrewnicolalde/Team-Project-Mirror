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
import database.tables.Transaction;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;
import util.JsonUtil;

/**
 * This class is responsible for creating Stripe charges.
 *
 * @author Andrew Nicolalde
 */
public class ChargeMaker {

  public static String createCharge(Request request, Response response) {
    Stripe.apiKey = "sk_test_nKon8YMF1HyqAvNgvFpFHGbi";
    ChargeMakerParams cm = JsonUtil.getInstance().fromJson(request.body(), ChargeMakerParams.class);

    // Get charge meta information
    String token = cm.getId();
    EntityManager em = DatabaseManager.getInstance().getEntityManager();
    TableSession session = em.find(TableSession.class, request.session().attribute("TableSessionKey"));
    Transaction transaction = getCurrentTransaction(session.getRestaurantTable());
    int total = (int)(transaction.getTotal() * 100);
    em.close();

    // Create params
    Map<String, Object> params = new HashMap<>();
    params.put("amount", total);
    params.put("currency", "gbp");
    params.put("description", "Your meal at Oaxaca");
    params.put("source", token);

    try {
      Charge charge = Charge.create(params);
    } catch (AuthenticationException e) {
      e.printStackTrace();
      return "failure";
    } catch (InvalidRequestException e) {
      e.printStackTrace();
      return "failure";
    } catch (APIConnectionException e) {
      e.printStackTrace();
      return "failure";
    } catch (CardException e) {
      e.printStackTrace();
      return "failure";
    } catch (APIException e) {
      e.printStackTrace();
      return "failure";
    }
    return "success";
  }

}
