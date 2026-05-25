import { useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import { adminAPI } from '../../api/services'
import DashboardCard from '../../components/DashboardCard'
import AdminLayout from './AdminLayout'

const statusColor = {
  PENDING: 'warning', CONFIRMED: 'info', PREPARING: 'primary',
  OUT_FOR_DELIVERY: 'secondary', DELIVERED: 'success', CANCELLED: 'danger',
}

const AdminDashboard = () => {
  const [data, setData] = useState(null)
  const [logs, setLogs] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([adminAPI.getDashboard(), adminAPI.getLogs()])
      .then(([dRes, lRes]) => {
        setData(dRes.data.data)
        setLogs(lRes.data.data || [])
      })
      .catch(() => toast.error('Failed to load admin dashboard'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div className="loading-spinner"><div className="spinner-border text-primary" /></div>

  return (
    <AdminLayout title="Admin Dashboard">
      <div className="row">
        <DashboardCard title="Total Users" value={data?.totalUsers} icon="👥" />
        <DashboardCard title="Restaurants" value={data?.totalRestaurants} icon="🏪" color="success" />
        <DashboardCard title="Total Orders" value={data?.totalOrders} icon="📦" color="warning" />
        <DashboardCard title="Revenue" value={`$${data?.totalRevenue || 0}`} icon="💰" color="info" />
      </div>
      <div className="row mt-3">
        <div className="col-md-7">
          <div className="card p-3">
            <h5>Recent Orders</h5>
            <div className="table-responsive">
              <table className="table table-sm">
                <thead><tr><th>ID</th><th>User</th><th>Total</th><th>Status</th></tr></thead>
                <tbody>
                  {data?.recentOrders?.map((o) => (
                    <tr key={o.id}>
                      <td>#{o.id}</td>
                      <td>{o.userName}</td>
                      <td>${o.totalAmount}</td>
                      <td><span className={`badge bg-${statusColor[o.status]}`}>{o.status}</span></td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <div className="col-md-5">
          <div className="card p-3" style={{ maxHeight: '400px', overflowY: 'auto' }}>
            <h5>Admin Activity Logs</h5>
            {logs.map((log) => (
              <div key={log.id} className="border-bottom py-2 small">
                <strong>{log.action}</strong>
                <p className="mb-0 text-muted">{log.details}</p>
                <small>{log.adminEmail} - {new Date(log.createdAt).toLocaleString()}</small>
              </div>
            ))}
          </div>
        </div>
      </div>
    </AdminLayout>
  )
}

export default AdminDashboard
