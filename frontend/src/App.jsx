import { useState } from 'react';
import { Toaster } from 'react-hot-toast';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import UsersPage from './pages/UsersPage';
import StoresPage from './pages/StoresPage';
import VehiclesPage from './pages/VehiclesPage';
import ReservationsPage from './pages/ReservationsPage';

function App() {
    const [activePage, setActivePage] = useState('dashboard');

    const renderPage = () => {
        switch (activePage) {
            case 'dashboard':
                return <Dashboard onNavigate={setActivePage} />;
            case 'users':
                return <UsersPage />;
            case 'stores':
                return <StoresPage />;
            case 'vehicles':
                return <VehiclesPage />;
            case 'reservations':
                return <ReservationsPage />;
            default:
                return <Dashboard onNavigate={setActivePage} />;
        }
    };

    return (
        <div className="app-container">
            <Toaster
                position="top-right"
                toastOptions={{
                    className: 'toast-custom',
                    duration: 3000,
                }}
            />
            <Sidebar activePage={activePage} onNavigate={setActivePage} />
            <main className="main-content">
                {renderPage()}
            </main>
        </div>
    );
}

export default App;
