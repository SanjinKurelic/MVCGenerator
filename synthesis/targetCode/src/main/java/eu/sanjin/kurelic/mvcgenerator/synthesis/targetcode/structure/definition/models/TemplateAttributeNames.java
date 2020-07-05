package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.models;

public class TemplateAttributeNames {

  // Name of generated project - used in main class definition (ex. class <Program>Main { function int main(String[] args){} })
  public static final String PROJECT_NAME = "project_name";
  // Root namespace/package in which all application resides
  public static final String ROOT_NAMESPACE = "root_namespace";
  // Table name (ex. Student)
  public static final String ENTITY_NAME = "entity_name";
  // Table name as variable (ex. Student student)
  public static final String ENTITY_NAME_LOWERCASE_FIRST = "entity_name_lowercase_first";
  // Used to map ORM with actual database (ex. @Table(name = "example_table") class ExampleTable {} )
  public static final String ENTITY_REAL_NAME = "entity_real_name";
  // Id class (ex. Integer id)
  public static final String ID_CLASS = "id_class";
  // Id name used in service for fetching the id (ex. studentRepository.findById(student.get<Id>()); )
  public static final String ID_NAME = "id_name";
  // Composite id (ex. true |false/null => new BookId(name, category) )
  public static final String ID_COMPOSITE = "id_composite";
  // Composite id parameters (ex. HashMap => function void addBook(String name, String category))
  public static final String ID_PARAMETERS = "id_parameters";
  // Additional imports required by code (ex. java.util.Date)
  public static final String EXTRA_IMPORTS = "extra_imports";
  // Used for getters and setters in entity class (ex name => function String getName() {} function void setName(String name) {})
  public static final String COLUMN_ATTRIBUTES = "column_attributes";
  // Used as the flag for generating entity id composite class (ex. class StudentId {})
  public static final String COMPOSITE_ENTITY = "composite_entity";
  // Used to generate validation if statements in service class (ex. if ( x < 5) throw new Exception(); )
  public static final String VALIDATION_RULES = "validation_rules";

}
