package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.structure;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ExtendedColumnAttribute extends ColumnAttribute {

  private static final Logger LOG = Logger.getLogger(ExtendedColumnAttribute.class.getName());

  public ExtendedColumnAttribute(ColumnAttribute columnAttribute) {
    try {
      BeanUtils.copyProperties(this, columnAttribute);
    } catch (IllegalAccessException | InvocationTargetException e) {
      LOG.log(Level.SEVERE, e.getMessage(), e);
    }
  }
}
