package lesx.property.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.ObjectProperty;
import lesx.gui.message.LesxMessage;
import lesx.property.costumer.LesxCostumerXMLParser;
import lesx.property.costumer.LesxListPropertiesXMLParser;
import lesx.property.price.LesxListPriceXMLParser;
import lesx.property.price.LesxPriceXMLParser;
import lesx.property.properties.ELesxPropertyKeys;
import lesx.property.properties.ELesxPropertyType;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxCostumer;
import lesx.property.properties.LesxPrice;
import lesx.property.properties.LesxReportItem;
import lesx.property.properties.LesxReportTree;
import lesx.property.report.LesxListReportItemXMLParser;
import lesx.property.report.LesxListReportTreeXMLParser;
import lesx.property.report.LesxReportItemsXMLParser;
import lesx.property.report.LesxReportTreeXMLParser;
import lesx.utils.LesxMisc;

public class LesxPropertyUtils {

  private final static Logger LOGGER = Logger.getLogger(LesxPropertyUtils.class.getName());

  /**
   * Returns a Map containing all the information of the properties on the given LesxListPropertiesXMLParser
   *
   * @param list LesxListPropertiesXMLParser
   * @param useCase Needed to add the key into the map
   * @return Map < Long, Map < Long, ? extends LesxProperty > > not null.
   */
  public static <T> Map<Long, Map<Long, ? extends LesxComponent>> converXMLPropertyIntoLesxProperty(T list, ELesxUseCase useCase) {
    Map<Long, Map<Long, ? extends LesxComponent>> result = new HashMap<>();
    switch (useCase) {
      case UC_XML_COSTOMER:
        LesxListPropertiesXMLParser castList = (LesxListPropertiesXMLParser) list;
        if (list != null && !LesxMisc.isEmpty(castList.getCostumer())) {
          LesxCostumer costumer;
          Map<Long, LesxComponent> mapCostumer = new HashMap<>();
          //Adding the costumer Property
          for (LesxCostumerXMLParser costumerXML : castList.getCostumer()) {
            costumer = new LesxCostumer(costumerXML);
            //No a la key con location, ID!
            mapCostumer.put(costumer.getId(), costumer);
          }
          result.put(ELesxPropertyKeys.COSTUMER.getValue(), mapCostumer);
        }
        break;
      case UC_XML_PRICE:
        LesxListPriceXMLParser castPriceList = (LesxListPriceXMLParser) list;
        if (list != null && !LesxMisc.isEmpty(castPriceList.getPrices())) {
          LesxPrice price;
          Map<Long, LesxComponent> mapPrice = new HashMap<>();
          //Adding the costumer Property
          for (LesxPriceXMLParser priceXML : castPriceList.getPrices()) {
            price = new LesxPrice(priceXML);
            //No a la key con location, ID!
            mapPrice.put(price.getId(), price);
          }
          result.put(ELesxPropertyKeys.PRICE.getValue(), mapPrice);
        }
        break;
      case UC_XML_REPORT_TREE:
        LesxListReportTreeXMLParser castTreeList = (LesxListReportTreeXMLParser) list;
        if (list != null && !LesxMisc.isEmpty(castTreeList.getTree())) {
          LesxReportTree tree;
          Map<Long, LesxComponent> mapTree = new HashMap<>();
          //Adding the costumer Property
          for (LesxReportTreeXMLParser treeXML : castTreeList.getTree()) {
            tree = new LesxReportTree(treeXML);
            //No a la key con location, ID!
            mapTree.put(tree.getId(), tree);
          }
          result.put(ELesxPropertyKeys.REPORT_TREE.getValue(), mapTree);
        }
        break;
      case UC_XML_REPORT_ITEMS:
        LesxListReportItemXMLParser castItemList = (LesxListReportItemXMLParser) list;
        if (list != null && !LesxMisc.isEmpty(list)) {
          LesxReportItem item;
          Map<Long, LesxComponent> mapItem = new HashMap<>();
          //Adding the costumer Property
          for (LesxReportItemsXMLParser treeXML : castItemList.getItems()) {
            item = new LesxReportItem(treeXML);
            //No a la key con location, ID!
            mapItem.put(item.getId(), item);
          }
          result.put(ELesxPropertyKeys.REPORT_ELEMENT.getValue(), mapItem);
        }
        break;
      default:
        break;
    }
    return result;
  }

  public static void copyValueToTypeProperty(ELesxPropertyType type, Object value, ObjectProperty<Object> valueProperty) {
    try {
      switch (type) {
        case INTEGER:
          if (value != null) {
            valueProperty.set(Integer.valueOf(value.toString()));
          }
          else {
            valueProperty.set(null);
          }
          break;

        case LOCATION:
        case LONG:
          if (!LesxMisc.isEmpty(value)) {
            if (!value.toString()
                .endsWith(".")) {
              valueProperty.set(Long.valueOf(value.toString()));//TODO Issue with '.' not Committing value
            }
          }
          else {
            valueProperty.set(null);
          }
          break;

        case TEXT:
          if (value != null) {
            valueProperty.set(value.toString());
          }
          else {
            valueProperty.set(null);
          }
          break;

        default:
          break;
      }
    }
    catch (NumberFormatException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-PROPERTY_UTILS_PARSING", type, valueProperty.get()));
      e.printStackTrace();
    }
  }

  public static List<LesxCostumerXMLParser> convertLesxCostumerIntoXMLParser(List<LesxCostumer> costumers) {
    List<LesxCostumerXMLParser> properties = new ArrayList<>();
    for (LesxCostumer costumer : costumers) {
      properties.add(new LesxCostumerXMLParser(costumer));
    }
    return properties;
  }

  public static List<LesxPriceXMLParser> convertLesxPriceIntoXMLParser(List<LesxPrice> prices) {
    List<LesxPriceXMLParser> properties = new ArrayList<>();
    for (LesxPrice price : prices) {
      properties.add(new LesxPriceXMLParser(price));
    }
    return properties;
  }

  public static List<LesxReportItemsXMLParser> convertLesxReportItemsIntoXMLParser(List<LesxReportItem> items) {
    List<LesxReportItemsXMLParser> properties = new ArrayList<>();
    for (LesxReportItem item : items) {
      properties.add(new LesxReportItemsXMLParser(item));
    }
    return properties;
  }

  public static List<LesxReportTreeXMLParser> convertLesxReportTreeIntoXMLParser(List<LesxReportTree> trees) {
    List<LesxReportTreeXMLParser> properties = new ArrayList<>();
    for (LesxReportTree tree : trees) {
      properties.add(new LesxReportTreeXMLParser(tree));
    }
    return properties;
  }

}
