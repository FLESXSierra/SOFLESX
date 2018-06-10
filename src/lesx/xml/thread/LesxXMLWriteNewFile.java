package lesx.xml.thread;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.google.common.collect.Sets;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Pair;
import lesx.gui.message.LesxMessage;
import lesx.property.costumer.LesxCostumerXMLParser;
import lesx.property.costumer.LesxListPropertiesXMLParser;
import lesx.property.price.LesxListPriceXMLParser;
import lesx.property.price.LesxPriceXMLParser;
import lesx.property.properties.ELesxFunction;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;
import lesx.property.report.LesxFunctionXMLParser;
import lesx.property.report.LesxListReportItemXMLParser;
import lesx.property.report.LesxListReportTreeXMLParser;
import lesx.property.report.LesxReportItemsXMLParser;
import lesx.property.report.LesxReportTreeXMLParser;
import lesx.property.utils.LesxPropertyUtils;
import lesx.utils.LesxString;

public class LesxXMLWriteNewFile extends Service<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> {

  private final static Logger LOGGER = Logger.getLogger(LesxXMLWriteNewFile.class.getName());

  private String path;
  private Boolean available;
  private ELesxUseCase useCase;

  public LesxXMLWriteNewFile(ELesxUseCase useCase) {
    this.useCase = useCase;
    switch (useCase) {
      case UC_XML_COSTOMER:
        path = LesxString.XML_PATH;
        break;
      case UC_XML_PRICE:
        path = LesxString.XML_PRICE_PATH;
        break;
      case UC_XML_REPORT_TREE:
        path = LesxString.XML_REPORT_TREE_PATH;
        break;
      case UC_XML_REPORT_ITEMS:
        path = LesxString.XML_REPORT_ITEMS_PATH;
        break;
      default:
        new Throwable(new IllegalArgumentException(LesxMessage.getMessage("ERROR-USE_CASE_NOT_SUPPORTED", useCase)));
        break;
    }
  }

  @Override
  protected Task<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> createTask() {
    return new Task<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>>() {

      @Override
      protected Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>> call() throws Exception {
        available = true;
        Map<Long, Map<Long, ? extends LesxComponent>> resultMap = new HashMap<>();
        try {
          //CREATING XML WITH ROOT NODE
          DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder dBuilder = dbfactory.newDocumentBuilder();
          Document doc;
          doc = dBuilder.newDocument();
          TransformerFactory transformerFactory = TransformerFactory.newInstance();
          Transformer transformer = transformerFactory.newTransformer();
          DOMSource source = new DOMSource(doc);
          StreamResult result = new StreamResult(new File(path));
          transformer.transform(source, result);

          //ADDING THE "NEW" INFO (DEMO)
          resultMap = createDefaultMap();
          LOGGER.log(Level.INFO, LesxMessage.getMessage("INFO-XML_WRITER_COMPLETE"));
        }
        catch (ParserConfigurationException pce) {
          available = false;
          LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_WRITER", path), pce);
          pce.printStackTrace();
          return new Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>(available, resultMap);
        }
        catch (TransformerException e) {
          available = false;
          LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_WRITER", path), e);
          e.printStackTrace();
          return new Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>(available, resultMap);
        }
        catch (Exception ex) {
          available = false;
          LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_WRITER", path), ex);
          ex.printStackTrace();
          return new Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>(available, resultMap);
        }
        return new Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>(available, resultMap);
      }

    };
  }

  private Map<Long, Map<Long, ? extends LesxComponent>> createDefaultMap() throws Exception {
    JAXBContext jaxbContext;
    Marshaller jaxbMarshaller;
    Map<Long, Map<Long, ? extends LesxComponent>> resultMap = new HashMap<>();
    File file = new File(path);
    switch (useCase) {
      case UC_XML_COSTOMER:
        jaxbContext = JAXBContext.newInstance(LesxListPropertiesXMLParser.class);
        jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        LesxCostumerXMLParser costumerDemo = new LesxCostumerXMLParser();
        costumerDemo.setCc(0L);
        costumerDemo.setId(0L);
        costumerDemo.setName(LesxString.ATTR_XML_NAME);
        costumerDemo.setLocation(0L);
        LesxListPropertiesXMLParser list = new LesxListPropertiesXMLParser();
        list.setCostumer(Arrays.asList(costumerDemo));
        jaxbMarshaller.marshal(list, file);
        resultMap = LesxPropertyUtils.converXMLPropertyIntoLesxProperty(list, ELesxUseCase.UC_XML_COSTOMER);
        break;
      case UC_XML_PRICE:
        jaxbContext = JAXBContext.newInstance(LesxListPriceXMLParser.class);
        jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        LesxPriceXMLParser priceDemo = new LesxPriceXMLParser();
        priceDemo.setId(0L);
        priceDemo.setName(LesxString.ATTR_XML_PRICE);
        priceDemo.setResource_id(0L);
        priceDemo.setTotal(150000L);
        priceDemo.setValidFrom(LocalDate.now()
            .toString());
        priceDemo.setTypePrice(true);
        LesxListPriceXMLParser listPrice = new LesxListPriceXMLParser();
        listPrice.setPrices(Arrays.asList(priceDemo));
        jaxbMarshaller.marshal(listPrice, file);
        resultMap = LesxPropertyUtils.converXMLPropertyIntoLesxProperty(listPrice, ELesxUseCase.UC_XML_PRICE);
        break;
      case UC_XML_REPORT_TREE:
        jaxbContext = JAXBContext.newInstance(LesxListReportTreeXMLParser.class);
        jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        LesxReportTreeXMLParser reportTreeDemo = new LesxReportTreeXMLParser();
        reportTreeDemo.setId(0L);
        reportTreeDemo.setName(LesxString.ATTR_XML_TREE);
        reportTreeDemo.setParent_id(-1L);
        reportTreeDemo.setCostumers(Sets.newHashSet(0L));
        LesxListReportTreeXMLParser listTree = new LesxListReportTreeXMLParser();
        listTree.setTree(Arrays.asList(reportTreeDemo));
        jaxbMarshaller.marshal(listTree, file);
        resultMap = LesxPropertyUtils.converXMLPropertyIntoLesxProperty(listTree, ELesxUseCase.UC_XML_REPORT_TREE);
        break;
      case UC_XML_REPORT_ITEMS:
        jaxbContext = JAXBContext.newInstance(LesxListReportItemXMLParser.class);
        jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        LesxReportItemsXMLParser reportItemDemo = new LesxReportItemsXMLParser();
        reportItemDemo.setId(0L);
        reportItemDemo.setCostumers(Arrays.asList(0L));
        reportItemDemo.setParent_id(0L);
        LesxFunctionXMLParser newFunction = new LesxFunctionXMLParser();
        newFunction.setType(ELesxFunction.SUM);
        newFunction.setValue("");
        reportItemDemo.setFunction(newFunction);
        LesxListReportItemXMLParser listItem = new LesxListReportItemXMLParser();
        listItem.setItems(Arrays.asList(reportItemDemo));
        jaxbMarshaller.marshal(listItem, file);
        resultMap = LesxPropertyUtils.converXMLPropertyIntoLesxProperty(listItem, ELesxUseCase.UC_XML_REPORT_ITEMS);
        break;
      default:
        new Throwable(new IllegalArgumentException(LesxMessage.getMessage("ERROR-USE_CASE_NOT_SUPPORTED", useCase)));
        break;
    }
    return resultMap;
  }

}
