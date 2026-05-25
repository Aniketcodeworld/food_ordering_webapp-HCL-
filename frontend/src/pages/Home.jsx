import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

const Home = () => {
  const { isAuthenticated, isAdmin } = useAuth()

  return (
    <>
      <section className="hero-section text-center">
        <div className="container">
          <h1 className="display-4 fw-bold">Order Food Online</h1>
          <p className="lead mb-4">Discover restaurants, browse menus, and get food delivered to your door.</p>
          <Link to="/restaurants" className="btn btn-light btn-lg me-2">Browse Restaurants</Link>
          {!isAuthenticated() && (
            <Link to="/register" className="btn btn-outline-light btn-lg">Get Started</Link>
          )}
          {isAuthenticated() && !isAdmin() && (
            <Link to="/dashboard" className="btn btn-outline-light btn-lg">My Dashboard</Link>
          )}
        </div>
      </section>
      <div className="container py-5">
        <div className="row text-center g-4">
          <div className="col-md-4">
            <div className="card p-4">
              <div className="fs-1 mb-3">🍕</div>
              <h5>Wide Selection</h5>
              <p className="text-muted">Pizza, burgers, sushi and more from top restaurants.</p>
            </div>
          </div>
          <div className="col-md-4">
            <div className="card p-4">
              <div className="fs-1 mb-3">🛒</div>
              <h5>Easy Ordering</h5>
              <p className="text-muted">Add to cart, place orders, and track delivery status.</p>
            </div>
          </div>
          <div className="col-md-4">
            <div className="card p-4">
              <div className="fs-1 mb-3">📧</div>
              <h5>Email Updates</h5>
              <p className="text-muted">Get notified on registration, order confirmation & cancellation.</p>
            </div>
          </div>
        </div>
      </div>
    </>
  )
}

export default Home
