package lesx.ui.property.editor.formatter;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;
import lesx.utils.LesxMisc;

public class LesxNumberFormatter {

  private static TextFormatter<Integer> format;

  public LesxNumberFormatter() {
    UnaryOperator<Change> integerFilter = change -> {
      String text = change.getControlNewText();
      if (text.matches("\\d{0,9}")) {
        return change;
      }
      return null;
    };
    format = new TextFormatter<Integer>(new NumberConverter(), null, integerFilter);
  }

  public TextFormatter<Integer> getFormat() {
    return format;
  }

  public class NumberConverter extends StringConverter<Integer> {

    @Override
    public Integer fromString(String string) {
      if (LesxMisc.equals("null", string) || LesxMisc.isEmpty(string)) {
        return null;
      }
      return Integer.parseInt(string);
    }

    @Override
    public String toString(Integer object) {
      return object == null ? "" : String.valueOf(object);
    }

  }

}
