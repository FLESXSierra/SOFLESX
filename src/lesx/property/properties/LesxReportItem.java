package lesx.property.properties;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lesx.gui.message.LesxMessage;
import lesx.property.report.LesxReportItemsXMLParser;
import lesx.utils.LesxString;

public class LesxReportItem extends LesxComponent {

  private final static Logger LOGGER = Logger.getLogger(LesxReportTree.class.getName());

  private LesxProperty id;
  private LesxProperty parent_id;
  private LesxProperty costumers;
  private LesxProperty function;

  public LesxReportItem(LesxReportItemsXMLParser itemParser) {
    initializeProperties();
    if (itemParser.getId() == null) {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_ID"));
      id.setValue(-1L);
    }
    else {
      id.setValue(itemParser.getId());
    }
    if (itemParser.getParent_id() == null) {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_PARENT_ID"));
      parent_id.setValue(0L);
    }
    else {
      parent_id.setValue(itemParser.getParent_id());
    }
    costumers.setValue(itemParser.getCostumers());
    function.setValue(new LesxReportFunction(itemParser.getFunction()));
    setKey(ELesxPropertyKeys.REPORT_ELEMENT);
  }

  private void initializeProperties() {
    id = new LesxProperty();
    id.setMandatory(true);
    id.setName(LesxString.PROPERTY_ID);
    id.setType(ELesxPropertyType.LONG);
    id.setReadOnly(true);
    id.setUnique(true);
    parent_id = new LesxProperty();
    parent_id.setName(LesxString.PROPERTY_PARENT_ID);
    parent_id.setType(ELesxPropertyType.LONG);
    costumers = new LesxProperty();
    costumers.setType(ELesxPropertyType.LIST);
    costumers.setName(LesxString.PROPERTY_LIST);
    function = new LesxProperty();
    function.setType(ELesxPropertyType.LIST);
    function.setName(LesxString.PROPERTY_LIST);
    setPropertyValues(Arrays.asList(id, parent_id, costumers, function));
  }

  public LesxReportItem() {
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

  public Long getParent_id() {
    return (Long) parent_id.getValue();
  }

  public void setParent_id(Long parent_id) {
    this.parent_id.setValue(parent_id);
  }

  public LesxProperty parent_idProperty() {
    return parent_id;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getCostumer() {
    return (List<Long>) costumers.getValue();
  }

  public void setCostumer(Collection<Long> costumers) {
    this.costumers.setValue(costumers);
  }

  public LesxProperty costumersProperty() {
    return costumers;
  }

  public LesxReportFunction getFunction() {
    return (LesxReportFunction) function.getValue();
  }

  public void setFunction(LesxReportFunction function) {
    this.function.setValue(function);
  }

  public LesxProperty functionProperty() {
    return function;
  }

  @Override
  public LesxReportItem clone() {
    LesxReportItem newItem = new LesxReportItem();
    newItem.costumers = this.costumers.clone();
    newItem.function = this.function.clone();
    newItem.id = this.id.clone();
    newItem.parent_id = this.parent_id.clone();
    return newItem;
  }

}
