package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.models;

public class TemplateAttributeNames {

  // Name of generated project - used in main class definition (ex. class <Program>Main { function int main(String[] args){} }
  public static final String PROJECT_NAME = "projectName";
  // Root namespace/package in which all application resides
  public static final String ROOT_NAMESPACE = "rootNamespace";
  // Table name (ex. Student)
  public static final String ENTITY_NAME = "entityName";
  // Table name as variable (ex. Student student)
  public static final String ENTITY_NAME_LOWERCASE_FIRST = "entityNameLowercaseFirst";
  // Id class (ex. Integer id)
  public static final String ID_CLASS = "idClass";
  // Import statement for id class (ex. java.util.Date)
  public static final String ID_CLASS_PACKAGE = "idClassPackage"; //TODO remove, ifComposite
  // Composite id (ex. true |false/null => new BookId(name, category) )
  public static final String ID_COMPOSITE = "idComposite";
  // Composite id parameters (ex. HashMap => function void addBook(String name, String category))
  public static final String ID_PARAMETERS = "idParameters";
  // Additional imports required by code (ex. java.util.Date)
  public static final String EXTRA_IMPORTS = "extraImports";

}
