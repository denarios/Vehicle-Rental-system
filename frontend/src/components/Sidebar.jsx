import { NavLink, useNavigate } from 'react-router-dom';
import {
    FiHome,
    FiUsers,
    FiMapPin,
    FiTruck,
    FiCalendar,
    FiLogOut,
    FiSearch,
    FiBarChart2
} from 'react-icons/fi';
import { useAuth } from '../context/AuthContext';
import toast from 'react-hot-toast';

const navItems = [
    // Admin items
    { id: 'dashboard', path: '/', label: 'Dashboard', icon: FiHome, roles: ['ADMIN'] },
    { id: 'users', path: '/users', label: 'Users', icon: FiUsers, roles: ['ADMIN'] },
    { id: 'stores', path: '/stores', label: 'Stores', icon: FiMapPin, roles: ['ADMIN'] },
    { id: 'vehicles', path: '/vehicles', label: 'Vehicles', icon: FiTruck, roles: ['ADMIN'] },
    { id: 'reservations', path: '/reservations', label: 'Reservations', icon: FiCalendar, roles: ['ADMIN'] },

    // Store Owner items
    { id: 'store-dashboard', path: '/store-dashboard', label: 'My Store', icon: FiBarChart2, roles: ['STORE_OWNER'] },
    { id: 'vehicles', path: '/vehicles', label: 'My Vehicles', icon: FiTruck, roles: ['STORE_OWNER'] },
    { id: 'reservations', path: '/reservations', label: 'Bookings', icon: FiCalendar, roles: ['STORE_OWNER'] },

    // Customer items
    { id: 'browse', path: '/browse', label: 'Browse Vehicles', icon: FiSearch, roles: ['CUSTOMER'] },
    { id: 'reservations', path: '/reservations', label: 'My Bookings', icon: FiCalendar, roles: ['CUSTOMER'] },
];

function Sidebar() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        toast.success('Logged out successfully');
        navigate('/login');
    };

    const canAccess = (item) => {
        if (!user) return false;
        return item.roles.includes(user.role);
    };

    const getRoleEmoji = () => {
        if (user?.role === 'ADMIN') return '👑';
        if (user?.role === 'STORE_OWNER') return '🏪';
        return '👤';
    };

    const getRoleLabel = () => {
        if (user?.role === 'ADMIN') return 'Admin';
        if (user?.role === 'STORE_OWNER') return 'Store Owner';
        return 'Customer';
    };

    return (
        <aside className="sidebar">
            <div className="sidebar-logo">
                <div className="sidebar-logo-icon">🚗</div>
                <h1>RentaVehicle</h1>
            </div>

            {/* User Info */}
            <div style={{
                padding: '16px',
                margin: '0 12px 16px',
                background: 'rgba(255, 255, 255, 0.05)',
                borderRadius: '8px',
                fontSize: '0.875rem'
            }}>
                <div style={{ fontWeight: 600, marginBottom: '4px' }}>{user?.name}</div>
                <div style={{ color: 'var(--gray-400)', fontSize: '0.75rem' }}>
                    {getRoleEmoji()} {getRoleLabel()}
                </div>
            </div>

            <nav className="sidebar-nav">
                {navItems.filter(canAccess).map((item) => (
                    <NavLink
                        key={item.id}
                        to={item.path}
                        end={item.path === '/'}
                        className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
                    >
                        <item.icon />
                        <span>{item.label}</span>
                    </NavLink>
                ))}
            </nav>

            <div style={{ marginTop: 'auto', paddingTop: '20px', borderTop: '1px solid rgba(255,255,255,0.08)' }}>
                <button className="nav-item" onClick={handleLogout}>
                    <FiLogOut />
                    <span>Logout</span>
                </button>
            </div>
        </aside>
    );
}

export default Sidebar;
