package lesx.property.costumer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lesx.property.properties.LesxCostumer;
import lesx.utils.LesxString;

@XmlAccessorType(XmlAccessType.FIELD)
public class LesxCostumerXMLParser {

  @XmlAttribute(name = LesxString.ATTR_XML_ID)
  private Long id;
  @XmlElement(name = LesxString.ATTR_XML_NAME)
  private String name;
  @XmlElement(name = LesxString.ATTR_XML_CC)
  private Long cc;
  @XmlElement(name = LesxString.ATTR_XML_LOCATION)
  private Long location;

  public LesxCostumerXMLParser() {

  }

  public LesxCostumerXMLParser(LesxCostumer costumer) {
    this.id = costumer.getId();
    this.name = costumer.getName();
    this.cc = costumer.getCc();
    this.location = costumer.getLocation();// TODO Add properties whenever is added new.
  }

  public Long getLocation() {
    return location;
  }

  public void setLocation(Long location) {
    this.location = location;
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

  public Long getCc() {
    return cc;
  }

  public void setCc(Long cc) {
    this.cc = cc;
  }
}
