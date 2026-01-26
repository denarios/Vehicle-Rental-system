import { useState, useEffect } from 'react';
import { FiSearch, FiMapPin, FiFilter } from 'react-icons/fi';
import { vehicleApi } from '../services/api';
import { useAuth } from '../context/AuthContext';
import toast from 'react-hot-toast';

const getVehicleEmoji = (type) => {
    const emojis = { CAR: '🚗', BIKE: '🏍️', TRUCK: '🚚' };
    return emojis[type] || '🚙';
};

const cities = [
    { name: 'All Cities', value: '' },
    { name: 'Hyderabad', value: 'Hyderabad', state: 'Telangana' },
    { name: 'Bangalore', value: 'Bangalore', state: 'Karnataka' },
    { name: 'Mumbai', value: 'Mumbai', state: 'Maharashtra' },
];

function BrowseVehiclesPage() {
    const { user } = useAuth();
    const [vehicles, setVehicles] = useState([]);
    const [filteredVehicles, setFilteredVehicles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filters, setFilters] = useState({
        city: '',
        state: '',
        type: ''
    });

    useEffect(() => {
        loadVehicles();
    }, []);

    useEffect(() => {
        applyFilters();
    }, [filters, vehicles]);

    const loadVehicles = async () => {
        try {
            const response = await vehicleApi.getAll();
            setVehicles(response.data);
            setFilteredVehicles(response.data);
        } catch (error) {
            toast.error('Failed to load vehicles');
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = async () => {
        try {
            setLoading(true);
            // Call location search API
            const params = new URLSearchParams();
            if (filters.city) params.append('city', filters.city);
            if (filters.state) params.append('state', filters.state);

            const response = await vehicleApi.searchByLocation(params.toString());
            let results = response.data;

            // Apply vehicle type filter if selected
            if (filters.type) {
                results = results.filter(v => v.vehicleType === filters.type);
            }

            setVehicles(response.data);
            setFilteredVehicles(results);
            toast.success(`Found ${results.length} vehicles`);
        } catch (error) {
            toast.error('Failed to search vehicles');
        } finally {
            setLoading(false);
        }
    };

    const applyFilters = () => {
        let filtered = [...vehicles];

        if (filters.type) {
            filtered = filtered.filter(v => v.vehicleType === filters.type);
        }

        setFilteredVehicles(filtered);
    };

    const handleCityChange = (cityName) => {
        const city = cities.find(c => c.value === cityName);
        setFilters({
            ...filters,
            city: city?.value || '',
            state: city?.state || ''
        });
    };

    const handleBookVehicle = (vehicleId) => {
        toast.success('Book vehicle feature coming soon!');
    };

    if (loading) {
        return (
            <div className="animate-fade-in">
                <div className="page-header">
                    <h2>Browse Vehicles</h2>
                    <p>Loading vehicles...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="animate-fade-in">
            {/* Hero Section */}
            <div style={{
                background: 'linear-gradient(135deg, rgba(99, 102, 241, 0.1) 0%, rgba(6, 182, 212, 0.1) 100%)',
                borderRadius: 'var(--radius-xl)',
                padding: '48px',
                marginBottom: '32px',
                textAlign: 'center'
            }}>
                <h1 style={{ marginBottom: '16px', fontSize: '2.5rem' }}>
                    🚗 Find Your Perfect Ride
                </h1>
                <p style={{ fontSize: '1.1rem', color: 'var(--gray-400)', marginBottom: '32px' }}>
                    Search and book vehicles near you in seconds
                </p>

                {/* Search Filters */}
                <div style={{
                    display: 'grid',
                    gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
                    gap: '16px',
                    maxWidth: '900px',
                    margin: '0 auto'
                }}>
                    <div>
                        <select
                            className="form-select"
                            value={filters.city}
                            onChange={(e) => handleCityChange(e.target.value)}
                        >
                            {cities.map(city => (
                                <option key={city.value} value={city.value}>
                                    📍 {city.name}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <select
                            className="form-select"
                            value={filters.type}
                            onChange={(e) => setFilters({ ...filters, type: e.target.value })}
                        >
                            <option value="">All Types</option>
                            <option value="CAR">🚗 Cars</option>
                            <option value="BIKE">🏍️ Bikes</option>
                            <option value="TRUCK">🚚 Trucks</option>
                        </select>
                    </div>

                    <button className="btn btn-accent" onClick={handleSearch}>
                        <FiSearch />
                        Search
                    </button>
                </div>
            </div>

            {/* Results */}
            <div className="page-header">
                <h3>Available Vehicles ({filteredVehicles.length})</h3>
                <p>Find the perfect vehicle for your needs</p>
            </div>

            {filteredVehicles.length === 0 ? (
                <div className="empty-state">
                    <div className="empty-state-icon">🚗</div>
                    <h3>No vehicles found</h3>
                    <p>Try adjusting your search filters</p>
                </div>
            ) : (
                <div className="card-grid">
                    {filteredVehicles.map(vehicle => (
                        <div key={vehicle.id} className="vehicle-card">
                            <div className="vehicle-card-image">
                                {getVehicleEmoji(vehicle.vehicleType)}
                            </div>
                            <div className="vehicle-card-body">
                                <div className="vehicle-card-title">
                                    <span>{vehicle.vehicleType}</span>
                                    <span className={`badge badge-${vehicle.status === 'ACTIVE' ? 'success' : 'gray'}`}>
                                        {vehicle.status}
                                    </span>
                                </div>
                                <div className="vehicle-card-id">
                                    ID: {vehicle.id.substring(0, 8)}...
                                </div>
                                <div style={{ marginTop: '16px' }}>
                                    <button
                                        className="btn btn-primary"
                                        style={{ width: '100%' }}
                                        onClick={() => handleBookVehicle(vehicle.id)}
                                        disabled={vehicle.status !== 'ACTIVE'}
                                    >
                                        Book Now
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default BrowseVehiclesPage;
