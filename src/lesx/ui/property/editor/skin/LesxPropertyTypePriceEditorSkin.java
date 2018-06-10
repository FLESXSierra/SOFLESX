package lesx.ui.property.editor.skin;

import com.sun.javafx.scene.control.skin.CheckBoxSkin;

import javafx.geometry.Insets;
import lesx.gui.message.LesxMessage;
import lesx.ui.property.editor.LesxPriceTypeEditor;

public class LesxPropertyTypePriceEditorSkin extends CheckBoxSkin {

  private static final String INVALID_STYLE = "-fx-text-fill: red;";
  private static final String VALID_STYLE = "-fx-text-fill: -fx-text-inner-color;";
  LesxPriceTypeEditor checkBox;

  public LesxPropertyTypePriceEditorSkin(LesxPriceTypeEditor editor) {
    super(editor);
    editor.validProperty()
        .addListener(obs -> validChangeProperty(editor.isValid()));
    editor.selectedProperty()
        .addListener(obs -> valueChangeProperty());
    editor.indeterminateProperty()
        .addListener(obs -> valueChangeProperty());
    editor.setPadding(new Insets(5, 0, 5, 0));
    checkBox = editor;
    valueChangeProperty();
  }

  private void valueChangeProperty() {
    if (getSkinnable().isIndeterminate()) {
      getSkinnable().setText(LesxMessage.getMessage("TEXT-PROPERTY_PRICE_EDITOR_NULL"));
      checkBox.setValue(null);
    }
    else {
      checkBox.setValue(getSkinnable().isSelected());
      if (getSkinnable().isSelected()) {
        getSkinnable().setText(LesxMessage.getMessage("TEXT-PROPERTY_PRICE_EDITOR_TRUE"));
      }
      else {
        getSkinnable().setText(LesxMessage.getMessage("TEXT-PROPERTY_PRICE_EDITOR_FALSE"));
      }
    }
  }

  private void validChangeProperty(boolean valid) {
    if (valid) {
      getSkinnable().setStyle(VALID_STYLE);
    }
    else {
      getSkinnable().setStyle(INVALID_STYLE);
    }
  }

}
