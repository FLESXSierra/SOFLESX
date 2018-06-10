package lesx.property.price;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lesx.utils.LesxString;

@XmlRootElement(name = LesxString.ROOT_PRICE_ELEMENT_XML)
@XmlAccessorType(XmlAccessType.FIELD)
public class LesxListPriceXMLParser {

  @XmlElement(name = LesxString.ELEMENT_XML_PRICE)
  private List<LesxPriceXMLParser> prices;

  public List<LesxPriceXMLParser> getPrices() {
    return prices;
  }

  public void setPrices(List<LesxPriceXMLParser> prices) {
    this.prices = prices;
  }

}
