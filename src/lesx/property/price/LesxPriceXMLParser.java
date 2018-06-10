package lesx.property.price;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lesx.property.properties.LesxPrice;
import lesx.utils.LesxString;

@XmlAccessorType(XmlAccessType.FIELD)
public class LesxPriceXMLParser {

  @XmlAttribute(name = LesxString.ATTR_XML_ID)
  private Long id;
  @XmlElement(name = LesxString.ATTR_XML_NAME)
  private String name;
  @XmlElement(name = LesxString.ATTR_XML_RESOURCE_ID)
  private Long resource_id;
  @XmlElement(name = LesxString.ATTR_XML_TOTAL)
  private Long total;
  @XmlElement(name = LesxString.ATTR_XML_TYPE)
  private Boolean typePrice;
  @XmlElement(name = LesxString.ATTR_XML_VALID_FROM)
  private String validFrom;

  public LesxPriceXMLParser() {

  }

  public LesxPriceXMLParser(LesxPrice price) {
    this.id = price.getId();
    this.name = price.getName();
    this.resource_id = price.getResource_id();
    this.total = price.getTotal();
    this.typePrice = price.getTypePrice();
    this.validFrom = price.getValidFrom();
    // TODO Add properties whenever is added new.
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getResource_id() {
    return resource_id;
  }

  public void setResource_id(Long resource_id) {
    this.resource_id = resource_id;
  }

  public Long getTotal() {
    return total;
  }

  public void setTotal(Long total) {
    this.total = total;
  }

  public void setTypePrice(Boolean typePrice) {
    this.typePrice = typePrice;
  }

  public Boolean getTypePrice() {
    return typePrice;
  }

  public String getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(String validFrom) {
    this.validFrom = validFrom;
  }

}
