package lesx.ui.property.editor.formatter;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;
import lesx.utils.LesxMisc;

public class LesxLongFormatter {

  private static TextFormatter<Long> format;

  public LesxLongFormatter(boolean needDot) {
    UnaryOperator<Change> integerFilter = change -> {
      String text = change.getControlNewText();
      if ((!needDot && text.matches("\\d{0,11}")) || (needDot && text.matches("\\d{0,9}([\\.]\\d{0,2})?"))) {
        return change;
      }
      return null;
    };
    format = new TextFormatter<Long>(new NumberConverter(), null, integerFilter);
  }

  public TextFormatter<Long> getFormat() {
    return format;
  }

  public class NumberConverter extends StringConverter<Long> {

    @Override
    public Long fromString(String string) {
      if (LesxMisc.equals("null", string) || LesxMisc.isEmpty(string)) {
        return null;
      }
      return Long.parseLong(string);
    }

    @Override
    public String toString(Long object) {
      return object == null ? "" : String.valueOf(object);
    }

  }

}
