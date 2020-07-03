package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.parser;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.TableAttribute;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.TargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetSettings;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.component.ClassComponent;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.component.Component;

public class TargetCodeParser {

  private final SemanticAttributeTable semanticAttributeTable;

  public TargetCodeParser(SemanticAttributeTable semanticAttributeTable) {
    this.semanticAttributeTable = semanticAttributeTable;
  }

  public void parse(TargetSettings targetSettings) throws TargetCodeException {
    TargetCodeSupplier targetCodeSupplier = new TargetCodeSupplier(targetSettings.getTargetFramework());

    // generate project structure
    targetCodeSupplier.getProjectWriter().generateCode(targetSettings.getProjectName(), targetSettings.getRootNamespace());

    ClassComponent entityComponent, repositoryComponent, serviceComponent, viewComponent;
    Component idComponent;
    for(TableAttribute tableAttribute : semanticAttributeTable.getTables().values()) {
      // generate entity
      entityComponent = targetCodeSupplier.getEntityWriter().generateCode(tableAttribute);
      idComponent = targetCodeSupplier.getEntityWriter().getIdComponent();

      // generate view
      viewComponent = targetCodeSupplier.getViewWriter().generateCode(entityComponent, idComponent, tableAttribute);

      // generate repository
      repositoryComponent = targetCodeSupplier.getRepositoryWriter().generateCode(entityComponent, idComponent);

      // generate service
      serviceComponent = targetCodeSupplier.getServiceWriter().generateCode(repositoryComponent, entityComponent, idComponent, tableAttribute);

      // generate controller
      targetCodeSupplier.getControllerWriter().generateCode(viewComponent, serviceComponent, entityComponent, idComponent);
    }
  }

  public String getStatus() {
    return "";
  }
}
