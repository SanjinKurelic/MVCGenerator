package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.parser;

import com.x5.template.Chunk;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.TableAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.reference.ReferenceAction;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.NoPrimaryKeyTargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.TargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetSettings;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.models.ColumnAttributeViewModel;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.models.TemplateAttributeNames;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.models.TypeDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class TargetCodeParser {

  private final SemanticAttributeTable semanticAttributeTable;
  private TargetCodeSupplier targetCodeSupplier;
  private TargetSettings targetSettings;

  public TargetCodeParser(SemanticAttributeTable semanticAttributeTable) {
    this.semanticAttributeTable = semanticAttributeTable;
  }

  public void parse(TargetSettings targetSettings) throws TargetCodeException {
    targetCodeSupplier = new TargetCodeSupplier(targetSettings);
    this.targetSettings = targetSettings;

    // generate project structure
    targetCodeSupplier.getProjectWriter().generateProjectStructure(targetSettings);

    for(TableAttribute tableAttribute : semanticAttributeTable.getTables().values()) {
      var primaryKeys = getPrimaryKeys(tableAttribute);

      if (primaryKeys.isEmpty()) {
        throw new NoPrimaryKeyTargetCodeException(tableAttribute);
      }

      targetCodeSupplier.setCommonAttributes(tableAttribute.getTableName(), (primaryKeys.size() > 1));

      // generate controller
      var idDefinition = generateController(tableAttribute, primaryKeys);

      // generate service
      generateService(tableAttribute, idDefinition);

      // generate repository
      generateRepository(tableAttribute, idDefinition);

      // generate entity
      generateEntity(tableAttribute, idDefinition, false);

      // generate entity id if composite
      if (primaryKeys.size() > 1) {
        generateEntity(tableAttribute, idDefinition, true);
      }
    }
  }

  private TypeDefinition generateController(TableAttribute tableAttribute, Map<String, ColumnAttribute> primaryKeys) throws TargetCodeException {
    TargetCodeType targetCodeType = TargetCodeType.CONTROLLER;
    List<String> extraParams = new ArrayList<>();
    HashMap<String, String> idParameters = new HashMap<>();

    // Logic
    primaryKeys.forEach((name, column) -> {
      var dataType = targetCodeSupplier.getConverter().convertSqlTypeToNative(column.getDataType());

      idParameters.put(
        StringUtils.uncapitalize(targetCodeSupplier.getConverter().convertSqlTableColumnToNativeAttributeName(name)),
        dataType.getName()
      );

      if (!Objects.isNull(dataType.getNamespace()) && !dataType.getNamespace().isEmpty()) {
        extraParams.add(dataType.getNamespace());
      }
    });

    // Generate template
    Chunk template = targetCodeSupplier.getTemplate(targetCodeType);
    template.set(TemplateAttributeNames.ID_PARAMETERS, idParameters);
    template.set(TemplateAttributeNames.EXTRA_IMPORTS, extraParams);

    // Write
    targetCodeSupplier.getOutputWriter().writeFileInNamespace(targetCodeType, getFileName(targetCodeType, tableAttribute), template.toString());

    // Define id class and import statement
    TypeDefinition idDefinition = new TypeDefinition();
    idDefinition.setVariableName(StringUtils.capitalize(idParameters.keySet().toArray()[0].toString()));
    if (idParameters.size() > 1) {
      // Composite
      idDefinition.setName(String.format("%sId", targetCodeSupplier.getConverter().convertSqlTableColumnToNativeAttributeName(tableAttribute.getTableName())));
    } else {
      idDefinition.setName(idParameters.values().toArray()[0].toString());
      // Define class import
      if (!extraParams.isEmpty()) {
        idDefinition.setNamespace(extraParams.get(0));
      }
    }
    return idDefinition;
  }

  private void generateService(TableAttribute tableAttribute, TypeDefinition idDefinition) throws TargetCodeException {
    TargetCodeType targetCodeType = TargetCodeType.SERVICE;
    List<String> extraParams = new ArrayList<>();

    // Append id class imports
    if (!Objects.isNull(idDefinition.getNamespace())) {
      extraParams.add(idDefinition.getNamespace());
    }

    // Every language has different style of writing
    var validations = targetCodeSupplier.getValidationWriter().transformCheckExpressionToNativeIf(tableAttribute.getCheckAttribute().getCheckExpressions());

    // Generate template
    Chunk template = targetCodeSupplier.getTemplate(targetCodeType);
    template.set(TemplateAttributeNames.ID_CLASS, idDefinition.getName());
    template.set(TemplateAttributeNames.ID_NAME, idDefinition.getVariableName());
    template.set(TemplateAttributeNames.EXTRA_IMPORTS, extraParams);
    template.set(TemplateAttributeNames.VALIDATION_RULES, validations);

    // Write
    targetCodeSupplier.getOutputWriter().writeFileInNamespace(targetCodeType, getFileName(targetCodeType, tableAttribute), template.toString());
  }

  private void generateRepository(TableAttribute tableAttribute, TypeDefinition idDefinition) throws TargetCodeException {
    TargetCodeType targetCodeType = TargetCodeType.REPOSITORY;

    // Generate template
    Chunk template = targetCodeSupplier.getTemplate(targetCodeType);
    template.set(TemplateAttributeNames.ID_CLASS, idDefinition.getName());
    if (!Objects.isNull(idDefinition.getNamespace())) {
      template.set(TemplateAttributeNames.EXTRA_IMPORTS, idDefinition.getNamespace());
    }

    // Write
    targetCodeSupplier.getOutputWriter().writeFileInNamespace(targetCodeType, getFileName(targetCodeType, tableAttribute), template.toString());
  }

  private void generateEntity(TableAttribute tableAttribute, TypeDefinition idDefinition, boolean compositeEntity) throws TargetCodeException {
    TargetCodeType targetCodeType = TargetCodeType.ENTITY;
    List<ColumnAttributeViewModel> attributes = new ArrayList<>();
    List<String> extraParams = new ArrayList<>();

    tableAttribute.getColumns().forEach((name, column) -> {
      // Use only primary key columns for composite entity flag
      if (compositeEntity && !column.isPrimary()) {
        return; // skip
      }

      // Convert attributes
      var attribute = new ColumnAttributeViewModel();
      attribute.setModel(column);
      attribute.setName(targetCodeSupplier.getConverter().convertSqlTableColumnToNativeAttributeName(column.getColumnName()));
      attribute.setNameLowercaseFirst(StringUtils.uncapitalize(attribute.getName()));

      // Set data type
      var typeDefinition = new TypeDefinition();
      if (column.isForeign()) {
        attribute.setForeignColumn(targetCodeSupplier.getConverter().convertSqlTableColumnToNativeAttributeName(column.getForeignColumn()));
        attribute.setForeignTable(targetCodeSupplier.getConverter().convertSqlTableNameToNativeClassName(column.getForeignTable()));
        // Set cascade
        attribute.setCascadeDelete(!ReferenceAction.NO_ACTION.equals(column.getForeignDeleteAction()));
        attribute.setCascadeUpdate(!ReferenceAction.NO_ACTION.equals(column.getForeignUpdateAction()));
        // Define data type as foreign table entity
        typeDefinition.setName(targetCodeSupplier.getConverter().convertSqlTableNameToNativeClassName(column.getForeignTable()));
        typeDefinition.setNamespace(String.format("%s.entity.%s;", targetSettings.getRootNamespace(), typeDefinition.getName()));
      } else {
        typeDefinition = targetCodeSupplier.getConverter().convertSqlTypeToNative(column.getDataType());
      }
      attribute.setType(typeDefinition.getName());
      if (!Objects.isNull(typeDefinition.getNamespace())) {
        extraParams.add(typeDefinition.getNamespace());
      }
      if (!Objects.isNull(column.getDefaultValue())) {
        attribute.setValue(targetCodeSupplier.getConverter().initializeSqlDataInNativeVariable(column.getDataType(), column.getDefaultValue().getValue()));
      }

      attributes.add(attribute);
    });

    // Generate template
    Chunk template = targetCodeSupplier.getTemplate(targetCodeType);
    template.set(TemplateAttributeNames.ID_CLASS, idDefinition.getName());
    template.set(TemplateAttributeNames.EXTRA_IMPORTS, extraParams);
    template.set(TemplateAttributeNames.COLUMN_ATTRIBUTES, attributes);
    template.set(TemplateAttributeNames.ENTITY_REAL_NAME, tableAttribute.getTableName().getValue());
    template.set(TemplateAttributeNames.COMPOSITE_ENTITY, compositeEntity);

    // Write
    targetCodeSupplier.getOutputWriter().writeFileInNamespace(targetCodeType, getFileName(targetCodeType, tableAttribute), template.toString());
  }

  private Map<String, ColumnAttribute> getPrimaryKeys(TableAttribute tableAttribute) {
    return tableAttribute.getColumns()
      .entrySet()
      .stream()
      .filter(e -> e.getValue().isPrimary())
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private String getFileName(TargetCodeType targetCodeType, TableAttribute tableAttribute) {
    return String.format("%s%s",
      targetCodeSupplier.getConverter().convertSqlTableColumnToNativeAttributeName(tableAttribute.getTableName()),
      (TargetCodeType.ENTITY.equals(targetCodeType)) ? "" : StringUtils.capitalize(targetCodeType.name().toLowerCase())
    );
  }
}
