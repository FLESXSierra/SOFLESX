package lesx.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import lesx.gui.message.LesxMessage;
import lesx.utils.LesxMisc;

public class LesxLogger extends Handler {

  @Override
  public void publish(LogRecord record) {
    StringBuilder log = new StringBuilder();
    Date date = new Date(record.getMillis());
    DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
    String dateFormatted = formatter.format(date);
    //WELCOME!
    if (record.getLevel() == Level.INFO && "Initializing".equals(record.getMessage())) {
      log.append("     ******************************************************\n");
      log.append("      *                     WELCOME                      *\n");
      log.append("      *                  LESX SOFTWARE                   *\n");
      log.append("      *                     SOFLEX                       *\n");
      log.append("     ******************************************************\n");
      log.append(dateFormatted + " : Initializing");
      System.out.println(log.toString());
      return;
    }
    if (record.getLevel() == Level.SEVERE) {
      log.append(LesxMessage.getMessage("LOGGER-ERROR_MESSAGE", dateFormatted));
    }
    else if (record.getLevel() == Level.WARNING) {
      log.append(LesxMessage.getMessage("LOGGER-WARNING_MESSAGE", dateFormatted));
    }
    else {
      log.append(LesxMessage.getMessage("LOGGER-LOG_MESSAGE", dateFormatted));
    }
    log.append(record.getLoggerName() + " : ");
    log.append(record.getMessage());
    if (record.getThrown() != null && !LesxMisc.isEmpty(record.getThrown()
        .getMessage())) {
      log.append("\n");
      log.append(record.getThrown()
          .getMessage());
    }
    System.out.println(log.toString());
  }

  @Override
  public void flush() {
  }

  @Override
  public void close() throws SecurityException {
  }

}
