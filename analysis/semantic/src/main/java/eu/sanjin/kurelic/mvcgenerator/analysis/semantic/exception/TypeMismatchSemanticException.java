package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;

public class TypeMismatchSemanticException extends SemanticException {

  private static final String COLUMN_TYPE_NO_MATCH_ERROR = "Column type does not match. Column with name %s have type %s but column with name %s have type %s";

  public TypeMismatchSemanticException(ColumnAttribute column1, ColumnAttribute column2) {
    super(String.format(
      COLUMN_TYPE_NO_MATCH_ERROR,
      column1.getColumnName().getValue(),
      column1.getDataType().name(),
      column2.getColumnName().getValue(),
      column2.getDataType().name()
    ));
  }
}
