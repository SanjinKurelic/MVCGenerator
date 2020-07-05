package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.parser;

import com.x5.template.Chunk;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.TableAttribute;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.NoPrimaryKeyTargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.OutputWriterTargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.TargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetSettings;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.models.TemplateAttributeNames;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.models.TypeDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class TargetCodeParser {

  private final SemanticAttributeTable semanticAttributeTable;
  private TargetCodeSupplier targetCodeSupplier;

  public TargetCodeParser(SemanticAttributeTable semanticAttributeTable) {
    this.semanticAttributeTable = semanticAttributeTable;
  }

  public void parse(TargetSettings targetSettings) throws TargetCodeException {
    targetCodeSupplier = new TargetCodeSupplier(targetSettings);

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

      // generate entity id if composite
      if (primaryKeys.size() > 1) {

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
        targetCodeSupplier.getConverter().convertSqlTableColumnToNativeAttributeName(name),
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

    // TODO generate validation
    //tableAttribute.getColumns()

    // Generate template
    Chunk template = targetCodeSupplier.getTemplate(targetCodeType);
    template.set(TemplateAttributeNames.ID_CLASS, idDefinition.getName());
    template.set(TemplateAttributeNames.EXTRA_IMPORTS, extraParams);

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
      StringUtils.capitalize(targetCodeType.name().toLowerCase())
    );
  }
}
