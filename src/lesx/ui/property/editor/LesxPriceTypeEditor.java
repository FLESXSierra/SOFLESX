package lesx.ui.property.editor;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Skin;
import lesx.property.properties.LesxProperty;
import lesx.ui.property.editor.skin.LesxPropertyTypePriceEditorSkin;

public class LesxPriceTypeEditor extends CheckBox {

  private BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);
  private ObjectProperty<Boolean> value = new SimpleObjectProperty<>(this, "value", null);

  public LesxPriceTypeEditor(LesxProperty fxProperty) {
    setAllowIndeterminate(true);
    valid.bind(fxProperty.validProperty());
    if (fxProperty.getValue() != null) {
      value.set(Boolean.valueOf(fxProperty.getValue()
          .toString()));
    }
    fxProperty.getPropertyValue()
        .bind(value);

  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new LesxPropertyTypePriceEditorSkin(this);
  }

  public BooleanProperty validProperty() {
    return valid;
  }

  public void setValue(Boolean value) {
    this.value.setValue(value);
  }

  public boolean isValid() {
    return valid.get();
  }
}
