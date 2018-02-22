package endpoints.order;

public class ChangeInstructionsParams {
  private Long orderMenuItemId;
  private String instructions;

  public ChangeInstructionsParams(Long orderMenuItemId, String instructions) {
    this.orderMenuItemId = orderMenuItemId;
    this.instructions = instructions;
  }

  public Long getOrderMenuItemId() {
    return orderMenuItemId;
  }

  public String getInstructions() {
    return instructions;
  }
}
