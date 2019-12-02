package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.components.DataTypeAttribute;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TypeMismatchSemanticException extends SemanticException {

  private static final String TWO_COLUMN_TYPE_NO_MATCH_ERROR = "Column type does not match. Column with name %s have type %s but column with name %s have type %s";
  private static final String TYPE_NO_MATCH_ERROR = "Type does not match. Expecting type %s, got %s, at line %d";
  private static final String JOIN_DELIMITER = "or";

  public TypeMismatchSemanticException(ColumnAttribute column1, ColumnAttribute column2) {
    super(String.format(
      TWO_COLUMN_TYPE_NO_MATCH_ERROR,
      column1.getColumnName().getValue(),
      column1.getDataType().name(),
      column2.getColumnName().getValue(),
      column2.getDataType().name()
    ));
  }

  public TypeMismatchSemanticException(DataTypeAttribute type, int lineNumber, DataTypeAttribute... expected) {
    super(String.format(
      TYPE_NO_MATCH_ERROR,
      Stream.of(expected).map(DataTypeAttribute::name).collect(Collectors.joining(JOIN_DELIMITER)),
      type.name(),
      lineNumber
    ));
  }
}
