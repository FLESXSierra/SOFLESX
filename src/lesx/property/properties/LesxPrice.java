package lesx.property.properties;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import lesx.gui.message.LesxMessage;
import lesx.property.price.LesxPriceXMLParser;
import lesx.utils.LesxString;

public class LesxPrice extends LesxComponent implements Cloneable {

  private final static Logger LOGGER = Logger.getLogger(LesxPrice.class.getName());

  private LesxProperty id;
  private LesxProperty name;
  private LesxProperty total;
  private LesxProperty resource_id;
  private LesxProperty typePrice;
  private LesxProperty validFrom;

  public LesxPrice(LesxPriceXMLParser price) {
    initializeProperties();
    if (price.getId() == null) {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_ID"));
      id.setValue(-1L);
    }
    else {
      id.setValue(price.getId());
    }
    if (price.getResource_id() == null) {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_RESOURCE_ID"));
      resource_id.setValue(0L);
    }
    else {
      resource_id.setValue(price.getResource_id());
    }
    if (price.getTotal() == null) {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_PRICE"));
      total.setValue(0L);
    }
    else {
      total.setValue(price.getTotal());
    }
    if (price.getValidFrom() == null) {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_VALID_FROM"));
      validFrom.setValue(LocalDate.now()
          .toString());
    }
    else {
      validFrom.setValue(price.getValidFrom());
    }
    typePrice.setValue(price.getTypePrice());
    name.setValue(price.getName());
    setKey(ELesxPropertyKeys.PRICE);
  }

  private void initializeProperties() {
    id = new LesxProperty();
    id.setMandatory(true);
    id.setName(LesxString.PROPERTY_ID);
    id.setType(ELesxPropertyType.LONG);
    id.setReadOnly(true);
    id.setUnique(true);
    name = new LesxProperty();
    name.setMandatory(true);
    name.setType(ELesxPropertyType.TEXT);
    name.setName(LesxString.PROPERTY_NAME);
    total = new LesxProperty();
    total.setName(LesxString.PROPERTY_TOTAL);
    total.setType(ELesxPropertyType.LONG);
    resource_id = new LesxProperty();
    resource_id.setMandatory(true);
    resource_id.setReadOnly(true);
    resource_id.setType(ELesxPropertyType.LONG);
    resource_id.setName(LesxString.PROPERTY_RESOURCE_ID);
    resource_id.setVisible(false);
    typePrice = new LesxProperty();
    typePrice.setName(LesxString.PROPERTY_PRICE_TYPE);
    typePrice.setType(ELesxPropertyType.PRICE_TYPE);
    typePrice.setMandatory(true);
    validFrom = new LesxProperty();
    validFrom.setMandatory(true);
    validFrom.setType(ELesxPropertyType.DATE);
    validFrom.setName(LesxString.PROPERTY_DATE);
    setPropertyValues(Arrays.asList(id, name, resource_id, total, typePrice, validFrom));
  }

  public LesxPrice() {
    initializeProperties();
  }

  public Long getId() {
    return (Long) id.getValue();
  }

  public void setId(Long id) {
    this.id.setValue(id);
  }

  public LesxProperty idProperty() {
    return id;
  }

  public String getName() {
    return (String) name.getValue();
  }

  public void setName(String name) {
    this.name.setValue(name);
  }

  public LesxProperty nameProperty() {
    return name;
  }

  public Long getTotal() {
    return (Long) total.getValue();
  }

  public void setTotal(Long total) {
    this.total.setValue(total);
  }

  public LesxProperty totalProperty() {
    return total;
  }

  public Long getResource_id() {
    return (Long) resource_id.getValue();
  }

  public void setResource_id(Long resource_id) {
    this.resource_id.setValue(resource_id);
  }

  public LesxProperty resourceIdProperty() {
    return resource_id;
  }

  public void setTypePrice(Boolean typePrice) {
    this.typePrice.setValue(typePrice);
  }

  public LesxProperty typePriceProperty() {
    return typePrice;
  }

  public Boolean getTypePrice() {
    return (Boolean) typePrice.getValue();
  }

  public String getValidFrom() {
    return (String) validFrom.getValue();
  }

  public void setValidFrom(String validFrom) {
    this.validFrom.setValue(validFrom);
  }

  public LesxProperty validFromProperty() {
    return validFrom;
  }

  @Override
  public String toString() {
    return name.getValue()
        .toString();
  }

}
