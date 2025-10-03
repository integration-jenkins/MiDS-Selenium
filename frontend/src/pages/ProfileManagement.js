import React, { useState, useEffect } from 'react';
import { FaUser, FaKey, FaPlus, FaEdit, FaTrash, FaSave } from 'react-icons/fa';
import api from '../api/axiosConfig';
import Layout from '../components/Layout';
import '../css/ProfileManagement.css';

const ProfileManagement = () => {
  const [userProfile, setUserProfile] = useState({
    id: '',
    email: '',
    username: '',
    firstName: '',
    lastName: '',
    createdDate: '',
    updatedDate: ''
  });
  
  const [credentials, setCredentials] = useState([]);
  const [newCredential, setNewCredential] = useState({
    userName: '',
    password: '',
    circle: '',
    role: ''
  });
  const [editingCredentialId, setEditingCredentialId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [notification, setNotification] = useState({ show: false, message: '', type: '' });

  useEffect(() => {
    fetchProfileData();
    fetchCredentials();
  }, []);

  const fetchProfileData = async () => {
    try {
      setLoading(true);
      const response = await api.get('/api/user/profile');
      setUserProfile(response.data);
    } catch (error) {
      showNotification('Failed to load profile data', 'error');
      console.error('Error fetching profile:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchCredentials = async () => {
    try {
      const response = await api.get('/api/sample-user-credentials');
      setCredentials(response.data);
    } catch (error) {
      showNotification('Failed to load credentials', 'error');
      console.error('Error fetching credentials:', error);
    }
  };

  const handleProfileChange = (e) => {
    const { name, value } = e.target;
    setUserProfile(prev => ({ ...prev, [name]: value }));
  };

  const handleCredentialChange = (e) => {
    const { name, value } = e.target;
    setNewCredential(prev => ({ ...prev, [name]: value }));
  };

  const handleSaveProfile = async () => {
    try {
      setLoading(true);
      await api.put(`/api/user/${userProfile.id}`, {
        username: userProfile.username,
        firstName: userProfile.firstName,
        lastName: userProfile.lastName
      });
      showNotification('Profile updated successfully!', 'success');
      fetchProfileData();
    } catch (error) {
      showNotification('Failed to update profile', 'error');
      console.error('Error updating profile:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAddCredential = async () => {
    try {
      setLoading(true);
      await api.post('/api/sample-user-credentials', {
        ...newCredential,
        doneBy: userProfile.username
      });
      showNotification('Credential added successfully!', 'success');
      setNewCredential({ userName: '', password: '', circle: '', role: '' });
      fetchCredentials();
    } catch (error) {
      showNotification('Failed to add credential', 'error');
      console.error('Error adding credential:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateCredential = async (id) => {
    try {
      setLoading(true);
      await api.put(`/api/sample-user-credentials/${id}`, newCredential);
      showNotification('Credential updated successfully!', 'success');
      setEditingCredentialId(null);
      setNewCredential({ userName: '', password: '', circle: '', role: '' });
      fetchCredentials();
    } catch (error) {
      showNotification('Failed to update credential', 'error');
      console.error('Error updating credential:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteCredential = async (id) => {
    try {
      setLoading(true);
      await api.delete(`/api/sample-user-credentials/${id}`);
      showNotification('Credential deleted successfully!', 'success');
      fetchCredentials();
    } catch (error) {
      showNotification('Failed to delete credential', 'error');
      console.error('Error deleting credential:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleEditCredential = (credential) => {
    setEditingCredentialId(credential.sampleUserId);
    setNewCredential({
      userName: credential.userName,
      password: credential.password,
      circle: credential.circle,
      role: credential.role
    });
  };

  const handleCancelEdit = () => {
    setEditingCredentialId(null);
    setNewCredential({ userName: '', password: '', circle: '', role: '' });
  };

  const showNotification = (message, type) => {
    setNotification({ show: true, message, type });
    setTimeout(() => {
      setNotification({ show: false, message: '', type: '' });
    }, 3000);
  };

  return (
    <Layout title="Profile Management">
      {loading ? (
        <div className="profile-management-loading">
          <div className="profile-management-spinner"></div>
          <p>Loading your profile...</p>
        </div>
      ) : (
        <div className="profile-management-container">
          {notification.show && (
            <div className={`profile-management-notification profile-management-${notification.type}`}>
              {notification.message}
            </div>
          )}
          
          <div className="profile-management-header">
            <p>Manage your personal information and credentials</p>
          </div>
          
          <div className="profile-management-content">
            <div className="profile-management-card profile-management-profile-section">
              <div className="profile-management-section-header">
                <FaUser className="profile-management-icon" />
                <h2>Personal Information</h2>
              </div>
              
              <div className="profile-management-info">
                <div className="profile-management-info-item">
                  <label>Email</label>
                  <div className="profile-management-static-value">{userProfile.email}</div>
                </div>
                
                <div className="profile-management-info-item">
                  <label>Username</label>
                  <input
                    className="profile-management-input"
                    type="text"
                    name="username"
                    value={userProfile.username}
                    onChange={handleProfileChange}
                  />
                </div>
                
                <div className="profile-management-info-grid">
                  <div className="profile-management-info-item">
                    <label>First Name</label>
                    <input
                      className="profile-management-input"
                      type="text"
                      name="firstName"
                      value={userProfile.firstName}
                      onChange={handleProfileChange}
                    />
                  </div>
                  
                  <div className="profile-management-info-item">
                    <label>Last Name</label>
                    <input
                      className="profile-management-input"
                      type="text"
                      name="lastName"
                      value={userProfile.lastName}
                      onChange={handleProfileChange}
                    />
                  </div>
                </div>
                
                <div className="profile-management-info-grid">
                  <div className="profile-management-info-item">
                    <label>Account Created</label>
                    <div className="profile-management-static-value">{userProfile.createdDate}</div>
                  </div>
                  
                  <div className="profile-management-info-item">
                    <label>Last Updated</label>
                    <div className="profile-management-static-value">{userProfile.updatedDate}</div>
                  </div>
                </div>
                
                <button 
                  className="profile-management-save-btn"
                  onClick={handleSaveProfile}
                >
                  <FaSave /> Save Profile
                </button>
              </div>
            </div>
            
            <div className="profile-management-card profile-management-credentials-section">
              <div className="profile-management-section-header">
                <FaKey className="profile-management-icon" />
                <h2>Sample User Credentials</h2>
              </div>
              
              <div className="profile-management-credential-form">
                <div className="profile-management-form-grid">
                  <div className="profile-management-form-group">
                    <label>Role</label>
                    <input
                      className="profile-management-input"
                      type="text"
                      name="role"
                      value={newCredential.role}
                      onChange={handleCredentialChange}
                      placeholder="Enter role (e.g., Admin)"
                    />
                  </div>
                  
                  <div className="profile-management-form-group">
                    <label>Username</label>
                    <input
                      className="profile-management-input"
                      type="text"
                      name="userName"
                      value={newCredential.userName}
                      onChange={handleCredentialChange}
                      placeholder="Enter username"
                    />
                  </div>
                  
                  <div className="profile-management-form-group">
                    <label>Password</label>
                    <input
                      className="profile-management-input"
                      type="password"
                      name="password"
                      value={newCredential.password}
                      onChange={handleCredentialChange}
                      placeholder="Enter password"
                    />
                  </div>
                  
                  <div className="profile-management-form-group">
                    <label>Circle</label>
                    <input
                      className="profile-management-input"
                      type="text"
                      name="circle"
                      value={newCredential.circle}
                      onChange={handleCredentialChange}
                      placeholder="Enter circle"
                    />
                  </div>
                </div>
                
                <div className="profile-management-form-actions">
                  {editingCredentialId ? (
                    <>
                      <button 
                        className="profile-management-cancel-btn"
                        onClick={handleCancelEdit}
                      >
                        Cancel
                      </button>
                      <button 
                        className="profile-management-update-btn"
                        onClick={() => handleUpdateCredential(editingCredentialId)}
                      >
                        <FaEdit /> Update Credential
                      </button>
                    </>
                  ) : (
                    <button 
                      className="profile-management-add-btn"
                      onClick={handleAddCredential}
                    >
                      <FaPlus /> Add Credential
                    </button>
                  )}
                </div>
              </div>
              
              <div className="profile-management-credentials-list">
                <h3>Your Credentials</h3>
                
                {credentials.length === 0 ? (
                  <div className="profile-management-empty-state">
                    <p>No credentials found. Add your first credential above.</p>
                  </div>
                ) : (
                  <div className="profile-management-credentials-grid">
                    {(Array.isArray(credentials) ? credentials : []).map(cred => (
                      <div className="profile-management-credential-card" key={cred.sampleUserId}>
                        <div className="profile-management-credential-header">
                          <div className="profile-management-role-badge">{cred.role}</div>
                          <div className="profile-management-credential-actions">
                            <button 
                              className="profile-management-edit-btn"
                              onClick={() => handleEditCredential(cred)}
                            >
                              <FaEdit />
                            </button>
                            <button 
                              className="profile-management-delete-btn"
                              onClick={() => handleDeleteCredential(cred.sampleUserId)}
                            >
                              <FaTrash />
                            </button>
                          </div>
                        </div>
                        
                        <div className="profile-management-credential-details">
                          <div className="profile-management-detail-item">
                            <label>Username:</label>
                            <span>{cred.userName}</span>
                          </div>
                          <div className="profile-management-detail-item">
                            <label>Password:</label>
                            <span>••••••••</span>
                          </div>
                          <div className="profile-management-detail-item">
                            <label>Circle:</label>
                            <span>{cred.circle}</span>
                          </div>
                          <div className="profile-management-detail-item">
                            <label>Added By:</label>
                            <span>{cred.doneBy}</span>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
};

export default ProfileManagement;