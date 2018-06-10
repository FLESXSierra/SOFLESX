package lesx.ui.toolbar;

import static lesx.property.properties.ELesxActions.ACTIONS_ADD;
import static lesx.property.properties.ELesxActions.ACTIONS_CHILDREN;
import static lesx.property.properties.ELesxActions.ACTIONS_DELETE;
import static lesx.property.properties.ELesxActions.ACTIONS_DESELECT;
import static lesx.property.properties.ELesxActions.ACTIONS_EDIT;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxActions;
import lesx.property.properties.ELesxUseCase;
import lesx.utils.LesxMisc;

public class LesxToolBar extends ToolBar {

  private Button deselect;
  private Button delete;
  private Button add;
  private Button edit;
  private ToggleButton children;

  private Map<ELesxActions, EventHandler<ActionEvent>> actions;
  private EventHandler<ActionEvent> deselectAction;
  private EventHandler<ActionEvent> editAction;
  private EventHandler<ActionEvent> deleteAction;
  private EventHandler<ActionEvent> addAction;
  private EventHandler<ActionEvent> childrenAction;

  private BooleanProperty selectedItem = new SimpleBooleanProperty(this, "selectedItem", false);
  private BooleanProperty selectedFilterTable = new SimpleBooleanProperty(this, "selectedFilterTable", true);

  private List<? extends ButtonBase> buttons;

  public LesxToolBar(ELesxUseCase useCase) {
    actions = new HashMap<>();
    deselect = new Button();
    deselect.setText(LesxMessage.getMessage("TEXT-DESELECT_BUTTON"));
    deselect.disableProperty()
        .bind(Bindings.not(selectedItem));
    buildButtons(useCase);
  }

  private Tooltip createTooltip(String message) {
    Tooltip tool = new Tooltip();
    tool.setText(message);
    tool.setWrapText(true);
    tool.setMaxWidth(400);
    return tool;
  }

  private void addButtonsToToolBar() {
    if (!LesxMisc.isEmpty(buttons)) {
      for (Object button : buttons) {
        Separator sep = new Separator();
        sep.setOrientation(Orientation.VERTICAL);
        getItems().add((ButtonBase) button);
        getItems().add(sep);
      }
    }
  }

  /**
   * Gets the map of Actions with the respective handler that will run on a CLICKED event.
   *
   * @return a Map
   */
  public Map<ELesxActions, EventHandler<ActionEvent>> getActions() {
    return actions;
  }

  /**
   * Install the EventHandler contained in the getActions() Map, is important to notice that null events will not be
   * added for obvious reason (NPE), please be sure that event handlers are assigned and this method is called to expect
   * this buttons do something with your App.
   */
  public void installHandlers() {
    for (Entry<ELesxActions, EventHandler<ActionEvent>> entry : actions.entrySet()) {
      if (entry.getValue() == null) {
        continue;
      }
      removeAction(entry.getKey());
      switch (entry.getKey()) {
        case ACTIONS_ADD:
          add.setOnAction(entry.getValue());
          break;
        case ACTIONS_CHILDREN:
          children.setOnAction(entry.getValue());
          break;
        case ACTIONS_DELETE:
          delete.setOnAction(entry.getValue());
          break;
        case ACTIONS_DESELECT:
          deselect.setOnAction(entry.getValue());
          break;
        case ACTIONS_EDIT:
          edit.setOnAction(entry.getValue());
          break;
      }
    }
  }

  public BooleanProperty getSelectedItemProperty() {
    return selectedItem;
  }

  public void setUseCase(ELesxUseCase useCase) {
    buildButtons(useCase);
  }

  private void buildButtons(ELesxUseCase useCase) {
    getItems().clear();
    actions.clear();
    actions.put(ACTIONS_DESELECT, deselectAction);
    switch (useCase) {
      case UC_TREE:
        children = new ToggleButton();
        children.setText(LesxMessage.getMessage("TEXT-SELECT_CHILDREN_BUTTON"));
        children.setTooltip(createTooltip(LesxMessage.getMessage("TEXT-TOOLTIP_SELECT_CHILDREN")));
        children.setSelected(true);
        buttons = Arrays.asList(deselect, children);
        actions.put(ACTIONS_CHILDREN, childrenAction);
        break;
      case UC_TREE_MODIFY:
      case UC_TABLE:
        delete = new Button();
        delete.setText(LesxMessage.getMessage("TEXT-DELETE_BUTTON"));
        delete.disableProperty()
            .bind(Bindings.not(selectedItem));
        add = new Button();
        add.setText(LesxMessage.getMessage("TEXT-ADD_BUTTON"));
        add.disableProperty()
            .bind(Bindings.not(selectedFilterTable));
        edit = new Button();
        edit.setText(LesxMessage.getMessage("TEXT-EDIT_BUTTON"));
        edit.disableProperty()
            .bind(Bindings.not(selectedItem));
        buttons = Arrays.asList(deselect, add, edit, delete);
        actions.put(ACTIONS_DELETE, deleteAction);
        actions.put(ACTIONS_ADD, addAction);
        actions.put(ACTIONS_EDIT, editAction);
        break;
      case UC_READ_ONLY:
        buttons = Arrays.asList(deselect);
        break;
      case UC_ADD_REMOVE_ONLY:
        delete = new Button();
        delete.setText(LesxMessage.getMessage("TEXT-DELETE_BUTTON"));
        delete.disableProperty()
            .bind(Bindings.not(selectedItem));
        add = new Button();
        add.setText(LesxMessage.getMessage("TEXT-ADD_BUTTON"));
        add.disableProperty()
            .bind(Bindings.not(selectedFilterTable));
        buttons = Arrays.asList(deselect, add, delete);
        actions.put(ACTIONS_DELETE, deleteAction);
        actions.put(ACTIONS_ADD, addAction);
        break;
      case UC_DELETE_ONLY:
        delete = new Button();
        delete.setText(LesxMessage.getMessage("TEXT-DELETE_BUTTON"));
        delete.disableProperty()
            .bind(Bindings.not(selectedItem));
        buttons = Arrays.asList(deselect, delete);
        actions.put(ACTIONS_DELETE, deleteAction);
        break;
      default:
        break;
    }
    addButtonsToToolBar();
  }

  public BooleanProperty selectedFilterTableProperty() {
    return selectedFilterTable;
  }

  /**
   * Removes the Event Handler from the given action
   *
   * @param action ELesxActions
   */
  public void removeAction(ELesxActions action) {
    switch (action) {
      case ACTIONS_ADD:
        add.setOnAction(null);
        break;
      case ACTIONS_CHILDREN:
        children.setOnAction(null);
        break;
      case ACTIONS_DELETE:
        delete.setOnAction(null);
        break;
      case ACTIONS_DESELECT:
        deselect.setOnAction(null);
        break;
      case ACTIONS_EDIT:
        edit.setOnAction(null);
        break;
    }
  }

}
