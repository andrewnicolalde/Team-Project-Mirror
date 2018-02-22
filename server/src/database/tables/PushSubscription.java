package database.tables;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class store the details of the push notification.
 *
 * @author Marcus Messer
 */
@Entity
@Table(name = "PUSH_SUBSCRIPTION")
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
   * Stores the Base64 encoded users publicKey
   */
  private String publicKey;

  /**
   * Stores the Base64 encoded authKey.
   */
  private String auth;

  public Long getSubscriptionId() {
    return subscriptionId;
  }

  public void setSubscriptionId(Long subscriptionId) {
    this.subscriptionId = subscriptionId;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public Timestamp getExpirationTime() {
    return expirationTime;
  }

  public void setExpirationTime(Timestamp expirationTime) {
    this.expirationTime = expirationTime;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getAuth() {
    return auth;
  }

  public void setAuth(String auth) {
    this.auth = auth;
  }

  @Override
  public String toString() {
    return "PushSubscription{" +
        "subscriptionId=" + subscriptionId +
        ", endpoint='" + endpoint + '\'' +
        ", expirationTime=" + expirationTime +
        ", publicKey='" + publicKey + '\'' +
        ", auth='" + auth + '\'' +
        '}';
  }
}
