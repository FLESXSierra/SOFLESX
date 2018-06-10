package lesx.ui.prices;

import java.util.function.Consumer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import lesx.gui.message.LesxMessage;
import lesx.property.price.LesxPriceDataModel;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxCostumer;
import lesx.property.properties.LesxPrice;
import lesx.scene.controller.LesxController;
import lesx.scene.controller.LesxSceneController;
import lesx.ui.components.LesxTableViewPane;
import lesx.ui.mainpage.datamodel.LesxCostumerDataModel;
import main.LesxMain;

public class LesxPricesDialogController extends LesxController {

  @FXML
  Label header;
  @FXML
  LesxTableViewPane<LesxCostumer> paneTableCostumer;
  @FXML
  LesxTableViewPane<LesxPrice> paneTablePrice;

  //Columns
  private TableColumn<LesxCostumer, String> nameColumn;
  private TableColumn<LesxCostumer, String> cc;
  private TableColumn<LesxPrice, String> namePrice;
  private TableColumn<LesxPrice, String> idPrice;
  private TableColumn<LesxPrice, String> totalPrice;
  private TableColumn<LesxPrice, String> priceType;
  private TableColumn<LesxPrice, String> validFrom;
  // data
  private LesxCostumerDataModel dataModel;
  private LesxPriceDataModel dataModelPrice;
  private final ObservableList<LesxPrice> currentList = FXCollections.observableArrayList();
  private BooleanProperty pendingChanges = new SimpleBooleanProperty(this, "pendingChanges", false);
  //tables
  private TableView<LesxCostumer> tableCostumer;
  private TableView<LesxPrice> tablePrice;
  private BooleanProperty selectedTableCostumer = new SimpleBooleanProperty(this, "selectedTableCostumer", false);
  //Consumer
  private Consumer<Boolean> addNewPrice;

  @FXML
  public void initialize() {
    setTitle(LesxMessage.getMessage("TEXT-TITLE_PRICES_PAGE"));
    header.setText(LesxMessage.getMessage("TEXT-PRICE_DIALOG_HEADER"));
    paneTableCostumer.setUseCase(ELesxUseCase.UC_READ_ONLY);
    dataModel = new LesxCostumerDataModel(LesxMain.getInstance()
        .getDbProperty()
        .getCostumerMap());
    dataModelPrice = new LesxPriceDataModel(LesxMain.getInstance()
        .getDbProperty()
        .getPriceMap());
    tableCostumer = paneTableCostumer.getTable();
    tablePrice = paneTablePrice.getTable();
    fillDataCostumerTable();
    addListeners();
    filterTable();
  }

  private void filterTable() {
    LesxCostumer selectedCostumer = tableCostumer.getSelectionModel()
        .getSelectedItem();
    selectedTableCostumer.set(false);
    currentList.clear();
    if (selectedCostumer != null) {
      selectedTableCostumer.set(true);
      dataModelPrice.setResourceId(selectedCostumer.getId());
      currentList.addAll(dataModelPrice.getPrices(selectedCostumer.getId()));
    }
  }

  private void addListeners() {
    tableCostumer.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> filterTable());
    addNewPrice = isCreate -> addNewPrice(isCreate);
    paneTablePrice.setOnDelete(() -> ondeletePrice());
    paneTablePrice.setOnAddNewItem(addNewPrice);
    paneTablePrice.selectedFilterTableProperty()
        .bind(selectedTableCostumer);
    tablePrice.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> dataModelPrice.setPriceSelected(tablePrice.getSelectionModel()
            .getSelectedItem()));
  }

  private void ondeletePrice() {
    dataModelPrice.deletePrice(tablePrice.getSelectionModel()
        .getSelectedItem()
        .getId());
    pendingChanges.set(true);
  }

  private void addNewPrice(boolean isCreate) {
    LesxSceneController.showPriceEditDialog(this, isCreate, dataModelPrice, () -> {
      pendingChanges.set(true);
      filterTable();
    });
  }

  @SuppressWarnings("unchecked")
  private void fillDataCostumerTable() {
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
    namePrice = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_PRICE"));
    namePrice.setCellValueFactory(new Callback<CellDataFeatures<LesxPrice, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxPrice, String> data) {
        return new SimpleStringProperty(data.getValue()
            .getName());
      }
    });
    idPrice = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_ID_PRICE"));
    idPrice.setCellValueFactory(new Callback<CellDataFeatures<LesxPrice, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxPrice, String> data) {
        return new SimpleStringProperty(data.getValue()
            .getId()
            .toString());
      }
    });
    totalPrice = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_TOTAL_PRICE"));
    totalPrice.setCellValueFactory(new Callback<CellDataFeatures<LesxPrice, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxPrice, String> data) {
        return new SimpleStringProperty(String.valueOf(data.getValue()
            .getTotal()));
      }
    });
    priceType = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_PRICE_TYPE"));
    priceType.setCellValueFactory(new Callback<CellDataFeatures<LesxPrice, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxPrice, String> data) {
        return new SimpleStringProperty(Boolean.TRUE.equals(Boolean.valueOf(data.getValue()
            .getTypePrice())) ? LesxMessage.getMessage("TEXT-PROPERTY_PRICE_EDITOR_TRUE") : LesxMessage.getMessage("TEXT-PROPERTY_PRICE_EDITOR_FALSE"));
      }
    });
    validFrom = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_VALID_FROM"));
    validFrom.setCellValueFactory(new Callback<CellDataFeatures<LesxPrice, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxPrice, String> data) {
        return new SimpleStringProperty(data.getValue()
            .getValidFrom());
      }
    });
    nameColumn.prefWidthProperty()
        .bind(tableCostumer.widthProperty()
            .divide(2));
    cc.prefWidthProperty()
        .bind(tableCostumer.widthProperty()
            .divide(2));
    tableCostumer.getColumns()
        .addAll(nameColumn, cc);
    tableCostumer.getItems()
        .addAll(dataModel.getCostumers());

    idPrice.prefWidthProperty()
        .bind(tablePrice.widthProperty()
            .multiply(0.1));
    namePrice.prefWidthProperty()
        .bind(tablePrice.widthProperty()
            .multiply(0.3));
    totalPrice.prefWidthProperty()
        .bind(tablePrice.widthProperty()
            .multiply(0.25));
    priceType.prefWidthProperty()
        .bind(tablePrice.widthProperty()
            .multiply(0.15));
    validFrom.prefWidthProperty()
        .bind(tablePrice.widthProperty()
            .multiply(0.2));
    tablePrice.getColumns()
        .addAll(idPrice, namePrice, totalPrice, priceType, validFrom);
    tablePrice.setItems(currentList);
  }

  @Override
  protected void onCloseWindow() {
    if (pendingChanges.get()) {
      LesxMain.getInstance()
          .getDbProperty()
          .savePriceXML(dataModelPrice.getPriceMap(), () -> closeWindow());
    }
  }

}
