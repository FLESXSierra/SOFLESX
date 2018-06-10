package lesx.xml.thread;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lesx.gui.message.LesxMessage;
import lesx.property.costumer.LesxListPropertiesXMLParser;
import lesx.property.price.LesxListPriceXMLParser;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxCostumer;
import lesx.property.properties.LesxPrice;
import lesx.property.properties.LesxReportItem;
import lesx.property.properties.LesxReportTree;
import lesx.property.report.LesxListReportItemXMLParser;
import lesx.property.report.LesxListReportTreeXMLParser;
import lesx.property.utils.LesxPropertyUtils;
import lesx.utils.LesxString;

public class LesxXMLSaveData extends Service<Boolean> {

  private final static Logger LOGGER = Logger.getLogger(LesxXMLSaveData.class.getName());

  private List<LesxCostumer> costumers = new ArrayList<>();
  private List<LesxPrice> price = new ArrayList<>();
  private List<LesxReportTree> tree = new ArrayList<>();
  private List<LesxReportItem> items = new ArrayList<>();
  private String path = "";
  private ELesxUseCase useCase;

  public LesxXMLSaveData(Collection<? extends LesxComponent> costumer, ELesxUseCase useCase) {
    this.useCase = useCase;
    try {
      switch (useCase) {
        case UC_XML_COSTOMER:
          costumers.clear();
          costumers.addAll(costumer.stream()
              .map(LesxCostumer.class::cast)
              .collect(Collectors.toList()));
          path = LesxString.XML_PATH;
          break;
        case UC_XML_PRICE:
          price.clear();
          price.addAll(costumer.stream()
              .map(LesxPrice.class::cast)
              .collect(Collectors.toList()));
          path = LesxString.XML_PRICE_PATH;
          break;
        case UC_XML_REPORT_TREE:
          tree.clear();
          tree.addAll(costumer.stream()
              .map(LesxReportTree.class::cast)
              .collect(Collectors.toList()));
          path = LesxString.XML_REPORT_TREE_PATH;
          break;
        case UC_XML_REPORT_ITEMS:
          items.clear();
          items.addAll(costumer.stream()
              .map(LesxReportItem.class::cast)
              .collect(Collectors.toList()));
          path = LesxString.XML_REPORT_ITEMS_PATH;
          break;
        default:
          new Throwable(new IllegalArgumentException("Use case not supported"));
          break;
      }
    }
    catch (ClassCastException ce) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-CAST_XML_DATA_SAVE", costumer.getClass(), useCase), ce);
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-SAVE_XML_DATA", useCase), e);
      e.printStackTrace();
    }

  }

  @Override
  protected Task<Boolean> createTask() {
    return new Task<Boolean>() {
      @Override
      protected Boolean call() throws JAXBException, IOException {
        boolean success = false;
        File xmlFile = new File(path);
        if (xmlFile.exists()) {
          JAXBContext context;
          Unmarshaller jaxbUnmarshaller;
          JAXBContext jaxbContext;
          Marshaller jaxbMarshaller;
          try {
            switch (useCase) {
              case UC_XML_COSTOMER:
                context = JAXBContext.newInstance(LesxListPropertiesXMLParser.class);
                jaxbUnmarshaller = context.createUnmarshaller();
                //convert to desired object
                final LesxListPropertiesXMLParser properties = (LesxListPropertiesXMLParser) jaxbUnmarshaller.unmarshal(xmlFile);
                properties.setCostumer(LesxPropertyUtils.convertLesxCostumerIntoXMLParser(costumers));
                //save data
                jaxbContext = JAXBContext.newInstance(LesxListPropertiesXMLParser.class);
                jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.marshal(properties, xmlFile);
                success = true;
                break;
              case UC_XML_PRICE:
                context = JAXBContext.newInstance(LesxListPriceXMLParser.class);
                jaxbUnmarshaller = context.createUnmarshaller();
                //convert to desired object
                final LesxListPriceXMLParser propertiesPrice = (LesxListPriceXMLParser) jaxbUnmarshaller.unmarshal(xmlFile);
                propertiesPrice.setPrices(LesxPropertyUtils.convertLesxPriceIntoXMLParser(price));
                //save data
                jaxbContext = JAXBContext.newInstance(LesxListPriceXMLParser.class);
                jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.marshal(propertiesPrice, xmlFile);
                success = true;
                break;
              case UC_XML_REPORT_ITEMS:
                context = JAXBContext.newInstance(LesxListReportItemXMLParser.class);
                jaxbUnmarshaller = context.createUnmarshaller();
                //convert to desired object
                final LesxListReportItemXMLParser propertiesItems = (LesxListReportItemXMLParser) jaxbUnmarshaller.unmarshal(xmlFile);
                propertiesItems.setItems(LesxPropertyUtils.convertLesxReportItemsIntoXMLParser(items));
                //save data
                jaxbContext = JAXBContext.newInstance(LesxListReportItemXMLParser.class);
                jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.marshal(propertiesItems, xmlFile);
                break;
              case UC_XML_REPORT_TREE:
                context = JAXBContext.newInstance(LesxListReportTreeXMLParser.class);
                jaxbUnmarshaller = context.createUnmarshaller();
                //convert to desired object
                final LesxListReportTreeXMLParser propertiesTree = (LesxListReportTreeXMLParser) jaxbUnmarshaller.unmarshal(xmlFile);
                propertiesTree.setTree(LesxPropertyUtils.convertLesxReportTreeIntoXMLParser(tree));
                //save data
                jaxbContext = JAXBContext.newInstance(LesxListReportTreeXMLParser.class);
                jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.marshal(propertiesTree, xmlFile);
                break;
              default:
                break;
            }
          }
          catch (Exception e) {
            LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-SAVE_XML_DATA", useCase), e);
            e.printStackTrace();
          }
        }
        return success;
      }

    };
  }

}
