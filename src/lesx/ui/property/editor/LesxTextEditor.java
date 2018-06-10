package lesx.ui.property.editor;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import lesx.property.properties.ELesxPropertyType;
import lesx.property.properties.LesxProperty;
import lesx.ui.property.editor.skin.LesxPropertyTextEditorSkin;

public class LesxTextEditor extends TextField {

  private BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);
  private ObjectProperty<Object> value;
  private ELesxPropertyType type;
  private boolean isReadOnly;

  public LesxTextEditor(LesxProperty fxProperty) {
    if (fxProperty.getValue() != null) {
      setText(fxProperty.getValue()
          .toString());
    }
    value = fxProperty.getPropertyValue();
    isReadOnly = fxProperty.isReadOnly();
    type = fxProperty.getType();
    valid.bind(fxProperty.validProperty());
  }

  public BooleanProperty validProperty() {
    return valid;
  }

  public void setValid(BooleanProperty valid) {
    this.valid = valid;
  }

  public boolean isValid() {
    return valid.get();
  }

  public boolean isReadOnly() {
    return isReadOnly;
  }

  public ELesxPropertyType getType() {
    return type;
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new LesxPropertyTextEditorSkin(this);
  }

  public ObjectProperty<Object> valueProperty() {
    return value;
  }

}
