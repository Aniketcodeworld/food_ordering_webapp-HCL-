import { useState } from 'react'
import { toast } from 'react-toastify'
import { cartAPI } from '../api/services'
import { useAuth } from '../context/AuthContext'
import { useNavigate } from 'react-router-dom'

const MenuCard = ({ item }) => {
  const { isAuthenticated } = useAuth()
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)

  const addToCart = async () => {
    if (!isAuthenticated()) {
      toast.info('Please login to add items to cart')
      navigate('/login')
      return
    }
    setLoading(true)
    try {
      await cartAPI.add({ menuItemId: item.id, quantity: 1 })
      toast.success(`${item.name} added to cart!`)
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to add to cart')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="col-md-6 col-lg-4 mb-4">
      <div className="card h-100">
        <img
          src={item.imageUrl || 'https://via.placeholder.com/300x200?text=Food'}
          className="card-img-top"
          alt={item.name}
          style={{ height: '160px', objectFit: 'cover' }}
        />
        <div className="card-body d-flex flex-column">
          <h6 className="card-title">{item.name}</h6>
          <p className="card-text small text-muted flex-grow-1">{item.description}</p>
          <div className="d-flex justify-content-between align-items-center mt-2">
            <span className="fw-bold text-primary">${item.price}</span>
            <button className="btn btn-sm btn-primary" onClick={addToCart} disabled={loading}>
              {loading ? '...' : '+ Add'}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default MenuCard
