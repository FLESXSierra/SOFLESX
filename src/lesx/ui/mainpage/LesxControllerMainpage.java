package lesx.ui.mainpage;

import static lesx.property.properties.ELesxLocations.COLOMBIA;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxLocations;
import lesx.property.properties.LesxCostumer;
import lesx.scene.controller.LesxController;
import lesx.scene.controller.LesxSceneController;
import lesx.ui.components.LesxTableViewPane;
import lesx.ui.components.LesxTreeViewPane;
import lesx.ui.mainpage.datamodel.LesxCostumerDataModel;
import lesx.utils.LesxString;
import main.LesxMain;

public class LesxControllerMainpage extends LesxController {
  @FXML
  LesxTreeViewPane<ELesxLocations> treePane;
  @FXML
  LesxTableViewPane<LesxCostumer> tablePane;
  @FXML
  MenuBar menu;
  @FXML
  Menu file;
  @FXML
  MenuItem saveXML;
  @FXML
  MenuItem exportXML;
  @FXML
  Menu edit;
  @FXML
  MenuItem prices;
  @FXML
  MenuItem report;
  @FXML
  Menu help;
  @FXML
  HBox progress;

  private final static Logger LOGGER = Logger.getLogger(LesxControllerMainpage.class.getName());

  //TODO agregar columnas a medida que se agregen más properties.
  private TableColumn<LesxCostumer, String> nameColumn;
  private TableColumn<LesxCostumer, String> cc;
  private TableColumn<LesxCostumer, String> locations;
  // Components
  private TreeView<ELesxLocations> tree;
  private TableView<LesxCostumer> table;
  // data
  private Map<Long, LesxCostumer> data;
  private LesxCostumerDataModel dataModel;
  private final ObservableList<LesxCostumer> currentList = FXCollections.observableArrayList();
  private BooleanProperty pendingChanges = new SimpleBooleanProperty(this, "pendingChanges");
  private BooleanProperty selectedItemTable = new SimpleBooleanProperty(this, "selectedItemTable");
  //Runnables
  private Runnable onDelete;
  private Consumer<Boolean> onAdd;

  public LesxControllerMainpage() {

  }

  public LesxControllerMainpage(Stage stage) {
    super(stage);
  }

  @FXML
  public void initialize() {
    setTitle(LesxMessage.getMessage("TEXT-TITLE_MAINPAGE"));
    dataModel = new LesxCostumerDataModel(LesxMain.getInstance()
        .getDbProperty()
        .getCostumerMap());
    data = dataModel.getData();
    tree = treePane.getTree();
    table = tablePane.getTable();
    initializeMenuBars();
    fillDataOnTable();
    fillDataOnTree();
    filterTable();
    installListeners();
    setVisibleProgress(false);
  }

  private void installListeners() {
    treePane.childrenAlsoProperty()
        .addListener(obs -> filterTable());
    tree.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> {
          boolean isSelected = tree.getSelectionModel()
              .getSelectedItem() != null;
          if (isSelected) {
            dataModel.setLocationsSelected(tree.getSelectionModel()
                .getSelectedItem()
                .getValue());
          }
          else {
            dataModel.setLocationsSelected(null);
          }
          filterTable();
        });
    table.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> selectedItemTable());
    createRunnables();
  }

  private void createRunnables() {
    onDelete = () -> {
      dataModel.deleteSelectedCostumer();
      pendingChanges.set(true);
      filterTable();
      fillDataOnTree();
    };
    tablePane.setOnDelete(onDelete);
    onAdd = (isCreate) -> addNewCostumer(isCreate);
    tablePane.setOnAddNewItem(onAdd);
  }

  private void initializeMenuBars() {
    file.setText(LesxMessage.getMessage("TEXT-MENUBAR_FILE"));
    saveXML.setText(LesxMessage.getMessage("TEXT-MENUITEM_FILE_SAVE"));
    saveXML.setOnAction(obs -> saveXML());
    exportXML.setText(LesxMessage.getMessage("TEXT-MENUITEM_EXPORT_SAVE"));
    exportXML.setOnAction(obs -> exportXML());
    prices.setText(LesxMessage.getMessage("TEXT-MENUITEM_EDIT_PRICES"));
    prices.setOnAction(obs -> openPrices());
    report.setText(LesxMessage.getMessage("TEXT-MENUITEM_REPORTS"));
    report.setOnAction(obs -> openReport());
    edit.setText(LesxMessage.getMessage("TEXT-MENUBAR_EDIT"));
    help.setText(LesxMessage.getMessage("TEXT-MENUBAR_HELP"));
  }

  private void exportXML() {
    final FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.xml)", "*.xml");
    fileChooser.setTitle("Save file");
    fileChooser.setSelectedExtensionFilter(extFilter);
    fileChooser.setInitialFileName(LesxString.XML_NAME_COSTUMER);
    final File dest = fileChooser.showSaveDialog(LesxMain.getInstance()
        .getStage());
    if (dest != null) {
      try {
        String path = dest.getPath()
            .replaceAll(dest.getName(), LesxString.XML_NAME_COSTUMER);
        File destination = new File(path);
        final File costumer = new File(LesxString.XML_PATH);
        Files.copy(costumer.toPath(), destination.toPath());

        final File price = new File(LesxString.XML_PRICE_PATH);
        path = dest.getPath()
            .replaceAll(dest.getName(), LesxString.XML_NAME_PRICE);
        destination = new File(path);
        Files.copy(price.toPath(), destination.toPath());

        final File tree = new File(LesxString.XML_REPORT_TREE_PATH);
        path = dest.getPath()
            .replaceAll(dest.getName(), LesxString.XML_NAME_TREE);
        destination = new File(path);
        Files.copy(tree.toPath(), destination.toPath());

        final File items = new File(LesxString.XML_REPORT_ITEMS_PATH);
        path = dest.getPath()
            .replaceAll(dest.getName(), LesxString.XML_NAME_ITEMS);
        destination = new File(path);
        Files.copy(items.toPath(), destination.toPath());
      }
      catch (FileAlreadyExistsException e) {
        LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FILES_ALREADY_EXIST"));
        Alert alert = new Alert(AlertType.WARNING);
        alert.setHeaderText(LesxMessage.getMessage("WARNING-FILES_ALREADY_EXIST"));
        alert.show();
      }
      catch (IOException ex) {
        LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-EXPORT_XMLS"), ex);
        ex.printStackTrace();
      }
    }
  }

  private void openReport() {
    LesxSceneController.showReportDialog(this, dataModel);
  }

  private void addNewCostumer(boolean isCreate) {
    LesxSceneController.showCostumerDialog(this, isCreate, dataModel, () -> {
      pendingChanges.set(true);
      filterTable();
      fillDataOnTree();
    });
  }

  /**
   * Filter the table, right now only from TreeView can be filtered.
   */
  private void filterTable() {
    ELesxLocations location = COLOMBIA;
    if (tree.getSelectionModel()
        .getSelectedItem() != null) {
      location = tree.getSelectionModel()
          .getSelectedItem()
          .getValue();
    }
    if (location != null) {
      currentList.setAll(dataModel.getValuesByLocation(location, treePane.isChildrenAlso()));
    }
    else {
      currentList.setAll(data.values());
    }
    table.refresh();
  }

  /**
   * Adds data on table.
   */
  @SuppressWarnings("unchecked")
  private void fillDataOnTable() {
    //Config. Columns.
    nameColumn = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_NAME"));
    nameColumn.setCellValueFactory(new Callback<CellDataFeatures<LesxCostumer, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxCostumer, String> data) {
        return new SimpleStringProperty(data.getValue()
            .getName());
      }
    });
    cc = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_CC"));
    cc.setCellValueFactory(new Callback<CellDataFeatures<LesxCostumer, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxCostumer, String> data) {
        return new SimpleStringProperty(data.getValue()
            .getCc()
            .toString());
      }
    });
    locations = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_LOCATION"));
    locations.setCellValueFactory(new Callback<CellDataFeatures<LesxCostumer, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxCostumer, String> data) {
        return new SimpleStringProperty(ELesxLocations.getLocation(data.getValue()
            .getLocation())
            .toString());
      }
    });
    nameColumn.prefWidthProperty()
        .bind(table.widthProperty()
            .divide(3));
    cc.prefWidthProperty()
        .bind(table.widthProperty()
            .divide(3));
    locations.prefWidthProperty()
        .bind(table.widthProperty()
            .divide(3));
    table.getColumns()
        .addAll(nameColumn, cc, locations);
    table.setItems(currentList);
  }

  private void selectedItemTable() {
    selectedItemTable.set(table.getSelectionModel()
        .getSelectedItem() != null);
    if (table.getSelectionModel() != null) {
      dataModel.setCostumerSelected(table.getSelectionModel()
          .getSelectedItem());
    }
    else {
      dataModel.setCostumerSelected(null);
    }
  }

  /**
   * Adds data on TreeView
   */
  private void fillDataOnTree() {
    tree.setRoot(null);
    TreeItem<ELesxLocations> rootItem = new TreeItem<ELesxLocations>(COLOMBIA);
    Set<ELesxLocations> locations = dataModel.getDataLocations();
    Map<ELesxLocations, TreeItem<ELesxLocations>> mapTree = new HashMap<>();
    Set<ELesxLocations> addedLocation = mapTree.keySet();
    mapTree.put(COLOMBIA, rootItem);
    rootItem.setExpanded(true);
    TreeItem<ELesxLocations> leafItem;
    for (ELesxLocations location : locations) {
      if (location == COLOMBIA) {
        continue;
      }
      if (!addedLocation.contains(location)) {
        leafItem = new TreeItem<ELesxLocations>(location);
        mapTree.put(location, leafItem);
        addMissingParentItem(location, mapTree, leafItem);
      }
    }
    tree.setRoot(rootItem);
  }

  /**
   * Recursive method to fill Tree data with no duplicates parents key
   *
   * @param location ELesxLocations needed to track the parent and map values of it.
   * @param mapTree Map of available children with item values
   * @param leafItem Tree item value to add
   */
  private void addMissingParentItem(ELesxLocations location, Map<ELesxLocations, TreeItem<ELesxLocations>> mapTree, TreeItem<ELesxLocations> leafItem) {
    if (mapTree.containsKey(ELesxLocations.getParentLocation(location))) {
      mapTree.get(ELesxLocations.getParentLocation(location))
          .getChildren()
          .add(leafItem);
      return;
    }
    ELesxLocations parentMissing = ELesxLocations.getParentLocation(location);
    TreeItem<ELesxLocations> parentTree = new TreeItem<ELesxLocations>(parentMissing);
    parentTree.getChildren()
        .add(leafItem);
    mapTree.put(parentMissing, parentTree);
    addMissingParentItem(parentMissing, mapTree, parentTree);
  }

  private void saveXML() {
    if (pendingChanges.get()) {
      progress.setVisible(true);
      dataModel.persistData(() -> progress.setVisible(false));
      pendingChanges.set(false);
    }
  }

  @Override
  protected void onCloseWindow() {
    if (pendingChanges.get()) {
      progress.setVisible(true);
      dataModel.persistData(() -> {
        progress.setVisible(false);
        closeWindow();
      });
    }
    else {
      closeWindow();
    }
  }

  private void setVisibleProgress(boolean visible) {
    progress.setCursor(visible ? Cursor.WAIT : Cursor.DEFAULT);
    progress.setVisible(visible);
  }

  private void openPrices() {
    LesxSceneController.showPricesDialog(this);
  }

}
