package endpoints.notification;

import database.tables.PushSubscription;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.Base64;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

/**
 * This class is PushSubscription repackaged due to persistence restrictions.
 * @author Roger Milroy
 */
public class Subscription {

  public Subscription(PushSubscription subscription) {
    this.subscriptionId = subscription.getSubscriptionId();
    this.endpoint = subscription.getEndpoint();
    this.expirationTime = subscription.getExpirationTime();
    this.publicKey = subscription.getPublicKey();
    this.auth = subscription.getAuth();
  }

  /**
   * Unique ID for the subscription.
   */
  private Long subscriptionId;

  /**
   * The end point that the push notifications use.
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

  /**
   * Returns the base64 encoded auth string as a byte[]
   * Credit: esphen https://github.com/web-push-libs/webpush-java/blob/master/doc/UsageExample.md
   */
  public byte[] getAuthAsBytes() {
    return Base64.getDecoder().decode(getAuth());
  }

  /**
   * Returns the base64 encoded public key string as a byte[]
   * Credit: esphen https://github.com/web-push-libs/webpush-java/blob/master/doc/UsageExample.md
   */
  public byte[] getKeyAsBytes() {
    return Base64.getDecoder().decode(getPublicKey());
  }

  /**
   * Returns the base64 encoded public key as a PublicKey object
   * Credit: esphen https://github.com/web-push-libs/webpush-java/blob/master/doc/UsageExample.md
   */
  public PublicKey getUserPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
    KeyFactory kf = KeyFactory.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME);
    ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
    ECPoint point = ecSpec.getCurve().decodePoint(getKeyAsBytes());
    ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);

    return kf.generatePublic(pubSpec);
  }
}
