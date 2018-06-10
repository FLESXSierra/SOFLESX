package lesx.property.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import lesx.gui.message.LesxMessage;
import lesx.utils.LesxMisc;

public class LesxComponent implements Cloneable {

  private final static Logger LOGGER = Logger.getLogger(LesxComponent.class.getName());

  private Collection<LesxProperty> propertyValues;
  private ELesxPropertyKeys key;

  public ELesxPropertyKeys getKey() {
    return key;
  }

  public void setKey(ELesxPropertyKeys key) {
    this.key = key;
  }

  public Collection<LesxProperty> getPropertyValues() {
    return propertyValues;
  }

  public void setPropertyValues(Collection<LesxProperty> propertyValues) {
    this.propertyValues = propertyValues;
  }

  /**
   * Finds the first property value filtered by name, can be null
   *
   * @param name to be searched
   * @return LesxProperty
   */
  public LesxProperty getPropertyByName(String name) {
    return propertyValues.stream()
        .filter(s -> LesxMisc.equals(name, s.getName()))
        .findFirst()
        .orElse(null);
  }

  public LesxComponent clone() {
    try {
      LesxComponent newComponent = (LesxComponent) super.clone();
      newComponent.setPropertyValues(new ArrayList<>(getPropertyValues().stream()
          .map(prop -> prop.clone())
          .collect(Collectors.toList())));
      return newComponent;
    }
    catch (CloneNotSupportedException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-PROPERTY_NOT_CLONNABLE", this), e);
      e.printStackTrace();
    }
    return new LesxComponent();
  }
}
