package lesx.gui.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import lesx.utils.LesxMisc;

public class LesxMessage {
  private final static Logger LOGGER = Logger.getLogger(LesxMessage.class.getName());

  public static String getMessage(String key) {
    Properties properties = new Properties();

    try {
      File file = new File("src\\" + LesxMessage.class.getName()
          .replace('.', '\\'));
      String filename = file.getAbsolutePath();
      if (LesxMisc.isEmpty(filename)) {
        LOGGER.severe("No se encuentra el archivo 'LesxMessage', por favor comuniquese con el admin.");
        return "";
      }
      properties.load(new FileInputStream(filename));
      Object value = properties.get(key);
      if (!LesxMisc.isEmpty(value)) {
        return String.valueOf(value);
      }
      LOGGER.warning(LesxMessage.getMessage("LOGGER-WARNING_NO_KEY", key));
      return "";
    }
    catch (FileNotFoundException e) {
      LOGGER.severe("No se encuentra el archivo 'LesxMessage', por favor comuniquese con el admin.");
      e.printStackTrace();
      return "";
    }
    catch (IOException e) {
      LOGGER.severe("Por favor comuniquese con el admin.");
      e.printStackTrace();
      return "";
    }
  }

  public static String getMessage(String key, Object... args) {
    String value = getMessage(key);
    if (args == null) {
      LOGGER.warning("argument is null on key: '" + key + "'");
      return value;
    }
    for (int i = 0; i < args.length; i++) {
      if (args[i] != null) {
        String sequence = "{" + String.valueOf(i) + "}";
        if (value.contains(sequence)) {
          value = value.replace(sequence, String.valueOf(args[i]));
        }
        else {
          LOGGER.warning("key '" + key + "' does not contain more arguments to replace");
        }
      }
      else {
        LOGGER.warning("argument is null on key: " + key);
      }
    }
    return value;
  }

}
