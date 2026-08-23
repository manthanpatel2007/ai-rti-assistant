import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { api } from '../lib/api';
import {
  FiEye,
  FiEyeOff,
  FiLock,
  FiMail,
  FiCheckCircle,
  FiArrowLeft,
} from 'react-icons/fi';

export default function ResetPassword() {
  const [email, setEmail] = useState('');
  const [otp, setOtp] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();


  /* ============================= */
  /* LOAD RESET EMAIL */
  /* ============================= */

  useEffect(() => {
    const savedEmail = sessionStorage.getItem('reset_email');

    if (!savedEmail) {
      navigate('/forgot-password', {
        replace: true,
      });

      return;
    }

    setEmail(savedEmail);
  }, [navigate]);


  /* ============================= */
  /* FORM SUBMIT */
  /* ============================= */

  const submit = async (e) => {
    e.preventDefault();

    setError('');
    setSuccess('');

    const cleanOtp = otp.trim();

    if (cleanOtp.length !== 6) {
      setError('Please enter the complete 6-digit OTP.');
      return;
    }

    if (
      newPassword.length < 6 ||
      newPassword.length > 10
    ) {
      setError(
        'Password must be between 6 and 10 characters.'
      );
      return;
    }

    if (newPassword !== confirmPassword) {
      setError(
        'New password and confirm password do not match.'
      );
      return;
    }

    setLoading(true);

    try {
      await api.resetPassword({
        email,
        otp: cleanOtp,
        newPassword,
      });

      sessionStorage.removeItem('reset_email');

      setSuccess(
        'Your password has been reset successfully.'
      );

      setTimeout(() => {
        navigate('/login', {
          replace: true,
        });
      }, 1500);

    } catch (err) {
      setError(
        err.message ||
        'Unable to reset your password. Please try again.'
      );
    } finally {
      setLoading(false);
    }
  };


  /* ============================= */
  /* OTP CHANGE */
  /* ============================= */

  const handleOtpChange = (e) => {
    const value = e.target.value
      .replace(/\D/g, '')
      .slice(0, 6);

    setOtp(value);

    if (error) {
      setError('');
    }
  };


  /* ============================= */
  /* PASSWORD CHANGE */
  /* ============================= */

  const handlePasswordChange = (e) => {
    setNewPassword(e.target.value);

    if (error) {
      setError('');
    }
  };


  const handleConfirmPasswordChange = (e) => {
    setConfirmPassword(e.target.value);

    if (error) {
      setError('');
    }
  };


  const passwordsMatch =
    confirmPassword.length > 0 &&
    newPassword === confirmPassword;


  return (
    <div className="center-page">

      <div className="otp-card reset-card">

        {/* ============================= */}
        {/* ICON */}
        {/* ============================= */}

        <div className="otp-icon">
          <FiLock />
        </div>


        {/* ============================= */}
        {/* HEADER */}
        {/* ============================= */}

        <div className="reset-header">

          <div className="reset-badge">
            <FiLock />
            Secure password reset
          </div>

          <h1>
            Reset your password
          </h1>

          <p>
            Enter the verification code from your email
            and choose a new password for your account.
          </p>

        </div>


        {/* ============================= */}
        {/* EMAIL INFO */}
        {/* ============================= */}

        <div className="reset-email-card">

          <div className="reset-email-icon">
            <FiMail />
          </div>

          <div>

            <span>
              Resetting password for
            </span>

            <strong>
              {email}
            </strong>

          </div>

        </div>


        {/* ============================= */}
        {/* ERROR */}
        {/* ============================= */}

        {error && (
          <div
            className="alert error"
            role="alert"
            aria-live="polite"
          >
            <span className="alert-icon">
              !
            </span>

            <span>
              {error}
            </span>
          </div>
        )}


        {/* ============================= */}
        {/* SUCCESS */}
        {/* ============================= */}

        {success && (
          <div
            className="alert success"
            role="status"
            aria-live="polite"
          >
            <FiCheckCircle />

            <span>
              {success}
              <small>
                Redirecting you to login...
              </small>
            </span>
          </div>
        )}


        {/* ============================= */}
        {/* FORM */}
        {/* ============================= */}

        <form onSubmit={submit}>

          {/* OTP */}

          <label className="input-group">

            <div className="reset-label-row">

              <span>
                Verification code
              </span>

              <small>
                6 digits
              </small>

            </div>

            <input
              className="otp-input"
              type="text"
              inputMode="numeric"
              autoComplete="one-time-code"
              maxLength={6}
              required
              value={otp}
              onChange={handleOtpChange}
              placeholder="000000"
              disabled={loading || !!success}
              aria-label="6-digit verification code"
            />

            <small className="input-hint">
              Enter the 6-digit OTP sent to your email.
            </small>

          </label>


          {/* NEW PASSWORD */}

          <label className="input-group">

            <span>
              New password
            </span>

            <div className="password-wrapper">

              <input
                type={
                  showPassword
                    ? 'text'
                    : 'password'
                }
                required
                minLength={6}
                maxLength={10}
                autoComplete="new-password"
                value={newPassword}
                onChange={handlePasswordChange}
                placeholder="Create a new password"
                disabled={loading || !!success}
              />

              <button
                type="button"
                className="password-toggle"
                onClick={() =>
                  setShowPassword(
                    (prev) => !prev
                  )
                }
                disabled={loading || !!success}
                aria-label={
                  showPassword
                    ? 'Hide password'
                    : 'Show password'
                }
              >
                {showPassword ? (
                  <FiEyeOff />
                ) : (
                  <FiEye />
                )}
              </button>

            </div>

            <small className="input-hint">
              Use 6–10 characters.
            </small>

          </label>


          {/* CONFIRM PASSWORD */}

          <label className="input-group">

            <span>
              Confirm new password
            </span>

            <div className="password-wrapper">

              <input
                type={
                  showConfirmPassword
                    ? 'text'
                    : 'password'
                }
                required
                minLength={6}
                maxLength={10}
                autoComplete="new-password"
                value={confirmPassword}
                onChange={
                  handleConfirmPasswordChange
                }
                placeholder="Enter your password again"
                disabled={loading || !!success}
              />

              <button
                type="button"
                className="password-toggle"
                onClick={() =>
                  setShowConfirmPassword(
                    (prev) => !prev
                  )
                }
                disabled={loading || !!success}
                aria-label={
                  showConfirmPassword
                    ? 'Hide password'
                    : 'Show password'
                }
              >
                {showConfirmPassword ? (
                  <FiEyeOff />
                ) : (
                  <FiEye />
                )}
              </button>

            </div>

            {/* PASSWORD MATCH */}

            {passwordsMatch && (
              <small className="password-match">
                <FiCheckCircle />
                Passwords match
              </small>
            )}

          </label>


          {/* SUBMIT */}

          <button
            type="submit"
            className="primary-btn wide"
            disabled={loading || !!success}
          >

            {loading ? (
              <>
                <span className="spinner"></span>
                Resetting password...
              </>
            ) : (
              <>
                <FiLock />
                Reset password
              </>
            )}

          </button>

        </form>


        {/* ============================= */}
        {/* BACK TO LOGIN */}
        {/* ============================= */}

        <Link
          to="/login"
          className="reset-back-link"
        >
          <FiArrowLeft />
          Back to login
        </Link>


        {/* ============================= */}
        {/* SECURITY */}
        {/* ============================= */}

        <div className="security-note">

          <FiLock />

          <span>
            Your password is securely updated and
            your reset session is cleared after
            successful verification.
          </span>

        </div>

      </div>

    </div>
  );
}