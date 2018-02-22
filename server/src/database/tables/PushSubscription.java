package database.tables;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class store the details of the push notification.
 *
 * @author Marcus Messer
 */
@Entity
public class PushSubscription {

  /**
   * The primary key for the table.
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long subscriptionId;

  /**
   * The end point that the push notification use.
   */
  private String endpoint;

  /**
   * This store when the subscription expires.
   */
  private Timestamp expirationTime;

  /**
   * Stores the keys used in the subscriptions
   */
  @ManyToOne
  private SubscriptionKey subscriptionKey;
}
