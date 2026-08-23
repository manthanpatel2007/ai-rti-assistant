import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../lib/api';

export default function Register() {

  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: '',
    email: '',
    password: '',
    phone: '',
    address: '',
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;

    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    setError('');

    // =========================
    // VALIDATION
    // =========================

    if (!form.name.trim()) {
      setError('Please enter your full name.');
      return;
    }

    if (!form.email.trim()) {
      setError('Please enter your email address.');
      return;
    }

    if (!form.password) {
      setError('Please enter your password.');
      return;
    }

    if (!form.phone.trim()) {
      setError('Please enter your phone number.');
      return;
    }

    if (!/^[0-9]{10}$/.test(form.phone.trim())) {
      setError('Please enter a valid 10-digit phone number.');
      return;
    }

    if (!form.address.trim()) {
      setError('Please enter your full postal address.');
      return;
    }

    try {
      setLoading(true);

      // =========================
      // REGISTER
      // =========================

      await api.register({
        name: form.name.trim(),
        email: form.email.trim(),
        password: form.password,
        phone: form.phone.trim(),
        address: form.address.trim(),
      });

      // =========================
      // OTP PAGE
      // =========================

      navigate(
        `/verify-otp?email=${encodeURIComponent(
          form.email.trim()
        )}`
      );

    } catch (err) {
  setError(err.message);

    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">

      <div className="auth-card">

        <h1>Create your account</h1>

        <p>
          Register to create and submit RTI applications.
        </p>

        {error && (
          <div className="alert error">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>

          {/* FULL NAME */}

          <div className="form-group">

            <label htmlFor="name">
              Full Name
            </label>

            <input
              id="name"
              name="name"
              type="text"
              value={form.name}
              onChange={handleChange}
              placeholder="Enter your full name"
              autoComplete="name"
              required
            />

          </div>


          {/* EMAIL */}

          <div className="form-group">

            <label htmlFor="email">
              Email Address
            </label>

            <input
              id="email"
              name="email"
              type="email"
              value={form.email}
              onChange={handleChange}
              placeholder="Enter your email address"
              autoComplete="email"
              required
            />

          </div>


          {/* PHONE */}

          <div className="form-group">

            <label htmlFor="phone">
              Phone Number
            </label>

            <input
              id="phone"
              name="phone"
              type="tel"
              value={form.phone}
              onChange={handleChange}
              placeholder="Enter 10-digit phone number"
              autoComplete="tel"
              maxLength={10}
              inputMode="numeric"
            
              required
            />

          </div>


          {/* POSTAL ADDRESS */}

          <div className="form-group">

            <label htmlFor="address">
              Postal Address
            </label>

            <textarea
              id="address"
              name="address"
              value={form.address}
              onChange={handleChange}
              placeholder="Enter your full postal address"
              autoComplete="street-address"
              rows={4}
              required
            />

          </div>


          {/* PASSWORD */}

          <div className="form-group">

            <label htmlFor="password">
              Password
            </label>

            <input
              id="password"
              name="password"
              type="password"
              value={form.password}
              onChange={handleChange}
              placeholder="Create a password"
              autoComplete="new-password"
              required
            />

          </div>


          {/* SUBMIT */}

          <button
            type="submit"
            className="primary-btn wide"
            disabled={loading}
          >
            {loading
              ? 'Creating account…'
              : 'Create account'}
          </button>

        </form>

      </div>

    </div>
  );
  
}