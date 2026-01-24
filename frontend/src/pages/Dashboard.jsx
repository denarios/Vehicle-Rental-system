import { useState, useEffect } from 'react';
import { FiUsers, FiMapPin, FiTruck, FiCalendar, FiArrowRight } from 'react-icons/fi';
import { userApi, storeApi, vehicleApi, reservationApi } from '../services/api';

function Dashboard({ onNavigate }) {
    const [stats, setStats] = useState({
        users: 0,
        stores: 0,
        vehicles: 0,
        reservations: 0,
    });
    const [recentReservations, setRecentReservations] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchDashboardData();
    }, []);

    const fetchDashboardData = async () => {
        try {
            const [usersRes, storesRes] = await Promise.all([
                userApi.getAll(),
                storeApi.getAll(),
            ]);

            const users = usersRes.data;
            const stores = storesRes.data;

            let totalVehicles = 0;
            let allReservations = [];

            // Fetch vehicles and reservations for each store
            for (const store of stores) {
                try {
                    const [vehiclesRes, reservationsRes] = await Promise.all([
                        vehicleApi.getAll(store.storeId),
                        reservationApi.getByStore(store.storeId),
                    ]);

                    // Count vehicles from the map structure
                    Object.values(vehiclesRes.data || {}).forEach(vehicles => {
                        totalVehicles += vehicles.length;
                    });

                    allReservations = [...allReservations, ...reservationsRes.data];
                } catch (err) {
                    console.log('Error fetching store data:', err);
                }
            }

            setStats({
                users: users.length,
                stores: stores.length,
                vehicles: totalVehicles,
                reservations: allReservations.length,
            });

            // Get recent reservations (last 5)
            setRecentReservations(allReservations.slice(-5).reverse());
        } catch (error) {
            console.error('Error fetching dashboard data:', error);
        } finally {
            setLoading(false);
        }
    };

    const getStatusBadge = (status) => {
        const statusMap = {
            ACTIVE: 'badge-success',
            COMPLETED: 'badge-primary',
            CANCELLED: 'badge-danger',
        };
        return statusMap[status] || 'badge-gray';
    };

    return (
        <div className="animate-fade-in">
            <div className="page-header">
                <h2>Dashboard</h2>
                <p>Welcome to your Vehicle Rental System management dashboard</p>
            </div>

            {/* Stats Grid */}
            <div className="stats-grid">
                <div className="stat-card" onClick={() => onNavigate('users')} style={{ cursor: 'pointer' }}>
                    <div className="stat-icon primary">
                        <FiUsers />
                    </div>
                    <div className="stat-content">
                        <h3>{loading ? '...' : stats.users}</h3>
                        <p>Total Users</p>
                    </div>
                </div>

                <div className="stat-card" onClick={() => onNavigate('stores')} style={{ cursor: 'pointer' }}>
                    <div className="stat-icon accent">
                        <FiMapPin />
                    </div>
                    <div className="stat-content">
                        <h3>{loading ? '...' : stats.stores}</h3>
                        <p>Active Stores</p>
                    </div>
                </div>

                <div className="stat-card" onClick={() => onNavigate('vehicles')} style={{ cursor: 'pointer' }}>
                    <div className="stat-icon success">
                        <FiTruck />
                    </div>
                    <div className="stat-content">
                        <h3>{loading ? '...' : stats.vehicles}</h3>
                        <p>Total Vehicles</p>
                    </div>
                </div>

                <div className="stat-card" onClick={() => onNavigate('reservations')} style={{ cursor: 'pointer' }}>
                    <div className="stat-icon warning">
                        <FiCalendar />
                    </div>
                    <div className="stat-content">
                        <h3>{loading ? '...' : stats.reservations}</h3>
                        <p>Reservations</p>
                    </div>
                </div>
            </div>

            {/* Quick Actions */}
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '24px', marginBottom: '32px' }}>
                <div className="card">
                    <div className="card-header">
                        <h4 className="card-title">Quick Actions</h4>
                    </div>
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                        <button className="btn btn-primary" onClick={() => onNavigate('users')}>
                            <FiUsers /> Add New User
                        </button>
                        <button className="btn btn-accent" onClick={() => onNavigate('stores')}>
                            <FiMapPin /> Add New Store
                        </button>
                        <button className="btn btn-outline" onClick={() => onNavigate('reservations')}>
                            <FiCalendar /> Create Reservation
                        </button>
                    </div>
                </div>

                <div className="card">
                    <div className="card-header">
                        <h4 className="card-title">System Overview</h4>
                    </div>
                    <div style={{ color: 'var(--gray-400)', lineHeight: '1.8' }}>
                        <p>✅ Spring Boot Backend Running on port 8080</p>
                        <p>✅ RESTful API with Service Layer</p>
                        <p>✅ Global Exception Handling</p>
                        <p>✅ Reservation Status Tracking</p>
                        <p>✅ Input Validation</p>
                    </div>
                </div>
            </div>

            {/* Recent Reservations */}
            <div className="card">
                <div className="card-header">
                    <h4 className="card-title">Recent Reservations</h4>
                    <button className="btn btn-outline btn-sm" onClick={() => onNavigate('reservations')}>
                        View All <FiArrowRight />
                    </button>
                </div>

                {recentReservations.length > 0 ? (
                    <div className="table-container">
                        <table className="table">
                            <thead>
                                <tr>
                                    <th>User</th>
                                    <th>Vehicle Type</th>
                                    <th>From</th>
                                    <th>To</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                {recentReservations.map((res) => (
                                    <tr key={res.reservationId}>
                                        <td>{res.user?.name || 'Unknown'}</td>
                                        <td>{res.vehicle?.type || 'Unknown'}</td>
                                        <td>{res.fromDate}</td>
                                        <td>{res.toDate}</td>
                                        <td>
                                            <span className={`badge ${getStatusBadge(res.status)}`}>
                                                {res.status}
                                            </span>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                ) : (
                    <div className="empty-state">
                        <div className="empty-state-icon">📅</div>
                        <h3>No reservations yet</h3>
                        <p>Create your first reservation to see it here</p>
                        <button className="btn btn-primary" onClick={() => onNavigate('reservations')}>
                            Create Reservation
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Dashboard;
