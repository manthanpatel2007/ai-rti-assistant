import { useState } from 'react';
import {
  useSearchParams,
  useNavigate,
  Link
} from 'react-router-dom';

import {
  MailCheck,
  ArrowLeft,
  RefreshCw,
  ShieldCheck
} from 'lucide-react';

import { api } from '../lib/api';

export default function VerifyOtp() {

  const [params] = useSearchParams();

  const email = params.get('email') || '';

  const [otp, setOtp] = useState('');
  const [msg, setMsg] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [resending, setResending] = useState(false);

  const nav = useNavigate();


  const verify = async e => {

    e.preventDefault();

    if (otp.length !== 6) {
      setError('Please enter the 6-digit OTP.');
      return;
    }

    setLoading(true);
    setError('');
    setMsg('');

    try {

      await api.verifyOtp({
        email,
        otp
      });

      setMsg(
        'Email verified successfully. Redirecting to sign in…'
      );

      setTimeout(() => {
        nav('/login');
      }, 900);

    } catch (err) {

      setError(err.message);

    } finally {

      setLoading(false);

    }
  };


  const resend = async () => {

    setResending(true);
    setError('');
    setMsg('');

    try {

      await api.resendOtp(email);

      setMsg(
        'A fresh OTP has been sent to your email.'
      );

    } catch (err) {

      setError(err.message);

    } finally {

      setResending(false);

    }
  };


  return (

    <div className="center-page verification-page">

      <div className="otp-card">

        <div className="otp-icon">
          <MailCheck size={27} />
        </div>


        <div className="eyebrow">
          EMAIL VERIFICATION
        </div>


        <h1>
          Check your inbox.
        </h1>


        <p className="otp-description">
          We sent a one-time password to
          <strong>{email}</strong>.
        </p>


        <div className="verification-note">

          <ShieldCheck size={16} />

          <span>
            Your email helps keep your RTI account secure.
          </span>

        </div>


        {error && (
          <div className="alert error">
            {error}
          </div>
        )}


        {msg && (
          <div className="alert success">
            {msg}
          </div>
        )}


        <form onSubmit={verify}>

          <label className="otp-label">
            6-digit OTP

            <input
              className="otp-input"
              inputMode="numeric"
              maxLength="6"
              autoComplete="one-time-code"
              required
              value={otp}
              onChange={e =>
                setOtp(
                  e.target.value.replace(/\D/g, '')
                )
              }
              placeholder="000000"
            />

          </label>


          <button
            className="primary-btn wide"
            disabled={loading}
          >
            {loading ? (
              <>
                <span className="button-spinner"></span>
                Verifying...
              </>
            ) : (
              <>
                Verify email
                <MailCheck size={17} />
              </>
            )}
          </button>

        </form>


        <button
          className="text-btn resend-btn"
          onClick={resend}
          disabled={resending}
        >

          <RefreshCw
            size={15}
            className={resending ? 'spin' : ''}
          />

          {resending
            ? 'Sending...'
            : 'Resend OTP'}

        </button>


        <Link
          className="back-link"
          to="/register"
        >
          <ArrowLeft size={15} />
          Use another email
        </Link>

      </div>

    </div>
  );
}