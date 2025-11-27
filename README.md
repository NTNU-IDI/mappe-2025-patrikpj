[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/INcAwgxk)
# Portfolio project IDATT1003

STUDENT NAME = Patrik Pereira Johannessen  
STUDENT ID = 157285


## Project description
A command-line diary application where the user can create and manage authors and diary entries. The application uses the Hibernate ORM with SQLite for persistence and follows an MVC architecture with clear separation between controllers, services, and repositories. Features include author management, entry creation, search functionality, and paginated list views.


## Project structure
The project follows an MVC architecture with clear separation of concerns. Source files are organized in `src/main/java/edu/ntnu/idi/idatt/` with the following packages:

- **`controller/`** - Handles user input and coordinates between services and views
- **`service/`** - Contains business logic and validation
- **`repository/`** - Data access layer for database operations
- **`model/entities/`** - Domain entities (Author, DiaryEntry)
- **`view/components/`** - Reusable UI components (MenuView, Paginator, AnsiColors)
- **`util/`** - Utility classes (HibernateUtil)

Test classes go in `src/test/java/` following the same package structure.

Configuration files are in `src/main/resources/` (Hibernate config, logging properties).


## Link to repository
https://github.com/NTNU-IDI/mappe-2025-patrikpj


## How to run the project
[//]: # (TODO: Describe how to run your project here. What is the main class? What is the main method?
What is the input and output of the program? What is the expected behaviour of the program?)


## How to run the tests
[//]: # (TODO: Describe how to run the tests here.)


## References
[//]: # (TODO: Include references here, if any. For example, if you have used code from the course book, include a reference to the chapter.
Or if you have used code from a website or other source, include a link to the source.)
