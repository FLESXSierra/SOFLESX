package lesx.property.report;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lesx.utils.LesxString;

@XmlRootElement(name = LesxString.ROOT_REPORT_ITEM_ELEMENT_XML)
@XmlAccessorType(XmlAccessType.FIELD)
public class LesxListReportItemXMLParser {
  @XmlElement(name = LesxString.ELEMENT_XML_REPORT_ELEMENT)
  private List<LesxReportItemsXMLParser> items;

  public List<LesxReportItemsXMLParser> getItems() {
    return items;
  }

  public void setItems(List<LesxReportItemsXMLParser> items) {
    this.items = items;
  }
}
