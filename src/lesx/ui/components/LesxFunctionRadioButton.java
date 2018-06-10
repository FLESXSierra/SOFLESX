package lesx.ui.components;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxFunction;
import lesx.property.properties.LesxReportFunction;
import lesx.ui.property.editor.LesxMonthPeriodEditor;

public class LesxFunctionRadioButton extends VBox {

  private final static Logger LOGGER = Logger.getLogger(LesxFunctionRadioButton.class.getName());

  // Components
  private Label functionLabel = new Label();
  private RadioButton sum = new RadioButton();
  private RadioButton period = new RadioButton();
  private RadioButton endMonth = new RadioButton();
  private ToggleGroup toggle = new ToggleGroup();
  private LesxMonthPeriodEditor datePicker = new LesxMonthPeriodEditor();
  private HBox hbox = new HBox();
  private Separator separator = new Separator(Orientation.HORIZONTAL);
  // Data
  private LesxReportFunction function = new LesxReportFunction();
  private final InvalidationListener listener;
  // Flags

  public LesxFunctionRadioButton() {
    listener = new InvalidationListener() {
      @Override
      public void invalidated(Observable observable) {
        if (period.isSelected()) {
          function.setValue(datePicker.getValue());
        }
      }
    };
    functionLabel.setText(LesxMessage.getMessage("TEXT-FUNCTION_LABEL"));
    sum.setToggleGroup(toggle);
    sum.setText(LesxMessage.getMessage("TEXT-FUNCTION_SUM"));
    sum.selectedProperty()
        .addListener((obs, oldV, newV) -> {
          if (newV) {
            function.setType(ELesxFunction.SUM);
            function.setValue("");
          }
        });
    period.setToggleGroup(toggle);
    period.setText(LesxMessage.getMessage("TEXT-FUNCTION_PERIOD"));
    period.selectedProperty()
        .addListener((obs, oldV, newV) -> {
          if (newV) {
            function.setType(ELesxFunction.PERIOD);
            datePicker.setVisible(true);
            datePicker.valueProperty()
                .addListener(listener);
          }
          else {
            datePicker.setVisible(false);
            datePicker.valueProperty()
                .removeListener(listener);
          }
        });
    period.setTooltip(generateToolTip());
    endMonth.setToggleGroup(toggle);
    endMonth.setText(LesxMessage.getMessage("TEXT-FUNCTION_END_MONTH"));
    endMonth.selectedProperty()
        .addListener((obs, oldV, newV) -> {
          if (newV) {
            function.setType(ELesxFunction.END_MONTH);
            function.setValue("");
          }
        });
    datePicker.valueProperty()
        .addListener(listener);
    datePicker.setVisible(false);
    hbox.getChildren()
        .addAll(period, datePicker);
    hbox.setSpacing(5);
    HBox.setHgrow(datePicker, Priority.ALWAYS);
    getChildren().addAll(functionLabel, separator, sum, endMonth, hbox);
  }

  /**
   * Creates a ToolTip to improve the user experience, telling why the given date is invalid
   *
   * @return a ToolTip
   */
  private Tooltip generateToolTip() {
    Tooltip tool = new Tooltip();
    StringBuilder text = new StringBuilder();
    text.append("Una fecha puede ser invalida por:\n")
        .append(" - Fecha incompleta.\n")
        .append(" - Fecha inicial es posterior a la final.");
    tool.setText(text.toString());
    return tool;
  }

  public LesxReportFunction getFunction() {
    return function.clone();
  }

  public void loadFunction(LesxReportFunction initialFunction) {
    reset();
    if (initialFunction != null) {
      function = initialFunction.clone();
      switch (initialFunction.getType()) {
        case END_MONTH:
          endMonth.setSelected(true);
          break;
        case PERIOD:
          period.setSelected(true);
          datePicker.setValueAndRefresh(initialFunction.getValue()
              .toString());
          break;
        case SUM:
          sum.setSelected(true);
          break;
        default:
          function.reset();
          LOGGER.log(Level.WARNING, LesxMessage.getMessage("ERROR-USE_CASE_NOT_SUPPORTED", initialFunction.getType()
              .toString()));
          break;
      }
    }
    else {
      function.reset();
    }
  }

  private void reset() {
    sum.setSelected(false);
    period.setSelected(false);
    endMonth.setSelected(false);
    datePicker.reset();
  }

}
