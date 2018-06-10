package lesx.ui.costumer.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxCostumer;
import lesx.scene.controller.LesxController;
import lesx.ui.components.LesxTableViewPane;
import lesx.ui.mainpage.datamodel.LesxCostumerDataModel;
import lesx.ui.reports.datamodel.LesxReportsDataModel;

public class LesxSelectCostumerDialogController extends LesxController {

  @FXML
  Label header;
  @FXML
  LesxTableViewPane<LesxCostumer> costumerTable;
  @FXML
  Button add;
  @FXML
  Button cancel;

  // Table
  private TableView<LesxCostumer> tableCostumer;
  //Columns
  private TableColumn<LesxCostumer, String> nameColumn;
  private TableColumn<LesxCostumer, String> cc;
  // Data Models
  private LesxCostumerDataModel dataModelCostumer;
  private LesxReportsDataModel dataModelReports;
  //Data
  private BooleanProperty closeProperty = new SimpleBooleanProperty(this, "closeProperty");
  private List<Long> selectedCostumers = new ArrayList<>();

  @FXML
  public void initialize() {
    setTitle(LesxMessage.getMessage("TEXT-TITLE_SELECT_COSTUMER_DIALOG"));
    costumerTable.setUseCase(ELesxUseCase.UC_READ_ONLY);
    tableCostumer = costumerTable.getTable();
    tableCostumer.getSelectionModel()
        .setSelectionMode(SelectionMode.MULTIPLE);
    tableCostumer.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> selectedCostumers());
    add.setText(LesxMessage.getMessage("TEXT-BUTTON_ADD"));
    cancel.setText(LesxMessage.getMessage("TEXT-BUTTON_CANCEL"));
  }

  private void selectedCostumers() {
    selectedCostumers.clear();
    if (tableCostumer.getSelectionModel()
        .getSelectedItems() != null) {
      selectedCostumers.addAll(tableCostumer.getSelectionModel()
          .getSelectedItems()
          .stream()
          .filter(costumer -> costumer != null)
          .map(costumer -> costumer.getId())
          .collect(Collectors.toList()));
    }
  }

  public void init(LesxCostumerDataModel dataModelCostumer, LesxReportsDataModel dataModelReports) {
    this.dataModelCostumer = dataModelCostumer;
    this.dataModelReports = dataModelReports;
    cancel.setOnAction(obs -> close());
    add.setOnAction(obs -> saveCostumers());
    fillDataCostumerTable();
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
    nameColumn.prefWidthProperty()
        .bind(tableCostumer.widthProperty()
            .divide(5));
    cc.prefWidthProperty()
        .bind(tableCostumer.widthProperty()
            .divide(5));
    tableCostumer.getColumns()
        .addAll(nameColumn, cc);
    tableCostumer.getItems()
        .addAll(dataModelCostumer.getCostumers());
  }

  /**
   * Saves the costumers selected.
   *
   * @return
   */
  private void saveCostumers() {
    dataModelReports.addCostumersToCurrentReport(selectedCostumers);
    close();
  }

  /**
   * Closes the dialog.
   */
  private void close() {
    closeProperty.set(true);
  }

  public BooleanProperty closeProperty() {
    return closeProperty;
  }

}
