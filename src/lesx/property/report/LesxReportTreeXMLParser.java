package lesx.property.report;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lesx.property.properties.LesxReportTree;
import lesx.utils.LesxString;

@XmlAccessorType(XmlAccessType.FIELD)
public class LesxReportTreeXMLParser {

  @XmlAttribute(name = LesxString.ATTR_XML_ID)
  private Long id;
  @XmlElement(name = LesxString.ATTR_XML_NAME)
  private String name;
  @XmlElement(name = LesxString.ATTR_XML_RARENT_ID)
  private Long parent_id;
  @XmlElement(name = LesxString.ATTR_XML_COSTUMERS)
  private Set<Long> costumers;

  public LesxReportTreeXMLParser() {

  }

  public LesxReportTreeXMLParser(LesxReportTree tree) {
    this.id = tree.getId();
    this.name = tree.getName();
    this.costumers = tree.getCostumers();
    this.parent_id = tree.getParent_id();// TODO Add properties whenever is added new.
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

  public Long getParent_id() {
    return parent_id;
  }

  public void setParent_id(Long parent_id) {
    this.parent_id = parent_id;
  }

  public void setCostumers(Set<Long> costumers) {
    this.costumers = costumers;
  }

  public Set<Long> getCostumers() {
    return costumers;
  }

}
