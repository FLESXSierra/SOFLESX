package lesx.ui.property.editor;

import lesx.property.properties.LesxProperty;
import lesx.ui.property.editor.formatter.LesxLongFormatter;

public class LesxLongEditor extends LesxTextEditor {

  public LesxLongEditor(boolean needDot, LesxProperty fxProperty) {
    super(fxProperty);
    LesxLongFormatter format = new LesxLongFormatter(needDot);
    setTextFormatter(format.getFormat());
  }
}
