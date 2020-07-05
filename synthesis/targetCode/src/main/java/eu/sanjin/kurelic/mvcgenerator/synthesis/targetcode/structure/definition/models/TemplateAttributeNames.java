package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.models;

public class TemplateAttributeNames {

  // Name of generated project - used in main class definition (ex. class <Program>Main { function int main(String[] args){} })
  public static final String PROJECT_NAME = "projectName";
  // Root namespace/package in which all application resides
  public static final String ROOT_NAMESPACE = "rootNamespace";
  // Table name (ex. Student)
  public static final String ENTITY_NAME = "entityName";
  // Table name as variable (ex. Student student)
  public static final String ENTITY_NAME_LOWERCASE_FIRST = "entityNameLowercaseFirst";
  // Used to map ORM with actual database (ex. @Table(name = "example_table") class ExampleTable {} )
  public static final String ENTITY_REAL_NAME = "entityRealName";
  // Id class (ex. Integer id)
  public static final String ID_CLASS = "idClass";
  // Id name used in service for fetching the id (ex. studentRepository.findById(student.get<Id>()); )
  public static final String ID_NAME = "idName";
  // Composite id (ex. true |false/null => new BookId(name, category) )
  public static final String ID_COMPOSITE = "idComposite";
  // Composite id parameters (ex. HashMap => function void addBook(String name, String category))
  public static final String ID_PARAMETERS = "idParameters";
  // Additional imports required by code (ex. java.util.Date)
  public static final String EXTRA_IMPORTS = "extraImports";
  // Used for getters and setters in entity class (ex name => function String getName() {} function void setName(String name) {})
  public static final String COLUMN_ATTRIBUTES = "columnAttributes";
  // Used as the flag for generating entity id composite class (ex. class StudentId {})
  public static final String COMPOSITE_ENTITY = "compositeEntity";
  // Used to generate validation if statements in service class (ex. if ( x < 5) throw new Exception(); )
  public static final String VALIDATION_RULES = "validationRules";

}
