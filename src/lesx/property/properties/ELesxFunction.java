package lesx.property.properties;

import java.util.HashMap;
import java.util.Map;

import lesx.utils.LesxString;

public enum ELesxFunction {
  SUM(1L) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_SUM;
    }
  },
  END_MONTH(2L) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_END_MONTH;
    }
  },
  PERIOD(3L) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_PERIOD;
    }
  };

  private static Map<Long, ELesxFunction> mapValues = new HashMap<>();

  static {
    for (ELesxFunction type : ELesxFunction.values()) {
      mapValues.put(type.get(), type);
    }
  }

  private Long key;

  private ELesxFunction(Long key) {
    this.key = key;
  }

  public Long get() {
    return key;
  }

  public static ELesxFunction getFunction(Long key) {
    return mapValues.get(key);
  }

}
