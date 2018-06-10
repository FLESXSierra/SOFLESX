package lesx.ui.costumer.dialog;

import java.util.Arrays;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.LesxCostumer;
import lesx.property.properties.LesxProperty;
import lesx.scene.controller.LesxController;
import lesx.ui.costumer.dialog.propertysheet.LesxPropertySheet;
import lesx.ui.mainpage.datamodel.LesxCostumerDataModel;
import lesx.utils.LesxString;

public class LesxCostumerDialogPaneController extends LesxController {

  @FXML
  private Button ok;
  @FXML
  private Button apply;
  @FXML
  private Button cancel;

  @FXML
  private Label descriptionLabel;
  @FXML
  private Label defaultName;

  @FXML
  private Pane propertiesPane;
  @FXML
  private Pane editorsPane;

  private LesxCostumerDataModel dataModel;
  private LesxCostumer costumer;
  private LesxPropertySheet propertySheet;
  private BooleanProperty closeProperty = new SimpleBooleanProperty(this, "closeProperty", false);
  private BooleanProperty pendingChanges = new SimpleBooleanProperty(this, "pendingChanges");
  private BooleanProperty afterSaveProperty = new SimpleBooleanProperty(this, "afterSaveProperty");
  private boolean canClose = true;
  private boolean isCreate;

  @FXML
  public void initialize() {
    setTitle(LesxMessage.getMessage("TEXT-TITLE_COSTUMER_DIALOG_CREATE"));
    ok.setText(LesxMessage.getMessage("TEXT-BUTTON_OK"));
    ok.setOnAction(obs -> save());
    apply.setText(LesxMessage.getMessage("TEXT-BUTTON_APPLY"));
    apply.setOnAction(obs -> applyChanges());
    cancel.setText(LesxMessage.getMessage("TEXT-BUTTON_CANCEL"));
    cancel.setOnAction(obs -> closeDialog());
  }

  public void init(LesxCostumerDataModel dataModel, boolean isCreate) {
    this.dataModel = dataModel;
    if (isCreate) {
      costumer = new LesxCostumer();
      costumer.setId(dataModel.createNewKeyForProperty(LesxString.PROPERTY_ID));
      descriptionLabel.setText(LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_NEW_COSTUMER"));
    }
    else {
      costumer = dataModel.getCostumerSelected();
      descriptionLabel.setText(LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_EDIT_COSTUMER"));
    }
    defaultName.setText(getHeader(isCreate));
    this.isCreate = isCreate;
    propertySheet = new LesxPropertySheet(propertiesPane, editorsPane, costumer,
        (Long newKey, LesxProperty property) -> dataModel.isUniqueProperty(property, newKey, this.isCreate));
    pendingChanges.bind(propertySheet.getPendingChangesProperty());
    installButtonsBinding();
  }

  private void save() {
    if (pendingChanges.get()) {
      boolean isValid = getPropertySheet().isValid();
      boolean duplicateId = isDuplicate();
      boolean showIssues = !isValid || !duplicateId;
      if (showIssues) {
        StringBuilder issues = new StringBuilder(1024);
        StringBuilder header = new StringBuilder(1024);
        if (!isValid) {
          issues.append(LesxMessage.getMessage("TEXT-INVALID_PROPERTY_ISSUE"));
          issues.append("\n");
        }
        if (!duplicateId) {
          issues.append(LesxMessage.getMessage("TEXT-DUPLICATE_ID_PROPERTY_ISSUE", costumer.getId()));
          issues.append("\n");
        }
        header.append(LesxMessage.getMessage("TEXT-ALERT_DIALOG_HEADER_ERROR"));
        header.append("\n");
        header.append(LesxMessage.getMessage("TEXT-ALERT_DIALOG_CONTEXT_ERROR"));
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(LesxMessage.getMessage("TEXT-ALERT_DIALOG_TITLE_ERROR"));
        alert.setHeaderText(header.toString());
        alert.setContentText(issues.toString());
        alert.getButtonTypes()
            .clear();
        alert.getButtonTypes()
            .addAll(ButtonType.OK);
        alert.showAndWait();
      }
      else if (canClose) {
        closeProperty.set(true);
      }
    }
  }

  /**
   * Override this method in order to search for the right component duplicate
   *
   * @return search for duplicates.
   */
  protected boolean isDuplicate() {
    return dataModel.addCostumers(Arrays.asList((LesxCostumer) getPropertySheet().getComponent()));
  }

  private void applyChanges() {
    canClose = false;
    save();
    afterSaveProperty.set(!afterSaveProperty.get());
    canClose = true;
    getPropertySheet().setPendingChanges(false);
    reInitialize();
  }

  /**
   * ReInitialize the dialog
   */
  protected void reInitialize() {
    isCreate = false;
    dataModel.setCostumerSelected(costumer);
    init(dataModel, isCreate);
  }

  private void closeDialog() {
    if (pendingChanges.get()) {
      ButtonType save = new ButtonType(LesxMessage.getMessage("TEXT-SAVE_BUTTON"));
      ButtonType dontSave = new ButtonType(LesxMessage.getMessage("TEXT-NO_SAVE_BUTTON"));
      ButtonType cancel = new ButtonType(LesxMessage.getMessage("TEXT-CANCEL_BUTTON"));
      String name = getComponentName();
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle(LesxMessage.getMessage("TEXT-ALERT_DIALOG_TITLE_PENDING_SAVES"));
      alert.setHeaderText(LesxMessage.getMessage("TEXT-ALERT_DIALOG_HEADER_PENDING_SAVES"));
      alert.setContentText(LesxMessage.getMessage("TEXT-ALERT_DIALOG_CONTEXT_PENDING_SAVES", name));
      alert.getButtonTypes()
          .clear();
      alert.getButtonTypes()
          .addAll(save, dontSave, cancel);
      ButtonType result = alert.showAndWait()
          .orElse(null);
      if (save.equals(result)) {
        save();
      }
      else if (dontSave.equals(result)) {
        closeProperty.set(true);
      }
    }
    else {
      closeProperty.set(true);
    }
  }

  /**
   * Override this method in order to search for the right name.
   *
   * @return Component name.
   */
  protected String getComponentName() {
    return (costumer.getName() == null) ? "*No Name*" : costumer.getName();
  }

  protected void installButtonsBinding() {
    ok.disableProperty()
        .bind(Bindings.or(Bindings.not(getPropertySheet().validProperty()), Bindings.not(getPendingChanges())));
    apply.disableProperty()
        .bind(Bindings.or(Bindings.not(getPropertySheet().validProperty()), Bindings.not(getPendingChanges())));
  }

  /**
   * Creates the header String
   *
   * @param isCreate boolean
   * @return a string
   */
  protected String getHeader(boolean isCreate) {
    StringBuilder string;
    string = new StringBuilder(128);
    if (isCreate) {
      string.append(LesxMessage.getMessage("TEXT-LOCATION_LABEL_COSTUMER_PANE", "Nuevo"));
      string.append(".");
    }
    else {
      string.append(LesxMessage.getMessage("TEXT-LOCATION_LABEL_COSTUMER_PANE", costumer.getName()));
      string.append(".");
    }
    return string.toString();
  }

  public BooleanProperty closeProperty() {
    return closeProperty;
  }

  public boolean isUniqueProperty(Long id, Long newKey) {
    return false;
  }

  public BooleanProperty afterSaveProperty() {
    return afterSaveProperty;
  }

  protected LesxPropertySheet getPropertySheet() {
    return propertySheet;
  }

  protected BooleanProperty getPendingChanges() {
    return pendingChanges;
  }

  protected Label getDescriptionLabel() {
    return descriptionLabel;
  }

  protected Label getDefaultName() {
    return defaultName;
  }

  /**
   * @return the propertiesPane
   */
  protected Pane getPropertiesPane() {
    return propertiesPane;
  }

  /**
   * @return the editorsPane
   */
  protected Pane getEditorsPane() {
    return editorsPane;
  }

}
