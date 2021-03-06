package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.impl.java.spring.converter;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.components.DataTypeAttribute;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.converter.Converter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.models.TypeDefinition;
import org.apache.commons.lang3.StringUtils;

public class JavaSpringConverter extends Converter {

  @Override
  public TypeDefinition convertSqlTypeToNative(DataTypeAttribute dataTypeAttribute) {
    switch (dataTypeAttribute) {
      case BOOLEAN:
        return new TypeDefinition("Boolean");
      case INTEGER:
        return new TypeDefinition("Integer");
      case REAL:
        return new TypeDefinition("Double");
      case DATE:
        return new TypeDefinition("LocalDate", "java.time.LocalDate");
      case DATETIME:
        return new TypeDefinition("LocalDateTime", "java.time.LocalDateTime");
      case TIME:
        return new TypeDefinition("LocalTime", "java.time.LocalTime");
      case TIMESTAMP:
      case INTERVAL:
        return new TypeDefinition("Instant", "java.time.Instant");
      case ZONED_DATETIME:
        return new TypeDefinition("ZonedDateTime", "java.time.ZonedDateTime");
      case STRING:
      default:
        return new TypeDefinition("String");
    }
  }

  @Override
  public String initializeSqlDataInNativeVariable(DataTypeAttribute dataTypeAttribute, String value) {
    switch (dataTypeAttribute) {
      case STRING:
        return '"' + value + '"';
      case BOOLEAN:
        return (value.equals("true") || value.equals("1")) ? "true" : "false";
      case INTEGER:
      case REAL:
        return value;
      case DATE:
        return String.format("LocalDate.parse(\"%s\")", value);
      case DATETIME:
        return String.format("LocalDateTime.parse(\"%s\")", value.replace(' ', 'T'));
      case TIME:
        return String.format("LocalTime.parse(\"%s\")", value);
      case TIMESTAMP:
      case INTERVAL:
        return String.format("Instant.parse(\"%s\")", value);
      case ZONED_DATETIME:
        return String.format("ZonedDateTime.parse(\"%s\")", value.replace(' ', 'T'));
      default:
        return "null";
    }
  }

  @Override
  public String convertSqlTableNameToNativeClassName(Token tableName) {
    return convertToCamelCase(tableName.getValue());
  }

  @Override
  public String convertSqlTableColumnToNativeAttributeName(String columnName) {
    return convertToCamelCase(columnName);
  }

  private String convertToCamelCase(String value) {
    // Convert to lowercase if everything is uppercase
    if (value.equals(value.toUpperCase())) {
      value = value.toLowerCase();
    }
    // Convert snake_case to space separated words
    if (value.contains("_")) {
      value = value.replace('_', ' ');
    }
    // Set first letter to uppercase
    value = StringUtils.capitalize(value);

    // If value contains space - transform it to CamelCase
    // Also remove all non alphabetic or numeric values
    StringBuilder camelCase = new StringBuilder();
    boolean upperCase = false;
    for (char character : value.toCharArray()) {
      if (Character.isWhitespace(character)) {
        upperCase = true;
        continue;
      }
      if (!Character.isAlphabetic(character) && !Character.isDigit(character)) {
        continue;
      }
      if (upperCase) {
        camelCase.append(Character.toUpperCase(character));
        upperCase = false;
      } else {
        camelCase.append(character);
      }
    }

    return camelCase.toString();
  }
}
