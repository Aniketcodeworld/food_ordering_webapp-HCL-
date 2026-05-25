const DashboardCard = ({ title, value, icon, color = 'primary' }) => (
  <div className="col-md-6 col-lg-3 mb-4">
    <div className={`card dashboard-card border-${color}`}>
      <div className="card-body">
        <div className="d-flex justify-content-between">
          <div>
            <p className="text-muted small mb-1">{title}</p>
            <h3 className="mb-0">{value}</h3>
          </div>
          <span className="fs-2">{icon}</span>
        </div>
      </div>
    </div>
  </div>
)

export default DashboardCard
