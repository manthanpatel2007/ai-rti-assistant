import { useEffect, useState } from 'react';
import {
  useLocation,
  useNavigate,
  useParams,
} from 'react-router-dom';

import { api } from '../lib/api';


export default function ReviewRti() {

  const { id } = useParams();

  const location = useLocation();

  const navigate = useNavigate();


  // =========================
  // STATE
  // =========================

  const [rti, setRti] = useState(location.state?.rti || null);

  const [loading, setLoading] = useState(!rti);

  const [sending, setSending] = useState(false);

  const [pdfLoading, setPdfLoading] = useState(false);

  const [error, setError] = useState('');

  const [pdfError, setPdfError] = useState('');


  // =========================
  // LOAD RTI
  // =========================

  useEffect(() => {

    if (rti) {
      setLoading(false);
      return;
    }

    api.myRtis()
      .then((items) => {

        const found = items.find(
          (item) =>
            String(item.id) === String(id)
        );

        setRti(found || null);
      })
      .catch((e) => {

        setError(e.message);
      })
      .finally(() => {

        setLoading(false);
      });

  }, [id, rti]);


  // =========================
  // GENERATE PDF
  // =========================

  const generate = async () => {

    try {

      setError('');

      setLoading(true);

      const data = await api.generatePdf(id);

      setRti(data);

    } catch (e) {

      setError(e.message);

    } finally {

      setLoading(false);
    }
  };


  // =========================
  // PREVIEW PDF
  // =========================

  const previewPdf = async () => {

    try {

      setPdfError('');

      setPdfLoading(true);


      /*
       * Open blank tab immediately.
       *
       * This prevents popup blockers from
       * blocking the new tab after async fetch.
       */

      const newTab = window.open('', '_blank');


      if (!newTab) {

        throw new Error(
          'Please allow popups to preview the PDF.'
        );
      }


      newTab.document.write(`
        <html>
          <head>
            <title>RTI PDF</title>
          </head>

          <body
            style="
              margin:0;
              display:flex;
              align-items:center;
              justify-content:center;
              height:100vh;
              font-family:Arial,sans-serif;
            "
          >
            Loading PDF...
          </body>
        </html>
      `);


      const blob = await api.getPdfBlob(
        id,
        'preview'
      );


      const pdfUrl =
        URL.createObjectURL(blob);


      newTab.location.href = pdfUrl;


      /*
       * Keep URL alive while the tab is using it.
       *
       * We intentionally do not revoke immediately.
       */

    } catch (e) {

      setPdfError(e.message);

    } finally {

      setPdfLoading(false);
    }
  };


  // =========================
  // DOWNLOAD PDF
  // =========================

  const downloadPdf = async () => {

    try {

      setPdfError('');

      setPdfLoading(true);

      await api.downloadPdf(id);

    } catch (e) {

      setPdfError(e.message);

    } finally {

      setPdfLoading(false);
    }
  };


  // =========================
  // SEND RTI
  // =========================

  const send = async () => {

    try {

      setError('');

      setSending(true);

      await api.sendRti(id);

      navigate('/sent', {
        state: {
          id,
        },
      });

    } catch (e) {

      setError(e.message);

    } finally {

      setSending(false);
    }
  };


  // =========================
  // LOADING
  // =========================

  if (loading) {

    return (
      <div className="center-page">

        <div className="loader-card">
          Preparing your application…
        </div>

      </div>
    );
  }


  // =========================
  // NOT FOUND
  // =========================

  if (!rti) {

    return (
      <div className="center-page">

        <div className="loader-card">
          RTI request not found.
        </div>

      </div>
    );
  }


  // =========================
  // UI
  // =========================

  return (

    <div className="workspace">

      {/* =========================
          PAGE HEADING
      ========================= */}

      <div className="page-heading">

        <div>

          <div className="eyebrow">
            STEP 2 OF 3
          </div>

          <h1>
            Review your RTI
          </h1>

          <p>
            Read the generated application before it
            is sent to the Public Information Officer.
          </p>

        </div>


        <span
          className={`status-pill ${
            String(rti.status || '').toLowerCase()
          }`}
        >
          {rti.status}
        </span>

      </div>


      {/* =========================
          GENERAL ERROR
      ========================= */}

      {error && (

        <div className="alert error">
          {error}
        </div>

      )}


      {/* =========================
          PDF ERROR
      ========================= */}

      {pdfError && (

        <div className="alert error">
          {pdfError}
        </div>

      )}


      <div className="review-grid">


        {/* =========================
            DOCUMENT
        ========================= */}

        <section className="document-panel">

          <div className="document-toolbar">

            <span>
              Generated application
            </span>


            {rti.pdfPath && (

              <button
                type="button"
                className="pdf-link-btn"
                onClick={downloadPdf}
                disabled={pdfLoading}
              >
                {pdfLoading
                  ? 'Preparing PDF…'
                  : 'Download PDF ↓'}
              </button>

            )}

          </div>


          <article className="document">

            <div className="doc-top">
              RIGHT TO INFORMATION ACT, 2005
            </div>


            <h2>
              Application for information
            </h2>


            <p>
              <b>Subject:</b>{' '}
              {rti.department}
            </p>


            <p>
              <b>Location:</b>{' '}
              {rti.location || '—'}
            </p>


            <div className="doc-rule"></div>


            <h3>
              Application details
            </h3>


            <p className="prewrap">
              {rti.generatedContent}
            </p>


            <div className="doc-sign">

              Applicant:{' '}

              {rti.user?.email ||
                'Registered applicant'}

            </div>

          </article>

        </section>


        {/* =========================
            ACTION PANEL
        ========================= */}

        <aside className="action-panel">

          <div className="eyebrow">
            READY WHEN YOU ARE
          </div>


          <h2>
            One final check.
          </h2>


          <p>
            Your application will be sent to the
            selected PIO and a copy will be delivered
            to your registered email.
          </p>


          {/* =========================
              GENERATE
          ========================= */}

          {!rti.pdfPath && (

            <button
              className="secondary-btn wide"
              onClick={generate}
              disabled={loading}
            >
              {loading
                ? 'Generating…'
                : 'Generate & preview PDF'}
            </button>

          )}


          {/* =========================
              PDF ACTIONS
          ========================= */}

          {rti.pdfPath && (

            <>

              <button
                type="button"
                className="secondary-btn wide"
                onClick={previewPdf}
                disabled={pdfLoading}
              >
                {pdfLoading
                  ? 'Opening PDF…'
                  : 'Preview PDF'}
              </button>


              <button
                type="button"
                className="primary-btn wide"
                onClick={send}
                disabled={sending}
              >
                {sending
                  ? 'Sending securely…'
                  : 'Send RTI to PIO →'}
              </button>

            </>

          )}


          <div className="secure-note">

            🔒 Your request is sent using your
            verified account and the registered
            PIO directory.

          </div>

        </aside>

      </div>

    </div>
  );
}