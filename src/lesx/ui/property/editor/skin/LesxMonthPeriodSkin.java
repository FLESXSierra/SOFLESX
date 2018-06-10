package lesx.ui.property.editor.skin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.sun.javafx.scene.control.skin.TextFieldSkin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import lesx.ui.property.editor.LesxMonthPeriodEditor;
import lesx.utils.LesxMisc;

public class LesxMonthPeriodSkin extends TextFieldSkin {

  private static final String INVALID_STYLE = "-fx-text-fill: red;";
  private static final String VALID_STYLE = "-fx-text-fill: -fx-text-inner-color;";
  private static final String PROMPT_TEXT = "MM.yyyy - MM.yyyy";
  // Patterns
  private static final String PATTERN_MONTH = "[0-1]";
  private static final String PATTERN_ALL = "[0-9]";

  // Data
  private final StringProperty showingText = new SimpleStringProperty(this, "showingText", "");
  private final LesxMonthPeriodEditor editor;
  private final List<Integer> caretDot = Arrays.asList(2, 12);
  private final List<Integer> caretSeparator = Arrays.asList(7, 8, 9);

  public LesxMonthPeriodSkin(LesxMonthPeriodEditor editor) {
    super(editor);
    this.editor = editor;
    editor.setPromptText(PROMPT_TEXT);
    editor.setOnKeyPressed(obs -> typeKeyOnTextValue(obs.getCode(), editor.getCaretPosition()));
    editor.focusedProperty()
        .addListener(obs -> addFocusedValue());
    editor.textProperty()
        .bind(showingText);
    editor.resetProperty()
        .addListener(obs -> resetValues());
    editor.refreshProperty()
        .addListener(obs -> refreshValue());
    validate();
  }

  /**
   * Refresh the value that's been set from outside the Skin
   */
  private void refreshValue() {
    showingText.setValue(editor.getValue());
    validate();
  }

  /**
   * Reset the values and set back the prompt text;
   */
  private void resetValues() {
    showingText.set("");
    editor.setValue("");
    validate();
  }

  /**
   * Gets the Key pressed and process it
   *
   * @param keyCode of the key pressed
   * @param caretPosition position of the text
   */
  private void typeKeyOnTextValue(KeyCode keyCode, int caretPosition) {
    String key = getCodeName(keyCode);
    boolean isDelete = KeyCode.BACK_SPACE.equals(keyCode);
    if (isDelete) {
      caretPosition -= 1;
    }
    else if (caretPosition == PROMPT_TEXT.length()) {
      return;
    }
    if (Pattern.matches(PATTERN_ALL, key) || isDelete) {
      if (caretDot.contains(caretPosition)) {
        caretPosition = isDelete ? caretPosition - 1 : caretPosition + 1;
      }
      if (caretSeparator.contains(caretPosition)) {
        caretPosition = isDelete ? 6 : 10;
      }
      if (caretPosition < 0) {
        caretPosition = 0;
      }
      else if (caretPosition > (PROMPT_TEXT.length() - 1)) {
        caretPosition = PROMPT_TEXT.length() - 1;
      }
      if (isDelete || varifyCharacterPattern(key.charAt(0), caretPosition)) {
        final StringBuilder newText = new StringBuilder(showingText.get());
        newText.setCharAt(caretPosition, isDelete ? PROMPT_TEXT.charAt(caretPosition) : key.charAt(0));
        showingText.set(newText.toString());
        validate();
        editor.positionCaret((isDelete ? caretPosition : caretPosition + 1));
      }
    }
  }

  /**
   * Gets the name of the Key Code, needed for NumPad names.
   *
   * @param keyCode key pressed
   * @return String of the name.
   */
  private String getCodeName(KeyCode keyCode) {
    switch (keyCode) {
      case NUMPAD0:
        return "0";
      case NUMPAD1:
        return "1";
      case NUMPAD2:
        return "2";
      case NUMPAD3:
        return "3";
      case NUMPAD4:
        return "4";
      case NUMPAD5:
        return "5";
      case NUMPAD6:
        return "6";
      case NUMPAD7:
        return "7";
      case NUMPAD8:
        return "8";
      case NUMPAD9:
        return "9";
      default:
        return keyCode.getName();
    }
  }

  private void addFocusedValue() {
    if (editor.isFocused()) {
      if (LesxMisc.isEmpty(editor.getValue())) {
        if (LesxMisc.isEmpty(showingText.get())) {
          showingText.set(PROMPT_TEXT);
        }
      }
      else {
        showingText.set(editor.getValue());
      }
      editor.selectPositionCaret(0);
    }

  }

  /**
   * Validates the Dates given, can't be incomplete, also it need to be validated by the parsing of the local date, and
   * end date can't come first than initial one.
   */
  private void validate() {
    if (LesxMisc.isEmpty(showingText.get())) {
      editor.setValid(false);
      editor.setStyle(INVALID_STYLE);
    }
    else {
      final String value = showingText.get();
      int cont = 0;
      boolean valid = true;
      for (char charVal : value.toCharArray()) {
        valid = varifyCharacterPattern(charVal, cont);
        if (!valid) {
          break;
        }
        cont++;
      }
      boolean validParse = true;
      try {
        LocalDate ini = LocalDate.parse("01." + showingText.get()
            .split("-")[0].trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        LocalDate end = LocalDate.parse("01." + showingText.get()
            .split("-")[1].trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        if (end.isBefore(ini)) {
          validParse = false;
        }
      }
      catch (DateTimeParseException ex) {
        validParse = false;
      }
      if (valid && validParse) {
        editor.setValue(showingText.get());
        editor.setValid(true);
        editor.setStyle(VALID_STYLE);
      }
      else {
        editor.setValue("");
        editor.setValid(false);
        editor.setStyle(INVALID_STYLE);
      }
    }

  }

  /**
   * Verifies the value of a character with the pattern "MM.yyyy - MM.yyyy"
   *
   * @param charVal the character needed to be validated
   * @param cont the position where the value is.
   * @return boolean
   */
  private boolean varifyCharacterPattern(char charVal, int cont) {
    List<Integer> allPatterns = Arrays.asList(1, 3, 4, 5, 6, 11, 13, 14, 15, 16);
    if (cont == 0 || cont == 10) {
      if (!Pattern.matches(PATTERN_MONTH, "" + charVal)) {
        return false;
      }
    }
    if (allPatterns.contains(cont)) {
      if (!Pattern.matches(PATTERN_ALL, "" + charVal)) {
        return false;
      }
    }
    if (cont == 2 || cont == 12) {
      if (!".".equals("" + charVal)) {
        return false;
      }
    }
    if (cont == 7 || cont == 9) {
      if (!" ".equals("" + charVal)) {
        return false;
      }
    }
    if (cont == 8 && !"-".equals("" + charVal)) {
      return false;
    }
    return true;
  }

}
