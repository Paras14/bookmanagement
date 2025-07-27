# ShelfSense: Intelligent Book Management System

## Overview
ShelfSense is a full-stack web application designed for intelligent management of personal book collections. It offers a suite of features for organizing physical books, tracking reading progress, and leveraging AI for insightful discussions about your literary collection.

## Technologies Used

### Backend
- Java 21
- Spring Boot 3.3.4
- Spring Security
- Spring Data JPA
- MySQL
- JWT for authentication
- Spring AI with Ollama for AI chat functionality

### Frontend
- React.js
- Axios for API calls
- React Markdown for rendering chat responses

## Features
1. User Authentication (Register/Login)
2. Book Management (Add, Remove, Update read status)
3. Admin Panel for managing all books
4. AI-powered chat about books using Ollama(llama3.2 3B)

## Screenshots
#### Login & Register
<img src="https://github.com/Paras14/bookmanagement/blob/master/Screenshots/Login.png?raw=true" alt="Login Page" width="500"/> <img src="https://github.com/Paras14/bookmanagement/blob/master/Screenshots/Register.png?raw=true" alt="Register Page" width="500"/> 

*Secure login interface for users to access their accounts and User-friendly registration form for new users to create an account.*

#### User Dashboard
![User Dashboard Image](https://github.com/Paras14/bookmanagement/blob/master/Screenshots/userBookPanel.png?raw=true)
*Main dashboard showing the user's book collection with options to add, remove, and update books.*

#### Admin Dashboard
![Admin Dashboard Image](https://github.com/Paras14/bookmanagement/blob/master/Screenshots/AdminPanel.png?raw=true)
*Admin view showing all books in the system with an option to delete.*

#### Add New Book Form
![Book Form Image](https://github.com/Paras14/bookmanagement/blob/master/Screenshots/addNewBook.png?raw=true)
*Modal Form for adding a new book to the user's collection, showcasing all input fields.*


#### Book Card
![Book Card Image](https://github.com/Paras14/bookmanagement/blob/master/Screenshots/ReadStatusButton.png?raw=true)
*Book Card with options to delete, edit Read Status, and AI Chat*


#### AI Chat Interface
![AI Chat Interface Image](https://github.com/Paras14/bookmanagement/blob/master/Screenshots/AIChatWindow.png?raw=true)
*The AI-powered chat interface where users can ask questions about a specific book.*

<hr>

![AI Chat Interface Result Image](https://github.com/Paras14/bookmanagement/blob/master/Screenshots/AIChatResult.png?raw=true)
*Response generated from llama3.2 3B running locally in docker container with ollama*

#### Docker Containers
![Docker containers Image](https://github.com/Paras14/bookmanagement/blob/master/Screenshots/Docker%20Containers.png?raw=true)
*Docker containers running mySQL Database and ollama with llama3.2 3B locally*

## Project Structure

### Backend (Java)
- `com.bookkeeping.bookmanagement.book`
  - `configuration`: Contains security and CORS configurations
  - `controller`: REST API endpoints
  - `model`: Entity classes (Book, Users, UserBook)
  - `repository`: JPA repositories
  - `service`: Business logic layer
  - `dtos`: Data Transfer Objects

### Frontend (React)
- `src`
  - `Components`: React components (BookCard, Container, Header, etc.)
  - `UserRegLogin`: Login and Registration components
  - `AIChat`: AI chat functionality

## Setup and Installation

### Prerequisites
- Java 21
- Node.js and npm
- MySQL
- Ollama (for AI functionality)

### Backend Setup
1. Clone the repository
2. Navigate to the project root directory
3. Update `application.properties` with your MySQL credentials
4. Run `mvn spring-boot:run` to start the backend server

### Frontend Setup
1. Navigate to the frontend directory
2. Run `npm install` to install dependencies
3. Run `npm start` to start the React development server

### Docker Compose (MySQL + Ollama)

Bring up both services with a single command:

```bash
docker-compose -f backend/docker-compose.yml up -d
```
Verify they’re running:
```bash
docker-compose -f backend/docker-compose.yml ps
```
(One‑time only) Pull the llama3.2 model into Ollama:
```bash
docker exec -it ollama ollama pull llama3.2
```



## API Endpoints
- `/api/auth/register`: User registration
- `/api/auth/login`: User login
- `/api/books`: CRUD operations for user's books
- `/api/books/admin`: Admin-only book management
- `/api/books/chat/{isbn}`: AI chat about a specific book

## Security
- JWT-based authentication
- Role-based access control (USER and ADMIN roles)

## AI Chat Feature
The application integrates with Ollama to provide an AI-powered chat feature about books. Users can ask questions about specific books in their collection.

## Future Improvements
- Implement password reset functionality
- Add book cover image upload feature
- Enhance AI chat with more context about books
- Implement unit and integration tests
- Add pagination for book listings

## Contributing
Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License
MIT License

Copyright (c) 2024 PARAS

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

