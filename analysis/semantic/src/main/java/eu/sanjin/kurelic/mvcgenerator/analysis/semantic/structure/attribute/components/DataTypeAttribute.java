package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.components;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.DataTypeToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.SemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.UnexpectedDataTypeSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.column.DataType;

public enum DataTypeAttribute {

  NULL(0), STRING(1), BOOLEAN(2), INTEGER(3), REAL(4),
  DATE(5), DATETIME(7), TIME(6), TIMESTAMP(8), INTERVAL(9), ZONED_DATETIME(10);

  private final int order;
  DataTypeAttribute(int order) {
    this.order = order;
  }

  public int getOrder() {
    return order;
  }

  public static DataTypeAttribute convertToDataTypeAttribute(DataType dataType) throws SemanticException {
    return convertToDataTypeAttribute(dataType.getType().getValue());
  }

  public static DataTypeAttribute convertToDataTypeAttribute(String dataTypeValue) throws SemanticException {
    if (DataTypeToken.isBitType(dataTypeValue)) {
      return BOOLEAN;
    }
    if (DataTypeToken.isIntegerNumber(dataTypeValue)) {
      return INTEGER;
    }
    if (DataTypeToken.isRealNumber(dataTypeValue)) {
      return REAL;
    }
    if (DataTypeToken.isCharacter(dataTypeValue) || DataTypeToken.isNationalCharacter(dataTypeValue)) {
      return STRING;
    }
    if (DataTypeToken.isDateTime(dataTypeValue)) {
      switch (DataTypeToken.valueOf(dataTypeValue)) {
        case DATE:
          return DATE;
        case DATETIME:
          return DATETIME;
        case TIME:
          return TIME;
        case TIMESTAMP:
          return TIMESTAMP;
      }
    }
    throw new UnexpectedDataTypeSemanticException(dataTypeValue);
  }

  public static boolean isTimeType(DataTypeAttribute dataTypeAttribute) {
    return DataTypeAttribute.TIME.equals(dataTypeAttribute) || DataTypeAttribute.TIMESTAMP.equals(dataTypeAttribute)
      || DataTypeAttribute.DATETIME.equals(dataTypeAttribute) || DataTypeAttribute.INTERVAL.equals(dataTypeAttribute);
  }

  public static boolean isDateType(DataTypeAttribute dataTypeAttribute) {
    return DataTypeAttribute.DATE.equals(dataTypeAttribute) || DataTypeAttribute.DATETIME.equals(dataTypeAttribute)
      || DataTypeAttribute.TIMESTAMP.equals(dataTypeAttribute) || DataTypeAttribute.INTERVAL.equals(dataTypeAttribute)
      || DataTypeAttribute.ZONED_DATETIME.equals(dataTypeAttribute);
  }

  public static boolean isDateTimeType(DataTypeAttribute dataTypeAttribute) {
    return isTimeType(dataTypeAttribute) || isDateType(dataTypeAttribute);
  }

  public static boolean isDateAndTimeType(DataTypeAttribute dataTypeAttribute) {
    return DataTypeAttribute.DATETIME.equals(dataTypeAttribute) || DataTypeAttribute.TIMESTAMP.equals(dataTypeAttribute)
      || DataTypeAttribute.ZONED_DATETIME.equals(dataTypeAttribute);
  }

  public static boolean isNumber(DataTypeAttribute dataTypeAttribute) {
    return DataTypeAttribute.INTEGER.equals(dataTypeAttribute) || DataTypeAttribute.REAL.equals(dataTypeAttribute);
  }
}
