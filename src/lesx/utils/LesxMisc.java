package lesx.utils;

import java.util.Collection;
import java.util.Map;

public class LesxMisc {

  /**
   * Evaluates if an object given is empty or {@code null}. Instances supported:
   * <ul>
   * <li>String</li>
   * <li>Collection<?></li>
   * <li>Map</li>
   * </ul>
   *
   * @param object Given object to be evaluated.
   * @return {@code true} if is empty or null, {@code false} otherwise.
   */
  public static <T> boolean isEmpty(T object) {
    if (object == null) {
      return true;
    }
    if (object instanceof String) {
      return "".equals(object);
    }
    if (object instanceof Collection<?>) {
      return ((Collection<?>) object).isEmpty();
    }
    if (object instanceof Map<?, ?>) {
      return ((Map<?, ?>) object).isEmpty();
    }
    return false;
  }

  /**
   * Compare if two objects of the same instance are the same, equals(a , b) follows this rules
   * <ul>
   * <li>if a = null and b = null, returns true</li>
   * <li>if a = null and b = something, returns false</li>
   * <li>if instance a != instance b returns false</li>
   * <li>whatever returns a.equals(b)</li>
   * <ul>
   * 
   * @param Object a
   * @param Object b
   * @return boolean of the result of the equals operation
   */
  public static boolean equals(Object a, Object b) {
    if (a == null || b == null) {
      if (a == null && b == null) {
        return true;
      }
      return false;
    }
    if (a.getClass() != b.getClass()) {
      return false;
    }
    return a.equals(b);
  }
}
