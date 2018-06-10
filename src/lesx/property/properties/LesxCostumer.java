package lesx.property.properties;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import lesx.gui.message.LesxMessage;
import lesx.property.costumer.LesxCostumerXMLParser;
import lesx.utils.LesxString;

public class LesxCostumer extends LesxComponent implements Cloneable {

  private final static Logger LOGGER = Logger.getLogger(LesxCostumer.class.getName());

  private LesxProperty id;
  private LesxProperty name;
  private LesxProperty cc;
  private LesxProperty location;

  public LesxCostumer(LesxCostumerXMLParser parse) {
    initializeProperties();
    if (parse.getId() == null) {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_ID"));
      id.setValue(-1L);
    }
    else {
      id.setValue(parse.getId());
    }
    if (parse.getLocation() == null) {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_LOCATION"));
      location.setValue(0L);
    }
    else {
      setLocation(parse.getLocation());
    }
    name.setValue(parse.getName());
    cc.setValue(parse.getCc());
    setKey(ELesxPropertyKeys.COSTUMER);
  }

  private void initializeProperties() {
    id = new LesxProperty();
    id.setType(ELesxPropertyType.LONG);
    id.setName(LesxString.PROPERTY_ID);
    id.setMandatory(true);
    id.setReadOnly(true);
    id.setUnique(true);
    name = new LesxProperty();
    name.setType(ELesxPropertyType.TEXT);
    name.setName(LesxString.PROPERTY_NAME);
    name.setMandatory(true);
    cc = new LesxProperty();
    cc.setType(ELesxPropertyType.LONG);
    cc.setName(LesxString.PROPERTY_CC);
    cc.setMandatory(true);
    location = new LesxProperty();
    location.setType(ELesxPropertyType.LOCATION);
    location.setName(LesxString.PROPERTY_LOCATION);
    location.setMandatory(true);
    setPropertyValues(Arrays.asList(id, name, cc, location));//TODO Add properties if added new
  }

  public LesxCostumer() {
    initializeProperties();
  }

  public String getName() {
    return (String) name.getValue();
  }

  public LesxProperty getNameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.setName(name);
  }

  public Long getCc() {
    return (Long) cc.getValue();
  }

  public LesxProperty getCcProperty() {
    return cc;
  }

  public void setCc(Long cc) {
    this.cc.setValue(cc);
  }

  public Long getLocation() {
    return (Long) location.getValue();
  }

  public LesxProperty getLocationProperty() {
    return location;
  }

  public void setLocation(Long location) {
    this.location.setValue(location);
  }

  public void setLocation(LesxLocation location) {
    this.location = location.getPropertyByName(LesxString.PROPERTY_ID);
  }

  public Long getId() {
    return (Long) id.getValue();
  }

  public void setId(Long id) {
    this.id.setValue(id);
  }

  @Override
  public LesxCostumer clone() {
    LesxCostumer clone = (LesxCostumer) super.clone();
    return clone;
  }

  @Override
  public String toString() {
    return name.getValue()
        .toString();
  }

}
