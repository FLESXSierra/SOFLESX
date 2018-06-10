package main;

import java.util.Map;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Pair;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;
import lesx.scene.controller.LesxController;
import lesx.utils.LesxString;
import lesx.xml.thread.LesxXMLUtils;

public class LesxControllerMain extends LesxController {
  @FXML
  Label header;
  @FXML
  Label loadText;
  @FXML
  ProgressIndicator progress;

  public LesxControllerMain() {
  }

  @FXML
  public void initialize() {
    setTitle(LesxMessage.getMessage("TEXT-TITLE_READING_XML"));
    final Consumer<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> consumer = obs -> {
      if (obs.getKey()) {
        LesxMain.getInstance()
            .getDbProperty()
            .setDataMap(obs.getValue());
        LesxMain.getInstance()
            .activateScene(LesxString.MAINPAGE_PATH);
      }
      else {
        LesxXMLUtils.writeNewXML(available -> {
          if (available.getKey()) {
            LesxMain.getInstance()
                .getDbProperty()
                .setDataMap(available.getValue());
            LesxMain.getInstance()
                .activateScene(LesxString.MAINPAGE_PATH);
          }
          else {
            loadText.setText(LesxMessage.getMessage("TEXT-LOADING_ERROR_TITLE"));
          }
        }, ELesxUseCase.UC_XML_COSTOMER);
      }
    };
    header.setText(LesxMessage.getMessage("TEXT-HEADER_READING_XML"));
    loadText.setText(LesxMessage.getMessage("TEXT-LOADING_TITLE"));
    LesxXMLUtils.importXMLFileToLesxProperty(consumer, ELesxUseCase.UC_XML_COSTOMER);
  }

}
