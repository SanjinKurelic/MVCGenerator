package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.TableAttribute;

public class NoPrimaryKeyTargetCodeException extends TargetCodeException {

  private static final String NO_PRIMARY_KEY_ERROR = "Unable to create CRUD operations on entity %s because it does not have any primary keys";

  public NoPrimaryKeyTargetCodeException(TableAttribute tableAttribute) {
    super(String.format(NO_PRIMARY_KEY_ERROR, tableAttribute.getTableName().getValue()));
  }
}
