# MVC Generator

Most of today's simpler software solutions closely link their work to basic database operations. The basic operations are retrieving, entering, modifying, and deleting individual data, so-called CRUD operations. To simplify the implementation of such software solutions, it is possible to build a code generator that builds a series of program sections based on the definition of the database structure. The database structure is defined using a structured query language (SQL).

The generator consists of two phases: the analysis phase and the synthesis phase. The analysis phase checks the correctness of the input file and builds a series of program structures suitable for generating the target program. The analysis phase consists of lexical, syntax, and semantic analyzer. Lexical analyzer groups characters into lexical tokens and checks the correctness of the input file using the allowed set of input characters. Syntax analyzer checks and defines the interrelationship of lexical tokens, and semantic analyzer checks the meanings of individual syntax units. The synthesis phase builds the target program based on the program structures created in the analysis phase and consists of the intermediate code generator and the target code generator. The intermediate code generator simplifies and optimizes program structures, and the target program generator replaces them with target program commands.

The generated program sections form a three-layer architecture composed of a presentation, service, and data layer. The presentation layer uses the REST architecture as a network programming interface that allows interoperability with other systems or users. Program sections are implemented using the MVC form of the software architecture, thus separating the responsibility of individual parts of the application into components dependent on their purpose. The generated target program sections use the Java programming language and the Spring and Hibernate programming frameworks.

## Getting started

### Prerequisites

MVC Generator is Maven project, which means all dependencies are handled by Maven. For running the project you need to have the following items:

- Maven
- Java SDK 11 or later

### Installing

Import project to IDE (ex. Intellij IDEA) as Maven project and run `main` method in `eu.sanjin.kurelic.mvcgenerator.view.Application`.

## Running

The graphical interface allows the user to interact more easily with the generator. The interface is created using the Swing programming framework built into standard Java libraries thus simplifying its creation. The user manages the settings of the input file (Input settings) and the settings of the output program (Output settings) using the graphical interface.

<p align="center"><img src="https://github.com/SanjinKurelic/MVCGenerator/blob/master/media/mainWindow.png" width="410"/></p>

The settings of the input file define the path to the input file (SQL file) and its type, the so-called dialect (SQL dialect). Due to the ease of creating a generator, the dialect of the input file can only be the standard SQL-92 format defined with BNF grammar: [Grammar.bnf](https://github.com/SanjinKurelic/MVCGenerator/blob/master/Grammar.bnf).

The output settings define the structure of the target program. The structure of the target program consists of the programming language in which the target program is written, the programming framework that will be used, and the package name (Root namespace). In various programming languages, packages are used to enable the grouping of code into meaningful functional units.

To simplify the creation of the target program, the selection of the programming language and programming framework is disabled. The generator can only generate programms for the Java programming language using Spring and Hibernate programming frameworks.

Clicking on the Generate option starts the algorithms of the analysis and synthesis module. The analysis module checks the correctness of the entered file, and the synthesis module builds the program sections of the target program.

For the following SQL code, generator will output Spring REST API with all required files as shown on image below.

```sql
CREATE TABLE Student(
    id INT PRIMARY KEY NOT NULL,
    name VARCHAR(50),
    surname VARCHAR(50) NOT NULL,
    dateOfBirth DATE CHECK (dateOfBirth <= CAST('2001-01-01' AS DATE)),
    grade FLOAT DEFAULT 0.0,
    address INT,
    CHECK (
        grade BETWEEN DEFAULT AND + 5.0
        AND name IS NOT NULL
    ),
    CONSTRAINT FK_address FOREIGN KEY (address) 
        REFERENCES "Address Table" (id)
);

CREATE TABLE "Address Table" (id INT PRIMARY KEY);
```

<p align="center"><img src="https://github.com/SanjinKurelic/MVCGenerator/blob/master/media/outputFiles.png" width="470"/></p>

If we manually include some data by defining `data.sql` file in generated resource folder, we could easily test REST API with Postman, Insomnia or similar tool.

```sql
INSERT INTO "Address Table" ("id") VALUES (1), (2), (3);

INSERT INTO "Student" ("name", "surname", "grade", "address", "dateOfBirth") VALUES
('John', 'Doe', 2.4, 1, DATE '1992-05-01'),
('Alice', 'Doe', 4.4, 2, DATE '1997-02-14'),
('Bob', 'Doe', 3.7, 3, DATE '1995-11-11');
```

<p align="center"><img src="https://github.com/SanjinKurelic/MVCGenerator/blob/master/media/response.png" width="1050"/></p>

## Technologies and keywords

- Java 11
- Spring
- Hibernate
- Chunk (template engine)
- Reflections
- Junit
- CRUD
- REST API
- MVC architecture
- three-tier architecture
- SQL
- code generator
- analysis phase
- synthesis phase
- lexical analyzer
- parsing
- semantic analyzer
- intermediate code generator
- target code generator
- compiler

## Notice

MVC Generator is not fully compatible with SQL-98 in some cases, and there are known issues. Do not use it in production before you double check generated values.
