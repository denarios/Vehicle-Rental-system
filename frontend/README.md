# Vehicle Rental System - React Frontend

A modern, premium React.js frontend for the Vehicle Rental System API.

## 🚀 Quick Start

### Prerequisites
- Node.js 18+ 
- npm or yarn
- Backend running on port 8080

### Installation

```bash
cd frontend
npm install
```

### Running the Development Server

```bash
npm run dev
```

The app will start at `http://localhost:3000`

## 🎨 Features

- **Dashboard** - Overview with stats and quick actions
- **Users Management** - Add and view registered users
- **Stores Management** - Create and manage store locations
- **Vehicles Management** - Add vehicles to stores, view by type
- **Reservations** - Create, cancel, and complete reservations

## 📁 Project Structure

```
frontend/
├── public/
│   └── vite.svg          # App favicon
├── src/
│   ├── components/
│   │   ├── Modal.jsx     # Reusable modal component
│   │   └── Sidebar.jsx   # Navigation sidebar
│   ├── pages/
│   │   ├── Dashboard.jsx
│   │   ├── UsersPage.jsx
│   │   ├── StoresPage.jsx
│   │   ├── VehiclesPage.jsx
│   │   └── ReservationsPage.jsx
│   ├── services/
│   │   └── api.js        # Axios API client
│   ├── App.jsx           # Main app with routing
│   ├── main.jsx          # React entry point
│   └── index.css         # Premium dark theme styles
├── index.html
├── package.json
└── vite.config.js
```

## 🔗 API Integration

The frontend connects to the Spring Boot backend via proxy:

| Frontend | Backend |
|----------|---------|
| `GET /api/users` | `GET http://localhost:8080/api/users` |
| `POST /api/stores` | `POST http://localhost:8080/api/stores` |
| etc. | etc. |

## 🎯 Design

- **Dark Mode** with premium glassmorphism effects
- **Gradient accents** for visual appeal
- **Micro animations** for smooth interactions
- **Responsive** design for all screen sizes
- **Toast notifications** for user feedback

## 🛠️ Tech Stack

- React 18
- Vite 5
- Axios for HTTP requests
- React Icons
- React Hot Toast
