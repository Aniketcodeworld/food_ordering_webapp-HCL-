import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { toast } from 'react-toastify'
import { restaurantAPI, menuAPI } from '../api/services'
import MenuCard from '../components/MenuCard'

const RestaurantDetails = () => {
  const { id } = useParams()
  const [restaurant, setRestaurant] = useState(null)
  const [menu, setMenu] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([
      restaurantAPI.getById(id),
      menuAPI.getByRestaurant(id),
    ])
      .then(([rRes, mRes]) => {
        setRestaurant(rRes.data.data)
        setMenu(mRes.data.data || [])
      })
      .catch(() => toast.error('Failed to load restaurant'))
      .finally(() => setLoading(false))
  }, [id])

  if (loading) return <div className="loading-spinner"><div className="spinner-border text-primary" /></div>
  if (!restaurant) return <div className="container py-4"><p>Restaurant not found</p></div>

  return (
    <div className="container py-4">
      <div className="card mb-4">
        <div className="row g-0">
          <div className="col-md-4">
            <img src={restaurant.imageUrl} className="img-fluid rounded-start h-100" alt={restaurant.name}
              style={{ objectFit: 'cover', minHeight: '200px' }} />
          </div>
          <div className="col-md-8">
            <div className="card-body">
              <h2>{restaurant.name}</h2>
              <p className="text-muted">{restaurant.description}</p>
              <p>📍 {restaurant.address}</p>
            </div>
          </div>
        </div>
      </div>
      <h4 className="mb-3">Menu</h4>
      <div className="row">
        {menu.map((item) => <MenuCard key={item.id} item={item} />)}
        {menu.length === 0 && <p className="text-muted">No menu items available.</p>}
      </div>
    </div>
  )
}

export default RestaurantDetails
