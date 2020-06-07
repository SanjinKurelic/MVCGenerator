package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public enum DataTypeToken {

  CHAR(DataTypeClass.CHARACTER), CHARACTER(DataTypeClass.CHARACTER), VARCHAR(DataTypeClass.CHARACTER),
  NCHAR(DataTypeClass.NATIONAL_CHARACTER), NVARCHAR(DataTypeClass.NATIONAL_CHARACTER),

  BIGINT(DataTypeClass.INTEGER_NUMBER), INT(DataTypeClass.INTEGER_NUMBER),
  INTEGER(DataTypeClass.INTEGER_NUMBER), MEDIUMINT(DataTypeClass.INTEGER_NUMBER), NUMERIC(DataTypeClass.INTEGER_NUMBER),
  SMALLINT(DataTypeClass.INTEGER_NUMBER), TINYINT(DataTypeClass.INTEGER_NUMBER),

  BOOLEAN(DataTypeClass.BIT_TYPE),

  DECIMAL(DataTypeClass.REAL_NUMBER), DEC(DataTypeClass.REAL_NUMBER), DOUBLE(DataTypeClass.REAL_NUMBER),
  FLOAT(DataTypeClass.REAL_NUMBER), REAL(DataTypeClass.REAL_NUMBER),

  DATE(DataTypeClass.DATE_TIME), DATETIME(DataTypeClass.DATE_TIME), TIME(DataTypeClass.DATE_TIME),
  TIMESTAMP(DataTypeClass.DATE_TIME);

  private final DataTypeClass type;

  DataTypeToken(DataTypeClass type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return name().toLowerCase();
  }

  /**
   * @param token with set value
   * @return DataTypeToken
   * @throws IllegalArgumentException if value is not found
   */
  public static DataTypeToken valueOf(Token token) {
    return DataTypeToken.valueOf(DataTypeToken.class, token.getValue().toUpperCase());
  }

  public DataTypeClass getRootType() {
    return type;
  }

  public static boolean contains(String value) {
    value = value.toLowerCase();
    for (DataTypeToken type : values()) {
      if (type.name().toLowerCase().equals(value)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isType(DataTypeClass type, String value) {
    value = value.toLowerCase();
    for (DataTypeToken t : values()) {
      if (t.toString().equals(value)) {
        return t.getRootType() == type;
      }
    }
    return false;
  }

  public static boolean isCharacter(String value) {
    return isType(DataTypeClass.CHARACTER, value);
  }

  public static boolean isNationalCharacter(String value) {
    return isType(DataTypeClass.NATIONAL_CHARACTER, value);
  }

  public static boolean isIntegerNumber(String value) {
    return isType(DataTypeClass.INTEGER_NUMBER, value);
  }

  public static boolean isBitType(String value) {
    return isType(DataTypeClass.BIT_TYPE, value);
  }

  public static boolean isRealNumber(String value) {
    return isType(DataTypeClass.REAL_NUMBER, value);
  }

  public static boolean isDateTime(String value) {
    return isType(DataTypeClass.DATE_TIME, value);
  }
}
