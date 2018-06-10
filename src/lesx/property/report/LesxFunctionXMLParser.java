package lesx.property.report;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import lesx.property.properties.ELesxFunction;
import lesx.property.properties.LesxReportFunction;
import lesx.utils.LesxString;

@XmlAccessorType(XmlAccessType.FIELD)
public class LesxFunctionXMLParser {

  @XmlAttribute(name = LesxString.ATTR_XML_TYPE)
  private ELesxFunction type;
  @XmlAttribute(name = LesxString.ATTR_XML_ID)
  private String value;

  public LesxFunctionXMLParser() {

  }

  public LesxFunctionXMLParser(LesxReportFunction function) {
    type = function.getType();
    value = function.getValue()
        .toString();
  }

  public ELesxFunction getType() {
    return type;
  }

  public void setType(ELesxFunction type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
