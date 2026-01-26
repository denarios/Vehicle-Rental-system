import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import LoginPage from './pages/LoginPage';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';
import UsersPage from './pages/UsersPage';
import StoresPage from './pages/StoresPage';
import VehiclesPage from './pages/VehiclesPage';
import ReservationsPage from './pages/ReservationsPage';
import BrowseVehiclesPage from './pages/BrowseVehiclesPage';
import StoreOwnerDashboard from './pages/StoreOwnerDashboard';

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Toaster
                    position="top-right"
                    toastOptions={{
                        className: 'toast-custom',
                        duration: 3000,
                    }}
                />
                <Routes>
                    {/* Public Route */}
                    <Route path="/login" element={<LoginPage />} />

                    {/* Protected Routes */}
                    <Route path="/" element={
                        <ProtectedRoute>
                            <Layout />
                        </ProtectedRoute>
                    }>
                        <Route index element={<Dashboard />} />
                        <Route path="browse" element={<BrowseVehiclesPage />} />
                        <Route path="store-dashboard" element={<StoreOwnerDashboard />} />
                        <Route path="users" element={<UsersPage />} />
                        <Route path="stores" element={<StoresPage />} />
                        <Route path="vehicles" element={<VehiclesPage />} />
                        <Route path="reservations" element={<ReservationsPage />} />
                    </Route>

                    {/* Fallback */}
                    <Route path="*" element={<Navigate to="/" replace />} />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;
