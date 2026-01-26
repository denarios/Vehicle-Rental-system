import { useState, useEffect } from 'react';
import { FiPlus, FiMapPin, FiTruck, FiCalendar } from 'react-icons/fi';
import toast from 'react-hot-toast';
import { storeApi, vehicleApi, reservationApi } from '../services/api';
import Modal from '../components/Modal';

function StoresPage() {
    const [stores, setStores] = useState([]);
    const [storeStats, setStoreStats] = useState({});
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({
        state: '',
        district: '',
        pincode: '',
    });

    useEffect(() => {
        fetchStores();
    }, []);

    const fetchStores = async () => {
        try {
            const response = await storeApi.getAll();
            const storesData = response.data;
            setStores(storesData);

            // Fetch stats for each store
            const stats = {};
            for (const store of storesData) {
                try {
                    const [vehiclesRes, reservationsRes] = await Promise.all([
                        vehicleApi.getByStore(store.storeId),
                        reservationApi.getByStore(store.storeId),
                    ]);

                    let vehicleCount = 0;
                    Object.values(vehiclesRes.data || {}).forEach(vehicles => {
                        vehicleCount += vehicles.length;
                    });

                    stats[store.storeId] = {
                        vehicles: vehicleCount,
                        reservations: reservationsRes.data.length,
                    };
                } catch (err) {
                    stats[store.storeId] = { vehicles: 0, reservations: 0 };
                }
            }
            setStoreStats(stats);
        } catch (error) {
            toast.error('Failed to fetch stores');
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!formData.state.trim() || !formData.district.trim() || !formData.pincode.trim()) {
            toast.error('Please fill all fields');
            return;
        }

        try {
            const response = await storeApi.create(formData);
            setStores([...stores, response.data]);
            setStoreStats({ ...storeStats, [response.data.storeId]: { vehicles: 0, reservations: 0 } });
            setIsModalOpen(false);
            setFormData({ state: '', district: '', pincode: '' });
            toast.success('Store created successfully!');
        } catch (error) {
            toast.error(error.response?.data?.message || 'Failed to create store');
        }
    };

    const copyId = (id) => {
        navigator.clipboard.writeText(id);
        toast.success('Store ID copied to clipboard!');
    };

    return (
        <div className="animate-fade-in">
            <div className="page-header">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <div>
                        <h2>Stores</h2>
                        <p>Manage rental store locations</p>
                    </div>
                    <button className="btn btn-primary" onClick={() => setIsModalOpen(true)}>
                        <FiPlus /> Add Store
                    </button>
                </div>
            </div>

            {loading ? (
                <div className="empty-state">
                    <p>Loading stores...</p>
                </div>
            ) : stores.length > 0 ? (
                <div className="card-grid">
                    {stores.map((store) => (
                        <div
                            key={store.storeId}
                            className="card"
                            style={{ cursor: 'pointer' }}
                            onClick={() => copyId(store.storeId)}
                        >
                            <div style={{ display: 'flex', alignItems: 'flex-start', gap: '16px', marginBottom: '16px' }}>
                                <div
                                    style={{
                                        width: '52px',
                                        height: '52px',
                                        borderRadius: 'var(--radius-md)',
                                        background: 'var(--gradient-accent)',
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        fontSize: '1.4rem',
                                        color: 'white',
                                        flexShrink: 0
                                    }}
                                >
                                    <FiMapPin />
                                </div>
                                <div style={{ flex: 1 }}>
                                    <h4 style={{ marginBottom: '4px' }}>{store.location?.district || 'Unknown'}</h4>
                                    <p style={{ color: 'var(--gray-400)', fontSize: '0.9rem' }}>
                                        {store.location?.state || 'Unknown'} - {store.location?.pinCode || 'N/A'}
                                    </p>
                                </div>
                            </div>

                            <div style={{
                                display: 'flex',
                                gap: '16px',
                                paddingTop: '16px',
                                borderTop: '1px solid rgba(255,255,255,0.08)'
                            }}>
                                <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'var(--gray-400)' }}>
                                    <FiTruck />
                                    <span>{storeStats[store.storeId]?.vehicles || 0} Vehicles</span>
                                </div>
                                <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'var(--gray-400)' }}>
                                    <FiCalendar />
                                    <span>{storeStats[store.storeId]?.reservations || 0} Reservations</span>
                                </div>
                            </div>

                            <p style={{
                                fontSize: '0.75rem',
                                color: 'var(--gray-600)',
                                fontFamily: 'monospace',
                                marginTop: '12px',
                                whiteSpace: 'nowrap',
                                overflow: 'hidden',
                                textOverflow: 'ellipsis'
                            }}>
                                ID: {store.storeId}
                            </p>
                        </div>
                    ))}
                </div>
            ) : (
                <div className="empty-state">
                    <div className="empty-state-icon">🏪</div>
                    <h3>No stores yet</h3>
                    <p>Add your first store location to get started</p>
                    <button className="btn btn-primary" onClick={() => setIsModalOpen(true)}>
                        <FiPlus /> Add Store
                    </button>
                </div>
            )}

            {/* Add Store Modal */}
            <Modal
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                title="Add New Store"
                footer={
                    <>
                        <button className="btn btn-outline" onClick={() => setIsModalOpen(false)}>
                            Cancel
                        </button>
                        <button className="btn btn-primary" onClick={handleSubmit}>
                            Create Store
                        </button>
                    </>
                }
            >
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label className="form-label">State</label>
                        <input
                            type="text"
                            className="form-input"
                            placeholder="e.g., Karnataka"
                            value={formData.state}
                            onChange={(e) => setFormData({ ...formData, state: e.target.value })}
                        />
                    </div>
                    <div className="form-group">
                        <label className="form-label">District / City</label>
                        <input
                            type="text"
                            className="form-input"
                            placeholder="e.g., Bangalore"
                            value={formData.district}
                            onChange={(e) => setFormData({ ...formData, district: e.target.value })}
                        />
                    </div>
                    <div className="form-group">
                        <label className="form-label">Pincode</label>
                        <input
                            type="text"
                            className="form-input"
                            placeholder="e.g., 560001"
                            value={formData.pincode}
                            onChange={(e) => setFormData({ ...formData, pincode: e.target.value })}
                        />
                    </div>
                </form>
            </Modal>
        </div>
    );
}

export default StoresPage;
