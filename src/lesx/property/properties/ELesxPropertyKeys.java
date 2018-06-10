package lesx.property.properties;

import lesx.utils.LesxString;

public enum ELesxPropertyKeys {
  COSTUMER(0L) {
    @Override
    public String toString() {
      return LesxString.ELEMENT_XML_COSTUMER;
    }
  },
  PRICE(1L) {
    @Override
    public String toString() {
      return LesxString.ELEMENT_XML_PRICE;
    }
  },
  REPORT_TREE(2L) {
    @Override
    public String toString() {
      return LesxString.ELEMENT_XML_REPORT_TREE;
    }
  },
  REPORT_ELEMENT(3L) {
    @Override
    public String toString() {
      return LesxString.ELEMENT_XML_REPORT_ELEMENT;
    }
  };

  private Long key;

  ELesxPropertyKeys(Long key) {
    this.key = key;
  }

  public Long getValue() {
    return key;
  }

}
