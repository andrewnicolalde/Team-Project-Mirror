package endpoints.notification;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import database.DatabaseManager;
import database.tables.FoodOrder;
import database.tables.OrderStatus;
import database.tables.RestaurantTable;
import database.tables.Transaction;
import java.sql.Timestamp;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;

public class TestNotificationServiceFactory {

  private NotificationServiceFactory notificationServiceFactory;
  private EntityManager entityManager;



  @Before
  public void setUp() {
    notificationServiceFactory = new NotificationServiceFactory();
  }

  @Test
  public void testGetOrderNotificationServiceFactory() {
    assertNotNull("Get NotificationServiceFactory", notificationServiceFactory);
  }

  @Test
  public void testGetOrderNotificationService() {
    entityManager = DatabaseManager.getInstance().getEntityManager();
    FoodOrder foodOrder = entityManager.find(FoodOrder.class, 10L);
    try {
      assertNotNull(notificationServiceFactory.getNotificationService(foodOrder));
    } catch (IncorrectTypeException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetTransactionNotificationService() {
    entityManager = DatabaseManager.getInstance().getEntityManager();
    Transaction transaction = entityManager.find(Transaction.class, 11L);
    try {
      assertNotNull(notificationServiceFactory.getNotificationService(transaction));
    } catch (IncorrectTypeException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetTableNotificationService() {
    entityManager = DatabaseManager.getInstance().getEntityManager();
    RestaurantTable table = entityManager.find(RestaurantTable.class, 1L);
    try {
      assertNotNull(notificationServiceFactory.getNotificationService(table));
    } catch (IncorrectTypeException e) {
      fail(e.getMessage());
    }
  }

  @Test (expected = IncorrectTypeException.class)
  public void testIncorrectTypeException() throws IncorrectTypeException {
    notificationServiceFactory.getNotificationService("Hello World");
  }
}
