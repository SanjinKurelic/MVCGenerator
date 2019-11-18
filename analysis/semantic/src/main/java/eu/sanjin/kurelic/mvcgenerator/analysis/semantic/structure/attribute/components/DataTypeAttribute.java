package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.components;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.DataTypeToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.column.DataType;

public enum DataTypeAttribute {

  BOOLEAN, DATE, DATETIME, INTEGER, REAL, STRING, TIME, TIMESTAMP, INTERVAL;

  public static DataTypeAttribute convertToDataTypeAttribute(DataType dataType) {
    return convertToDataTypeAttribute(dataType.getType().getValue());
  }

  public static DataTypeAttribute convertToDataTypeAttribute(String dataTypeValue) {
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
    return null;
  }
}
