package database.tables;

import static org.junit.Assert.assertEquals;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CategoryTest {

  private EntityManagerFactory entityManagerFactory;

  @Before
  public void setUp() {
    //Create link to the database
    entityManagerFactory = Persistence.createEntityManagerFactory("server.database");
  }

  @After
  public void tearDown() {
    //Close link to the database
    entityManagerFactory.close();
  }

  @Test
  public void createCategoryTest() {

    EntityManager entityManager;
    //Create new category
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Category category = new Category("Food", 1L);
    entityManager.persist(category);
    entityManager.getTransaction().commit();
    entityManager.close();

    //Get category from database.
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    List<Category> result = entityManager.createQuery("from Category ",
        Category.class).getResultList();

    for (Category item : result) {
      assertEquals("Check id", category.getCategoryId(), item.getCategoryId());
      assertEquals("Check name", category.getName(), item.getName());
    }

    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
