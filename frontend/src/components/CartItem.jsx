const CartItem = ({ item, onUpdate, onRemove, loading }) => (
  <div className="card mb-3">
    <div className="card-body">
      <div className="row align-items-center">
        <div className="col-md-2">
          <img
            src={item.imageUrl || 'https://via.placeholder.com/80'}
            alt={item.itemName}
            className="rounded"
            style={{ width: '80px', height: '80px', objectFit: 'cover' }}
          />
        </div>
        <div className="col-md-4">
          <h6 className="mb-0">{item.itemName}</h6>
          <small className="text-muted">${item.price} each</small>
        </div>
        <div className="col-md-3">
          <div className="input-group input-group-sm" style={{ maxWidth: '130px' }}>
            <button
              className="btn btn-outline-secondary"
              onClick={() => onUpdate(item.id, item.quantity - 1)}
              disabled={loading || item.quantity <= 1}
            >-</button>
            <input className="form-control text-center" value={item.quantity} readOnly />
            <button
              className="btn btn-outline-secondary"
              onClick={() => onUpdate(item.id, item.quantity + 1)}
              disabled={loading}
            >+</button>
          </div>
        </div>
        <div className="col-md-2 text-end">
          <strong>${item.subtotal}</strong>
        </div>
        <div className="col-md-1 text-end">
          <button className="btn btn-sm btn-outline-danger" onClick={() => onRemove(item.id)} disabled={loading}>
            ✕
          </button>
        </div>
      </div>
    </div>
  </div>
)

export default CartItem
