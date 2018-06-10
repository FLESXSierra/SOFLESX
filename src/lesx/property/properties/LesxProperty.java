package lesx.property.properties;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import lesx.gui.message.LesxMessage;

public class LesxProperty implements Cloneable {

  private final static Logger LOGGER = Logger.getLogger(LesxProperty.class.getName());

  private String name;
  private ObjectProperty<Object> value;
  private ELesxPropertyType type;
  private boolean mandatory;
  private boolean readOnly;
  private BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);
  private boolean unique;
  private boolean visible = true;

  public boolean isReadOnly() {
    return readOnly;
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public LesxProperty() {
    value = new SimpleObjectProperty<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

  public void setValue(Object value) {
    this.value.set(value);
  }

  public Object getValue() {
    return value.get();
  }

  public ObjectProperty<Object> getPropertyValue() {
    return value;
  }

  public ELesxPropertyType getType() {
    return type;
  }

  public void setType(ELesxPropertyType type) {
    this.type = type;
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

  public boolean isUnique() {
    return unique;
  }

  public void setUnique(boolean unique) {
    this.unique = unique;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  @Override
  protected LesxProperty clone() {
    try {
      LesxProperty clone = (LesxProperty) super.clone();
      clone.value = new SimpleObjectProperty<>(this.getValue());
      return clone;
    }
    catch (CloneNotSupportedException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-PROPERTY_NOT_CLONNABLE", this), e);
      e.printStackTrace();
    }
    return new LesxProperty();
  }

}
