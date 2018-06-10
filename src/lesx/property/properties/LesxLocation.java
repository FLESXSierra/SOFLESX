package lesx.property.properties;

import java.util.Arrays;
import java.util.List;

public class LesxLocation extends LesxComponent {

  private LesxProperty id;
  private LesxProperty name;
  private LesxProperty parent_id;
  private List<LesxProperty> propertyValues;

  public LesxLocation() {
    id = new LesxProperty();
    id.setType(ELesxPropertyType.INTEGER);
    name = new LesxProperty();
    name.setType(ELesxPropertyType.TEXT);
    parent_id = new LesxProperty();
    parent_id.setType(ELesxPropertyType.INTEGER);
    parent_id.setVisible(false);
    propertyValues = Arrays.asList(id, name, parent_id);
    setPropertyValues(propertyValues);
  }

  protected void setId(Long id) {
    this.id.setValue(id);
  }

  protected Long getId() {
    return (Long) id.getValue();
  }

  protected void setName(String name) {
    this.name.setValue(name);
  }

  protected String getName() {
    return (String) name.getValue();
  }

  public Long getParent_id() {
    return (Long) parent_id.getValue();
  }

  public void setParent_id(Long parent_id) {
    this.parent_id.setValue(parent_id);
  }

}
