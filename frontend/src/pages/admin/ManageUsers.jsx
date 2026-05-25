import { useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import { adminAPI } from '../../api/services'
import AdminLayout from './AdminLayout'

const ManageUsers = () => {
  const [users, setUsers] = useState([])
  const [selectedOrders, setSelectedOrders] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    adminAPI.getUsers()
      .then((res) => setUsers(res.data.data || []))
      .finally(() => setLoading(false))
  }, [])

  const viewOrders = async (userId) => {
    try {
      const res = await adminAPI.getUserOrders(userId)
      setSelectedOrders(res.data.data)
    } catch {
      toast.error('Failed to load orders')
    }
  }

  const deleteUser = async (id) => {
    if (!window.confirm('Delete this user?')) return
    try {
      await adminAPI.deleteUser(id)
      toast.success('User deleted')
      setUsers(users.filter((u) => u.id !== id))
    } catch (err) {
      toast.error(err.response?.data?.message || 'Delete failed')
    }
  }

  return (
    <AdminLayout title="Manage Users">
      {loading ? <div className="spinner-border" /> : (
        <div className="row">
          <div className={selectedOrders ? 'col-md-6' : 'col-12'}>
            <table className="table table-striped">
              <thead><tr><th>Name</th><th>Email</th><th>Phone</th><th>Actions</th></tr></thead>
              <tbody>
                {users.map((u) => (
                  <tr key={u.id}>
                    <td>{u.name}</td>
                    <td>{u.email}</td>
                    <td>{u.phone || 'N/A'}</td>
                    <td>
                      <button className="btn btn-sm btn-outline-info me-1" onClick={() => viewOrders(u.id)}>Orders</button>
                      <button className="btn btn-sm btn-outline-danger" onClick={() => deleteUser(u.id)}>Delete</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          {selectedOrders && (
            <div className="col-md-6">
              <div className="card p-3">
                <div className="d-flex justify-content-between">
                  <h5>User Orders</h5>
                  <button className="btn-close" onClick={() => setSelectedOrders(null)} />
                </div>
                {selectedOrders.length === 0 ? (
                  <p className="text-muted">No orders</p>
                ) : (
                  selectedOrders.map((o) => (
                    <div key={o.id} className="border-bottom py-2">
                      <strong>#{o.id}</strong> - ${o.totalAmount} - <span className="badge bg-secondary">{o.status}</span>
                    </div>
                  ))
                )}
              </div>
            </div>
          )}
        </div>
      )}
    </AdminLayout>
  )
}

export default ManageUsers
