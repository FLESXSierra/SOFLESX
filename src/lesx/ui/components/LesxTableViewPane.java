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
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxActions;
import lesx.property.properties.ELesxUseCase;
import lesx.ui.toolbar.LesxToolBar;

public class LesxTableViewPane<T> extends VBox {

  private LesxToolBar toolBar;
  private TableView<T> table;
  private BooleanProperty selectedItemTable = new SimpleBooleanProperty(this, "selectedItemTable");
  private BooleanProperty selectedFilterTable = new SimpleBooleanProperty(this, "selectedFilterTable", true);
  private Consumer<Boolean> addNewItem;
  private Runnable onDeleteItem;
  private ELesxUseCase useCase;

  public LesxTableViewPane() {
    initialize();
    toolBar = new LesxToolBar(useCase);
    toolBar.setPrefHeight(40);
    toolBar.setMinHeight(Region.USE_PREF_SIZE);
    toolBar.selectedFilterTableProperty()
        .bind(selectedFilterTable);
    table = new TableView<>();
    table.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> selectedItemTable());
    installToolBarActions();
    getChildren().addAll(toolBar, table);
    VBox.setVgrow(table, Priority.ALWAYS);
  }

  private void initialize() {
    useCase = ELesxUseCase.UC_TABLE;
    addNewItem = (isCreate) -> {
    };
    onDeleteItem = () -> {
    };
  }

  private void installToolBarActions() {
    Map<ELesxActions, EventHandler<ActionEvent>> mapActions;
    mapActions = toolBar.getActions();
    List<ELesxActions> actions = new ArrayList<>();
    actions.addAll(mapActions.keySet());
    for (ELesxActions action : actions) {
      if (mapActions.get(action) != null) {
        toolBar.removeAction(action);
      }
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
          actionHandler = s -> table.getSelectionModel()
              .clearSelection();
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
    toolBar.getSelectedItemProperty()
        .bind(selectedItemTable);
  }

  private void deleteItem() {
    if (selectedItemTable.get()) {
      List<T> temp = table.getSelectionModel()
          .getSelectedItems();
      if (temp != null && !temp.isEmpty()) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(LesxMessage.getMessage("TEXT-ALERT_CONFIRMATION_TITLE"));
        if (useCase == ELesxUseCase.UC_ADD_REMOVE_ONLY || useCase == ELesxUseCase.UC_DELETE_ONLY) {
          alert.setHeaderText(LesxMessage.getMessage("TEXT-ALERT_CONFIRMATION_DELETE_OBJECTS_HEADER", table.getSelectionModel()
              .getSelectedItems()
              .size()));
        }
        else {
          alert.setHeaderText(LesxMessage.getMessage("TEXT-ALERT_CONFIRMATION_DELETE_HEADER", temp.toString()));
        }
        ButtonType result = alert.showAndWait()
            .orElse(null);
        if (ButtonType.OK.equals(result)) {
          onDeleteItem.run();
          table.getItems()
              .removeAll(temp);
        }
      }
    }
  }

  public LesxToolBar getToolBar() {
    return toolBar;
  }

  public TableView<T> getTable() {
    return table;
  }

  private void selectedItemTable() {
    selectedItemTable.set(table.getSelectionModel()
        .getSelectedItem() != null);
  }

  public void setOnAddNewItem(Consumer<Boolean> consumer) {
    addNewItem = consumer;
    installToolBarActions();
  }

  public void setUseCase(ELesxUseCase useCase) {
    this.useCase = useCase;
    toolBar.setUseCase(useCase);
  }

  public void setOnDelete(Runnable runnable) {
    onDeleteItem = runnable;
    installToolBarActions();
  }

  public BooleanProperty selectedFilterTableProperty() {
    return selectedFilterTable;
  }

}
