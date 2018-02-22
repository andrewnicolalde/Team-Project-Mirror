package database.tables;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;

/**
 * This table stores the elements used in the push notifications.
 *
 * @author Marcus Messer
 */
@Entity
public class StaffNotification {

  /**
   * An auto generated primary key, for the use of storing the notification
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long notificationId;

  /**
   * Stores the staff member that the notification is going to
   */
  @ManyToOne
  private Staff staff;

  /**
   * Stores the push subscriptions details
   */
  @OneToOne
  private PushSubscription pushSubscription;

  public Long getNotificationId() {
    return notificationId;
  }

  public void setNotificationId(Long notificationId) {
    this.notificationId = notificationId;
  }

  public Staff getStaff() {
    return staff;
  }

  public void setStaff(Staff staff) {
    this.staff = staff;
  }

  public PushSubscription getPushSubscription() {
    return pushSubscription;
  }

  public void setPushSubscription(PushSubscription pushSubscription) {
    this.pushSubscription = pushSubscription;
  }
}
