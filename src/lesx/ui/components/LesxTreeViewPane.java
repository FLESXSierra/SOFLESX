package lesx.ui.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxActions;
import lesx.property.properties.ELesxUseCase;
import lesx.ui.toolbar.LesxToolBar;

public class LesxTreeViewPane<T> extends VBox {

  private BooleanProperty childrenAlsoProperty = new SimpleBooleanProperty(this, "childrenAlsoProperty", true);
  private BooleanProperty selectedItemTree = new SimpleBooleanProperty(this, "selectedItemTable");
  private LesxToolBar toolBar;
  private TreeView<T> treeView;
  private Consumer<Boolean> addNewItem;
  private Runnable onDeleteItem;
  private ELesxUseCase useCase;

  public LesxTreeViewPane() {
    useCase = ELesxUseCase.UC_TREE;
    toolBar = new LesxToolBar(useCase);
    toolBar.setPrefHeight(40);
    toolBar.setMinHeight(Region.USE_PREF_SIZE);
    toolBar.getSelectedItemProperty()
        .bind(selectedItemTree);
    installToolBarActions();
    treeView = new TreeView<>();
    getChildren().addAll(toolBar, treeView);
    setVgrow(treeView, Priority.ALWAYS);
    treeView.getSelectionModel()
        .selectedItemProperty()
        .addListener((obs, oldV, newV) -> selectedItemTree.set(newV != null));
  }

  private void installToolBarActions() {
    Map<ELesxActions, EventHandler<ActionEvent>> mapActions;
    mapActions = toolBar.getActions();
    List<ELesxActions> actions = new ArrayList<>();
    actions.addAll(mapActions.keySet());
    for (ELesxActions action : actions) {
      mapActions.remove(action);
      EventHandler<ActionEvent> actionHandler;
      switch (action) {
        case ACTIONS_ADD:
          actionHandler = s -> addNewItem.accept(true);
          break;
        case ACTIONS_DELETE:
          actionHandler = s -> deleteItem();
          break;
        case ACTIONS_DESELECT:
          actionHandler = s -> treeView.getSelectionModel()
              .clearSelection();
          break;
        case ACTIONS_CHILDREN:
          actionHandler = s -> {
            setChildrenAlso(((ToggleButton) s.getSource()).isSelected());
          };
          break;
        case ACTIONS_EDIT:
          actionHandler = s -> addNewItem.accept(false);
          break;
        default:
          actionHandler = null;
          break;
      }
      mapActions.put(action, actionHandler);
    }
    toolBar.installHandlers();
  }

  private void deleteItem() {
    if (selectedItemTree.get()) {
      T temp = treeView.getSelectionModel()
          .getSelectedItem()
          .getValue();
      if (temp != null) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(LesxMessage.getMessage("TEXT-ALERT_CONFIRMATION_TITLE"));
        if (useCase == ELesxUseCase.UC_ADD_REMOVE_ONLY || useCase == ELesxUseCase.UC_DELETE_ONLY) {
          alert.setHeaderText(LesxMessage.getMessage("TEXT-ALERT_CONFIRMATION_DELETE_OBJECTS_HEADER", 1));
        }
        else {
          alert.setHeaderText(LesxMessage.getMessage("TEXT-ALERT_CONFIRMATION_DELETE_HEADER", temp.toString()));
        }
        ButtonType result = alert.showAndWait()
            .orElse(null);
        if (ButtonType.OK.equals(result)) {
          onDeleteItem.run();
          if (temp.equals(treeView.getRoot()
              .getValue())) {
            treeView.setRoot(new TreeItem<>());
          }
          else {
            treeView.getRoot()
                .getChildren()
                .remove(temp);
          }
        }
      }
    }
  }

  public BooleanProperty childrenAlsoProperty() {
    return childrenAlsoProperty;
  }

  public boolean isChildrenAlso() {
    return childrenAlsoProperty.get();
  }

  public void setChildrenAlso(boolean childrenAlsoProperty) {
    this.childrenAlsoProperty.set(childrenAlsoProperty);
  }

  public LesxToolBar getToolBar() {
    return toolBar;
  }

  public TreeView<T> getTree() {
    return treeView;
  }

  public void setUseCase(ELesxUseCase useCase) {
    this.useCase = useCase;
    toolBar.setUseCase(useCase);
  }

  public BooleanProperty selectedItemTreeProperty() {
    return selectedItemTree;
  }

  public void setOnDelete(Runnable runnable) {
    onDeleteItem = runnable;
    installToolBarActions();
  }

  public void setOnAddNewItem(Consumer<Boolean> consumer) {
    addNewItem = consumer;
    installToolBarActions();
  }

}
