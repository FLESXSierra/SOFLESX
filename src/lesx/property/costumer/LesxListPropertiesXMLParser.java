package lesx.property.costumer;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lesx.utils.LesxString;

@XmlRootElement(name = LesxString.ROOT_ELEMENT_XML)
@XmlAccessorType(XmlAccessType.FIELD)
public class LesxListPropertiesXMLParser {

  @XmlElement(name = LesxString.ELEMENT_XML_COSTUMER)
  private List<LesxCostumerXMLParser> costumer;

  public List<LesxCostumerXMLParser> getCostumer() {
    return costumer;
  }

  public void setCostumer(List<LesxCostumerXMLParser> costumer) {
    this.costumer = costumer;
  }

}
