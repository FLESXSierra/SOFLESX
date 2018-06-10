package lesx.property.properties;

import java.util.logging.Level;
import java.util.logging.Logger;

import lesx.gui.message.LesxMessage;
import lesx.property.report.LesxFunctionXMLParser;

public class LesxReportFunction implements Cloneable {
  private final static Logger LOGGER = Logger.getLogger(LesxReportFunction.class.getName());
  private Long id;
  private ELesxFunction type;
  private Object value;

  public LesxReportFunction() {
    id = -1L;
  }

  public LesxReportFunction(LesxFunctionXMLParser parser) {
    type = parser.getType();
    value = parser.getValue();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public ELesxFunction getType() {
    return type;
  }

  public void setType(ELesxFunction type) {
    this.type = type;
  }

  public void reset() {
    type = null;
    id = -1L;
    value = null;
  }

  public LesxReportFunction clone() {
    try {
      LesxReportFunction function = (LesxReportFunction) super.clone();
      if (this.type != null) {
        function.type = ELesxFunction.getFunction(this.type.get());
      }
      return function;
    }
    catch (CloneNotSupportedException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-PROPERTY_NOT_CLONNABLE", this), e);
      e.printStackTrace();
      return new LesxReportFunction();
    }
  }

}
