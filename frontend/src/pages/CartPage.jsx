import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import { cartAPI, orderAPI } from '../api/services'
import CartItem from '../components/CartItem'

const CartPage = () => {
  const [cart, setCart] = useState(null)
  const [loading, setLoading] = useState(true)
  const [actionLoading, setActionLoading] = useState(false)
  const navigate = useNavigate()

  const loadCart = () => {
    cartAPI.get()
      .then((res) => setCart(res.data.data))
      .catch(() => toast.error('Failed to load cart'))
      .finally(() => setLoading(false))
  }

  useEffect(() => { loadCart() }, [])

  const updateQty = async (cartItemId, quantity) => {
    if (quantity < 1) return
    setActionLoading(true)
    try {
      const res = await cartAPI.updateQuantity(cartItemId, quantity)
      setCart(res.data.data)
    } catch (err) {
      toast.error(err.response?.data?.message || 'Update failed')
    } finally {
      setActionLoading(false)
    }
  }

  const removeItem = async (cartItemId) => {
    setActionLoading(true)
    try {
      const res = await cartAPI.remove(cartItemId)
      setCart(res.data.data)
      toast.info('Item removed')
    } catch (err) {
      toast.error('Remove failed')
    } finally {
      setActionLoading(false)
    }
  }

  const placeOrder = async () => {
    setActionLoading(true)
    try {
      await orderAPI.place()
      toast.success('Order placed! Check your email for confirmation.')
      navigate('/orders')
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to place order')
    } finally {
      setActionLoading(false)
    }
  }

  if (loading) return <div className="loading-spinner"><div className="spinner-border text-primary" /></div>

  return (
    <div className="container py-4">
      <h2 className="mb-4">Shopping Cart</h2>
      {cart?.items?.length > 0 ? (
        <>
          {cart.items.map((item) => (
            <CartItem key={item.id} item={item} onUpdate={updateQty} onRemove={removeItem} loading={actionLoading} />
          ))}
          <div className="card p-3">
            <div className="d-flex justify-content-between align-items-center">
              <h4>Total: ${cart.total}</h4>
              <button className="btn btn-primary btn-lg" onClick={placeOrder} disabled={actionLoading}>
                Place Order
              </button>
            </div>
          </div>
        </>
      ) : (
        <div className="text-center py-5">
          <p className="text-muted fs-4">Your cart is empty</p>
          <button className="btn btn-primary" onClick={() => navigate('/restaurants')}>Browse Restaurants</button>
        </div>
      )}
    </div>
  )
}

export default CartPage
