import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { api } from '../lib/api';

export default function ForgotPassword() {
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const submit = async (e) => {
    e.preventDefault();

    setError('');
    setLoading(true);

    try {
      await api.forgotPassword(email);

      // Keep email for the reset page
      sessionStorage.setItem('reset_email', email);

      navigate('/reset-password');
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-visual">
        <div className="eyebrow">
          RIGHT TO INFORMATION • 2005
        </div>

        <h1>
          Get back to your <em>RTI journey.</em>
        </h1>

        <p>
          Enter your registered email address and we'll send you
          a secure OTP to reset your AI RTI Assistant password.
        </p>

        <div className="trust-row">
          <span>✓ Secure password reset</span>
          <span>✓ OTP protected</span>
        </div>
      </div>

      <div className="auth-card">
        <div className="mobile-brand">
          AI RTI Assistant
        </div>

        <div className="otp-icon">
          🔐
        </div>

        <h2>Forgot password?</h2>

        <p className="muted">
          Enter your registered email to receive a password reset OTP.
        </p>

        {error && (
          <div className="alert error">
            {error}
          </div>
        )}

        <form onSubmit={submit}>
          <label>
            Email

            <input
              type="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="you@example.com"
            />
          </label>

          <button
            className="primary-btn"
            disabled={loading}
          >
            {loading ? 'Sending OTP…' : 'Send OTP'}
          </button>
        </form>

        <p className="switch">
          Remember your password?{' '}
          <Link to="/login">
            Back to login
          </Link>
        </p>
      </div>
    </div>
  );
}