import { Link, useLocation, useNavigate } from 'react-router-dom';
import { FilePlus2, LayoutDashboard, ListChecks, LogOut, UserPlus } from 'lucide-react';

export default function Layout({ children }) {
  const location = useLocation();
  const navigate = useNavigate();
  const loggedIn = !!localStorage.getItem('rti_token');
  const logout = () => { localStorage.removeItem('rti_token'); navigate('/login'); };
  const nav = loggedIn ? [
    { to: '/home', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/create', label: 'Create RTI', icon: FilePlus2 },
    { to: '/requests', label: 'My requests', icon: ListChecks },
  ] : [];

  return <div className="app-shell">
    <header className="topbar">
      <Link className="brand" to={loggedIn ? '/home' : '/login'}><span className="brand-mark">RTI</span><span>AI RTI Assistant</span></Link>
      <nav>{nav.map(({ to, label, icon: Icon }) => <Link className={location.pathname===to?'active':''} key={to} to={to}><Icon size={15} />{label}</Link>)}</nav>
      <div className="top-actions">{loggedIn ? <button className="ghost-btn" onClick={logout}><LogOut size={15} />Sign out</button> : <Link className="outline-btn" to="/register"><UserPlus size={15} />Create account</Link>}</div>
    </header>
    <main>{children}</main>
    <footer><span>AI RTI Assistant</span><span>Secure • Transparent • Citizen-first</span></footer>
  </div>;
}
