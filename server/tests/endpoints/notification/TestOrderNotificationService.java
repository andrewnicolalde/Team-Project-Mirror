package endpoints.notification;

import static org.junit.Assert.assertNotNull;

import database.DatabaseManager;
import database.tables.FoodOrder;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;

public class TestOrderNotificationService {

  private OrderNotificationService notificationService;
  private EntityManager entityManager;
  private FoodOrder foodOrder;

  @Before
  public void setUp() {
    entityManager = DatabaseManager.getInstance().getEntityManager();
    foodOrder = entityManager.find(FoodOrder.class, 10L);
  }

  @Test
  public void testConstructor() {
    notificationService = new OrderNotificationService(foodOrder);
    assertNotNull(notificationService);
  }

}
