package util;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import spark.ResponseTransformer;

/**
 * Utility class to remove Gson objects floating around the project.
 * credit DZone.com
 */
public class JsonUtil {
  public static String toJson(Object object){ return new Gson().toJson(object); }
}
