import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import {
  Plus,
  ArrowRight,
  FileText,
  Clock3,
  CheckCircle2,
  XCircle,
  Inbox,
} from 'lucide-react';
import { api } from '../lib/api';

function getStatusIcon(status) {
  const value = String(status || '').toUpperCase();

  if (value === 'SENT') {
    return <CheckCircle2 size={15} />;
  }

  if (value === 'FAILED') {
    return <XCircle size={15} />;
  }

  if (value === 'PDF_GENERATED') {
    return <FileText size={15} />;
  }

  return <Clock3 size={15} />;
}

function getStatusLabel(status) {
  const value = String(status || '').toUpperCase();

  switch (value) {
    case 'PDF_GENERATED':
      return 'PDF ready';

    case 'GENERATED':
      return 'Generated';

    case 'DRAFT':
      return 'Draft';

    case 'SENT':
      return 'Sent';

    case 'FAILED':
      return 'Failed';

    default:
      return status || 'Unknown';
  }
}

export default function Requests() {

  const [data, setData] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {

    api.myRtis()
      .then(setData)
      .catch(e => setError(e.message))
      .finally(() => setLoading(false));

  }, []);

  return (
    <div className="workspace requests-page">

      {/* HEADER */}
      <div className="page-heading">

        <div>

          <div className="eyebrow">
            YOUR RECORD
          </div>

          <h1>My RTI requests</h1>

          <p>
            Everything you generate stays organized here.
          </p>

        </div>

        <Link
          className="primary-btn inline-btn"
          to="/create"
        >
          <Plus size={18} />
          New RTI
        </Link>

      </div>


      {error && (
        <div className="alert error">
          {error}
        </div>
      )}


      {/* LOADING */}
      {loading && (

        <div className="requests-loading">

          <div className="loading-spinner"></div>

          <p>Loading your RTI requests...</p>

        </div>

      )}


      {/* EMPTY */}
      {!loading && data.length === 0 && !error && (

        <div className="empty-state">

          <div className="empty-icon">
            <Inbox size={28} />
          </div>

          <h3>No RTIs yet</h3>

          <p>
            Your first RTI application will appear here
            after you create it.
          </p>

          <Link
            to="/create"
            className="primary-btn inline-btn"
          >
            <FileText size={17} />
            Create your first RTI
            <ArrowRight size={17} />
          </Link>

        </div>

      )}


      {/* REQUEST LIST */}
      {!loading && data.length > 0 && (

        <div className="request-list">

          {data.map(r => (

            <article
              className="request-card"
              key={r.id}
            >

              <div className="request-icon">
                <FileText size={22} />
              </div>


              <div className="request-main">

                <span className="request-id">
                  RTI #{r.id}
                </span>

                <h3>
                  {r.department || 'Government department'}
                </h3>

                <p>
                  {r.issueDescription || 'No issue description available.'}
                </p>

                {r.location && (
                  <span className="request-location">
                    {r.location}
                  </span>
                )}

              </div>


              <div className="request-meta">

                <span
                  className={`status-pill ${String(
                    r.status || ''
                  ).toLowerCase()}`}
                >
                  {getStatusIcon(r.status)}
                  {getStatusLabel(r.status)}
                </span>

                <Link
                  className="request-open"
                  to={`/review/${r.id}`}
                >
                  Open
                  <ArrowRight size={15} />
                </Link>

              </div>

            </article>

          ))}

        </div>

      )}

    </div>
  );
}