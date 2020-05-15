#API Automation Documentation 
Framework to test Deck of Cards API

## Directory Structure
        root
        ---src
           ---main
               ---java
                   ---endPoints
                       ---ApiEndPoints.java
                   ---pojos
                       ---Card.java
                       ---Deck.java
                       ---Draw.java
           ---test
               ---java
                    ---TestBase
                        ---BaseClass.java
                    ---tests
                        ---CreateNewDeckTest.java
                        ---DrawCardsTest.java
                    ---utilities
                        ---ConfigReader.java
                        ---DeckIdGenerator.java
                        ---JavaUtil.java
               ---resources
                    ---CreateNewDeckWithJokerSchema.json
                    ---deck_count.csv
                    ---JsonSchemaDrawDeck.json
                    ---log4j.properties
                    ---new_deck.json
        ---configuration.properties
        ---log4j.log
        ---pom.xml
        ---Readme.md
#### Packages and Classes Design

- ApiEndPoints Enum in endPoints package contains the end points  {   NEW_DECK("/new"), SHUFFLE("/shuffle"), DRAW_CARD("/draw")  } because 
they are constants.
- pojo package contains three classes Card.java, Deck.java, Draw.java Records returned in the response from the APIs will be automatically converted into objects of this class. 
@Data annotation from Project Lombok, which help to generate getters and setters and  toString methods for all fields, and also creates equals and hashCode implementations and   Builds a constructor:for serialization and deserialization 
- TestBase package has BaseClass.java with @BeforeAll basURISetUp() method to load baseURI  before every test 
- utilities package has ConfigReader.java to reade configuration.properties where BaseURI is located,
JavaUtil.java class has method for closing the source or destination need to be closed. 

### Tools Used

### Java 
### Junit 5
To create a series of tests that will consume and process the data set. It provides a quick way of writing small units of code execution and parametrization. 
### Maven build management tool 
### Project Lombok 
Lombok allows for the creation of getter and setter methods without the need to explicitly declare them in source code, along with other features. As I decided to use data access objects (DAOs) in my framework, this library comes in very handy when the DAOs become exceptionally large, and every field requires its own getter method.
### Simple Logging Facade for Java (SLF4J)
SLF4J allows for easy management of logging within projects, as it allows you to add any compatible logging framework to the project. Adding expressive logging to the framework, along with establishing a consistent and robust logging process as the automation solution expands, would allow for easier debugging of issues and the ability to track the test run throughout its run.
### Hamcrest 
is a framework for writing matcher objects allowing ‘match’ rules to be defined declaratively. There are a number of situations where matchers are invaluble, such as UI validation, or data filtering, but it is in the area of writing flexible tests that matchers are most commonly used. This tutorial shows you how to use Hamcrest for unit testing.
### Gson
is for deserialization/serialization purposes




##  Defects Found

- Create new deck with negative number

    Expected status code 422
    
    Actual status code 200


- Post new Deck 

    Expected status code 201

   Actual status code 301 Moved Permanently 



#### To Run the Project

-through mvn 


