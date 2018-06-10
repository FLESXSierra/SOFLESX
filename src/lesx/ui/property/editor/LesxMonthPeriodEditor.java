package lesx.ui.property.editor;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import lesx.ui.property.editor.skin.LesxMonthPeriodSkin;
import lesx.utils.LesxMisc;

public class LesxMonthPeriodEditor extends TextField {

  private BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);
  private BooleanProperty reset = new SimpleBooleanProperty(this, "reset", true);
  private BooleanProperty refresh = new SimpleBooleanProperty(this, "refresh", true);
  private StringProperty period = new SimpleStringProperty(this, "period", "");

  public LesxMonthPeriodEditor() {
    //Nothing
  }

  public LesxMonthPeriodEditor(String initialPeriod) {
    if (!LesxMisc.isEmpty(initialPeriod)) {
      period.setValue(initialPeriod);
    }
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new LesxMonthPeriodSkin(this);
  }

  public void setValid(boolean valid) {
    this.valid.set(valid);
  }

  public boolean getValid() {
    return valid.get();
  }

  public BooleanProperty validProperty() {
    return valid;
  }

  public String getValue() {
    return period.get();
  }

  public StringProperty valueProperty() {
    return period;
  }

  public void setValue(String value) {
    period.set(value);
  }

  public void setValueAndRefresh(String value) {
    period.set(value);
    refresh.set(!refresh.get());
  }

  public void reset() {
    reset.set(!reset.get());
  }

  public BooleanProperty resetProperty() {
    return reset;
  }

  public BooleanProperty refreshProperty() {
    return refresh;
  }

}
