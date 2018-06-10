package lesx.xml.thread;

import static lesx.property.properties.ELesxUseCase.UC_XML_PRICE;
import static lesx.property.properties.ELesxUseCase.UC_XML_REPORT_ITEMS;
import static lesx.property.properties.ELesxUseCase.UC_XML_REPORT_TREE;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.concurrent.WorkerStateEvent;
import javafx.util.Pair;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;
import main.LesxMain;

public class LesxXMLUtils {

  private final static Logger LOGGER = Logger.getLogger(LesxXMLUtils.class.getName());

  public static void importXMLFileToLesxProperty(Consumer<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> consumer, ELesxUseCase useCase) {
    LesxXMLRead read = new LesxXMLRead(useCase);
    read.start();
    read.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, obs -> consumer.accept(read.getValue()));
  }

  public static void importPriceXMLFileToLesxProperty(Runnable loadDialog) {
    final Consumer<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> consumer = obs -> {
      if (obs.getKey()) {
        LesxMain.getInstance()
            .getDbProperty()
            .addDataMap(obs.getValue());
        loadDialog.run();
      }
      else {
        LesxXMLUtils.writeNewXML(available -> {
          if (available.getKey()) {
            LesxMain.getInstance()
                .getDbProperty()
                .addDataMap(available.getValue());
            loadDialog.run();
          }
        }, UC_XML_PRICE);
      }
    };
    importXMLFileToLesxProperty(consumer, UC_XML_PRICE);
  }

  public static void importReportsXMLFileToLesxProperty(Runnable loadDialog) {
    if (!LesxMain.getInstance()
        .getDbProperty()
        .isPriceLoaded()) {
      importPriceXMLFileToLesxProperty(() -> {
      });
    }
    importRerpotItemsXMLFileToLesxProperty(loadDialog);
  }

  public static void importRerpotItemsXMLFileToLesxProperty(Runnable loadDialog) {
    final Consumer<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> consumer = obs -> {
      if (obs.getKey()) {
        LesxMain.getInstance()
            .getDbProperty()
            .addDataMap(obs.getValue());
        importRerpotElementsXMLFileToLesxProperty(loadDialog);
      }
      else {
        LesxXMLUtils.writeNewXML(available -> {
          if (available.getKey()) {
            LesxMain.getInstance()
                .getDbProperty()
                .addDataMap(available.getValue());
            importRerpotElementsXMLFileToLesxProperty(loadDialog);
          }
        }, UC_XML_REPORT_TREE);
      }
    };
    importXMLFileToLesxProperty(consumer, UC_XML_REPORT_TREE);

  }

  private static void importRerpotElementsXMLFileToLesxProperty(Runnable loadDialog) {
    final Consumer<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> consumer = obs -> {
      if (obs.getKey()) {
        LesxMain.getInstance()
            .getDbProperty()
            .addDataMap(obs.getValue());
        loadDialog.run();
      }
      else {
        LesxXMLUtils.writeNewXML(available -> {
          if (available.getKey()) {
            LesxMain.getInstance()
                .getDbProperty()
                .addDataMap(available.getValue());
            loadDialog.run();
          }
        }, UC_XML_REPORT_ITEMS);
      }
    };
    importXMLFileToLesxProperty(consumer, UC_XML_REPORT_ITEMS);
  }

  public static void writeNewXML(Consumer<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> consumer, ELesxUseCase useCase) {
    LesxXMLWriteNewFile write = new LesxXMLWriteNewFile(useCase);
    write.addEventFilter(WorkerStateEvent.WORKER_STATE_SUCCEEDED, obs -> consumer.accept(write.getValue()));
    write.start();
  }

  /**
   * Reads file XML content
   *
   * @param xmlPath
   * @return the XML converted
   */
  public static String getXMLContentToString(String xmlPath) {
    FileReader fr = null;
    char[] buffer = new char[1024];
    StringBuffer fileContent = new StringBuffer();
    try {
      fr = new FileReader(xmlPath);
      while (fr.read(buffer) != -1) {
        fileContent.append(new String(buffer));
      }
    }
    catch (FileNotFoundException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_READER", xmlPath), e);
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_READER", xmlPath), e);
    }
    finally {
      if (fr != null) {
        try {
          fr.close();
        }
        catch (IOException e) {
          LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_READER", xmlPath), e);
        }
      }
    }
    return fileContent.toString();
  }

}
