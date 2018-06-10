package lesx.property.properties;

import lesx.utils.LesxString;

public enum ELesxPropertyType {
  INTEGER(0) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_INTEGER;
    }
  },
  TEXT(1) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_TEXT;
    }
  },
  LONG(2) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_LONG;
    }
  },
  LOCATION(3) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_LOCATION;
    }
  },
  PRICE_TYPE(4) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_PRICE_TYPE;
    }
  },
  LIST(5) {
    @Override
    public String toString() {
      return LesxString.PROPERTY_LIST;
    }
  },
  DATE(6) {
    @Override
    public String toString() {
      return "FECHA";
    }
  };

  private int key;

  ELesxPropertyType(int key) {
    this.key = key;
  }

  public int getValue() {
    return key;
  }
}
