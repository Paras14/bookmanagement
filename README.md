# ShelfSense: Intelligent Book Management System

## Overview

ShelfSense is a full-stack web application for organizing and tracking your personal book collection. It features user authentication, a responsive book‑like UI, an admin panel, and AI‑driven book discussions.

## Technologies Used

**Backend:** Java 21, Spring Boot 3.3.4, Spring Security & JWT, Spring Data JPA, MySQL, Spring AI (Ollama llama3.2)

**Frontend:** Next.js 14 (App Router) + TypeScript, Tailwind CSS, ShadCN/ui, Fetch API

## Features

* User Authentication (Register & Login)
* Book Management (Add, Remove, Toggle Read Status)
* Responsive, book-like UI
* Admin Panel for global book oversight
* AI-Powered Chat about books

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

#### AI Chat Interface

![AI Chat Interface Image](https://github.com/Paras14/bookmanagement/blob/master/Screenshots/AIChatWindow.png?raw=true)
*The AI-powered chat interface where users can ask questions about a specific book.*

<hr>

![AI Chat Interface Result Image](https://github.com/Paras14/bookmanagement/blob/master/Screenshots/AIChatResult.png?raw=true)
*Response generated from llama3.2 3B running locally in docker container with ollama.*

#### Docker Containers

![Docker containers Image](https://github.com/Paras14/bookmanagement/blob/master/Screenshots/DockerContainers.png?raw=true)
*Docker containers running MySQL database and Ollama with llama3.2 3B locally.*

## Project Structure

```
bookmanagement/
├─ backend/               # Spring Boot service
│  └─ ...                 # Models, Controllers, Services, Repos
├─ frontend-cra/         # Legacy CRA frontend (to be removed)
└─ frontend/              # Next.js app
   ├─ app/                # Page routes
   ├─ components/         # UI components
   ├─ public/             # Static assets (Screenshots folder here)
   ├─ styles/             # Global CSS
   └─ ...                 # Config (tailwind.config.js, next.config.js)
```

## Setup & Installation

### Prerequisites

Node.js ≥18, npm
Java 21
MySQL
o
**Docker** (for MySQL & Ollama)

### Backend

```bash
cd backend
git pull
# configure src/main/resources/application.properties with MySQL creds
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
# open http://localhost:3000
```

### Docker Compose

```bash
cd backend
docker-compose up -d
docker-compose ps //confirm ollama container running
docker exec -it ollama ollama pull llama3.2
```

## API Endpoints

* **POST** `/api/auth/register` — Register new user
* **POST** `/api/auth/login` — Login; returns JWT
* **GET** `/api/books` — List user’s books
* **POST** `/api/books` — Add new book
* **PUT** `/api/books/{id}` — Update read status
* **DELETE** `/api/books/{id}` — Remove a book
* **GET** `/api/books/admin/all` — (ADMIN) List all books
* **DELETE** `/api/books/admin/{id}` — (ADMIN) Delete book
* **POST** `/api/books/chat/{id}` — Chat about a book

## Security

JWT-based authentication with USER & ADMIN roles.

## Contributing

Contributions welcome! Fork & submit a PR.

## License

MIT © 2025 PARAS
