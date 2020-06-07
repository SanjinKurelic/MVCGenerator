package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;

import java.util.HashMap;

public class TableAttribute {

  private Token tableName;
  private CheckAttribute checkAttribute;
  private final HashMap<String, ColumnAttribute> columns;

  public TableAttribute() {
    columns = new HashMap<>();
    checkAttribute = new CheckAttribute();
  }

  public Token getTableName() {
    return tableName;
  }

  public void setTableName(Token tableName) {
    this.tableName = tableName;
  }

  public CheckAttribute getCheckAttribute() {
    return checkAttribute;
  }

  public void setCheckAttribute(CheckAttribute checkAttribute) {
    this.checkAttribute = checkAttribute;
  }

  public void addCheckExpression(Expression checkExpression) {
    checkAttribute.addExpression(checkExpression);
  }

  public HashMap<String, ColumnAttribute> getColumns() {
    return columns;
  }

  public boolean hasColumn(ColumnAttribute columnAttribute) {
    return hasColumn(columnAttribute.getColumnName().getValue());
  }

  public boolean hasColumn(String columnName) {
    return columns.containsKey(columnName);
  }

  public void addColumn(ColumnAttribute columnAttribute) {
    columns.put(columnAttribute.getColumnName().getValue(), columnAttribute);
  }

  public ColumnAttribute getColumn(String columnName) {
    return columns.get(columnName);
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("=".repeat(80)).append("\n");
    stringBuilder.append("= ").append(tableName.getValue()).append("\n");
    stringBuilder.append("=".repeat(80)).append("\n");

    columns.forEach((columnName, column) -> stringBuilder.append(column.toString()).append("\n"));

    stringBuilder.append("=".repeat(80)).append("\n");
    stringBuilder.append(checkAttribute.toString());
    stringBuilder.append("=".repeat(80));

    return stringBuilder.toString();
  }
}
