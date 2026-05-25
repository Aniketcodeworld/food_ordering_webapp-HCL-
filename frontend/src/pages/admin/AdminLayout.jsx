import Sidebar from '../../components/Sidebar'

const adminLinks = [
  { to: '/admin', label: 'Dashboard', icon: '📊' },
  { to: '/admin/restaurants', label: 'Restaurants', icon: '🏪' },
  { to: '/admin/menu', label: 'Menu Items', icon: '🍽️' },
  { to: '/admin/orders', label: 'Orders', icon: '📦' },
  { to: '/admin/users', label: 'Users', icon: '👥' },
]

const AdminLayout = ({ children, title }) => (
  <div className="d-flex">
    <Sidebar links={adminLinks} />
    <div className="flex-grow-1 p-4">
      <h2 className="mb-4">{title}</h2>
      {children}
    </div>
  </div>
)

export default AdminLayout
