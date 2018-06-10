package lesx.ui.property.label;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;

public class LesxPropertyLabel extends Label {
  private BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);

  public LesxPropertyLabel() {
    installListeners();
  }

  private void installListeners() {
    valid.addListener(obs -> validChanged());
  }

  public LesxPropertyLabel(String text) {
    installListeners();
    setText(text);
  }

  private void validChanged() {
    setStyle(valid.get() ? "-fx-text-fill: -fx-text-inner-color;" : "-fx-text-fill: red;");
  }

  public BooleanProperty getValidProperty() {
    return valid;
  }

}
