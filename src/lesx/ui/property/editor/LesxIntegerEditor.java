package lesx.ui.property.editor;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lesx.property.properties.LesxProperty;
import lesx.ui.property.editor.formatter.LesxNumberFormatter;

public class LesxIntegerEditor extends LesxTextEditor {
  private BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);

  public LesxIntegerEditor(LesxProperty fxProperty) {
    super(fxProperty);// TODO Triggering peding changes
    LesxNumberFormatter format = new LesxNumberFormatter();
    setTextFormatter(format.getFormat());
  }

  public BooleanProperty getValid() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid.set(valid);
  }

}
