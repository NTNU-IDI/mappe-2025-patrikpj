# 4.2 Implementation

## 4.2.1 External Libraries and Frameworks

The application relies on several external libraries to handle database persistence and testing. The primary framework used is **Hibernate ORM (version 6.4.1)**, an Object-Relational Mapping framework that enables the mapping of Java objects to relational database tables. Hibernate abstracts away the complexity of manual SQL queries and transaction management, allowing the application to work with domain objects directly while the framework handles persistence behind the scenes.

For the database layer, the application uses **SQLite** through the SQLite JDBC driver (version 3.44.1). SQLite was chosen for its simplicity as a file-based database requiring no separate server installation, making the application portable and easy to set up. The **Hibernate Community Dialects** package provides the necessary SQLite dialect that allows Hibernate to generate SQLite-compatible SQL statements.

Logging output from Hibernate is suppressed using **SLF4J NOP (version 1.7.36)**, a "no-operation" logger implementation. This ensures clean terminal output for the end user without Hibernate's verbose startup messages.

For testing purposes, the project includes **JUnit 5 (version 5.10.1)** as a test-scoped dependency, enabling unit and integration testing of the application components.

The application also makes use of the **Jakarta Persistence API (JPA)** annotations provided through Hibernate for entity mapping, as well as standard Java libraries including `java.util` for collections and Optional handling, `java.time` for date/time operations, and `java.util.regex` for email validation.

---

## 4.2.2 Architecture and Class Structure

The application follows a layered MVC (Model-View-Controller) architecture enhanced with the Repository and Service patterns. This architectural approach provides clear separation of concerns, where each layer has distinct responsibilities and communicates only with adjacent layers. The layered structure improves maintainability, testability, and makes the codebase easier to understand and extend.

{UML Class Diagram: Overall Architecture}

### 4.2.2.1 Model Layer

The model layer forms the foundation of the application and contains the domain entities that represent the core business concepts. These entities are annotated with JPA annotations to enable Hibernate to persist them to the database.

The **Author** class represents a diary author with attributes for first name, last name, and email address. The email serves as a natural identifier and is enforced to be unique across all authors. The class implements comprehensive input validation: names cannot be null or blank, and email addresses must conform to a standard format validated through a regular expression pattern. The class also maintains audit timestamps for creation and last update, which are automatically managed through JPA lifecycle callbacks (`@PrePersist` and `@PreUpdate`). The `equals()` and `hashCode()` methods are overridden to use email as the basis for equality comparison.

The **DiaryEntry** class represents an individual diary entry containing a title, content text, and a reference to its author. It maintains a many-to-one relationship with Author, meaning each entry belongs to exactly one author while an author can have multiple entries. Similar to Author, the class validates that title and content are not null or blank, and maintains creation and update timestamps. The content field uses TEXT column definition to support longer text entries.

{UML Class Diagram: Domain Model}

### 4.2.2.2 Repository Layer

The repository layer encapsulates all database access logic, providing an abstraction over Hibernate's session management and transaction handling. This separation ensures that database implementation details do not leak into higher layers.

The **AuthorRepository** class provides CRUD (Create, Read, Update, Delete) operations for Author entities. Beyond basic operations, it includes specialized query methods such as `findByEmail()` for looking up authors by their email address and `existsByEmail()` for checking uniqueness before creating new authors. All write operations are wrapped in transactions through a private `executeInTransaction()` helper method that handles session lifecycle, transaction commit, and rollback on errors.

The **DiaryEntryRepository** class follows the same pattern for DiaryEntry entities. It additionally provides methods for querying entries by author (`findByAuthor()`, `findByAuthorId()`) and a search method `searchByTitleOrContent()` that performs case-insensitive text matching using HQL (Hibernate Query Language) LIKE queries. Count operations support the statistics feature in the application.

### 4.2.2.3 Service Layer

The service layer sits between controllers and repositories, containing business logic that should not reside in either the presentation or persistence layers. Services coordinate operations, enforce business rules, and provide a clean API for controllers to use.

The **AuthorService** class encapsulates business rules for author management. Its primary responsibility is ensuring email uniqueness when creating authors. It offers two creation methods: `createAuthor()` returns an `Optional<Author>` that is empty if the email already exists, while `createAuthorOrThrow()` throws an exception in the same scenario. This dual approach allows controllers to choose the appropriate error handling strategy for their context.

The **DiaryEntryService** class manages diary entry operations. It provides methods for creating entries with validation, searching entries by text, and counting entries for statistics. The service delegates all persistence operations to the repository while exposing a focused API that matches the application's use cases.

{UML Class Diagram: Service and Repository Layers}

### 4.2.2.4 Controller Layer

The controller layer handles user interaction logic, serving as the bridge between the view components and the service layer. Controllers are responsible for gathering user input, invoking appropriate service methods, and directing the flow of the application.

The **AuthorController** class manages all author-related user interactions. It constructs and returns menu structures for author management, handles the creation flow with input prompts and validation feedback, and provides author listing with pagination. The controller also implements protective logic such as preventing deletion of authors who have existing diary entries, which would violate referential integrity.

The **DiaryEntryController** class handles diary entry interactions following a similar pattern. It manages the entry creation workflow, which includes author selection through a paginated list. If no authors exist when creating an entry, the controller offers inline author creation to improve user experience. The controller also handles entry searching, viewing, editing, and deletion with appropriate confirmation prompts.

### 4.2.2.5 View Layer

The view layer consists of reusable UI components that handle terminal-based presentation and user input. These components are designed to be generic and composable, enabling flexible menu construction throughout the application.

The **DiaryApp** class serves as the application's entry point and composition root. It initializes all components in the correct dependency order: first the Hibernate session factory through `HibernateUtil`, then repositories, services, and finally controllers. The main menu is constructed declaratively using the view components. A shutdown hook is registered to ensure graceful cleanup of database connections when the application terminates.

The **MenuView** class implements a text-based menu system that displays numbered options and handles user input in a loop. It distinguishes between the main menu (showing "Exit" as the back option) and submenus (showing "Back"). Menu options can contain either direct actions or nested submenus, enabling hierarchical navigation.

The **MenuOption** class represents individual menu choices. Each option has a label, an optional color for the option number, and either a `Runnable` action or a reference to a submenu. This design enables both immediate actions and navigation to deeper menu levels.

The **Paginator** class is a generic component for displaying long lists across multiple pages. It accepts a list of items, a formatting function, and provides navigation controls (Next, Previous) along with item selection. A read-only mode is available for viewing lists without selection capability. The use of generics and functional interfaces makes this component reusable across different entity types.

The **AnsiColors** class provides constants for ANSI terminal escape codes, enabling colored output for improved visual feedback. Different colors highlight menu numbers, success messages, and error states.

{UML Class Diagram: View Components}

### 4.2.2.6 Utility Classes

The **HibernateUtil** class implements the singleton pattern to manage the Hibernate `SessionFactory`. The session factory is initialized once in a static block when the class is first loaded, reading configuration from `hibernate.cfg.xml`. The class also configures Java logging to suppress verbose Hibernate output. A `shutdown()` method allows the application to release database connections cleanly on exit.

---

## 4.2.3 Program Flow

### 4.2.3.1 Startup and Menu Navigation

When the application starts, the `DiaryApp.init()` method executes a carefully ordered initialization sequence. First, a `Scanner` is created for reading user input. Next, the Hibernate session factory is obtained, which triggers database connection establishment and schema validation. Repositories are then instantiated with the session factory, followed by services that receive repositories, and finally controllers that receive both services and the shared scanner.

After initialization, the main menu is constructed by adding menu options that either execute actions directly or navigate to controller-provided submenus. The application displays an ASCII art banner and enters the main menu loop. Users navigate by entering numbers corresponding to menu options. Selecting a submenu pushes a new menu context; selecting "Back" returns to the parent menu, while "Exit" from the main menu terminates the application.

{UML Activity Diagram: Main Flow}

### 4.2.3.2 Creating a New Diary Entry

The entry creation process demonstrates the collaboration between multiple layers. When the user selects "Create Entry," the controller first retrieves all authors from the service. If no authors exist, the user is prompted to create one inline before proceeding, preventing orphaned entries.

Authors are displayed using the Paginator component, allowing the user to browse and select. Once an author is selected, the controller prompts for a title, then collects multi-line content until an empty line signals completion. The controller creates the entry through the service, which constructs the entity and delegates to the repository for persistence. Success or error feedback is displayed to the user.

{UML Activity Diagram: Create Diary Entry}

### 4.2.3.3 Searching Diary Entries

The search functionality allows users to find entries containing specific text. After prompting for a search term, the controller calls the service's search method, which delegates to the repository. The repository executes an HQL query with LIKE clauses that match the search term against both title and content fields, case-insensitively.

Results are displayed through the Paginator. If the user selects an entry from the results, an action menu appears offering options to view the full entry, edit it, or delete it.

{UML Activity Diagram: Search Diary Entries}

---

## 4.2.4 Object Collaboration

### 4.2.4.1 Creating an Author

The author creation sequence illustrates how objects collaborate across layers. The controller prompts the user for first name, last name, and email. Email validation occurs in two stages: first, the static `Author.isValidEmail()` method checks format validity; second, the controller calls the service to check if the email already exists in the database.

Once input is collected and validated, the controller calls `authorService.createAuthor()`. The service performs a final uniqueness check, constructs a new Author entity (which validates inputs in its constructor), and passes it to the repository. The repository opens a Hibernate session, begins a transaction, persists the entity, and commits the transaction. The new author with its generated ID is returned through the layers back to the controller, which displays a confirmation message.

{UML Sequence Diagram: Create Author}

### 4.2.4.2 Listing and Selecting Diary Entries

When listing entries, the controller requests all entries from the service, which retrieves them from the repository via an HQL query. The resulting list is wrapped in a Paginator configured with a formatting function that displays each entry's title and author name.

The Paginator displays one page of entries at a time, showing navigation options based on the current position (Next if not on the last page, Previous if not on the first). When the user enters a number, the Paginator returns the corresponding entry object to the controller. The controller then constructs and displays an action menu specific to the selected entry.

{UML Sequence Diagram: List and Select Diary Entries}

### 4.2.4.3 Editing a Diary Entry

The editing flow demonstrates direct entity manipulation with deferred persistence. The controller displays current values and prompts for new ones. When the user enters a new title, the controller calls the entity's `setTitle()` method directly, which performs validation. Similarly for content if the user chooses to replace it.

If any changes were made, the controller calls the service's update method, which delegates to the repository. The repository merges the modified entity into a Hibernate session and commits the transaction. The `@PreUpdate` callback automatically updates the `updatedAt` timestamp before the database write. If no changes were made, the controller simply informs the user and returns.

{UML Sequence Diagram: Edit Diary Entry}

---

## 4.2.5 Architectural Principles

The implementation adheres to several software engineering principles that contribute to code quality and maintainability.

The **Model-View-Controller pattern** provides clear separation between data representation, user interface, and control flow. This separation allows each layer to evolve independently and makes the codebase easier to understand for new developers.

The **Repository pattern** abstracts database access behind a clean interface. This abstraction would allow the underlying persistence mechanism to be changed (for example, from SQLite to PostgreSQL) without affecting the service or controller layers. It also facilitates unit testing by enabling mock repositories.

The **Service layer pattern** centralizes business logic in dedicated classes. This prevents business rules from being scattered across controllers and keeps controllers focused on user interaction concerns.

**Dependency injection** through constructor parameters ensures that all dependencies are explicit and provided at construction time. This approach improves testability since dependencies can be replaced with test doubles, and makes the dependency graph visible in the code.

**Defensive programming** practices protect against invalid states. Entity constructors and setters validate their inputs, throwing exceptions for null or invalid values. The use of `Objects.requireNonNull()` provides clear error messages when preconditions are violated. `Optional` is used as a return type when values may legitimately be absent, making null handling explicit in the API.

The **Single Responsibility Principle** guides class design, with each class having one focused purpose. This keeps classes small, understandable, and testable.
