package lesx.ui.costumer.dialog.propertysheet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxPropertyType;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxProperty;
import lesx.ui.property.editor.LesxIntegerEditor;
import lesx.ui.property.editor.LesxLocationEditor;
import lesx.ui.property.editor.LesxLongEditor;
import lesx.ui.property.editor.LesxPeriodDatePicker;
import lesx.ui.property.editor.LesxPriceTypeEditor;
import lesx.ui.property.editor.LesxTextEditor;
import lesx.ui.property.label.LesxPropertyLabel;
import lesx.utils.LesxMisc;
import lesx.utils.LesxString;

public class LesxPropertySheet {

  private VBox wrapperName;
  private VBox wrapperEditor;
  private LesxComponent component;
  private BooleanProperty pendingChanges = new SimpleBooleanProperty(this, "pendingChanges");
  private BooleanProperty valid = new SimpleBooleanProperty(this, "valid");
  private BiPredicate<Long, LesxProperty> uniqueSearch;

  public BooleanProperty validProperty() {
    return valid;
  }

  public boolean isValid() {
    return valid.get();
  }

  public LesxPropertySheet(Pane propertyNamePane, Pane propertyEditorPane, LesxComponent property, BiPredicate<Long, LesxProperty> uniqueSearch) {
    component = property;
    wrapperName = new VBox();
    wrapperName.setAlignment(Pos.TOP_RIGHT);
    wrapperName.setSpacing(5);
    wrapperName.setPadding(new Insets(5, 2, 5, 5));
    wrapperEditor = new VBox();
    wrapperEditor.setAlignment(Pos.TOP_LEFT);
    wrapperEditor.setSpacing(5);
    wrapperEditor.setPadding(new Insets(5, 5, 5, 2));
    init();
    validate();
    propertyNamePane.getChildren()
        .add(wrapperName);
    propertyEditorPane.getChildren()
        .add(wrapperEditor);
    AnchorPane.setBottomAnchor(wrapperName, 0.0);
    AnchorPane.setLeftAnchor(wrapperName, 0.0);
    AnchorPane.setRightAnchor(wrapperName, 0.0);
    AnchorPane.setTopAnchor(wrapperName, 0.0);
    AnchorPane.setBottomAnchor(wrapperEditor, 0.0);
    AnchorPane.setLeftAnchor(wrapperEditor, 0.0);
    AnchorPane.setRightAnchor(wrapperEditor, 0.0);
    AnchorPane.setTopAnchor(wrapperEditor, 0.0);
    this.uniqueSearch = uniqueSearch;
    pendingChanges.set(false);
  }

  private void init() {
    //Preparing property sheet
    StringBuilder string = new StringBuilder(254);
    for (LesxProperty fxProperty : component.getPropertyValues()) {
      if (!fxProperty.isVisible()) {
        continue;
      }
      //Add tool bar and Label name
      string.setLength(0);
      if (fxProperty.isMandatory() || fxProperty.isUnique()) {
        List<String> specialString = new ArrayList<>();
        if (fxProperty.isMandatory()) {
          specialString.add("M");
        }
        if (fxProperty.isUnique()) {
          specialString.add("U");
        }
        string.append("(");
        string.append(specialString.stream()
            .collect(Collectors.joining(",")));
        string.append(") ");
      }
      string.append(fxProperty.getName());
      string.append(" : ");
      final LesxPropertyLabel nombre = new LesxPropertyLabel(string.toString());
      nombre.setTooltip(installToolTip(fxProperty));
      nombre.setPadding(new Insets(4, 0, 4, 0));
      wrapperName.getChildren()
          .add(nombre);

      //adding Listener
      fxProperty.getPropertyValue()
          .addListener(obs -> validate());

      // Add Editor
      switch (fxProperty.getType()) {
        case INTEGER:
          final LesxIntegerEditor integerEditor = new LesxIntegerEditor(fxProperty);
          nombre.getValidProperty()
              .bind(integerEditor.validProperty());
          wrapperEditor.getChildren()
              .add(integerEditor);
          break;
        case LONG:
          final LesxLongEditor longEditor = new LesxLongEditor(LesxMisc.equals(fxProperty.getName(), LesxString.PROPERTY_PRICE), fxProperty);
          nombre.getValidProperty()
              .bind(longEditor.validProperty());
          wrapperEditor.getChildren()
              .add(longEditor);
          break;
        case TEXT:
          final LesxTextEditor textEditor = new LesxTextEditor(fxProperty);
          nombre.getValidProperty()
              .bind(textEditor.validProperty());
          wrapperEditor.getChildren()
              .add(textEditor);
          break;
        case LOCATION:
          final LesxLocationEditor locationEditor = new LesxLocationEditor(fxProperty);
          nombre.getValidProperty()
              .bind(locationEditor.validProperty());
          wrapperEditor.getChildren()
              .add(locationEditor);
          break;
        case PRICE_TYPE:
          final LesxPriceTypeEditor priceTypeEditor = new LesxPriceTypeEditor(fxProperty);
          nombre.getValidProperty()
              .bind(priceTypeEditor.validProperty());
          wrapperEditor.getChildren()
              .add(priceTypeEditor);
          break;
        case DATE:
          final LesxPeriodDatePicker dateEditor = new LesxPeriodDatePicker(fxProperty);
          nombre.getValidProperty()
              .bind(dateEditor.validProperty());
          wrapperEditor.getChildren()
              .add(dateEditor);
        default:
          break;
      }
    }
  }

  private void validate() {
    boolean allValid = true;
    for (LesxProperty property : component.getPropertyValues()) {
      // Validate value on location editor
      if (ELesxPropertyType.LOCATION.equals(property.getType())) {
        //Keep validation if is not valid
        if (!property.isValid()) {
          allValid = false;
          continue;
        }
      }
      // Validate Mandatory
      if (property.isMandatory() && LesxMisc.isEmpty(property.getValue())) {
        allValid = false;
        property.setValid(false);
        continue;
      }
      // Validate unique
      if (property.isUnique() && uniqueSearch != null && (!uniqueSearch.test((Long) component.getPropertyByName(LesxString.PROPERTY_ID)
          .getValue(), property))) {
        allValid = false;
        property.setValid(false);
        continue;
      }
      property.setValid(true);
    }
    valid.set(allValid);
    pendingChanges.set(true);
  }

  private Tooltip installToolTip(LesxProperty fxProperty) {
    final StringBuilder string = new StringBuilder(1024);
    string.append(LesxMessage.getMessage("TEXT-TOOLTIP_TYPE_PROPERTY", fxProperty.getType()
        .toString()));
    string.append("\n");
    Tooltip tool = new Tooltip();
    tool.setWrapText(true);
    tool.setWidth(250);
    if (fxProperty.isMandatory() || fxProperty.isUnique()) {
      string.append("Restricción:\n");
      if (fxProperty.isMandatory()) {
        string.append(" - ");
        string.append(LesxMessage.getMessage("TEXT-TOOLTIP_MANDATORY_PROPERTY"));
        string.append("\n");
      }
      if (fxProperty.isUnique()) {
        string.append(" - ");
        string.append(LesxMessage.getMessage("TEXT-TOOLTIP_UNIQUE_PROPERTY"));
        string.append("\n");
      }
    }
    tool.setText(string.toString());
    return tool;
  }

  public BooleanProperty getPendingChangesProperty() {
    return pendingChanges;
  }

  public void setPendingChanges(boolean pendingChanges) {
    this.pendingChanges.set(pendingChanges);
  }

  public LesxComponent getComponent() {
    return component.clone();
  }

}
