package lesx.ui.prices.dialog;

import java.util.Arrays;

import javafx.fxml.FXML;
import lesx.gui.message.LesxMessage;
import lesx.property.price.LesxPriceDataModel;
import lesx.property.properties.LesxPrice;
import lesx.property.properties.LesxProperty;
import lesx.ui.costumer.dialog.LesxCostumerDialogPaneController;
import lesx.ui.costumer.dialog.propertysheet.LesxPropertySheet;
import lesx.ui.mainpage.datamodel.LesxCostumerDataModel;
import lesx.utils.LesxString;

public class LesxPriceDialogPaneController extends LesxCostumerDialogPaneController {

  private LesxPriceDataModel dataModel;
  private LesxPrice price;
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
    dataModel.setPriceSelected(price);
    init(dataModel, isCreate);
  }

  public void init(LesxPriceDataModel dataModel, boolean isCreate) {
    setTitle(LesxMessage.getMessage("TEXT-TITLE_PRICE_DIALOG_CREATE"));
    this.dataModel = dataModel;
    this.isCreate = isCreate;
    if (isCreate) {
      price = new LesxPrice();
      price.setId(dataModel.createNewKeyForProperty(LesxString.PROPERTY_ID));
      price.setResource_id(dataModel.getResourceId());
      getDescriptionLabel().setText(LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_NEW_PRICE"));
    }
    else {
      price = dataModel.getPriceSelected();
      getDescriptionLabel().setText(LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_EDIT_PRICE"));
    }
    getDefaultName().setText(getHeader(isCreate));
    propertySheet = new LesxPropertySheet(getPropertiesPane(), getEditorsPane(), price,
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
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_PRICE_PANE", "Nuevo"));
      string.append(".");
    }
    else {
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_PRICE_PANE", price.getName()));
      string.append(".");
    }
    return string.toString();
  }

  @Override
  protected boolean isDuplicate() {
    return dataModel.addPrice(Arrays.asList(price));
  }

  @Override
  protected LesxPropertySheet getPropertySheet() {
    return propertySheet;
  }

  @Override
  protected String getComponentName() {
    return price.getName() == null ? "*No Name*" : price.getName();
  }

}
