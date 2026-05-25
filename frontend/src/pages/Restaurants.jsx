import { useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import { restaurantAPI } from '../api/services'
import RestaurantCard from '../components/RestaurantCard'

const Restaurants = () => {
  const [restaurants, setRestaurants] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    restaurantAPI.getAll()
      .then((res) => setRestaurants(res.data.data || []))
      .catch(() => toast.error('Failed to load restaurants'))
      .finally(() => setLoading(false))
  }, [])

  return (
    <div className="container py-4">
      <h2 className="mb-4">Restaurants</h2>
      {loading ? (
        <div className="loading-spinner"><div className="spinner-border text-primary" /></div>
      ) : (
        <div className="row">
          {restaurants.map((r) => <RestaurantCard key={r.id} restaurant={r} />)}
          {restaurants.length === 0 && <p className="text-muted">No restaurants available.</p>}
        </div>
      )}
    </div>
  )
}

export default Restaurants
