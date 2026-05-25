import { NavLink } from 'react-router-dom'

const Sidebar = ({ links }) => (
  <div className="sidebar p-3 col-md-3 col-lg-2">
    <nav className="nav flex-column">
      {links.map((link) => (
        <NavLink
          key={link.to}
          to={link.to}
          className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
        >
          {link.icon} {link.label}
        </NavLink>
      ))}
    </nav>
  </div>
)

export default Sidebar
