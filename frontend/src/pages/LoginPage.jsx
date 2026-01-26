import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FiMail, FiLock, FiLogIn } from 'react-icons/fi';
import toast from 'react-hot-toast';
import { authApi } from '../services/api';
import { useAuth } from '../context/AuthContext';

function LoginPage() {
    const navigate = useNavigate();
    const { login } = useAuth();
    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            const response = await authApi.login(formData);
            const userData = response.data;

            // Save user data
            login(userData);

            // Show success message
            toast.success(`Welcome back, ${userData.name}!`);

            // Redirect based on role
            switch (userData.role) {
                case 'ADMIN':
                    navigate('/');
                    break;
                case 'STORE_MANAGER':
                    navigate('/vehicles');
                    break;
                case 'CUSTOMER':
                default:
                    navigate('/reservations');
                    break;
            }
        } catch (error) {
            toast.error(error.response?.data?.message || 'Invalid email or password');
        } finally {
            setLoading(false);
        }
    };

    const fillDemoCredentials = (role) => {
        switch (role) {
            case 'customer':
                setFormData({
                    email: 'pranjal.goyal@kfintech.com',
                    password: 'password123',
                });
                break;
            case 'admin':
                setFormData({
                    email: 'admin@rental.com',
                    password: 'admin123',
                });
                break;
            default:
                break;
        }
    };

    return (
        <div style={{
            minHeight: '100vh',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            background: 'linear-gradient(135deg, var(--primary-600) 0%, var(--accent-600) 100%)',
            padding: '20px'
        }}>
            <div className="card" style={{ maxWidth: '450px', width: '100%' }}>
                <div style={{ textAlign: 'center', marginBottom: '32px' }}>
                    <div style={{
                        fontSize: '3rem',
                        marginBottom: '16px'
                    }}>🚗</div>
                    <h1 style={{ marginBottom: '8px' }}>Vehicle Rental System</h1>
                    <p style={{ color: 'var(--gray-400)' }}>Sign in to continue</p>
                </div>

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label className="form-label">
                            <FiMail style={{ verticalAlign: 'middle', marginRight: '8px' }} />
                            Email
                        </label>
                        <input
                            type="email"
                            className="form-input"
                            placeholder="your.email@example.com"
                            value={formData.email}
                            onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label className="form-label">
                            <FiLock style={{ verticalAlign: 'middle', marginRight: '8px' }} />
                            Password
                        </label>
                        <input
                            type="password"
                            className="form-input"
                            placeholder="••••••••"
                            value={formData.password}
                            onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                            required
                        />
                    </div>

                    <button
                        type="submit"
                        className="btn btn-primary"
                        style={{ width: '100%', marginTop: '8px' }}
                        disabled={loading}
                    >
                        <FiLogIn />
                        {loading ? 'Signing in...' : 'Sign In'}
                    </button>
                </form>

                <div style={{
                    marginTop: '24px',
                    paddingTop: '24px',
                    borderTop: '1px solid var(--gray-700)'
                }}>
                    <p style={{
                        fontSize: '0.875rem',
                        color: 'var(--gray-400)',
                        marginBottom: '12px',
                        textAlign: 'center'
                    }}>
                        Demo Accounts:
                    </p>
                    <div style={{ display: 'flex', gap: '8px' }}>
                        <button
                            type="button"
                            className="btn btn-outline"
                            style={{ flex: 1, fontSize: '0.875rem' }}
                            onClick={() => fillDemoCredentials('customer')}
                        >
                            👤 Customer
                        </button>
                        <button
                            type="button"
                            className="btn btn-outline"
                            style={{ flex: 1, fontSize: '0.875rem' }}
                            onClick={() => fillDemoCredentials('admin')}
                        >
                            👑 Admin
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default LoginPage;
