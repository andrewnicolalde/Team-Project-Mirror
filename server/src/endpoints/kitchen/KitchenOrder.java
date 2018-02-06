package endpoints.kitchen;

import static util.JsonUtil.toJson;

import com.google.gson.Gson;
import database.DatabaseManager;
import database.tables.FoodOrder;
import database.tables.MenuItem;
import database.tables.OrderMenuItem;
import database.tables.OrderStatus;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;

/**
 * Endpoint class serving fetching from the database and serving JSON.
 *
 * @author Roger Milroy
 */
public class KitchenOrder {

  /**
   * End point method to get all Orders and their respective items with status COOKING.
   *
   * @return a JSON string of an array of orders containing an array of items.
   */
  public static String getCookingOrders(Request request, Response response) {
    List<KitchenOrderData> orders = getCookingOrderData();
    return toJson(orders);
  }

  /**
   * Helper method that queries the database and packages the returned Objects into new Objects that
   * can be converted to JSON.
   *
   * @return an List of KitchenOrderData objects.
   */
  private static List<KitchenOrderData> getCookingOrderData() {

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    List items = entityManager.createQuery(
        "select item.foodOrder, item.menuItem, item from OrderMenuItem item where item.foodOrder.status = "
            + OrderStatus.COOKING.ordinal())
        .getResultList();
    Iterator results = items.iterator();
    List<KitchenOrderData> cookingItems = new LinkedList<>();

    while (results.hasNext()) {
      Object[] row = (Object[]) results.next();
      FoodOrder order = (FoodOrder) row[0];
      MenuItem item = (MenuItem) row[1];
      OrderMenuItem orderMenuItem = (OrderMenuItem) row[2];
      KitchenOrderData found = null;
      if ((found = containsOrder(order, cookingItems)) == null) {
        found = new KitchenOrderData(order.getOrderId());
        cookingItems.add(found);
      }
      found.addKitchenOrderItemData(item, orderMenuItem);
    }

    return cookingItems;
  }

  private static KitchenOrderData containsOrder(FoodOrder order, List<KitchenOrderData> list) {
    if (list.isEmpty()) {
      return null;
    } else {
      int index = -1;
      if ((index = list.indexOf(new KitchenOrderData(order.getOrderId()))) >= 0) {
        return list.get(index);
      }
    }
    return null;
  }
}
