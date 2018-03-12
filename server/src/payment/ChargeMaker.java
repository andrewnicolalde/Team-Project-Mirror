package payment;

import com.stripe.Stripe;

/**
 * This class is responsible for creating Stripe charges.
 *
 * @author Andrew Nicolalde
 */
public class ChargeMaker {

  public void createCharge(){
    Stripe.apiKey = "sk_test_nKon8YMF1HyqAvNgvFpFHGbi";
  }

}
