package lesx.property.report;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lesx.property.properties.LesxReportItem;
import lesx.utils.LesxString;

@XmlAccessorType(XmlAccessType.FIELD)
public class LesxReportItemsXMLParser {

  @XmlAttribute(name = LesxString.ATTR_XML_ID)
  private Long id;
  @XmlElement(name = LesxString.ATTR_XML_RARENT_ID)
  private Long parent_id;
  @XmlElement(name = LesxString.ATTR_XML_COSTUMERS)
  private List<Long> costumers;
  @XmlElement(name = LesxString.ATTR_XML_FUNCTIONS)
  private LesxFunctionXMLParser function;

  public LesxReportItemsXMLParser() {

  }

  public LesxReportItemsXMLParser(LesxReportItem report) {
    this.id = report.getId();
    this.parent_id = report.getParent_id();
    this.costumers = report.getCostumer();
    this.function = new LesxFunctionXMLParser(report.getFunction());
    // TODO Add properties whenever is added new.
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getParent_id() {
    return parent_id;
  }

  public void setParent_id(Long parent_id) {
    this.parent_id = parent_id;
  }

  public List<Long> getCostumers() {
    return costumers;
  }

  public void setCostumers(List<Long> costumers) {
    this.costumers = costumers;
  }

  public void setFunction(LesxFunctionXMLParser function) {
    this.function = function;
  }

  public LesxFunctionXMLParser getFunction() {
    return function;
  }

}
