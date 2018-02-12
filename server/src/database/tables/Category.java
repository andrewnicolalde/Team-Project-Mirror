package database.tables;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class maps the category table.
 *
 * @author Marcus Messer
 */
@Entity
@Table(name = "CATEGORY")
public class Category {

  /**
   * This field is the primary key for the category.
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long categoryId;

  /**
   * This field is the name of the category.
   */
  private String name;

  public Category() {
    //Empty Body
  }

  public Category(String name) {
    this.name = name;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
