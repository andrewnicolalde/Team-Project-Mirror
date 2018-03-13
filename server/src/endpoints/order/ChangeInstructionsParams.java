package endpoints.order;

class ChangeInstructionsParams {

  private final Long orderMenuItemId;
  private final String instructions;

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
