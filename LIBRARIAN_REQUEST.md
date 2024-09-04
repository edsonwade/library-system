### Librarian API Documentation

## Overview

The Librarian API enables you to manage a collection of books, authors, users, and other relevant library resources.
It allows for adding, updating, deleting, and retrieving information about these resources.
This API can be used for library management systems or similar applications where library functions are required.

## Endpoints

1. Users
    - Create a New Librarian
    - Endpoint: POST /api/librarian/
    - Description: Create a new user librarian in the library system.
    - **Request Header**:

```json
{
  "Authorization": "Bearer {api_key}",
  "Content-Type": "application/json"
}
```

- **Request Body:**

```json
{
  "name": "Ella Adams",
  "email": "ella.adams@example.com",
  "address": {
    "street": "83 Rua do Lago",
    "city": "Brasília",
    "state": "Federal District",
    "country": "Brazil",
    "postalCode": "70000-000"
  },
  "contact": "+556 132-109-876",
  "employeeCode": "EMP020",
  "admin": 12
}
```

- **Response:**
    - 201 Created: Librarian added successfully.
    - 400 Bad Request: Invalid request parameters.