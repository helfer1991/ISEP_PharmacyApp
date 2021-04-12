# Supplementary Specification

## Functionality

**Security**
- The interactions between each of the users must be done after a login. This way, each type of user will have different functionalities available. For example, a non-registered user can se the items that each pharmacy has, but in order to buy them he must first register himself and then login into his newly created account.
- The administrator is the one that "commands" the system (so to speak), as he's the one that manages the pharmacies.

**Information Persistence**
- This application, in order to ensure the persistence of data between multiple runs, uses an Oracle Database using the SQL language.

## Usability

**Aesthetics** 
- This application has an UI interface so that each user can interact with it.
- Also, if the user wants, he can interact with this application using the IDE console.
- The application can also read files (for example, a file that contains a list of clients and a list of products) and input their data directly into the database.
- The UI also has the purpose of showing the output of each action. For example, how much money does a given order cost. 
- If no UI is used, the console provides the output data and, if the user wants, that data can also be saved into an external file.


## Performance
- N/A

## Supportability

**Configurability:**
- Each user can interact with the application either through the IDE console, or through the provided UI.
- The application uses an Email API in order to send emails to couriers, clients, etc.
- The application uses also some APIs in order to check road conditions (their elevations relatively to the sea) and weather conditions (especially when it comes to the wind and its effect on the energetic cost of each vehicle trip).

   
### Design Restrictions

- Implementation process must follow a TDD (Test Driven Development) approach.
- Implementation must use an OOP approach.
- Implementation must use software engineering good practices such as the right use of design patterns.
- Technical documentation must be provided such as a Readme file and Javadoc.

### Implementation Restrictions

- The core language of this project is Java.
- Some features were implemented using Assembly and C programming languages.
- To ensure data persistence an Oracle Database was used (in the SQL language).

### Interface Restrictions

- Certain use cases required the use of external APIs. For example, to verify weather and road conditions.

### Physical Restrictions

- N/A