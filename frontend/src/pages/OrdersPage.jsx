import { useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import { orderAPI } from '../api/services'

const statusColor = {
  PENDING: 'warning', CONFIRMED: 'info', PREPARING: 'primary',
  OUT_FOR_DELIVERY: 'secondary', DELIVERED: 'success', CANCELLED: 'danger',
}

const OrdersPage = () => {
  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(true)

  const loadOrders = () => {
    orderAPI.getMyOrders()
      .then((res) => setOrders(res.data.data || []))
      .catch(() => toast.error('Failed to load orders'))
      .finally(() => setLoading(false))
  }

  useEffect(() => { loadOrders() }, [])

  const cancelOrder = async (id) => {
    if (!window.confirm('Cancel this order?')) return
    try {
      await orderAPI.cancel(id)
      toast.success('Order cancelled')
      loadOrders()
    } catch (err) {
      toast.error(err.response?.data?.message || 'Cannot cancel order')
    }
  }

  if (loading) return <div className="loading-spinner"><div className="spinner-border text-primary" /></div>

  return (
    <div className="container py-4">
      <h2 className="mb-4">My Orders</h2>
      {orders.length === 0 ? (
        <p className="text-muted">No orders yet.</p>
      ) : (
        orders.map((order) => (
          <div key={order.id} className="card mb-3">
            <div className="card-body">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <h5>Order #{order.id}</h5>
                  <small className="text-muted">{new Date(order.createdAt).toLocaleString()}</small>
                </div>
                <span className={`badge bg-${statusColor[order.status]}`}>{order.status}</span>
              </div>
              <hr />
              <ul className="list-unstyled mb-2">
                {order.items?.map((item) => (
                  <li key={item.id} className="small">
                    {item.itemName} x{item.quantity} - ${item.subtotal}
                  </li>
                ))}
              </ul>
              <div className="d-flex justify-content-between">
                <strong>Total: ${order.totalAmount}</strong>
                {!['CANCELLED', 'DELIVERED'].includes(order.status) && (
                  <button className="btn btn-sm btn-outline-danger" onClick={() => cancelOrder(order.id)}>
                    Cancel Order
                  </button>
                )}
              </div>
            </div>
          </div>
        ))
      )}
    </div>
  )
}

export default OrdersPage
