import React, { useState, useEffect } from 'react';
import { FaUser, FaEdit, FaTimes, FaSave } from 'react-icons/fa';
import Lottie from 'lottie-react';
import api from '../api/axiosConfig';
import Layout from '../components/Layout';
import profileAnimation from '../assets/lottie/profile.json';
import loaderAnimation from '../assets/lottie/loader.json';
import successAnimation from '../assets/lottie/success.json';
import errorAnimation from '../assets/lottie/error.json';
import "../css/ProfileMangement.css";
const ProfileManagement = () => {
  const [userProfile, setUserProfile] = useState({
    id: 'USR-12345',
    email: 'john.doe@example.com',
    username: 'johndoe',
    firstName: 'John',
    lastName: 'Doe',
    createdDate: 'Jan 15, 2023',
    updatedDate: 'Apr 20, 2024',
  });
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
  });
  const [loading, setLoading] = useState(false);
  const [notification, setNotification] = useState({ show: false, message: '', type: '' });
  const [animationError, setAnimationError] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);

  useEffect(() => {
    // Simulate API call
    setLoading(true);
    setTimeout(() => {
      setFormData({
        firstName: userProfile.firstName,
        lastName: userProfile.lastName,
      });
      setLoading(false);
    }, 1500);
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSaveProfile = () => {
    setLoading(true);
    setTimeout(() => {
      showNotification('Profile updated successfully!', 'success');
      setUserProfile((prev) => ({
        ...prev,
        firstName: formData.firstName,
        lastName: formData.lastName,
      }));
      setIsModalOpen(false);
      setLoading(false);
    }, 1500);
  };

  const showNotification = (message, type) => {
    setNotification({ show: true, message, type });
    setTimeout(() => {
      setNotification({ show: false, message: '', type: '' });
    }, 4000);
  };

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);
  
  const openDeleteDialog = () => setIsDeleting(true);
  const closeDeleteDialog = () => setIsDeleting(false);

  const handleDeleteAccount = () => {
    setLoading(true);
    setTimeout(() => {
      showNotification('Account deletion scheduled successfully', 'success');
      closeDeleteDialog();
      setLoading(false);
    }, 2000);
  };

  return (
    <Layout>
      <div className="profile-management">
        {loading ? (
          <div className="pm-loading">
            {animationError ? (
              <div className="pm-spinner-fallback" />
            ) : (
              <Lottie
                animationData={loaderAnimation}
                loop={true}
                style={{ width: 120, height: 120 }}
                onError={() => setAnimationError(true)}
              />
            )}
            <p>Loading your profile...</p>
          </div>
        ) : (
          <div className="pm-container">
            {notification.show && (
              <div className={`pm-notification pm-${notification.type}`}>
                {animationError ? (
                  <span className="pm-notification-icon">
                    {notification.type === 'success' ? '✔' : '✖'}
                  </span>
                ) : (
                  <Lottie
                    animationData={notification.type === 'success' ? successAnimation : errorAnimation}
                    loop={false}
                    style={{ width: 60, height: 60, marginRight: 12 }}
                    onError={() => setAnimationError(true)}
                  />
                )}
                <span>{notification.message}</span>
              </div>
            )}
            
            <div className="pm-header">
              <div className="pm-avatar-container">
                <div className="pm-avatar">
                  <div className="pm-avatar-initials">
                    {userProfile.firstName.charAt(0)}{userProfile.lastName.charAt(0)}
                  </div>
                  <div className="pm-avatar-status" />
                </div>
                <div className="pm-avatar-edit">
                  <FaEdit />
                </div>
              </div>
              <h1 className="pm-title">Profile Dashboard</h1>
              <p className="pm-subtitle">Shape your digital identity</p>
            </div>
            
            <div className="pm-content">
              <div className="pm-card">
                <div className="pm-section-header">
                  <div className="pm-icon-container">
                    <FaUser className="pm-icon" />
                  </div>
                  <h2>Personal Information</h2>
                  <button
                    onClick={openModal}
                    className="pm-edit-btn"
                    title="Edit Profile"
                  >
                    <FaEdit />
                  </button>
                </div>
                
                <div className="pm-info">
                  <div className="pm-info-item">
                    <label>Email</label>
                    <div className="pm-static-value">{userProfile.email}</div>
                  </div>
                  
                  <div className="pm-info-item">
                    <label>Username</label>
                    <div className="pm-static-value">{userProfile.username}</div>
                  </div>
                  
                  <div className="pm-info-grid">
                    <div className="pm-info-item">
                      <label>First Name</label>
                      <div className="pm-static-value">{userProfile.firstName}</div>
                    </div>
                    <div className="pm-info-item">
                      <label>Last Name</label>
                      <div className="pm-static-value">{userProfile.lastName}</div>
                    </div>
                  </div>
                  
                  <div className="pm-info-grid">
                    <div className="pm-info-item">
                      <label>Account Created</label>
                      <div className="pm-static-value">{userProfile.createdDate}</div>
                    </div>
                    <div className="pm-info-item">
                      <label>Last Updated</label>
                      <div className="pm-static-value">{userProfile.updatedDate}</div>
                    </div>
                  </div>
                </div>
              </div>
              
              <div className="pm-card">
                <div className="pm-section-header">
                  <div className="pm-icon-container">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                      <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
                    </svg>
                  </div>
                  <h2>Account Security</h2>
                </div>
                
                <div className="pm-security-grid">
                  <div className="pm-security-item">
                    <div className="pm-security-icon">
                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                        <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                      </svg>
                    </div>
                    <div>
                      <h3>Password</h3>
                      <p>Last changed 3 months ago</p>
                    </div>
                    <button className="pm-security-action">Change</button>
                  </div>
                  
                  <div className="pm-security-item">
                    <div className="pm-security-icon">
                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
                      </svg>
                    </div>
                    <div>
                      <h3>Two-Factor Authentication</h3>
                      <p>Not enabled</p>
                    </div>
                    <button className="pm-security-action">Enable</button>
                  </div>
                  
                  <div className="pm-security-item">
                    <div className="pm-security-icon">
                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                        <polyline points="22,6 12,13 2,6"/>
                      </svg>
                    </div>
                    <div>
                      <h3>Email Address</h3>
                      <p>Verified</p>
                    </div>
                    <button className="pm-security-action">Edit</button>
                  </div>
                  
                  <div className="pm-security-item">
                    <div className="pm-security-icon danger">
                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <circle cx="12" cy="12" r="10"/>
                        <line x1="15" y1="9" x2="9" y2="15"/>
                        <line x1="9" y1="9" x2="15" y2="15"/>
                      </svg>
                    </div>
                    <div>
                      <h3>Delete Account</h3>
                      <p>Permanently remove your account</p>
                    </div>
                    <button className="pm-security-action danger" onClick={openDeleteDialog}>Delete</button>
                  </div>
                </div>
              </div>
            </div>
            
            {isModalOpen && (
              <div className="pm-modal">
                <div className="pm-modal-content">
                  <div className="pm-section-header">
                    <h2>Update Profile</h2>
                    <button onClick={closeModal} className="pm-close-btn">
                      <FaTimes />
                    </button>
                  </div>
                  <div className="pm-form-grid">
                    <div className="pm-form-group">
                      <label>First Name</label>
                      <input
                        className="pm-input"
                        type="text"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleInputChange}
                      />
                    </div>
                    <div className="pm-form-group">
                      <label>Last Name</label>
                      <input
                        className="pm-input"
                        type="text"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleInputChange}
                      />
                    </div>
                  </div>
                  <div className="pm-form-actions">
                    <button onClick={closeModal} className="pm-cancel-btn">
                      Cancel
                    </button>
                    <button onClick={handleSaveProfile} className="pm-save-btn">
                      <FaSave /> Save Changes
                    </button>
                  </div>
                </div>
              </div>
            )}
            
            {isDeleting && (
              <div className="pm-dialog">
                <div className="pm-dialog-content">
                  <div className="pm-dialog-icon danger">
                    <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                      <circle cx="12" cy="12" r="10"/>
                      <line x1="15" y1="9" x2="9" y2="15"/>
                      <line x1="9" y1="9" x2="15" y2="15"/>
                    </svg>
                  </div>
                  <h2>Delete Your Account?</h2>
                  <p>This action cannot be undone. All your data will be permanently removed from our servers.</p>
                  
                  <div className="pm-dialog-actions">
                    <button onClick={closeDeleteDialog} className="pm-dialog-cancel">
                      Cancel
                    </button>
                    <button onClick={handleDeleteAccount} className="pm-dialog-confirm danger">
                      Delete Account
                    </button>
                  </div>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </Layout>
  );
};

export default ProfileManagement;

<style jsx>{`
  /* Root variables for consistent theming */
  
`}</style>