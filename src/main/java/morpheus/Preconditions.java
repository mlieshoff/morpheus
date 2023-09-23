package morpheus;

public class Preconditions {

  public static void notEmpty(String message, String s) {
    if (s == null || s.isEmpty()) {
      throw new IllegalArgumentException(message + " can't be empty!");
    }
  }

}
