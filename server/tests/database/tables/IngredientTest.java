package database.tables;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IngredientTest {

  private EntityManagerFactory entityManagerFactory = Persistence
      .createEntityManagerFactory("server.database.test");
  private EntityManager entityManager;

  @Before
  public void setUp() {
    entityManager = entityManagerFactory.createEntityManager();
  }

  @After
  public void tearDown() {
    entityManager.close();
  }

  @Test
  public void addIngredient() {
    entityManager.getTransaction().begin();
    Ingredient ingredient = new Ingredient("Beef");
    entityManager.persist(ingredient);
    entityManager.getTransaction().commit();

    Ingredient ingredients = entityManager.createQuery("from Ingredient ", Ingredient.class)
        .getSingleResult();

    assertEquals("Added ingredient id", ingredient.getIngredientId(),
        ingredients.getIngredientId());
    assertEquals("Added ingredient name", ingredient.getIngredientName(),
        ingredients.getIngredientName());
  }

}
