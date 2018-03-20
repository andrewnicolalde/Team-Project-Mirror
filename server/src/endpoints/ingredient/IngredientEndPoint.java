package endpoints.ingredient;

import database.DatabaseManager;
import database.tables.Ingredient;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import spark.Request;
import spark.Response;
import util.JsonUtil;

public class IngredientEndPoint {
  /**
   * This get method returns a list of ingredients in JSON.
   * @param request A HTML request.
   * @param response A HTML response.
   * @return The list of ingredients in JSON.
   */
  public static String getIngredients(Request request, Response response) {
    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();

    List<Ingredient> ingredients = entityManager.createQuery("from Ingredient ingredient",
        Ingredient.class).getResultList();

    entityManager.close();

    List<IngredientParams> ingredientsToSend = new ArrayList<>();
    for (Ingredient i : ingredients) {
      ingredientsToSend.add(new IngredientParams(i));
    }

    return JsonUtil.getInstance().toJson(ingredientsToSend);
  }

  /**
   * This method takes JSON and creates a new ingredient. See <code>IngredientParams</code> for
   * what needs to get sent in the JSON.
   * @param request A HTML request.
   * @param response A HTML response.
   * @return success after the new item is created.
   */
  public static String newIngredient(Request request, Response response) {
    IngredientParams ingredientParams = JsonUtil.getInstance().fromJson(request.body(),
        IngredientParams.class);

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(new Ingredient(ingredientParams.getIngredientName()));
    entityManager.getTransaction().commit();

    entityManager.close();
    return "success";
  }

  /**
   * This method takes JSON and removes an ingredient from the database.
   * See <code>IngredientParams</code> for JSON details.
   * @param request A HTML request.
   * @param response A HTML response.
   * @return success after the item has been removed.
   */
  public static String removeIngredient(Request request, Response response) {
    IngredientParams ingredientParams = JsonUtil.getInstance().fromJson(request.body(),
        IngredientParams.class);

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    entityManager.remove(ingredientParams.getId());

    entityManager.close();

    return "success";
  }

  /**
   * This method takes JSON to change the name of an ingredient.
   * See <code>IngredientParams</code> for JSON details.
   * @param request A HTML request.
   * @param response A HTML response.
   * @return success after the rename has happened.
   */
  public static String renameIngredient(Request request, Response response) {
    IngredientParams ingredientParams = JsonUtil.getInstance().fromJson(request.body(),
        IngredientParams.class);

    EntityManager entityManager = DatabaseManager.getInstance().getEntityManager();
    Ingredient ingredient = entityManager.find(Ingredient.class, ingredientParams.getId());
    entityManager.getTransaction().begin();
    ingredient.setIngredientName(ingredientParams.getIngredientName());
    entityManager.getTransaction().commit();

    entityManager.close();
    return "success";
  }

}
