import React, { useState, useEffect } from 'react';
import { FaKey, FaPlus, FaEdit, FaTrash, FaTimes, FaSave, FaEye, FaEyeSlash } from 'react-icons/fa';
import api from '../api/axiosConfig';
import Layout from '../components/Layout';
import '../css/SampleCredentialManagement.css';

const SampleCredentialManagement = () => {
  // Sample credentials data
  const sampleCredentials = [
    {
      sampleUserId: 1,
      userName: 'JK_ravi',
      password: 'test123',
      circle: 'MS Partner',
      role: 'Asinghal',
      doneBy: 'Admin'
    },
    {
      sampleUserId: 2,
      userName: 'JK_ravi',
      password: 'test123',
      circle: 'MW Planner',
      role: 'Asinghal',
      doneBy: 'Admin'
    },
    {
      sampleUserId: 3,
      userName: 'JK_ravi',
      password: 'test123',
      circle: 'Operation Team',
      role: 'Asinghal',
      doneBy: 'Admin'
    },
    {
      sampleUserId: 4,
      userName: 'JK_ravi',
      password: 'test123',
      circle: 'Deployment Team',
      role: 'Asinghal',
      doneBy: 'Admin'
    },
    {
      sampleUserId: 5,
      userName: 'JK_ravi',
      password: 'test123',
      circle: 'I&C Partner',
      role: 'Asinghal',
      doneBy: 'Admin'
    }
  ];

  const [credentials, setCredentials] = useState(sampleCredentials);
  const [newCredential, setNewCredential] = useState({
    userName: '',
    password: '',
    circle: '',
    role: '',
  });
  const [editingCredentialId, setEditingCredentialId] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [notification, setNotification] = useState({ show: false, message: '', type: '' });
  const [showPassword, setShowPassword] = useState(false);
  const [showPasswordInModal, setShowPasswordInModal] = useState(false);

  useEffect(() => {
    // Simulate loading
    setLoading(true);
    const timer = setTimeout(() => {
      setLoading(false);
    }, 1000);
    return () => clearTimeout(timer);
  }, []);

  const handleCredentialChange = (e) => {
    const { name, value } = e.target;
    setNewCredential((prev) => ({ ...prev, [name]: value }));
  };

  const handleAddCredential = () => {
    if (!newCredential.userName || !newCredential.password || !newCredential.circle || !newCredential.role) {
      showNotification('Please fill all fields', 'error');
      return;
    }
    
    setLoading(true);
    setTimeout(() => {
      const username = localStorage.getItem('username') || 'Admin';
      const newCred = {
        sampleUserId: credentials.length + 1,
        ...newCredential,
        doneBy: username
      };
      
      setCredentials([...credentials, newCred]);
      setNewCredential({ userName: '', password: '', circle: '', role: '' });
      showNotification('Credential added successfully!', 'success');
      setLoading(false);
    }, 1000);
  };

  const handleUpdateCredential = () => {
    setLoading(true);
    setTimeout(() => {
      const updatedCredentials = credentials.map(cred => 
        cred.sampleUserId === editingCredentialId ? { ...cred, ...newCredential } : cred
      );
      
      setCredentials(updatedCredentials);
      setEditingCredentialId(null);
      setNewCredential({ userName: '', password: '', circle: '', role: '' });
      setIsModalOpen(false);
      showNotification('Credential updated successfully!', 'success');
      setLoading(false);
    }, 1000);
  };

  const handleDeleteCredential = (id) => {
    setLoading(true);
    setTimeout(() => {
      const filteredCredentials = credentials.filter(cred => cred.sampleUserId !== id);
      setCredentials(filteredCredentials);
      showNotification('Credential deleted successfully!', 'success');
      setLoading(false);
    }, 1000);
  };

  const handleEditCredential = (credential) => {
    setEditingCredentialId(credential.sampleUserId);
    setNewCredential({
      userName: credential.userName,
      password: credential.password,
      circle: credential.circle,
      role: credential.role,
    });
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setEditingCredentialId(null);
    setNewCredential({ userName: '', password: '', circle: '', role: '' });
    setIsModalOpen(false);
  };

  const showNotification = (message, type) => {
    setNotification({ show: true, message, type });
    setTimeout(() => {
      setNotification({ show: false, message: '', type: '' });
    }, 4000);
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const togglePasswordInModal = () => {
    setShowPasswordInModal(!showPasswordInModal);
  };

  return (
    <Layout>
      <div className="credential-management">
        {loading ? (
          <div className="cm-loading">
            <div className="cm-spinner"></div>
            <p>Loading credentials...</p>
          </div>
        ) : (
          <div className="cm-container">
            {notification.show && (
              <div className={`cm-notification cm-${notification.type}`}>
                <div className="cm-notification-content">
                  <span>{notification.message}</span>
                </div>
              </div>
            )}
            
            <div className="cm-header">
              <div className="cm-header-icon">
                <FaKey />
              </div>
              <h1 className="cm-title">Sample Credential Dashboard</h1>
              <p className="cm-subtitle">Manage your sample user credentials for testing</p>
            </div>
            
            <div className="cm-content">
              <div className="cm-card">
                <div className="cm-section-header">
                  <h2>Add New Credential</h2>
                </div>
                
                <div className="cm-form-grid">
                  <div className="cm-form-group">
                    <label>Role</label>
                    <input
                      type="text"
                      name="role"
                      value={newCredential.role}
                      onChange={handleCredentialChange}
                      placeholder="Enter role (e.g., Admin)"
                    />
                  </div>
                  
                  <div className="cm-form-group">
                    <label>Username</label>
                    <input
                      type="text"
                      name="userName"
                      value={newCredential.userName}
                      onChange={handleCredentialChange}
                      placeholder="Enter username"
                    />
                  </div>
                  
                  <div className="cm-form-group">
                    <label>Password</label>
                    <div>
                      <input
                        type={showPassword ? "text" : "password"}
                        name="password"
                        value={newCredential.password}
                        onChange={handleCredentialChange}
                        placeholder="Enter password"
                      />
                      <button 
                        className="password-toggle"
                        onClick={togglePasswordVisibility}
                      >
                        {showPassword ? <FaEyeSlash /> : <FaEye />}
                      </button>
                    </div>
                  </div>
                  
                  <div className="cm-form-group">
                    <label>Circle</label>
                    <input
                      type="text"
                      name="circle"
                      value={newCredential.circle}
                      onChange={handleCredentialChange}
                      placeholder="Enter circle"
                    />
                  </div>
                </div>
                
                <div className="cm-form-actions">
                  <button
                    className="cm-save-btn"
                    onClick={handleAddCredential}
                  >
                    <FaPlus /> Add Credential
                  </button>
                </div>
              </div>
              
              <div className="cm-card">
                <div className="cm-section-header">
                  <h2>Your Sample Credentials</h2>
                  <div className="cm-counter">{credentials.length} credentials</div>
                </div>
                
                {credentials.length === 0 ? (
                  <div className="cm-empty-state">
                    <div className="cm-empty-icon">🔑</div>
                    <h3>No Credentials Found</h3>
                    <p>Add your first credential to get started</p>
                  </div>
                ) : (
                  <div className="cm-credentials-slider">
                    <div className="cm-credentials-container">
                      {credentials.map((cred) => (
                        <div key={cred.sampleUserId} className="cm-credential-card">
                          <div className="cm-credential-header">
                            <div className="cm-role-badge">{cred.role}</div>
                            <div className="cm-credential-actions">
                              <button
                                className="cm-edit-btn"
                                onClick={() => handleEditCredential(cred)}
                                title="Edit"
                              >
                                <FaEdit />
                              </button>
                              <button
                                className="cm-delete-btn"
                                onClick={() => handleDeleteCredential(cred.sampleUserId)}
                                title="Delete"
                              >
                                <FaTrash />
                              </button>
                            </div>
                          </div>
                          
                          <div className="cm-credential-body">
                            <div className="cm-credential-field">
                              <span>Username:</span>
                              <strong>{cred.userName}</strong>
                            </div>
                            <div className="cm-credential-field">
                              <span>Password:</span>
                              <strong>••••••••</strong>
                            </div>
                            <div className="cm-credential-field">
                              <span>Circle:</span>
                              <strong>{cred.circle}</strong>
                            </div>
                          </div>
                          
                          <div className="cm-credential-footer">
                            <span>Added by: {cred.doneBy}</span>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                )}
              </div>
            </div>
            
            {isModalOpen && (
              <div className="cm-modal-overlay">
                <div className="cm-modal">
                  <div className="cm-modal-header">
                    <h2>Update Credential</h2>
                    <button onClick={closeModal} className="cm-close-btn">
                      <FaTimes />
                    </button>
                  </div>
                  
                  <div className="cm-modal-body">
                    <div className="cm-form-grid">
                      <div className="cm-form-group">
                        <label>Role</label>
                        <input
                          type="text"
                          name="role"
                          value={newCredential.role}
                          onChange={handleCredentialChange}
                          placeholder="Enter role (e.g., Admin)"
                        />
                      </div>
                      
                      <div className="cm-form-group">
                        <label>Username</label>
                        <input
                          type="text"
                          name="userName"
                          value={newCredential.userName}
                          onChange={handleCredentialChange}
                          placeholder="Enter username"
                        />
                      </div>
                      
                      <div className="cm-form-group">
                        <label>Password</label>
                        <div >
                          <input
                            type={showPasswordInModal ? "text" : "password"}
                            name="password"
                            value={newCredential.password}
                            onChange={handleCredentialChange}
                            placeholder="Enter password"
                          />
                          <button 
                            className="password-toggle"
                            onClick={togglePasswordInModal}
                          >
                            {showPasswordInModal ? <FaEyeSlash /> : <FaEye />}
                          </button>
                        </div>
                      </div>
                      
                      <div className="cm-form-group">
                        <label>Circle</label>
                        <input
                          type="text"
                          name="circle"
                          value={newCredential.circle}
                          onChange={handleCredentialChange}
                          placeholder="Enter circle"
                        />
                      </div>
                    </div>
                  </div>
                  
                  <div className="cm-modal-footer">
                    <button onClick={closeModal} className="cm-cancel-btn">
                      Cancel
                    </button>
                    <button onClick={handleUpdateCredential} className="cm-save-btn">
                      <FaSave /> Save Changes
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

export default SampleCredentialManagement;