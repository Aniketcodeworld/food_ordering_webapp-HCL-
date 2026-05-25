import { useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import { adminAPI } from '../../api/services'
import AdminLayout from './AdminLayout'

const statuses = ['PENDING', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED']

const ManageOrders = () => {
  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(true)

  const load = () => {
    adminAPI.getOrders()
      .then((res) => setOrders(res.data.data || []))
      .finally(() => setLoading(false))
  }

  useEffect(() => { load() }, [])

  const updateStatus = async (id, status) => {
    try {
      await adminAPI.updateOrderStatus(id, { status })
      toast.success('Status updated')
      load()
    } catch (err) {
      toast.error(err.response?.data?.message || 'Update failed')
    }
  }

  const cancelOrder = async (id) => {
    if (!window.confirm('Cancel this order?')) return
    try {
      await adminAPI.cancelOrder(id)
      toast.success('Order cancelled')
      load()
    } catch (err) {
      toast.error('Cancel failed')
    }
  }

  return (
    <AdminLayout title="Manage Orders">
      {loading ? <div className="spinner-border" /> : (
        <div className="table-responsive">
          <table className="table table-striped">
            <thead>
              <tr><th>ID</th><th>User</th><th>Items</th><th>Total</th><th>Status</th><th>Date</th><th>Actions</th></tr>
            </thead>
            <tbody>
              {orders.map((o) => (
                <tr key={o.id}>
                  <td>#{o.id}</td>
                  <td>{o.userName}<br /><small>{o.userEmail}</small></td>
                  <td>
                    <ul className="list-unstyled mb-0 small">
                      {o.items?.map((i) => <li key={i.id}>{i.itemName} x{i.quantity}</li>)}
                    </ul>
                  </td>
                  <td>${o.totalAmount}</td>
                  <td>
                    <select className="form-select form-select-sm" value={o.status}
                      onChange={(e) => updateStatus(o.id, e.target.value)}
                      disabled={o.status === 'CANCELLED'}>
                      {statuses.map((s) => <option key={s} value={s}>{s}</option>)}
                    </select>
                  </td>
                  <td><small>{new Date(o.createdAt).toLocaleString()}</small></td>
                  <td>
                    {o.status !== 'CANCELLED' && (
                      <button className="btn btn-sm btn-outline-danger" onClick={() => cancelOrder(o.id)}>Cancel</button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </AdminLayout>
  )
}

export default ManageOrders
