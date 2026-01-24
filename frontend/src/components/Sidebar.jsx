import {
    FiHome,
    FiUsers,
    FiMapPin,
    FiTruck,
    FiCalendar,
    FiSettings
} from 'react-icons/fi';

const navItems = [
    { id: 'dashboard', label: 'Dashboard', icon: FiHome },
    { id: 'users', label: 'Users', icon: FiUsers },
    { id: 'stores', label: 'Stores', icon: FiMapPin },
    { id: 'vehicles', label: 'Vehicles', icon: FiTruck },
    { id: 'reservations', label: 'Reservations', icon: FiCalendar },
];

function Sidebar({ activePage, onNavigate }) {
    return (
        <aside className="sidebar">
            <div className="sidebar-logo">
                <div className="sidebar-logo-icon">🚗</div>
                <h1>RentaVehicle</h1>
            </div>

            <nav className="sidebar-nav">
                {navItems.map((item) => (
                    <button
                        key={item.id}
                        className={`nav-item ${activePage === item.id ? 'active' : ''}`}
                        onClick={() => onNavigate(item.id)}
                    >
                        <item.icon />
                        <span>{item.label}</span>
                    </button>
                ))}
            </nav>

            <div style={{ marginTop: 'auto', paddingTop: '20px', borderTop: '1px solid rgba(255,255,255,0.08)' }}>
                <button className="nav-item">
                    <FiSettings />
                    <span>Settings</span>
                </button>
            </div>
        </aside>
    );
}

export default Sidebar;
