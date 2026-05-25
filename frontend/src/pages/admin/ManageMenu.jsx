import { useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import { adminAPI } from '../../api/services'
import AdminLayout from './AdminLayout'

const emptyForm = { name: '', description: '', price: '', imageUrl: '', available: true, restaurantId: '' }

const ManageMenu = () => {
  const [items, setItems] = useState([])
  const [restaurants, setRestaurants] = useState([])
  const [form, setForm] = useState(emptyForm)
  const [editId, setEditId] = useState(null)
  const [loading, setLoading] = useState(true)

  const load = () => {
    Promise.all([adminAPI.getMenu(), adminAPI.getRestaurants()])
      .then(([mRes, rRes]) => {
        setItems(mRes.data.data || [])
        setRestaurants(rRes.data.data || [])
      })
      .finally(() => setLoading(false))
  }

  useEffect(() => { load() }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    const payload = { ...form, price: parseFloat(form.price), restaurantId: parseInt(form.restaurantId) }
    try {
      if (editId) {
        await adminAPI.updateMenuItem(editId, payload)
        toast.success('Menu item updated')
      } else {
        await adminAPI.createMenuItem(payload)
        toast.success('Menu item created')
      }
      setForm(emptyForm)
      setEditId(null)
      load()
    } catch (err) {
      toast.error(err.response?.data?.message || 'Operation failed')
    }
  }

  const handleEdit = (item) => {
    setEditId(item.id)
    setForm({
      name: item.name, description: item.description, price: item.price,
      imageUrl: item.imageUrl, available: item.available, restaurantId: item.restaurantId,
    })
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this menu item?')) return
    try {
      await adminAPI.deleteMenuItem(id)
      toast.success('Deleted')
      load()
    } catch (err) {
      toast.error('Delete failed')
    }
  }

  return (
    <AdminLayout title="Manage Menu">
      <div className="row">
        <div className="col-md-4">
          <div className="card p-3">
            <h5>{editId ? 'Edit' : 'Add'} Menu Item</h5>
            <form onSubmit={handleSubmit}>
              <input className="form-control form-control-sm mb-2" placeholder="Name" value={form.name} required
                onChange={(e) => setForm({ ...form, name: e.target.value })} />
              <textarea className="form-control form-control-sm mb-2" placeholder="Description" value={form.description}
                onChange={(e) => setForm({ ...form, description: e.target.value })} />
              <input className="form-control form-control-sm mb-2" placeholder="Price" type="number" step="0.01" value={form.price} required
                onChange={(e) => setForm({ ...form, price: e.target.value })} />
              <input className="form-control form-control-sm mb-2" placeholder="Image URL" value={form.imageUrl}
                onChange={(e) => setForm({ ...form, imageUrl: e.target.value })} />
              <select className="form-select form-select-sm mb-2" value={form.restaurantId} required
                onChange={(e) => setForm({ ...form, restaurantId: e.target.value })}>
                <option value="">Select Restaurant</option>
                {restaurants.map((r) => <option key={r.id} value={r.id}>{r.name}</option>)}
              </select>
              <div className="form-check mb-2">
                <input type="checkbox" className="form-check-input" checked={form.available}
                  onChange={(e) => setForm({ ...form, available: e.target.checked })} />
                <label className="form-check-label">Available</label>
              </div>
              <button type="submit" className="btn btn-primary btn-sm w-100">{editId ? 'Update' : 'Create'}</button>
            </form>
          </div>
        </div>
        <div className="col-md-8">
          {loading ? <div className="spinner-border" /> : (
            <table className="table table-striped table-sm">
              <thead><tr><th>Name</th><th>Restaurant</th><th>Price</th><th>Actions</th></tr></thead>
              <tbody>
                {items.map((item) => (
                  <tr key={item.id}>
                    <td>{item.name}</td>
                    <td>{item.restaurantName}</td>
                    <td>${item.price}</td>
                    <td>
                      <button className="btn btn-sm btn-outline-primary me-1" onClick={() => handleEdit(item)}>Edit</button>
                      <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(item.id)}>Delete</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </AdminLayout>
  )
}

export default ManageMenu
