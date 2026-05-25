import { useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import { userAPI } from '../api/services'

const ProfilePage = () => {
  const [profile, setProfile] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    userAPI.getProfile()
      .then((res) => setProfile(res.data.data))
      .catch(() => toast.error('Failed to load profile'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div className="loading-spinner"><div className="spinner-border text-primary" /></div>

  return (
    <div className="container py-4">
      <h2 className="mb-4">My Profile</h2>
      <div className="card p-4 col-md-6">
        <p><strong>Name:</strong> {profile?.name}</p>
        <p><strong>Email:</strong> {profile?.email}</p>
        <p><strong>Phone:</strong> {profile?.phone || 'N/A'}</p>
        <p><strong>Role:</strong> <span className="badge bg-secondary">{profile?.role}</span></p>
        <p><strong>Member since:</strong> {profile?.createdAt ? new Date(profile.createdAt).toLocaleDateString() : 'N/A'}</p>
      </div>
    </div>
  )
}

export default ProfilePage
