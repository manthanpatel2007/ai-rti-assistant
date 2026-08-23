import { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { api } from '../lib/api';

export default function Sent() {

  const id = useLocation().state?.id;

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const viewPdf = async () => {

    try {

      setError('');
      setLoading(true);

      const newTab = window.open('', '_blank');

      if (!newTab) {
        throw new Error('Please allow popups to view the PDF.');
      }

      const blob = await api.getPdfBlob(id, 'preview');

      const pdfUrl = URL.createObjectURL(blob);

      newTab.location.href = pdfUrl;

    } catch (e) {

      setError(e.message);

    } finally {

      setLoading(false);
    }
  };

  return (
    <div className="center-page">
      <div className="success-card">
        <div className="success-mark">✓</div>
        <div className="eyebrow">SUBMISSION COMPLETE</div>
        <h1>Your RTI is on its way.</h1>
        <p>
          The application was sent to the selected Public Information
          Officer. A copy is also delivered to your registered email
          for your records.
        </p>

        {error && <div className="alert error">{error}</div>}

        <div className="success-actions">
          {id && (
            <button
              type="button"
              className="secondary-btn"
              onClick={viewPdf}
              disabled={loading}
            >
              {loading ? 'Opening PDF…' : 'View PDF'}
            </button>
          )}

          <Link className="primary-btn" to="/requests">
            View my requests
          </Link>
        </div>
      </div>
    </div>
  );
}