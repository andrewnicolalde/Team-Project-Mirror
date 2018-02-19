package util;

import com.google.gson.Gson;

/**
 * Utility class to remove Gson objects floating around the project. credit DZone.com
 */
public class JsonUtil {

  private static Gson instance = null;

  private JsonUtil() { /* Do nothing, only here to prevent instantiation */ }

  public static Gson getInstance() {
    if (instance == null) {
      instance = new Gson();
    }
    return instance;
  }
}
