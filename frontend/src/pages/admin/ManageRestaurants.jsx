import { useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import { adminAPI } from '../../api/services'
import AdminLayout from './AdminLayout'

const emptyForm = { name: '', description: '', address: '', imageUrl: '', active: true }

const ManageRestaurants = () => {
  const [restaurants, setRestaurants] = useState([])
  const [form, setForm] = useState(emptyForm)
  const [editId, setEditId] = useState(null)
  const [loading, setLoading] = useState(true)

  const load = () => {
    adminAPI.getRestaurants()
      .then((res) => setRestaurants(res.data.data || []))
      .finally(() => setLoading(false))
  }

  useEffect(() => { load() }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      if (editId) {
        await adminAPI.updateRestaurant(editId, form)
        toast.success('Restaurant updated')
      } else {
        await adminAPI.createRestaurant(form)
        toast.success('Restaurant created')
      }
      setForm(emptyForm)
      setEditId(null)
      load()
    } catch (err) {
      toast.error(err.response?.data?.message || 'Operation failed')
    }
  }

  const handleEdit = (r) => {
    setEditId(r.id)
    setForm({ name: r.name, description: r.description, address: r.address, imageUrl: r.imageUrl, active: r.active })
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this restaurant?')) return
    try {
      await adminAPI.deleteRestaurant(id)
      toast.success('Deleted')
      load()
    } catch (err) {
      toast.error('Delete failed')
    }
  }

  return (
    <AdminLayout title="Manage Restaurants">
      <div className="row">
        <div className="col-md-4">
          <div className="card p-3">
            <h5>{editId ? 'Edit' : 'Add'} Restaurant</h5>
            <form onSubmit={handleSubmit}>
              {Object.keys(emptyForm).map((key) => (
                key === 'active' ? (
                  <div className="form-check mb-2" key={key}>
                    <input type="checkbox" className="form-check-input" checked={form.active}
                      onChange={(e) => setForm({ ...form, active: e.target.checked })} />
                    <label className="form-check-label">Active</label>
                  </div>
                ) : (
                  <div className="mb-2" key={key}>
                    <input className="form-control form-control-sm" placeholder={key}
                      value={form[key]} required={key === 'name'}
                      onChange={(e) => setForm({ ...form, [key]: e.target.value })} />
                  </div>
                )
              ))}
              <button type="submit" className="btn btn-primary btn-sm w-100">{editId ? 'Update' : 'Create'}</button>
              {editId && <button type="button" className="btn btn-secondary btn-sm w-100 mt-1"
                onClick={() => { setEditId(null); setForm(emptyForm) }}>Cancel</button>}
            </form>
          </div>
        </div>
        <div className="col-md-8">
          {loading ? <div className="spinner-border" /> : (
            <table className="table table-striped">
              <thead><tr><th>Name</th><th>Address</th><th>Active</th><th>Actions</th></tr></thead>
              <tbody>
                {restaurants.map((r) => (
                  <tr key={r.id}>
                    <td>{r.name}</td>
                    <td>{r.address}</td>
                    <td>{r.active ? '✅' : '❌'}</td>
                    <td>
                      <button className="btn btn-sm btn-outline-primary me-1" onClick={() => handleEdit(r)}>Edit</button>
                      <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(r.id)}>Delete</button>
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

export default ManageRestaurants
