import { useState, useEffect } from 'react';
import { FiPlus, FiTruck, FiDollarSign, FiSearch, FiX } from 'react-icons/fi';
import toast from 'react-hot-toast';
import { storeApi, vehicleApi } from '../services/api';
import Modal from '../components/Modal';

// Default daily rates for each vehicle type (matches backend VehicleFactory)
const VEHICLE_PRICING = {
    BIKE: { dailyRate: 25, capacity: '2 passengers', emoji: '🏍️' },
    CAR: { dailyRate: 50, capacity: '5 passengers', emoji: '🚗' },
    TRUCK: { dailyRate: 100, capacity: '10 ton cargo', emoji: '🚚' },
};

function VehiclesPage() {
    const [stores, setStores] = useState([]);
    const [selectedStore, setSelectedStore] = useState('');
    const [vehicles, setVehicles] = useState({});
    const [searchResults, setSearchResults] = useState(null); // null = show all, array = show search results
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({
        type: 'BIKE',
        status: 'ACTIVE',
    });

    // Search filters (Specification Pattern)
    const [searchFilters, setSearchFilters] = useState({
        type: '',
        status: '',
        minPrice: '',
        maxPrice: '',
    });
    const [isSearching, setIsSearching] = useState(false);

    useEffect(() => {
        fetchStores();
    }, []);

    useEffect(() => {
        if (selectedStore) {
            fetchVehicles(selectedStore);
            setSearchResults(null); // Clear search when store changes
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
        if (!storeId) {
            setVehicles({});
            return;
        }

        try {
            setLoading(true);
            const response = await vehicleApi.getByStore(storeId);
            setVehicles(response.data || {});
        } catch (error) {
            toast.error('Failed to fetch vehicles');
            setVehicles({});
        } finally {
            setLoading(false);
        }
    };

    // Search vehicles using Specification Pattern API
    const handleSearch = async () => {
        if (!selectedStore) return;

        // Check if any filter is set
        const hasFilters = Object.values(searchFilters).some(v => v !== '');
        if (!hasFilters) {
            setSearchResults(null);
            return;
        }

        try {
            setIsSearching(true);
            const response = await vehicleApi.search(selectedStore, {
                type: searchFilters.type || undefined,
                status: searchFilters.status || undefined,
                minPrice: searchFilters.minPrice || undefined,
                maxPrice: searchFilters.maxPrice || undefined,
            });
            setSearchResults(response.data);
            toast.success(`Found ${response.data.length} vehicle(s)`);
        } catch (error) {
            toast.error('Search failed');
        } finally {
            setIsSearching(false);
        }
    };

    const clearSearch = () => {
        setSearchFilters({ type: '', status: '', minPrice: '', maxPrice: '' });
        setSearchResults(null);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!selectedStore) {
            toast.error('Please select a store first');
            return;
        }

        try {
            await vehicleApi.add(selectedStore, formData);
            await fetchVehicles(selectedStore);
            setIsModalOpen(false);
            toast.success('Vehicle added successfully!');
        } catch (error) {
            toast.error(error.response?.data?.message || 'Failed to add vehicle');
        }
    };

    const getVehicleIcon = (type) => {
        return <FiTruck />;
    };

    const getVehicleEmoji = (type) => {
        return VEHICLE_PRICING[type]?.emoji || '🚙';
    };

    const getVehiclePricing = (type) => {
        return VEHICLE_PRICING[type] || VEHICLE_PRICING.CAR;
    };

    const getAllVehiclesFlat = () => {
        // If search results exist, use them
        if (searchResults !== null) {
            return searchResults;
        }
        // Otherwise, flatten the vehicles map
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

    const getIconColorClass = (type) => {
        switch (type) {
            case 'CAR': return 'primary';
            case 'BIKE': return 'accent';
            case 'TRUCK': return 'warning';
            default: return 'primary';
        }
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

            {/* Search Filters - Specification Pattern */}
            <div className="card" style={{ marginBottom: '24px' }}>
                <div className="card-header" style={{ marginBottom: '16px' }}>
                    <h4 className="card-title" style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                        <FiSearch /> Search Vehicles (Specification Pattern)
                    </h4>
                </div>
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', gap: '16px' }}>
                    <div className="form-group" style={{ marginBottom: 0 }}>
                        <label className="form-label">Type</label>
                        <select
                            className="form-select"
                            value={searchFilters.type}
                            onChange={(e) => setSearchFilters({ ...searchFilters, type: e.target.value })}
                        >
                            <option value="">All Types</option>
                            <option value="BIKE">🏍️ Bike</option>
                            <option value="CAR">🚗 Car</option>
                            <option value="TRUCK">🚚 Truck</option>
                        </select>
                    </div>
                    <div className="form-group" style={{ marginBottom: 0 }}>
                        <label className="form-label">Status</label>
                        <select
                            className="form-select"
                            value={searchFilters.status}
                            onChange={(e) => setSearchFilters({ ...searchFilters, status: e.target.value })}
                        >
                            <option value="">All Status</option>
                            <option value="ACTIVE">Active</option>
                            <option value="INACTIVE">Inactive</option>
                        </select>
                    </div>
                    <div className="form-group" style={{ marginBottom: 0 }}>
                        <label className="form-label">Min Price</label>
                        <input
                            type="number"
                            className="form-input"
                            placeholder="$0"
                            value={searchFilters.minPrice}
                            onChange={(e) => setSearchFilters({ ...searchFilters, minPrice: e.target.value })}
                        />
                    </div>
                    <div className="form-group" style={{ marginBottom: 0 }}>
                        <label className="form-label">Max Price</label>
                        <input
                            type="number"
                            className="form-input"
                            placeholder="$999"
                            value={searchFilters.maxPrice}
                            onChange={(e) => setSearchFilters({ ...searchFilters, maxPrice: e.target.value })}
                        />
                    </div>
                </div>
                <div style={{ display: 'flex', gap: '12px', marginTop: '16px' }}>
                    <button
                        className="btn btn-primary"
                        onClick={handleSearch}
                        disabled={!selectedStore || isSearching}
                    >
                        <FiSearch /> {isSearching ? 'Searching...' : 'Search'}
                    </button>
                    {searchResults !== null && (
                        <button className="btn btn-outline" onClick={clearSearch}>
                            <FiX /> Clear
                        </button>
                    )}
                </div>
                {searchResults !== null && (
                    <div style={{ marginTop: '12px', padding: '8px 12px', background: 'var(--gray-800)', borderRadius: '6px', fontSize: '0.9rem' }}>
                        📊 Showing <strong>{searchResults.length}</strong> result(s) from search
                    </div>
                )}
            </div>

            {/* Vehicle Stats */}
            {Object.keys(vehicles).length > 0 && searchResults === null && (
                <div style={{ display: 'flex', gap: '16px', marginBottom: '24px', flexWrap: 'wrap' }}>
                    {Object.entries(vehicles).map(([type, list]) => (
                        <div
                            key={type}
                            className="stat-card"
                            style={{ flex: '1', minWidth: '150px' }}
                        >
                            <div className={`stat-icon ${getIconColorClass(type)}`}>
                                {getVehicleIcon(type)}
                            </div>
                            <div className="stat-content">
                                <h3>{list.length}</h3>
                                <p>{type}s</p>
                                <small style={{ color: 'var(--success-500)' }}>
                                    ${getVehiclePricing(type).dailyRate}/day
                                </small>
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
                                <div className="vehicle-card-pricing" style={{
                                    display: 'flex',
                                    gap: '12px',
                                    fontSize: '0.85rem',
                                    color: 'var(--gray-400)',
                                    marginTop: '8px'
                                }}>
                                    <span style={{ color: 'var(--success-400)' }}>
                                        <FiDollarSign style={{ verticalAlign: 'middle' }} />
                                        {vehicle.dailyRate || getVehiclePricing(vehicle.type).dailyRate}/day
                                    </span>
                                    <span>
                                        {vehicle.capacity || getVehiclePricing(vehicle.type).capacity}
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
                    <div className="empty-state-icon">{searchResults !== null ? '🔍' : '🚗'}</div>
                    <h3>{searchResults !== null ? 'No matching vehicles' : 'No vehicles in this store'}</h3>
                    <p>{searchResults !== null ? 'Try adjusting your search filters' : 'Add vehicles to this store to start accepting reservations'}</p>
                    {searchResults !== null ? (
                        <button className="btn btn-outline" onClick={clearSearch}>
                            <FiX /> Clear Search
                        </button>
                    ) : (
                        <button
                            className="btn btn-primary"
                            onClick={() => setIsModalOpen(true)}
                            disabled={!selectedStore}
                        >
                            <FiPlus /> Add Vehicle
                        </button>
                    )}
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
                            <option value="BIKE">🏍️ Bike - $25/day (2 passengers)</option>
                            <option value="CAR">🚗 Car - $50/day (5 passengers)</option>
                            <option value="TRUCK">🚚 Truck - $100/day (10 ton cargo)</option>
                        </select>
                    </div>

                    <div style={{
                        background: 'var(--gray-800)',
                        borderRadius: '8px',
                        padding: '12px',
                        marginBottom: '16px',
                        border: '1px solid var(--gray-700)'
                    }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
                            <span style={{ fontSize: '1.5rem' }}>{getVehicleEmoji(formData.type)}</span>
                            <strong>{formData.type}</strong>
                        </div>
                        <div style={{ fontSize: '0.9rem', color: 'var(--gray-400)' }}>
                            <div>💰 Daily Rate: <span style={{ color: 'var(--success-400)' }}>${getVehiclePricing(formData.type).dailyRate}</span></div>
                            <div>👥 Capacity: {getVehiclePricing(formData.type).capacity}</div>
                        </div>
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
