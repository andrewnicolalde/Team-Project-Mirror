package endpoints.notification;

public class IncorrectTypeException extends Throwable {
  public IncorrectTypeException(){
    super("The type passed in is not of an accepted Type");
  }
}
