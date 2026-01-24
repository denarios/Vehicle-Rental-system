import { useState, useEffect } from 'react';
import { FiPlus, FiTruck } from 'react-icons/fi';
import toast from 'react-hot-toast';
import { storeApi, vehicleApi } from '../services/api';
import Modal from '../components/Modal';

function VehiclesPage() {
    const [stores, setStores] = useState([]);
    const [selectedStore, setSelectedStore] = useState('');
    const [vehicles, setVehicles] = useState({});
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({
        type: 'BIKE',
        status: 'ACTIVE',
    });

    useEffect(() => {
        fetchStores();
    }, []);

    useEffect(() => {
        if (selectedStore) {
            fetchVehicles(selectedStore);
        }
    }, [selectedStore]);

    const fetchStores = async () => {
        try {
            const response = await storeApi.getAll();
            setStores(response.data);
            if (response.data.length > 0) {
                setSelectedStore(response.data[0].storeId);
            }
        } catch (error) {
            toast.error('Failed to fetch stores');
        } finally {
            setLoading(false);
        }
    };

    const fetchVehicles = async (storeId) => {
        try {
            setLoading(true);
            const response = await vehicleApi.getAll(storeId);
            setVehicles(response.data || {});
        } catch (error) {
            toast.error('Failed to fetch vehicles');
            setVehicles({});
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!selectedStore) {
            toast.error('Please select a store first');
            return;
        }

        try {
            const response = await vehicleApi.add(selectedStore, formData);
            // Refresh vehicles list
            await fetchVehicles(selectedStore);
            setIsModalOpen(false);
            toast.success('Vehicle added successfully!');
        } catch (error) {
            toast.error(error.response?.data?.message || 'Failed to add vehicle');
        }
    };

    const getVehicleIcon = (type) => {
        // Using FiTruck for all types - icons don't have specific car/bike variants
        return <FiTruck />;
    };

    const getVehicleEmoji = (type) => {
        switch (type) {
            case 'CAR': return '🚗';
            case 'BIKE': return '🏍️';
            default: return '🚙';
        }
    };

    const getAllVehiclesFlat = () => {
        const allVehicles = [];
        Object.entries(vehicles).forEach(([type, vehicleList]) => {
            vehicleList.forEach(v => {
                allVehicles.push({ ...v, type });
            });
        });
        return allVehicles;
    };

    const copyId = (id) => {
        navigator.clipboard.writeText(id);
        toast.success('Vehicle ID copied!');
    };

    return (
        <div className="animate-fade-in">
            <div className="page-header">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <div>
                        <h2>Vehicles</h2>
                        <p>Manage vehicles in your rental fleet</p>
                    </div>
                    <button
                        className="btn btn-primary"
                        onClick={() => setIsModalOpen(true)}
                        disabled={!selectedStore}
                    >
                        <FiPlus /> Add Vehicle
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

            {/* Vehicle Stats */}
            {Object.keys(vehicles).length > 0 && (
                <div style={{ display: 'flex', gap: '16px', marginBottom: '24px' }}>
                    {Object.entries(vehicles).map(([type, list]) => (
                        <div
                            key={type}
                            className="stat-card"
                            style={{ flex: 1 }}
                        >
                            <div className={`stat-icon ${type === 'CAR' ? 'primary' : 'accent'}`}>
                                {getVehicleIcon(type)}
                            </div>
                            <div className="stat-content">
                                <h3>{list.length}</h3>
                                <p>{type}s</p>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {/* Vehicles Grid */}
            {loading ? (
                <div className="empty-state">
                    <p>Loading vehicles...</p>
                </div>
            ) : getAllVehiclesFlat().length > 0 ? (
                <div className="card-grid">
                    {getAllVehiclesFlat().map((vehicle) => (
                        <div
                            key={vehicle.id}
                            className="vehicle-card"
                            onClick={() => copyId(vehicle.id)}
                        >
                            <div className="vehicle-card-image">
                                <span>{getVehicleEmoji(vehicle.type)}</span>
                            </div>
                            <div className="vehicle-card-body">
                                <div className="vehicle-card-title">
                                    <span>{vehicle.type}</span>
                                    <span className={`badge ${vehicle.status === 'ACTIVE' ? 'badge-success' : 'badge-danger'}`}>
                                        {vehicle.status}
                                    </span>
                                </div>
                                <div className="vehicle-card-id">
                                    ID: {vehicle.id}
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <div className="empty-state">
                    <div className="empty-state-icon">🚗</div>
                    <h3>No vehicles in this store</h3>
                    <p>Add vehicles to this store to start accepting reservations</p>
                    <button
                        className="btn btn-primary"
                        onClick={() => setIsModalOpen(true)}
                        disabled={!selectedStore}
                    >
                        <FiPlus /> Add Vehicle
                    </button>
                </div>
            )}

            {/* Add Vehicle Modal */}
            <Modal
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                title="Add New Vehicle"
                footer={
                    <>
                        <button className="btn btn-outline" onClick={() => setIsModalOpen(false)}>
                            Cancel
                        </button>
                        <button className="btn btn-primary" onClick={handleSubmit}>
                            Add Vehicle
                        </button>
                    </>
                }
            >
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label className="form-label">Vehicle Type</label>
                        <select
                            className="form-select"
                            value={formData.type}
                            onChange={(e) => setFormData({ ...formData, type: e.target.value })}
                        >
                            <option value="BIKE">🏍️ Bike</option>
                            <option value="CAR">🚗 Car</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <label className="form-label">Status</label>
                        <select
                            className="form-select"
                            value={formData.status}
                            onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                        >
                            <option value="ACTIVE">Active (Available)</option>
                            <option value="INACTIVE">Inactive (Maintenance)</option>
                        </select>
                    </div>
                </form>
            </Modal>
        </div>
    );
}

export default VehiclesPage;
