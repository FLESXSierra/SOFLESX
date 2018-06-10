package lesx.property.report;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lesx.utils.LesxString;

@XmlRootElement(name = LesxString.ROOT_REPORT_TREE_XML)
@XmlAccessorType(XmlAccessType.FIELD)
public class LesxListReportTreeXMLParser {

  @XmlElement(name = LesxString.ELEMENT_XML_REPORT_TREE)
  private List<LesxReportTreeXMLParser> tree;

  public List<LesxReportTreeXMLParser> getTree() {
    return tree;
  }

  public void setTree(List<LesxReportTreeXMLParser> tree) {
    this.tree = tree;
  }

}
