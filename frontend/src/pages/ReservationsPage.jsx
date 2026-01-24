import { useState, useEffect } from 'react';
import { FiPlus, FiCheck, FiX, FiCalendar } from 'react-icons/fi';
import toast from 'react-hot-toast';
import { userApi, storeApi, vehicleApi, reservationApi } from '../services/api';
import Modal from '../components/Modal';

function ReservationsPage() {
    const [stores, setStores] = useState([]);
    const [users, setUsers] = useState([]);
    const [selectedStore, setSelectedStore] = useState('');
    const [reservations, setReservations] = useState([]);
    const [availableVehicles, setAvailableVehicles] = useState({});
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({
        userId: '',
        vehicleId: '',
        fromDate: '',
        toDate: '',
    });

    useEffect(() => {
        fetchInitialData();
    }, []);

    useEffect(() => {
        if (selectedStore) {
            fetchReservations(selectedStore);
            fetchAvailableVehicles(selectedStore);
        }
    }, [selectedStore]);

    const fetchInitialData = async () => {
        try {
            const [storesRes, usersRes] = await Promise.all([
                storeApi.getAll(),
                userApi.getAll(),
            ]);
            setStores(storesRes.data);
            setUsers(usersRes.data);
            if (storesRes.data.length > 0) {
                setSelectedStore(storesRes.data[0].storeId);
            }
            if (usersRes.data.length > 0) {
                setFormData(prev => ({ ...prev, userId: usersRes.data[0].id }));
            }
        } catch (error) {
            toast.error('Failed to fetch data');
        } finally {
            setLoading(false);
        }
    };

    const fetchReservations = async (storeId) => {
        try {
            const response = await reservationApi.getByStore(storeId);
            setReservations(response.data);
        } catch (error) {
            console.error('Failed to fetch reservations');
            setReservations([]);
        }
    };

    const fetchAvailableVehicles = async (storeId) => {
        try {
            const response = await vehicleApi.getAvailable(storeId);
            setAvailableVehicles(response.data || {});
        } catch (error) {
            setAvailableVehicles({});
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!formData.userId || !formData.vehicleId || !formData.fromDate || !formData.toDate) {
            toast.error('Please fill all fields');
            return;
        }

        try {
            await reservationApi.create({
                storeId: selectedStore,
                ...formData,
            });
            await fetchReservations(selectedStore);
            await fetchAvailableVehicles(selectedStore);
            setIsModalOpen(false);
            setFormData(prev => ({
                ...prev,
                vehicleId: '',
                fromDate: '',
                toDate: '',
            }));
            toast.success('Reservation created successfully!');
        } catch (error) {
            toast.error(error.response?.data?.message || 'Failed to create reservation');
        }
    };

    const handleCancel = async (reservationId) => {
        try {
            await reservationApi.cancel(selectedStore, reservationId);
            await fetchReservations(selectedStore);
            await fetchAvailableVehicles(selectedStore);
            toast.success('Reservation cancelled!');
        } catch (error) {
            toast.error(error.response?.data?.message || 'Failed to cancel reservation');
        }
    };

    const handleComplete = async (reservationId) => {
        try {
            await reservationApi.complete(selectedStore, reservationId);
            await fetchReservations(selectedStore);
            await fetchAvailableVehicles(selectedStore);
            toast.success('Reservation marked as complete!');
        } catch (error) {
            toast.error(error.response?.data?.message || 'Failed to complete reservation');
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

    const getAllAvailableVehiclesFlat = () => {
        const all = [];
        Object.entries(availableVehicles).forEach(([type, list]) => {
            list.forEach(v => all.push({ ...v, type }));
        });
        return all;
    };

    const getVehicleEmoji = (type) => {
        return type === 'CAR' ? '🚗' : '🏍️';
    };

    // Get today's date in YYYY-MM-DD format for min date
    const today = new Date().toISOString().split('T')[0];

    return (
        <div className="animate-fade-in">
            <div className="page-header">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <div>
                        <h2>Reservations</h2>
                        <p>Manage vehicle bookings and reservations</p>
                    </div>
                    <button
                        className="btn btn-primary"
                        onClick={() => setIsModalOpen(true)}
                        disabled={!selectedStore || users.length === 0}
                    >
                        <FiPlus /> New Reservation
                    </button>
                </div>
            </div>

            {/* Store Selector */}
            <div className="card" style={{ marginBottom: '24px' }}>
                <div className="form-group" style={{ marginBottom: 0 }}>
                    <label className="form-label">Select Store</label>
                    <select
                        className="form-select"
                        value={selectedStore}
                        onChange={(e) => setSelectedStore(e.target.value)}
                    >
                        {stores.length === 0 && <option value="">No stores available</option>}
                        {stores.map((store) => (
                            <option key={store.storeId} value={store.storeId}>
                                {store.location?.district}, {store.location?.state} - {store.location?.pinCode}
                            </option>
                        ))}
                    </select>
                </div>
            </div>

            {/* Reservations Table */}
            {loading ? (
                <div className="empty-state">
                    <p>Loading reservations...</p>
                </div>
            ) : reservations.length > 0 ? (
                <div className="card">
                    <div className="card-header">
                        <h4 className="card-title">Reservations ({reservations.length})</h4>
                    </div>
                    <div className="table-container">
                        <table className="table">
                            <thead>
                                <tr>
                                    <th>User</th>
                                    <th>Vehicle</th>
                                    <th>From</th>
                                    <th>To</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {reservations.map((res) => (
                                    <tr key={res.reservationId}>
                                        <td>
                                            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                                                <div style={{
                                                    width: '32px',
                                                    height: '32px',
                                                    borderRadius: '50%',
                                                    background: 'var(--gradient-primary)',
                                                    display: 'flex',
                                                    alignItems: 'center',
                                                    justifyContent: 'center',
                                                    fontSize: '0.8rem',
                                                    color: 'white'
                                                }}>
                                                    {res.user?.name?.charAt(0) || '?'}
                                                </div>
                                                <span>{res.user?.name || 'Unknown'}</span>
                                            </div>
                                        </td>
                                        <td>
                                            <span style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                                                {getVehicleEmoji(res.vehicle?.type)} {res.vehicle?.type || 'Unknown'}
                                            </span>
                                        </td>
                                        <td>{res.fromDate}</td>
                                        <td>{res.toDate}</td>
                                        <td>
                                            <span className={`badge ${getStatusBadge(res.status)}`}>
                                                {res.status}
                                            </span>
                                        </td>
                                        <td>
                                            {res.status === 'ACTIVE' && (
                                                <div style={{ display: 'flex', gap: '8px' }}>
                                                    <button
                                                        className="btn btn-success btn-sm"
                                                        onClick={() => handleComplete(res.reservationId)}
                                                        title="Mark as Complete"
                                                    >
                                                        <FiCheck />
                                                    </button>
                                                    <button
                                                        className="btn btn-danger btn-sm"
                                                        onClick={() => handleCancel(res.reservationId)}
                                                        title="Cancel"
                                                    >
                                                        <FiX />
                                                    </button>
                                                </div>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            ) : (
                <div className="empty-state">
                    <div className="empty-state-icon">📅</div>
                    <h3>No reservations for this store</h3>
                    <p>Create your first reservation to populate this list</p>
                    <button
                        className="btn btn-primary"
                        onClick={() => setIsModalOpen(true)}
                        disabled={!selectedStore || users.length === 0}
                    >
                        <FiPlus /> New Reservation
                    </button>
                </div>
            )}

            {/* Create Reservation Modal */}
            <Modal
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                title="Create New Reservation"
                footer={
                    <>
                        <button className="btn btn-outline" onClick={() => setIsModalOpen(false)}>
                            Cancel
                        </button>
                        <button className="btn btn-primary" onClick={handleSubmit}>
                            Create Reservation
                        </button>
                    </>
                }
            >
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label className="form-label">Select User</label>
                        <select
                            className="form-select"
                            value={formData.userId}
                            onChange={(e) => setFormData({ ...formData, userId: e.target.value })}
                        >
                            {users.length === 0 && <option value="">No users available</option>}
                            {users.map((user) => (
                                <option key={user.id} value={user.id}>
                                    {user.name} {user.drivingLicence ? '✓' : '(No License)'}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label className="form-label">Select Vehicle</label>
                        <select
                            className="form-select"
                            value={formData.vehicleId}
                            onChange={(e) => setFormData({ ...formData, vehicleId: e.target.value })}
                        >
                            <option value="">Choose a vehicle</option>
                            {getAllAvailableVehiclesFlat().map((vehicle) => (
                                <option key={vehicle.id} value={vehicle.id}>
                                    {getVehicleEmoji(vehicle.type)} {vehicle.type} - {vehicle.id.substring(0, 8)}...
                                </option>
                            ))}
                        </select>
                        {getAllAvailableVehiclesFlat().length === 0 && (
                            <p style={{ color: 'var(--warning-500)', fontSize: '0.85rem', marginTop: '8px' }}>
                                ⚠️ No available vehicles in this store
                            </p>
                        )}
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label className="form-label">From Date</label>
                            <input
                                type="date"
                                className="form-input"
                                min={today}
                                value={formData.fromDate}
                                onChange={(e) => setFormData({ ...formData, fromDate: e.target.value })}
                            />
                        </div>
                        <div className="form-group">
                            <label className="form-label">To Date</label>
                            <input
                                type="date"
                                className="form-input"
                                min={formData.fromDate || today}
                                value={formData.toDate}
                                onChange={(e) => setFormData({ ...formData, toDate: e.target.value })}
                            />
                        </div>
                    </div>
                </form>
            </Modal>
        </div>
    );
}

export default ReservationsPage;
