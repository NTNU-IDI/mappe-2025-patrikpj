[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/INcAwgxk)
# Portfolio project IDATT1003

STUDENT NAME = Patrik Pereira Johannessen  
STUDENT ID = 157285


## Project description
A command-line diary application where the user can create and manage authors and diary entries. The application uses the Hibernate ORM with SQLite for persistence and follows an MVC architecture with clear separation between controllers, services, and repositories. Features include author management, entry creation, search functionality, and paginated list views.


## Project structure
The project follows an MVC architecture with a clear separation of concerns. Source files are organized in `src/main/java/edu/ntnu/idi/idatt/` with the following packages:

| Package | Description |
|---------|-------------|
| `controller/` | Handles user input and coordinates between services and views |
| `service/` | Contains business logic and validation |
| `repository/` | Data access layer for database operations |
| `model/entities/` | Domain entities (Author, DiaryEntry) |
| `view/` | UI views organized by feature (author, diary, mainmenu, statistics) |
| `view/_components/` | Reusable UI components (MenuView, Paginator, AnsiColors) |
| `util/` | Utility classes (HibernateUtil) |


The sourcefiles are organized as follows:

```
├───java
│   └───edu
│       └───ntnu
│           └───idi
│               └───idatt
│                   ├───controller
│                   ├───model
│                   │   └───entities
│                   ├───repository
│                   ├───service
│                   ├───util
│                   └───view
│                       ├───author
│                       ├───diary
│                       ├───mainmenu
│                       ├───statistics
│                       └───_components
└───resources
```

Test classes go in `src/test/java/` following the same package structure.

Configuration files are in `src/main/resources/` (Hibernate config, logging properties).

## Link to repository
https://github.com/NTNU-IDI/mappe-2025-patrikpj


## How to run the project

> **Note:** All commands should be run from the root of the project.

**Windows users:**
```powershell
.\scripts\run.ps1
```

**Other platforms (or manually):**
```bash
mvn compile exec:java
```

The application starts a command-line interface where you can navigate through menus to manage authors and diary entries. Use the number keys to select menu options.


## How to run the tests
> **Note:** All commands should be run from the root of the project.

**Windows users:**
```powershell
.\scripts\test.ps1
```

**Other platforms (or manually):**
```bash
mvn test
```


## References
[//]: # (TODO: Include references here, if any. For example, if you have used code from the course book, include a reference to the chapter.
Or if you have used code from a website or other source, include a link to the source.)
