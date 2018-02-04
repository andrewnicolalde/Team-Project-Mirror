package endpoints.kitchen;

import com.google.gson.Gson;
import database.tables.FoodOrder;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Endpoint class serving fetching from the database and serving JSON.
 *
 * @author Roger Milroy
 */
public class KitchenOrder {

  /**
   * Queries the database for all orders that are Cooking.
   *
   * NOT FINISHED!
   *
   * @return a JSON string of the order IDs of orders cooking
   */
  public static String getCookingOrders() {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("server.database.dev");
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    List<FoodOrder> orders = entityManager.createQuery("from FoodOrder f where f.status = 3",FoodOrder.class).getResultList();

    KitchenOrderData[] cookingOrders = new KitchenOrderData[orders.size()];
    int i = 0;

    Iterator<FoodOrder> it = orders.iterator();
    while (it.hasNext()) {
      FoodOrder temp = it.next();
      System.out.println(temp.getStatus().toString());
      cookingOrders[i] = new KitchenOrderData(temp.getOrderId());
      i++;
    }

    Gson gson = new Gson();
    return gson.toJson(cookingOrders);

//    return "[{\"orderId\":4,\"orderContents\":" + being kept for now as a tempplate TODO remove.
//        "[{\"orderMenuItemId\":1,\"itemName\":\"Taco\",\"requirements\":\"Extra spicy\"}," +
//        "{\"orderMenuItemId\":3,\"itemName\":\"Burrito\",\"requirements\":\"\"}]}," +
//        "{\"orderId\":5,\"orderContents\":[{\"orderMenuItemId\":5,\"itemName\":\"taco\",\"requirements\":\"None\"}]}]";
  }
}
