package database.tables;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class stores the stock level for each franchise per ingredient.
 *
 * @author Marcus Messer
 */
@Entity
class Stock {

  /**
   * Required primary key.
   */
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private Long id;

  /**
   * This field links the stock count to the franchise.
   */
  @ManyToOne
  private Franchise franchise;

  /**
   * This field links the ingredient to the stock count.
   */
  @ManyToOne
  private Ingredient ingredient;

  private int stockCount;

  /**
   * Empty constructor used by Hibernate.
   */
  public Stock(){
    //Empty Body
  }

  /**
   * This constructor creates new stock entities.
   * @param franchise The franchise the stock belongs to.
   * @param ingredient The stock itself.
   * @param stockCount The count of the stock.
   */
  public Stock(Franchise franchise, Ingredient ingredient, int stockCount) {
    this.franchise = franchise;
    this.ingredient = ingredient;
    this.stockCount = stockCount;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Franchise getFranchise() {
    return franchise;
  }

  public void setFranchise(Franchise franchise) {
    this.franchise = franchise;
  }

  public Ingredient getIngredient() {
    return ingredient;
  }

  public void setIngredient(Ingredient ingredient) {
    this.ingredient = ingredient;
  }

  public int getStockCount() {
    return stockCount;
  }

  public void setStockCount(int stockCount) {
    this.stockCount = stockCount;
  }

}
