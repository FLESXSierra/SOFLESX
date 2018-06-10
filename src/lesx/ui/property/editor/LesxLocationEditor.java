package lesx.ui.property.editor;

import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import lesx.property.properties.ELesxLocations;
import lesx.property.properties.ELesxPropertyType;
import lesx.property.properties.LesxProperty;
import lesx.property.utils.LesxPropertyUtils;
import lesx.utils.LesxMisc;

public class LesxLocationEditor extends ComboBox<String> {

  private ObservableList<String> values = FXCollections.observableArrayList();
  private FilteredList<String> filteredValues;
  private BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);
  private ELesxPropertyType type;
  private ObjectProperty<Object> value;
  private boolean mandatory;
  private String text;

  public LesxLocationEditor(LesxProperty fxProperty) {
    values.setAll(ELesxLocations.getValues()
        .stream()
        .map(s -> s.toString())
        .collect(Collectors.toList()));
    if (fxProperty.getValue() != null) {
      getEditor().setText((ELesxLocations.getLocation(Long.valueOf(fxProperty.getValue()
          .toString()))
          .toString()));
    }
    mandatory = fxProperty.isMandatory();
    type = fxProperty.getType();
    value = fxProperty.getPropertyValue();
    setEditable(true);
    getEditor().textProperty()
        .addListener(obs -> valueChanged());
    filteredValues = new FilteredList<String>(values, p -> true);
    filterList();
    setItems(filteredValues);
    valid.bindBidirectional(fxProperty.validProperty());
    valid.addListener(obs -> changeStyle());
    setDisable(fxProperty.isReadOnly());
  }

  private void valueChanged() {
    text = getEditor().getText();
    filterList();
    Long temp = ELesxLocations.getLocationKeyByName(getEditor().getText());
    if (isValid() && !LesxMisc.equals(temp, -1L)) {// TODO Is not triggering value listener
      LesxPropertyUtils.copyValueToTypeProperty(type, temp, value);
    }
    else {
      LesxPropertyUtils.copyValueToTypeProperty(type, null, value);
    }
  }

  private void changeStyle() {
    getEditor().setStyle(isValid() ? "-fx-text-fill: -fx-text-inner-color;" : "-fx-text-fill: red;");
  }

  private void filterList() {
    validate();
    Platform.runLater(() -> {
      if (LesxMisc.isEmpty(text)) {
        filteredValues.setPredicate(s -> true);
      }
      else {
        filteredValues.setPredicate(s -> s.toLowerCase()
            .startsWith(text.toLowerCase()));
      }
    });
  }

  protected void validate() {
    if (filteredValues.contains(text)) {
      valid.set(true);
    }
    else if (LesxMisc.equals(values.size(), filteredValues.size()) && LesxMisc.isEmpty(getEditor().getText()) && !mandatory) {
      valid.set(true);
    }
    else {
      valid.set(false);
    }
  }

  public void setValid(boolean valid) {
    this.valid.set(valid);
  }

  public boolean isValid() {
    return valid.get();
  }

  public BooleanProperty validProperty() {
    return valid;
  }

  public String getStringValue() {
    return isValid() ? getValue() : "";
  }

}
