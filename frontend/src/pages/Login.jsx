import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { api } from '../lib/api';
import { FiEye, FiEyeOff, FiShield, FiCheckCircle } from 'react-icons/fi';

export default function Login() {
  const [form, setForm] = useState({
    email: '',
    password: '',
  });

  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const nav = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const d = await api.login(form);

      localStorage.setItem('rti_token', d.token);

      nav('/home');
    } catch (err) {
      setError(err.message || 'Unable to sign in. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));

    if (error) {
      setError('');
    }
  };

  return (
    <div className="auth-page">

      {/* LEFT VISUAL SECTION */}
      <div className="auth-visual">

        <div className="visual-content">

          <div className="brand-mark">
            <div className="brand-icon">RTI</div>

            <span>AI RTI Assistant</span>
          </div>

          <div className="eyebrow">
            RIGHT TO INFORMATION • 2005
          </div>

          <h1>
            Turn a problem into a{' '}
            <em>proper RTI.</em>
          </h1>

          <p className="visual-description">
            Describe your issue in simple words. Let AI structure
            your RTI application, help you review it, and guide
            you toward the right Public Information Officer.
          </p>

          <div className="trust-row">

            <div className="trust-item">
              <FiCheckCircle />
              <span>Verified PIO directory</span>
            </div>

            <div className="trust-item">
              <FiShield />
              <span>Secure account</span>
            </div>

          </div>

          <div className="rti-note">
            <strong>Know your right.</strong>
            <span>
              Ask. Understand. Get informed.
            </span>
          </div>

        </div>
      </div>


      {/* LOGIN CARD */}
      <div className="auth-card-wrapper">

        <div className="auth-card">

          <div className="mobile-brand">
            <div className="brand-icon">RTI</div>
            <span>AI RTI Assistant</span>
          </div>

          <div className="login-header">

            <h2>Welcome back</h2>

            <p className="muted">
              Sign in to continue your RTI journey.
            </p>

          </div>


          {/* ERROR */}
          {error && (
            <div
              className="alert error"
              role="alert"
              aria-live="polite"
            >
              <span className="alert-icon">!</span>
              <span>{error}</span>
            </div>
          )}


          {/* FORM */}
          <form onSubmit={submit}>

            {/* EMAIL */}
            <label className="input-group">

              <span>Email address</span>

              <input
                type="email"
                name="email"
                required
                autoComplete="email"
                value={form.email}
                onChange={handleChange}
                placeholder="you@example.com"
                disabled={loading}
              />

            </label>


            {/* PASSWORD */}
            <label className="input-group">

              <div className="password-label">

                <span>Password</span>

                <Link to="/forgot-password">
                  Forgot password?
                </Link>

              </div>

              <div className="password-wrapper">

                <input
                  type={showPassword ? 'text' : 'password'}
                  name="password"
                  required
                  autoComplete="current-password"
                  value={form.password}
                  onChange={handleChange}
                  placeholder="Enter your password"
                  disabled={loading}
                />

                <button
                  type="button"
                  className="password-toggle"
                  onClick={() =>
                    setShowPassword((prev) => !prev)
                  }
                  aria-label={
                    showPassword
                      ? 'Hide password'
                      : 'Show password'
                  }
                  disabled={loading}
                >
                  {showPassword ? <FiEyeOff /> : <FiEye />}
                </button>

              </div>

            </label>


            {/* SUBMIT */}
            <button
              type="submit"
              className="primary-btn"
              disabled={loading}
            >

              {loading ? (
                <>
                  <span className="spinner"></span>
                  Signing in...
                </>
              ) : (
                'Sign in'
              )}

            </button>

          </form>


          {/* REGISTER */}
          <p className="switch">
            New here?{' '}
            <Link to="/register">
              Create an account
            </Link>
          </p>


          {/* SECURITY NOTE */}
          <div className="security-note">
            <FiShield />

            <span>
              Your account information is protected
              with secure authentication.
            </span>
          </div>

        </div>

      </div>

    </div>
  );
}