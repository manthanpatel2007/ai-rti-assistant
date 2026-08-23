import { Link } from 'react-router-dom';
import {
  ArrowRight,
  FileText,
  Building2,
  Send,
  ShieldCheck,
  Sparkles,
  ClipboardCheck,
} from 'lucide-react';

export default function Home() {
  return (
    <div className="dashboard home-page">

      <section className="hero-card">

        <div className="hero-content">

          <div className="eyebrow hero-eyebrow">
            <Sparkles size={15} />
            AI-POWERED RTI WORKSPACE
          </div>

          <h1>
            Get clarity from{' '}
            <em>public authorities.</em>
          </h1>

          <p>
            Build a legally structured RTI application from your issue,
            choose the correct government office, review the generated PDF,
            and send it securely.
          </p>

          <div className="hero-actions">

            <Link
              className="primary-btn inline-btn"
              to="/create"
            >
              <FileText size={18} />
              Start a new RTI
              <ArrowRight size={18} />
            </Link>

            <Link
              className="hero-secondary-link"
              to="/requests"
            >
              View my requests
              <ArrowRight size={15} />
            </Link>

          </div>

          <div className="hero-trust">

            <div>
              <ShieldCheck size={16} />
              Secure
            </div>

            <div>
              <ClipboardCheck size={16} />
              Review before sending
            </div>

          </div>

        </div>

        <div className="hero-art">

          <div className="hero-glow"></div>

          <div className="floating-badge badge-one">
            <Sparkles size={15} />
            AI drafted
          </div>

          <div className="floating-badge badge-two">
            <ShieldCheck size={15} />
            Ready to send
          </div>

          <div className="paper-card">

            <div className="paper-icon">
              <FileText size={24} />
            </div>

            <div className="paper-title">
              RIGHT TO INFORMATION
            </div>

            <div className="paper-subtitle">
              APPLICATION
            </div>

            <div className="paper-line"></div>

            <div className="paper-row">
              <span></span>
              <span></span>
            </div>

            <div className="paper-row short">
              <span></span>
              <span></span>
            </div>

            <div className="paper-status">
              <ShieldCheck size={14} />
              Structured & ready
            </div>

          </div>

        </div>

      </section>

      <section className="how-section">

        <div className="section-heading">

          <div>
            <div className="eyebrow">HOW IT WORKS</div>

            <h2>
              From issue to RTI in three simple steps.
            </h2>
          </div>

          <p>
            No legal drafting experience required. Just explain the issue
            and let the workspace guide you.
          </p>

        </div>

        <div className="feature-grid">

          <div className="feature-card">

            <div className="feature-icon">
              <FileText size={22} />
            </div>

            <span className="feature-number">01</span>

            <h3>Tell us the issue</h3>

            <p>
              Explain the public issue in your own words.
              No legal drafting required.
            </p>

            <div className="feature-arrow">
              <ArrowRight size={16} />
            </div>

          </div>

          <div className="feature-card">

            <div className="feature-icon">
              <Building2 size={22} />
            </div>

            <span className="feature-number">02</span>

            <h3>Find the right office</h3>

            <p>
              Select district, sub-district and department
              from the government directory.
            </p>

            <div className="feature-arrow">
              <ArrowRight size={16} />
            </div>

          </div>

          <div className="feature-card">

            <div className="feature-icon">
              <Send size={22} />
            </div>

            <span className="feature-number">03</span>

            <h3>Review & send</h3>

            <p>
              Preview your generated RTI, accept the declarations,
              then send it to the PIO.
            </p>

            <div className="feature-arrow">
              <ArrowRight size={16} />
            </div>

          </div>

        </div>

      </section>

    </div>
  );
}