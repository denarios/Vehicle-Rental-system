import { useState, useEffect } from 'react';
import { FiPlus, FiUser, FiCheck, FiX } from 'react-icons/fi';
import toast from 'react-hot-toast';
import { userApi } from '../services/api';
import Modal from '../components/Modal';

function UsersPage() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({
        name: '',
        drivingLicence: true,
    });

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const response = await userApi.getAll();
            setUsers(response.data);
        } catch (error) {
            toast.error('Failed to fetch users');
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!formData.name.trim()) {
            toast.error('Please enter a name');
            return;
        }

        try {
            const response = await userApi.create(formData);
            setUsers([...users, response.data]);
            setIsModalOpen(false);
            setFormData({ name: '', drivingLicence: true });
            toast.success('User created successfully!');
        } catch (error) {
            toast.error(error.response?.data?.message || 'Failed to create user');
        }
    };

    const copyId = (id) => {
        navigator.clipboard.writeText(id);
        toast.success('ID copied to clipboard!');
    };

    return (
        <div className="animate-fade-in">
            <div className="page-header">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <div>
                        <h2>Users</h2>
                        <p>Manage registered users in the system</p>
                    </div>
                    <button className="btn btn-primary" onClick={() => setIsModalOpen(true)}>
                        <FiPlus /> Add User
                    </button>
                </div>
            </div>

            {loading ? (
                <div className="empty-state">
                    <p>Loading users...</p>
                </div>
            ) : users.length > 0 ? (
                <div className="card-grid">
                    {users.map((user) => (
                        <div key={user.id} className="card" style={{ cursor: 'pointer' }} onClick={() => copyId(user.id)}>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                                <div
                                    style={{
                                        width: '56px',
                                        height: '56px',
                                        borderRadius: '50%',
                                        background: 'var(--gradient-primary)',
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        fontSize: '1.5rem',
                                        color: 'white'
                                    }}
                                >
                                    <FiUser />
                                </div>
                                <div style={{ flex: 1 }}>
                                    <h4 style={{ marginBottom: '4px' }}>{user.name}</h4>
                                    <p style={{
                                        fontSize: '0.8rem',
                                        color: 'var(--gray-500)',
                                        fontFamily: 'monospace',
                                        whiteSpace: 'nowrap',
                                        overflow: 'hidden',
                                        textOverflow: 'ellipsis',
                                        maxWidth: '180px'
                                    }}>
                                        {user.id}
                                    </p>
                                </div>
                                <span className={`badge ${user.drivingLicence ? 'badge-success' : 'badge-danger'}`}>
                                    {user.drivingLicence ? <FiCheck /> : <FiX />}
                                    {user.drivingLicence ? 'Licensed' : 'No License'}
                                </span>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <div className="empty-state">
                    <div className="empty-state-icon">👤</div>
                    <h3>No users yet</h3>
                    <p>Add your first user to get started</p>
                    <button className="btn btn-primary" onClick={() => setIsModalOpen(true)}>
                        <FiPlus /> Add User
                    </button>
                </div>
            )}

            {/* Add User Modal */}
            <Modal
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                title="Add New User"
                footer={
                    <>
                        <button className="btn btn-outline" onClick={() => setIsModalOpen(false)}>
                            Cancel
                        </button>
                        <button className="btn btn-primary" onClick={handleSubmit}>
                            Create User
                        </button>
                    </>
                }
            >
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label className="form-label">Full Name</label>
                        <input
                            type="text"
                            className="form-input"
                            placeholder="Enter user's name"
                            value={formData.name}
                            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                        />
                    </div>
                    <div className="form-group">
                        <label className="form-checkbox">
                            <input
                                type="checkbox"
                                checked={formData.drivingLicence}
                                onChange={(e) => setFormData({ ...formData, drivingLicence: e.target.checked })}
                            />
                            <span>Has Valid Driving License</span>
                        </label>
                    </div>
                </form>
            </Modal>
        </div>
    );
}

export default UsersPage;
