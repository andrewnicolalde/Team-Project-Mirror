package endpoints.menu;

/**
 * This class creates a usable object from JSON.
 *
 * @author Marcus Messer
 */
public class RemoveMenuItemParams {

  /**
   * This field stores the id of the item that is going to be removed
   */
  private Long id;

  /**
   * This constructor converts the JSON into a usable object.
   * @param id The id of the item that is going to be removed.
   */
  public RemoveMenuItemParams(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

}
