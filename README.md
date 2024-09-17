# Book Management System

This is a fullstack application for managing a personal library of books. Users can add, view, update, and delete books in their collection.

## Features

- User authentication and authorization
- Add new books to the library
- View all books in the library
- Mark books as read or unread
- Delete books from the library
- Admin functionality to manage all books

## Technologies Used

### Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL

### Frontend
- React
- Axios for API calls
- CSS for styling

## Setup and Installation

### Prerequisites
- Java JDK 11 or higher
- Node.js and npm
- MySQL

### Backend Setup

1. Clone the repository
2. Navigate to the backend directory
3. Configure the `application.properties` file with your MySQL credentials
4. Run the Spring Boot application:
   ```
   ./mvnw spring-boot:run
   ```

### Frontend Setup

1. Navigate to the frontend directory
2. Install dependencies:
   ```
   npm install
   ```
3. Start the React development server:
   ```
   npm start
   ```

## API Endpoints

- `POST /register`: Register a new user
- `GET /api/books`: Get all books for the authenticated user
- `POST /api/books`: Add a new book
- `GET /api/books/{isbn}`: Get a specific book by ISBN
- `PUT /api/books/{isbn}`: Update a book's read status
- `DELETE /api/books/{isbn}`: Remove a book from the user's library
- `GET /api/books/admin/all`: Get all books (admin only)
- `DELETE /api/books/admin/{isbn}`: Delete a book from the system (admin only)

## Security

The application uses Spring Security for authentication and authorization. It implements stateless authentication using HTTP Basic Auth.

## Frontend Structure

- `App.js`: Main component
- `Container.jsx`: Manages the book list and form display
- `BookCard.jsx`: Displays individual book information
- `BookAddForm.jsx`: Form for adding new books
- `Header.jsx` and `Footer.jsx`: Layout components

## Contribution

Contributions, issues, and feature requests are welcome. Feel free to check [issues page](https://github.com/yourusername/book-management-system/issues) if you want to contribute.

## License

[MIT](https://choosealicense.com/licenses/mit/)
