# 📚 Library Management System

## Description

The Library Management System is a web application built using Spring Boot that allows users to manage books, members,
and checkouts. It provides features to add, update, and delete books and members, track overdue books, and search for
books and members.

## Features

- 📚 **Manage Books**: Add, update, and remove books.
- 🧑‍🤝‍🧑 **Manage Members**: Add, update, and remove library members.
- 📅 **Checkout Management**: Check in and check out books.
- ⏳ **Overdue Tracking**: Identify and list books that are overdue.
- 🔍 **Search Functionality**: Search for books and members by name or title, and view books checked out by specific
  members.

## Project Structure

The project follows Agile methodology, with the following structure:

- **Sprint 1:** Project setup and initial implementation.
- **Sprint 2:** Member management and book issuing.
- **Sprint 3:** Fine management and reporting.
- **Sprint 4:** Search functionality and final integrations.
- **Sprint 5:** Testing and deployment.


## Technologies Used

- **Java**: Programming language ☕
- **Spring Boot**: Framework for building the application 🚀
- **PostgreSQL**: Database for data storage 🗄️
- **Flyway**: Database migration tool 🔄
- **JUnit**: Testing framework 🧪

## Installation

### Prerequisites

- Java 11 or higher ☕
- PostgreSQL 🗄️
- Maven 📦

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/edsonwade/library-management-system.git
   ```
2. Navigate to the project directory
   ```bash
   cd library-management-system
   ```
3. Update src/main/resources/application.properties with your PostgreSQL credentials.
4. Install dependencies and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
5. Access the application at http://localhost:8080.

### Usage

## API Endpoints

- **Add a Book**
    1. Endpoint: POST /api/books
    2. Request Body**:

  ````json
   {
     "title": "Book Title",
     "author": "Author Name",
     "isbn": "ISBN Number",
     "genre": "Genre",
     "publishedDate": "YYYY-MM-DD",
     "availability": true
   }
  ````
- **Search Books**
    1. Endpoint: GET /api/books/search?query={query}
    2. Response:

````json
    [
  {
    "id": 1,
    "title": "Book Title",
    "author": "Author Name",
    "isbn": "ISBN Number",
    "genre": "Genre",
    "publishedDate": "YYYY-MM-DD",
    "availability": true
  }
]
````

- **Check Out a Book**
    1. Endpoint:  POST /api/checkouts
    2. Response:

````json
{
  "bookId": 1,
  "memberId": 1
}
````

### Testing

- **To run the tests, use the following Maven command**:

```bash
mvn test
````

### Acknowledgements

- **Spring Boot**: For the framework 🚀
- **PostgreSQL**: For the database 🗄️
- **Flyway Migration**: For database migration 🔄
- **Mockito**: For testing 🧪

## Branch Naming Conventions

- **Feature Branches**: `feature/short-description`
- **Bug Fixes**: `bugfix/short-description`
- **Hotfixes**: `hotfix/short-description`
- **Release Branches**: `release/version-number`
- **Development Branches**: `develop`


### Contributing

1. Fork the repository 🍴
2. Create a new branch (git checkout -b feature-branch) 🌿
3. Commit your changes (git commit -am 'Add new feature') ✍️
4. Push to the branch (git push origin feature-branch) 📤
5. Create a new Pull Request 📩
   Please read [CONTRIBUTING.md](link-to-contributing-file) for details on how to contribute to this project.🤝
6. http://prometheus:9090 - for grafana 

### Contact

For questions or feedback, please reach out to:

- **GitHub**: edsonwade 🐱

### License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).📝.