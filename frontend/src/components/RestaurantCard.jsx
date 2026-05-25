import { Link } from 'react-router-dom'

const RestaurantCard = ({ restaurant }) => (
  <div className="col-md-6 col-lg-4 mb-4">
    <div className="card h-100">
      <img
        src={restaurant.imageUrl || 'https://via.placeholder.com/400x200?text=Restaurant'}
        className="card-img-top"
        alt={restaurant.name}
        style={{ height: '180px', objectFit: 'cover' }}
      />
      <div className="card-body d-flex flex-column">
        <h5 className="card-title">{restaurant.name}</h5>
        <p className="card-text text-muted small flex-grow-1">
          {restaurant.description?.substring(0, 100)}...
        </p>
        <p className="small text-muted mb-2">📍 {restaurant.address}</p>
        <Link to={`/restaurants/${restaurant.id}`} className="btn btn-primary btn-sm mt-auto">
          View Menu
        </Link>
      </div>
    </div>
  </div>
)

export default RestaurantCard
