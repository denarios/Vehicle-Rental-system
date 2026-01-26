import { useState, useEffect } from 'react';
import { FiDollarSign, FiTruck, FiCalendar, FiTrendingUp, FiPackage } from 'react-icons/fi';
import { storeApi } from '../services/api';
import { useAuth } from '../context/AuthContext';
import toast from 'react-hot-toast';

function StoreOwnerDashboard() {
    const { user } = useAuth();
    const [analytics, setAnalytics] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (user?.assignedStoreId) {
            loadAnalytics();
        }
    }, [user]);

    const loadAnalytics = async () => {
        try {
            const response = await storeApi.getAnalytics(user.assignedStoreId);
            setAnalytics(response.data);
        } catch (error) {
            toast.error('Failed to load analytics');
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="animate-fade-in">
                <div className="page-header">
                    <h2>Store Dashboard</h2>
                    <p>Loading analytics...</p>
                </div>
            </div>
        );
    }

    if (!analytics) {
        return (
            <div className="empty-state">
                <h3>No analytics available</h3>
                <p>Please contact admin to assign you a store</p>
            </div>
        );
    }

    return (
        <div className="animate-fade-in">
            {/* Header */}
            <div className="page-header">
                <div>
                    <h2>🏪 {analytics.storeName}</h2>
                    <p>📍 {analytics.city}, {analytics.state}</p>
                </div>
            </div>

            {/* Stats Grid */}
            <div className="stats-grid">
                <div className="stat-card">
                    <div className="stat-icon primary">
                        <FiDollarSign />
                    </div>
                    <div className="stat-content">
                        <h3>${analytics.monthlyRevenue.toFixed(2)}</h3>
                        <p>Monthly Revenue</p>
                    </div>
                </div>

                <div className="stat-card">
                    <div className="stat-icon accent">
                        <FiCalendar />
                    </div>
                    <div className="stat-content">
                        <h3>{analytics.activeReservations}</h3>
                        <p>Active Bookings</p>
                    </div>
                </div>

                <div className="stat-card">
                    <div className="stat-icon success">
                        <FiTruck />
                    </div>
                    <div className="stat-content">
                        <h3>{analytics.totalVehicles}</h3>
                        <p>Total Vehicles</p>
                    </div>
                </div>

                <div className="stat-card">
                    <div className="stat-icon warning">
                        <FiTrendingUp />
                    </div>
                    <div className="stat-content">
                        <h3>{analytics.completedReservations}</h3>
                        <p>Completed</p>
                    </div>
                </div>
            </div>

            {/* Revenue Card */}
            <div className="card" style={{ marginBottom: '24px' }}>
                <div className="card-header">
                    <h3 className="card-title">💰 Revenue Overview</h3>
                </div>
                <div style={{ padding: '20px 0' }}>
                    <div style={{
                        display: 'grid',
                        gridTemplateColumns: 'repeat(2, 1fr)',
                        gap: '24px'
                    }}>
                        <div>
                            <p style={{ color: 'var(--gray-400)', fontSize: '0.9rem', marginBottom: '8px' }}>
                                Total Revenue (All Time)
                            </p>
                            <h2 style={{ color: 'var(--accent-400)' }}>
                                ${analytics.totalRevenue.toFixed(2)}
                            </h2>
                        </div>
                        <div>
                            <p style={{ color: 'var(--gray-400)', fontSize: '0.9rem', marginBottom: '8px' }}>
                                This Month
                            </p>
                            <h2 style={{ color: 'var(--success-500)' }}>
                                ${analytics.monthlyRevenue.toFixed(2)}
                            </h2>
                        </div>
                    </div>
                </div>
            </div>

            {/* Fleet Breakdown */}
            <div className="card">
                <div className="card-header">
                    <h3 className="card-title">🚗 Fleet Breakdown</h3>
                </div>
                <div style={{ padding: '20px 0' }}>
                    <div style={{
                        display: 'grid',
                        gridTemplateColumns: 'repeat(3, 1fr)',
                        gap: '16px'
                    }}>
                        <div style={{
                            padding: '20px',
                            background: 'rgba(99, 102, 241, 0.1)',
                            borderRadius: 'var(--radius-md)',
                            textAlign: 'center'
                        }}>
                            <div style={{ fontSize: '2rem', marginBottom: '8px' }}>🚗</div>
                            <h3>{analytics.vehicleTypeBreakdown.CAR || 0}</h3>
                            <p style={{ color: 'var(--gray-400)', fontSize: '0.9rem' }}>Cars</p>
                        </div>

                        <div style={{
                            padding: '20px',
                            background: 'rgba(6, 182, 212, 0.1)',
                            borderRadius: 'var(--radius-md)',
                            textAlign: 'center'
                        }}>
                            <div style={{ fontSize: '2rem', marginBottom: '8px' }}>🏍️</div>
                            <h3>{analytics.vehicleTypeBreakdown.BIKE || 0}</h3>
                            <p style={{ color: 'var(--gray-400)', fontSize: '0.9rem' }}>Bikes</p>
                        </div>

                        <div style={{
                            padding: '20px',
                            background: 'rgba(34, 197, 94, 0.1)',
                            borderRadius: 'var(--radius-md)',
                            textAlign: 'center'
                        }}>
                            <div style={{ fontSize: '2rem', marginBottom: '8px' }}>🚚</div>
                            <h3>{analytics.vehicleTypeBreakdown.TRUCK || 0}</h3>
                            <p style={{ color: 'var(--gray-400)', fontSize: '0.9rem' }}>Trucks</p>
                        </div>
                    </div>
                </div>
            </div>

            {/* Booking Stats */}
            <div className="card" style={{ marginTop: '24px' }}>
                <div className="card-header">
                    <h3 className="card-title">📊 Booking Statistics</h3>
                </div>
                <div className="table-container">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Metric</th>
                                <th>Count</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Total Reservations</td>
                                <td><strong>{analytics.totalReservations}</strong></td>
                                <td><span className="badge badge-primary">All Time</span></td>
                            </tr>
                            <tr>
                                <td>Active Bookings</td>
                                <td><strong>{analytics.activeReservations}</strong></td>
                                <td><span className="badge badge-accent">Current</span></td>
                            </tr>
                            <tr>
                                <td>Completed</td>
                                <td><strong>{analytics.completedReservations}</strong></td>
                                <td><span className="badge badge-success">Done</span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}

export default StoreOwnerDashboard;
