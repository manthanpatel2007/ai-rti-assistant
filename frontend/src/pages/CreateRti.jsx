import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../lib/api';

export default function CreateRti() {

  const nav = useNavigate();

  const [districts, setDistricts] = useState([]);
  const [subs, setSubs] = useState([]);
  const [deps, setDeps] = useState([]);
  const [problems, setProblems] = useState([]);

  const [form, setForm] = useState({
    issueDescription: '',
    location: '',
    department: '',
    informationConfirmed: false,
    submissionConsent: false,
  });

  const [ids, setIds] = useState({ district: '', sub: '', department: '' });

  // 👇 naye state: naam store karne ke liye
  const [names, setNames] = useState({ district: '', sub: '' });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    api.districts().then(setDistricts).catch((e) => setError(e.message));
  }, []);

  const chooseDistrict = async (id) => {

    setIds({ district: id, sub: '', department: '' });
    setSubs([]);
    setDeps([]);
    setProblems([]);

    // 👇 selected district ka naam bhi save karo
    const d = districts.find((x) => String(x.id) === String(id));
    setNames((v) => ({ ...v, district: d?.name || '', sub: '' }));

    if (id) setSubs(await api.subDistricts(id));
  };

  const chooseSub = async (id) => {

    setIds((v) => ({ ...v, sub: id, department: '' }));
    setDeps([]);
    setProblems([]);

    // 👇 selected sub-district ka naam bhi save karo
    const s = subs.find((x) => String(x.id) === String(id));
    setNames((v) => ({ ...v, sub: s?.name || '' }));

    if (id) setDeps(await api.departments(id));
  };

  const chooseDep = async (id) => {

    setIds((v) => ({ ...v, department: id }));

    const dep = deps.find((x) => String(x.id) === String(id));

    setForm((v) => ({ ...v, department: dep?.name || '' }));

    if (id) setProblems(await api.problems(id));
  };

  const submit = async (e) => {

    e.preventDefault();

    if (!form.informationConfirmed || !form.submissionConsent) {
      return setError('Please accept both declarations before generating the RTI.');
    }

    setError('');
    setLoading(true);

    try {

      // 👇 district + sub-district + landmark ko combine karke final location banao
      const combinedLocation = [
        form.location,   // landmark (optional, user typed)
        names.sub,        // sub-district name
        names.district,   // district name
      ]
        .filter(Boolean)
        .join(', ');

      const payload = {
        ...form,
        location: combinedLocation,
      };

      const r = await api.createRti(payload);

      nav(`/review/${r.id}`, { state: { rti: r } });

    } catch (err) {

      setError(err.message);

    } finally {

      setLoading(false);
    }
  };

  return (
    <div className="workspace">
      <div className="page-heading">
        <div>
          <div className="eyebrow">STEP 1 OF 3</div>
          <h1>Create your RTI</h1>
          <p>Give us the facts. AI will turn them into a structured application.</p>
        </div>
        <div className="progress">
          <span className="done"></span>
          <span className="current"></span>
          <span></span>
        </div>
      </div>

      {error && <div className="alert error">{error}</div>}

      <form className="form-layout" onSubmit={submit}>

        <section className="panel">
          <div className="panel-head">
            <span className="panel-number">01</span>
            <div>
              <h2>Where is the issue?</h2>
              <p>Choose the government jurisdiction connected to your issue.</p>
            </div>
          </div>

          <div className="field-grid">

            <label>
              District
              <select
                value={ids.district}
                onChange={(e) => chooseDistrict(e.target.value)}
                required
              >
                <option value="">Select district</option>
                {districts.map((x) => (
                  <option key={x.id} value={x.id}>{x.name}</option>
                ))}
              </select>
            </label>

            <label>
              Sub-district
              <select
                value={ids.sub}
                onChange={(e) => chooseSub(e.target.value)}
                required
                disabled={!ids.district}
              >
                <option value="">Select sub-district</option>
                {subs.map((x) => (
                  <option key={x.id} value={x.id}>{x.name}</option>
                ))}
              </select>
            </label>

            <label className="full">
              Department
              <select
                value={ids.department}
                onChange={(e) => chooseDep(e.target.value)}
                required
                disabled={!ids.sub}
              >
                <option value="">Select department</option>
                {deps.map((x) => (
                  <option key={x.id} value={x.id}>{x.name}</option>
                ))}
              </select>
            </label>

            {problems.length > 0 && (
              <div className="suggestion full">
                <b>Common issue categories</b>
                <div>
                  {problems.map((p) => (
                    <button
                      type="button"
                      key={p.id}
                      onClick={() =>
                        setForm((v) => ({
                          ...v,
                          issueDescription: v.issueDescription || p.name,
                        }))
                      }
                    >
                      {p.name}
                    </button>
                  ))}
                </div>
              </div>
            )}

          </div>
        </section>

        <section className="panel">
          <div className="panel-head">
            <span className="panel-number">02</span>
            <div>
              <h2>Describe the issue</h2>
              <p>Be factual. Include dates, places, records or actions you want information about.</p>
            </div>
          </div>

          <label>
            Issue description
            <textarea
              required
              minLength="10"
              rows="8"
              value={form.issueDescription}
              onChange={(e) => setForm({ ...form, issueDescription: e.target.value })}
              placeholder="Example: I want information about the status, sanctioned amount, expenditure and completion date of…"
            />
          </label>

          <label>
            Location <span className="optional">Optional</span>
            <input
              value={form.location}
              onChange={(e) => setForm({ ...form, location: e.target.value })}
              placeholder="Village, ward, landmark or office location"
            />
          </label>
        </section>

        <section className="panel consent-panel">
          <div className="panel-head">
            <span className="panel-number">03</span>
            <div>
              <h2>Your declarations</h2>
              <p>Both must be accepted before the application can be generated and submitted.</p>
            </div>
          </div>

          <label className="check">
            <input
              type="checkbox"
              checked={form.informationConfirmed}
              onChange={(e) => setForm({ ...form, informationConfirmed: e.target.checked })}
            />
            <span><b>I confirm that the information provided by me is true and accurate to the best of my knowledge.</b></span>
          </label>

          <label className="check">
            <input
              type="checkbox"
              checked={form.submissionConsent}
              onChange={(e) => setForm({ ...form, submissionConsent: e.target.checked })}
            />
            <span><b>I have reviewed the RTI application generated from my information and authorize the application to be sent to the selected Public Information Officer.</b></span>
          </label>

          <button className="primary-btn wide" disabled={loading}>
            {loading ? 'Generating application…' : 'Generate RTI application →'}
          </button>
        </section>

      </form>
    </div>
  );
}