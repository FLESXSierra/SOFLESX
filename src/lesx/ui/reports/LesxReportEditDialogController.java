package lesx.ui.reports;

import java.util.Arrays;
import java.util.HashSet;

import javafx.fxml.FXML;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.LesxProperty;
import lesx.property.properties.LesxReportTree;
import lesx.ui.costumer.dialog.LesxCostumerDialogPaneController;
import lesx.ui.costumer.dialog.propertysheet.LesxPropertySheet;
import lesx.ui.mainpage.datamodel.LesxCostumerDataModel;
import lesx.ui.reports.datamodel.LesxReportsDataModel;

public class LesxReportEditDialogController extends LesxCostumerDialogPaneController {
  private LesxReportsDataModel dataModel;
  private LesxReportTree tree;
  private boolean isCreate;
  private LesxPropertySheet propertySheet;

  @FXML
  @Override
  public void initialize() {
    super.initialize();
  }

  @Override
  public void init(LesxCostumerDataModel dataModel, boolean isCreate) {
    //Nothing
  }

  @Override
  protected void reInitialize() {
    isCreate = false;
    dataModel.setSelectedReportTree(tree);
    init(dataModel, isCreate);
  }

  public void init(LesxReportsDataModel dataModel, boolean isCreate) {
    setTitle(LesxMessage.getMessage("TEXT-TITLE_REPORT_DIALOG_CREATE"));
    this.dataModel = dataModel;
    this.isCreate = isCreate;
    if (isCreate) {
      tree = new LesxReportTree();
      tree.setId(dataModel.createNewKeyForTreeId());
      tree.setCostumers(new HashSet<>());
      if (dataModel.getSelectedReportTree() != null) {
        tree.setParent_id(dataModel.getSelectedReportTree()
            .getId());
      }
      else {
        tree.setParent_id(-1L);
      }
      getDescriptionLabel().setText(LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_NEW_REPORT"));
    }
    else {
      tree = dataModel.getSelectedReportTree();
      getDescriptionLabel().setText(LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_EDIT_REPORT"));
    }
    getDefaultName().setText(getHeader(isCreate));
    propertySheet = new LesxPropertySheet(getPropertiesPane(), getEditorsPane(), tree,
        (Long newKey, LesxProperty property) -> dataModel.isUniqueProperty(property, newKey, this.isCreate));
    getPendingChanges().bind(propertySheet.getPendingChangesProperty());
    installButtonsBinding();
  }

  @Override
  public String getTitle() {
    return super.getTitle();
  }

  @Override
  protected String getHeader(boolean isCreate) {
    StringBuilder string;
    string = new StringBuilder(128);
    if (isCreate) {
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_REPORT_PANE", "Nuevo"));
      string.append(".");
    }
    else {
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_REPORT_PANE", tree.getName()));
      string.append(".");
    }
    return string.toString();
  }

  @Override
  protected boolean isDuplicate() {
    return dataModel.addReport(Arrays.asList(tree));
  }

  @Override
  protected LesxPropertySheet getPropertySheet() {
    return propertySheet;
  }

  @Override
  protected String getComponentName() {
    return tree.getName() == null ? "*No Name*" : tree.getName();
  }

}
