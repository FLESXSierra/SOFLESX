package lesx.property.properties;

import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.TreeItem;
import lesx.gui.message.LesxMessage;
import lesx.property.report.LesxReportTreeXMLParser;
import lesx.utils.LesxString;

public class LesxReportTree extends LesxComponent {

  private final static Logger LOGGER = Logger.getLogger(LesxReportTree.class.getName());

  private LesxProperty id;
  private LesxProperty name;
  private LesxProperty parent_id;
  private LesxProperty costumers;

  public LesxReportTree(LesxReportTreeXMLParser report) {
    initializeProperties();
    if (report.getId() == null) {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_ID"));
      id.setValue(-1L);
    }
    else {
      id.setValue(report.getId());
    }
    if (report.getParent_id() == null) {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_RESOURCE_ID"));
      parent_id.setValue(0L);
    }
    else {
      parent_id.setValue(report.getParent_id());
    }
    name.setValue(report.getName());
    costumers.setValue(report.getCostumers());
    setKey(ELesxPropertyKeys.REPORT_TREE);
  }

  public LesxReportTree() {
    initializeProperties();
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
    parent_id = new LesxProperty();
    parent_id.setMandatory(true);
    parent_id.setReadOnly(true);
    parent_id.setType(ELesxPropertyType.LONG);
    parent_id.setName(LesxString.PROPERTY_PARENT_ID);
    parent_id.setVisible(false);
    costumers = new LesxProperty();
    costumers.setType(ELesxPropertyType.LIST);
    costumers.setName(LesxString.PROPERTY_LIST);
    costumers.setVisible(false);
    setPropertyValues(Arrays.asList(id, parent_id, name, costumers));
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

  public LesxProperty parent_idProperty() {
    return parent_id;
  }

  public Long getParent_id() {
    return (Long) parent_id.getValue();
  }

  public void setParent_id(Long parent_id) {
    this.parent_id.setValue(parent_id);
  }

  @SuppressWarnings("unchecked")
  public Set<Long> getCostumers() {
    return (Set<Long>) costumers.getValue();
  }

  public void setCostumers(Set<Long> costumers) {
    this.costumers.setValue(costumers);
  }

  public LesxProperty costumersProperty() {
    return costumers;
  }

  public static TreeItem<LesxReportTree> createRootTreeItem() {
    LesxReportTree root = new LesxReportTree();
    root.setId(-1L);
    root.setParent_id(-10000L);
    root.setName("Root");
    return new TreeItem<>(root);
  }

  @Override
  public String toString() {
    return getName();
  }

}
