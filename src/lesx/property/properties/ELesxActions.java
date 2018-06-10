package lesx.property.properties;

public enum ELesxActions {
  ACTIONS_ADD(0),
  ACTIONS_DELETE(1),
  ACTIONS_DESELECT(2),
  ACTIONS_CHILDREN(3),
  ACTIONS_EDIT(4);

  private int key;

  private ELesxActions(int key) {
    this.key = key;
  }

  public int getKey() {
    return key;
  }
}
