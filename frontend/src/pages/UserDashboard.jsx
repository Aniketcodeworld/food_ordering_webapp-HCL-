import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { toast } from 'react-toastify'
import { userAPI } from '../api/services'
import DashboardCard from '../components/DashboardCard'

const statusColor = {
  PENDING: 'warning', CONFIRMED: 'info', PREPARING: 'primary',
  OUT_FOR_DELIVERY: 'secondary', DELIVERED: 'success', CANCELLED: 'danger',
}

const UserDashboard = () => {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    userAPI.getDashboard()
      .then((res) => setData(res.data.data))
      .catch(() => toast.error('Failed to load dashboard'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div className="loading-spinner"><div className="spinner-border text-primary" /></div>
  if (!data) return null

  return (
    <div className="container py-4">
      <h2 className="mb-4">Welcome, {data.profile?.name}!</h2>
      <div className="row">
        <DashboardCard title="Total Orders" value={data.totalOrders} icon="📦" />
        <DashboardCard title="Cart Items" value={data.cartSummary?.itemCount || 0} icon="🛒" color="success" />
        <DashboardCard title="Cart Total" value={`$${data.cartSummary?.total || 0}`} icon="💰" color="warning" />
        <DashboardCard title="Email" value={data.profile?.email} icon="📧" color="info" />
      </div>
      <div className="row mt-2">
        <div className="col-md-6">
          <div className="card p-3">
            <h5>Recent Orders</h5>
            {data.recentOrders?.length > 0 ? (
              <ul className="list-group list-group-flush">
                {data.recentOrders.map((o) => (
                  <li key={o.id} className="list-group-item d-flex justify-content-between">
                    <span>Order #{o.id} - ${o.totalAmount}</span>
                    <span className={`badge bg-${statusColor[o.status] || 'secondary'}`}>{o.status}</span>
                  </li>
                ))}
              </ul>
            ) : <p className="text-muted">No orders yet.</p>}
            <Link to="/orders" className="btn btn-sm btn-outline-primary mt-2">View All Orders</Link>
          </div>
        </div>
        <div className="col-md-6">
          <div className="card p-3">
            <h5>Cart Summary</h5>
            {data.cartSummary?.items?.length > 0 ? (
              <ul className="list-group list-group-flush">
                {data.cartSummary.items.map((i) => (
                  <li key={i.id} className="list-group-item d-flex justify-content-between">
                    <span>{i.itemName} x{i.quantity}</span>
                    <span>${i.subtotal}</span>
                  </li>
                ))}
              </ul>
            ) : <p className="text-muted">Cart is empty.</p>}
            <Link to="/cart" className="btn btn-sm btn-primary mt-2">Go to Cart</Link>
          </div>
        </div>
      </div>
      <Link to="/profile" className="btn btn-outline-secondary mt-3">View Profile</Link>
    </div>
  )
}

export default UserDashboard
