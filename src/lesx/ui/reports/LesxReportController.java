package lesx.ui.reports;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import lesx.gui.message.LesxMessage;
import lesx.property.price.LesxPriceDataModel;
import lesx.property.properties.ELesxFunction;
import lesx.property.properties.ELesxLocations;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxReportFunction;
import lesx.property.properties.LesxReportItem;
import lesx.property.properties.LesxReportTree;
import lesx.scene.controller.LesxController;
import lesx.scene.controller.LesxSceneController;
import lesx.ui.components.LesxFunctionRadioButton;
import lesx.ui.components.LesxTableViewPane;
import lesx.ui.components.LesxTreeViewPane;
import lesx.ui.mainpage.datamodel.LesxCostumerDataModel;
import lesx.ui.reports.datamodel.LesxReportsDataModel;
import lesx.utils.LesxMisc;
import main.LesxMain;

public class LesxReportController extends LesxController {

  @FXML
  LesxTreeViewPane<LesxReportTree> reportsPane;
  @FXML
  LesxTableViewPane<Long> addResourcePane;
  @FXML
  LesxTableViewPane<Long> functionPane;
  @FXML
  Label headerReport;
  @FXML
  Label reportFunctionHeader;
  @FXML
  LesxFunctionRadioButton functionVBox;
  @FXML
  Button addReport;
  @FXML
  Button editReport;

  // DataModels
  private LesxReportsDataModel dataModelReport;
  private LesxCostumerDataModel dataModelCostumer;
  private LesxPriceDataModel dataModelPrice;
  // Data
  private LesxReportTree currentReportTree;
  private LesxReportItem functionSelected;
  private BooleanProperty selectedTableCostumer = new SimpleBooleanProperty(this, "selectedTableCostumer", false);
  private BooleanProperty pendingChanges = new SimpleBooleanProperty(this, "pendingChanges", false);
  //Observable Lists
  private ObservableList<Long> functionList = FXCollections.observableArrayList();
  private ObservableList<Long> currentCostomers = FXCollections.observableArrayList();
  // UI Components
  private TreeView<LesxReportTree> tree;
  private TableView<Long> tableCostomer;
  private TableView<Long> tableFunction;
  // Columns
  private TableColumn<Long, String> nameColumn;
  private TableColumn<Long, String> cc;
  private TableColumn<Long, String> locations;
  private TableColumn<Long, String> typeItemColumn;
  private TableColumn<Long, String> valueItem;
  //Consumers
  private Consumer<Boolean> addNewCostumers;
  private Consumer<Boolean> addNewReport;

  @FXML
  public void initialize() {
    headerReport.setText(LesxMessage.getMessage("TEXT-REPORT_BUILDER_HEADER"));
    reportFunctionHeader.setText(LesxMessage.getMessage("TEXT-REPORT_BUILDER_FUNCTION_HEADER"));
    reportFunctionHeader.setWrapText(true);
    reportFunctionHeader.setPadding(new Insets(0, 2, 0, 2));
    functionPane.setUseCase(ELesxUseCase.UC_DELETE_ONLY);
    reportsPane.setUseCase(ELesxUseCase.UC_TREE_MODIFY);
    addResourcePane.setUseCase(ELesxUseCase.UC_ADD_REMOVE_ONLY);
    tree = reportsPane.getTree();
    addResourcePane.selectedFilterTableProperty()
        .bind(selectedTableCostumer);
    tableCostomer = addResourcePane.getTable();
    tableCostomer.getSelectionModel()
        .setSelectionMode(SelectionMode.MULTIPLE);
    tableFunction = functionPane.getTable();
    tableFunction.getSelectionModel()
        .setSelectionMode(SelectionMode.MULTIPLE);
    tableFunction.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> functionTableSelected());
    addReport.setOnAction(obs -> addFunction());
    editReport.setOnAction(obs -> editFunction());
    setUpConsumers();
    fillDataOnTree();
    fillDataOnTableCostumer();
    fillDataOnTableFunctions();
  }

  /**
   * Sets the consumers needed.
   */
  private void setUpConsumers() {
    addNewCostumers = isCreate -> addNewCostumers(isCreate);
    addNewReport = isCreate -> addNewReport(isCreate);
    addResourcePane.setOnAddNewItem(addNewCostumers);
    addResourcePane.setOnDelete(() -> removeSelected());
    functionPane.setOnDelete(() -> removeSelectedFunction());
    reportsPane.setOnDelete(() -> removeSelectedReport());
    reportsPane.setOnAddNewItem(addNewReport);
  }

  private void removeSelectedReport() {
    dataModelReport.removeReport(currentReportTree);
    refrestDataOnTree();
    pendingChanges.set(true);
  }

  private void removeSelectedFunction() {
    dataModelReport.removeFunctionFromCurrentReport(tableFunction.getSelectionModel()
        .getSelectedItems());
    pendingChanges.set(true);
  }

  private void removeSelected() {
    dataModelReport.removeCostumerFromCurrentReport(tableCostomer.getSelectionModel()
        .getSelectedItems());
    pendingChanges.set(true);
  }

  /**
   * Calls the add new report dialog
   *
   * @param isCreate Boolean
   */
  private void addNewReport(Boolean isCreate) {
    LesxSceneController.ShowAddReportTreeDialog(this, dataModelReport, isCreate, () -> {
      refrestDataOnTree();
      pendingChanges.set(true);
    });
  }

  /**
   * Calls the add new costumers already created Dialog, pretty damn long name, lmao.
   *
   * @param isCreate Boolean
   */
  private void addNewCostumers(Boolean isCreate) {
    LesxSceneController.showAddCostumerDialog(this, dataModelCostumer, dataModelReport, () -> {
      refreshCostumerTable();
      pendingChanges.set(true);
    });
  }

  /**
   * Refresh the consumer table
   */
  private void refreshCostumerTable() {
    if (currentReportTree != null) {
      currentCostomers.clear();
      currentCostomers.addAll(currentReportTree.getCostumers());
    }
  }

  /**
   * Edits a Function
   */
  private void editFunction() {
    final LesxReportFunction edited = functionVBox.getFunction();
    if (edited.getType() == null) {
      emptyFunctionError("'Función'");
      return;
    }
    if (functionSelected != null) {
      dataModelReport.editFunction(functionSelected, edited);
      final Long tempKey = functionSelected.getId();
      refreshReportFunctionTable();
      tableFunction.getSelectionModel()
          .select(tempKey);
      pendingChanges.set(true);
    }
    else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle(LesxMessage.getMessage("TEXT-ALERT_DIALOG_TITLE_ERROR"));
      alert.setHeaderText(LesxMessage.getMessage("TEXT-ALERT_DIALOG_CONTEXT_REPORT_FUNCTION_SELECT_NULL"));
      alert.showAndWait();
    }
  }

  /**
   * Adds a given function to the model
   */
  private void addFunction() {
    if (functionVBox.getFunction()
        .getType() == null) {
      emptyFunctionError("'Función'");
      return;
    }
    if (functionVBox.getFunction()
        .getType() == ELesxFunction.PERIOD
        && LesxMisc.isEmpty(functionVBox.getFunction()
            .getValue())) {
      emptyFunctionError("'Periodo'");
      return;
    }
    if (currentReportTree != null) {
      final Long key = dataModelReport.addFunction(currentReportTree.getId(), functionVBox.getFunction(), currentCostomers);
      refreshReportFunctionTable();
      tableFunction.getSelectionModel()
          .select(key);
      pendingChanges.set(true);
    }
    else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle(LesxMessage.getMessage("TEXT-ALERT_DIALOG_TITLE_ERROR"));
      alert.setHeaderText(LesxMessage.getMessage("TEXT-ALERT_DIALOG_CONTEXT_REPORT_TREE_SELECT_NULL"));
      alert.showAndWait();
    }
  }

  private void emptyFunctionError(String string) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(LesxMessage.getMessage("TEXT-ALERT_DIALOG_TITLE_ERROR"));
    alert.setHeaderText(LesxMessage.getMessage("ERROR-NO_NULL_VALUE", string));
    alert.showAndWait();
  }

  /**
   * Triggered when an item is selected/unselected from the Function Table
   */
  private void functionTableSelected() {
    Long keySelected = tableFunction.getSelectionModel()
        .getSelectedItem();
    functionSelected = keySelected != null ? dataModelReport.getReportItem(keySelected) : null;
    functionVBox.loadFunction(functionSelected != null ? functionSelected.getFunction() : null);
  }

  /**
   * Fills the Data of Functions of the selected Tree Report
   */
  @SuppressWarnings("unchecked")
  private void fillDataOnTableFunctions() {
    //Config. Columns.
    typeItemColumn = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_NAME"));
    typeItemColumn.setCellValueFactory(new Callback<CellDataFeatures<Long, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<Long, String> data) {
        return new SimpleStringProperty(dataModelReport.getFunction(data.getValue())
            .getFunction()
            .getType()
            .toString());
      }
    });
    valueItem = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_VALUE"));
    valueItem.setCellValueFactory(new Callback<CellDataFeatures<Long, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<Long, String> data) {
        return new SimpleStringProperty(getValueFunction(data.getValue()));
      }
    });
    tableFunction.getColumns()
        .addAll(typeItemColumn, valueItem);
    tableFunction.setItems(functionList);
    typeItemColumn.prefWidthProperty()
        .bind(tableFunction.widthProperty()
            .divide(2));
    valueItem.prefWidthProperty()
        .bind(tableFunction.widthProperty()
            .divide(2));
  }

  protected String getValueFunction(Long value) {
    String stringValue = "";
    if (currentReportTree != null) {
      stringValue = dataModelReport.generateReport(value, dataModelPrice.getPrices(currentCostomers));
    }
    return stringValue;
  }

  /**
   * Fills the Data of Costumer of the selected Tree Report
   */
  @SuppressWarnings("unchecked")
  private void fillDataOnTableCostumer() {
    //Config. Columns.
    nameColumn = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_NAME"));
    nameColumn.setCellValueFactory(new Callback<CellDataFeatures<Long, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<Long, String> data) {
        return new SimpleStringProperty(dataModelCostumer.getCostumer(data.getValue())
            .getName());
      }
    });
    cc = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_CC"));
    cc.setCellValueFactory(new Callback<CellDataFeatures<Long, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<Long, String> data) {
        return new SimpleStringProperty(dataModelCostumer.getCostumer(data.getValue())
            .getCc()
            .toString());
      }
    });
    locations = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_LOCATION"));
    locations.setCellValueFactory(new Callback<CellDataFeatures<Long, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<Long, String> data) {
        return new SimpleStringProperty(ELesxLocations.getLocation(dataModelCostumer.getCostumer(data.getValue())
            .getLocation())
            .toString());
      }
    });
    tableCostomer.getColumns()
        .addAll(nameColumn, cc, locations);
    tableCostomer.setItems(currentCostomers);
    nameColumn.prefWidthProperty()
        .bind(tableCostomer.widthProperty()
            .divide(3));
    cc.prefWidthProperty()
        .bind(tableCostomer.widthProperty()
            .divide(3));
    locations.prefWidthProperty()
        .bind(tableCostomer.widthProperty()
            .divide(3));
  }

  /**
   * Initializes Data Models
   *
   * @param dataModel Costumer data Model, is needed since main page already got it.
   */
  public void init(LesxCostumerDataModel dataModel) {
    dataModelCostumer = dataModel;
    dataModelPrice = new LesxPriceDataModel(LesxMain.getInstance()
        .getDbProperty()
        .getPriceMap());
    dataModelReport = new LesxReportsDataModel(LesxMain.getInstance()
        .getDbProperty()
        .getReportTree(),
        LesxMain.getInstance()
            .getDbProperty()
            .getReportItems());
  }

  /**
   * Adds data on Tree Report View Pane
   */
  private void fillDataOnTree() {
    tree.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> selectedReportTree(tree.getSelectionModel()
            .getSelectedItem() != null));
    tree.setRoot(null);
    tree.setShowRoot(false);
    refrestDataOnTree();
  }

  /**
   * Refresh the data in the tree
   */
  private void refrestDataOnTree() {
    TreeItem<LesxReportTree> rootItem = LesxReportTree.createRootTreeItem();
    Set<LesxReportTree> reportTrees = dataModelReport.getSetReportTree();
    Map<Long, TreeItem<LesxReportTree>> mapTree = new HashMap<>();
    Set<Long> addedReportTree = mapTree.keySet();
    mapTree.put(rootItem.getValue()
        .getId(), rootItem);
    rootItem.setExpanded(true);
    TreeItem<LesxReportTree> leafItem;
    for (LesxReportTree reportLeaf : reportTrees) {
      if (reportLeaf.getId() == rootItem.getValue()
          .getId()) {
        continue;
      }
      if (!addedReportTree.contains(reportLeaf.getId())) {
        leafItem = new TreeItem<>(reportLeaf);
        mapTree.put(reportLeaf.getId(), leafItem);
        addMissingParentItem(reportLeaf, mapTree, leafItem);
      }
    }
    tree.setRoot(rootItem);
  }

  /**
   * If a Tree Item is selected/unselected this method is triggered, modifying the current costumer list and current
   * selected tree
   *
   * @param isSelected Report Tree
   */
  private void selectedReportTree(boolean isSelected) {
    selectedTableCostumer.set(isSelected);
    if (isSelected) {
      currentReportTree = tree.getSelectionModel()
          .getSelectedItem()
          .getValue();
      refreshCostumerTable();
      refreshReportFunctionTable();
    }
    else {
      currentCostomers.clear();
      functionList.clear();
      currentReportTree = null;
    }
    dataModelReport.setSelectedReportTree(currentReportTree);
  }

  /**
   * Refresh the items in the functionList
   */
  private void refreshReportFunctionTable() {
    if (currentReportTree != null) {
      functionList.clear();
      functionList.addAll(dataModelReport.getFunctions(currentReportTree));
    }
  }

  /**
   * Recursive method to fill Tree data with no duplicates parents key
   *
   * @param reportLeaf LesxReportTree needed to track the parent and map values of it.
   * @param mapTree Map of available children with item values
   * @param leafItem Tree item value to add
   */
  private void addMissingParentItem(LesxReportTree reportLeaf, Map<Long, TreeItem<LesxReportTree>> mapTree, TreeItem<LesxReportTree> leafItem) {
    if (mapTree.containsKey(reportLeaf.getParent_id())) {
      mapTree.get(reportLeaf.getParent_id())
          .getChildren()
          .add(leafItem);
      return;
    }
    LesxReportTree parentReportMissing = dataModelReport.getReportTree(reportLeaf.getParent_id());
    TreeItem<LesxReportTree> parentReportTree = new TreeItem<>(parentReportMissing);
    parentReportTree.getChildren()
        .add(leafItem);
    mapTree.put(parentReportMissing.getId(), parentReportTree);
    addMissingParentItem(parentReportMissing, mapTree, parentReportTree);
  }

  @Override
  protected boolean showAlert() {
    return false;
  }

  @Override
  public String getTitle() {
    return LesxMessage.getMessage("TEXT-REPORT_TITLE");
  }

  @Override
  protected void onCloseWindow() {
    if (pendingChanges.get()) {
      LesxMain.getInstance()
          .getDbProperty()
          .saveReportXML(dataModelReport.getReportTreeMap(), dataModelReport.getReportItemMap(), () -> closeWindow());
    }
  }

}
