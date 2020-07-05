package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.parser;

import com.x5.template.Chunk;
import com.x5.template.Theme;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.TargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.UnsupportedTargetFrameworkTargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetFramework;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetSettings;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.converter.Converter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.models.TemplateAttributeNames;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.writer.ProjectWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.writer.ValidationWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.impl.java.spring.converter.JavaSpringConverter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.impl.java.spring.writer.JavaSpringProjectWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.impl.java.spring.writer.JavaSpringValidationWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.util.CodeOutputWriterUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class TargetCodeSupplier {

  public static final String JAVA_SPRING_TEMPLATE = "java_spring_";

  private final TargetSettings targetSettings;
  private final ProjectWriter projectWriter;
  private final ValidationWriter validationWriter;
  private final Converter converter;
  private final String templatePrefix;

  private Map<String, Object> commonAttributes;

  public TargetCodeSupplier(TargetSettings targetSettings) throws TargetCodeException {
    this.targetSettings = targetSettings;
    commonAttributes = Map.of();

    if (TargetFramework.SPRING.equals(targetSettings.getTargetFramework())) {
      projectWriter = new JavaSpringProjectWriter();
      validationWriter = new JavaSpringValidationWriter();
      converter = new JavaSpringConverter();
      templatePrefix = JAVA_SPRING_TEMPLATE;
    } else {
      throw new UnsupportedTargetFrameworkTargetCodeException(targetSettings.getTargetFramework());
    }
  }

  public void setCommonAttributes(Token tableName, Boolean isIdComposite) {
    String entityName = getConverter().convertSqlTableNameToNativeClassName(tableName);
    commonAttributes = Map.of(
      TemplateAttributeNames.ROOT_NAMESPACE, targetSettings.getRootNamespace(),
      TemplateAttributeNames.ENTITY_NAME, entityName,
      TemplateAttributeNames.ENTITY_NAME_LOWERCASE_FIRST, StringUtils.uncapitalize(entityName),
      TemplateAttributeNames.ID_COMPOSITE, isIdComposite
    );
  }

  public ProjectWriter getProjectWriter() {
    return projectWriter;
  }

  public ValidationWriter getValidationWriter() {
    return validationWriter;
  }

  public Converter getConverter() {
    return converter;
  }

  public CodeOutputWriterUtil getOutputWriter() {
    return CodeOutputWriterUtil.getInstance(targetSettings);
  }

  public Chunk getTemplate(TargetCodeType targetCodeType) {
    return getChunk(targetCodeType.name().toLowerCase());
  }

  private Chunk getChunk(String template) {
    var chunk = new Theme().makeChunk(templatePrefix + template);
    commonAttributes.forEach(chunk::set);
    return chunk;
  }
}
