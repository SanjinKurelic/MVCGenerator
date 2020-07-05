package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.converter;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.components.DataTypeAttribute;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.models.TypeDefinition;

public abstract class Converter {

  public abstract TypeDefinition convertSqlTypeToNative(DataTypeAttribute dataTypeAttribute);

  public abstract String convertSqlTableNameToNativeClassName(Token tableName);

  public abstract String convertSqlTableColumnToNativeAttributeName(String columnName);

  public String convertSqlTableColumnToNativeAttributeName(Token columnName) {
    return convertSqlTableColumnToNativeAttributeName(columnName.getValue());
  }
}
