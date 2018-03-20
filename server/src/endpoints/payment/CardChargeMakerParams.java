package endpoints.payment;

/**
 * This class is a wrapper for the Stripe transaction token ID
 *
 * @author Andrew Nicolalde
 */
public class CardChargeMakerParams {

  /**
   * The id of the transaction
   */
  String id;

  public CardChargeMakerParams(String id){
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
