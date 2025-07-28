import React, { useState } from 'react';
import '../css/MWDpr.css';
import { FaTable, FaToggleOn, FaCheckCircle, FaTimesCircle, FaSpinner } from 'react-icons/fa';
import { FiX, FiCheck, FiAlertCircle, FiClock, FiBarChart, FiTrendingUp } from 'react-icons/fi';
import api from '../api/axiosConfig';
import { useNavigate } from 'react-router-dom';
import Layout from "../components/Layout";

const statusOptions = {
  SR_RFAI: ["SR Pending", "SP Pending", "SO Pending", "RFAI Pending"],
  Material: ["MO Pending", "Material Delivery Pending"],
  INC: ["HOP Alignment Pending/Phy-AT Pending", "HOP Alignment Pending/Phy-AT Raised", "HOP Alignment Pending/Phy-AT Rejected", "HOP Alignment Pending/Phy-AT Accept", "I&C Pending"],
  AT: ["PHY-AT RAISED/SOFT-AT PENDING", "PHY-AT REJECTED/SOFT-AT RAISED", "PHY-AT RAISED/SOFT-AT RAISED", "AT ACCEPTED", "PHY-AT PENDING/SOFT-AT ACCEPTED", "PHY-AT ACCEPTED/SOFT-AT PENDING", "PHY+SOFT AT PENDING", "PHY-AT ACCEPTED/SOFT-AT REJECTED", "PHY-AT PENDING/SOFT-AT RAISED", "PHY-AT RAISED/SOFT-AT REJECTED", "PHY-AT REJECTED/SOFT-AT REJECTED", "PHY-AT PENDING/SOFT-AT REJECTED", "PHY-AT REJECTED/SOFT-AT ACCEPTED", "PHY-AT ACCEPTED/SOFT-AT RAISED", "PHY-AT REJECTED/SOFT-AT PENDING", "PHY-AT RAISED/SOFT-AT ACCEPTED"],
  Traffic: ["TS Completed"],
  Cancel: ["Canceled", "Request for Cancellation", "Material Returned"],
  softUpgrade: ["Upgrade Pending", "Upgrade Completed"]
};
const statusSequence = Object.values(statusOptions).flat();
const statusIndexMap = {};
statusSequence.forEach((status, index) => {
  statusIndexMap[status] = index;
});

const TestForm = () => {
  const [showTable, setShowTable] = useState(false);
  const [showOtpDialog, setShowOtpDialog] = useState(false);
  const [otp, setOtp] = useState('');
  const [showErrorDialog, setShowErrorDialog] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [formData, setFormData] = useState({
    startPoint: '',
    endPoint: ''
  });
  const [pendingPayload, setPendingPayload] = useState(null);
  const navigate = useNavigate(); 
  const [testResults, setTestResults] = useState([]);
  let userId = localStorage.getItem('username');
  const [status, setStatus] = useState('idle');
  const [result, setResult] = useState(null);
  const [stats, setStats] = useState({
    successRate: 92,
    avgDuration: '45s',
    totalRuns: 128
  });
  
  if (!userId) {
    userId = 'Guest';
  }
 const handleShowHistory = () => {
    navigate('/testreports');
  };
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (formData.startPoint && formData.endPoint) {
      const startIndex = statusIndexMap[formData.startPoint];
      const endIndex = statusIndexMap[formData.endPoint];
      
      if (endIndex <= startIndex) {
        setErrorMessage('End Point must be after Start Point in the sequence');
        setShowErrorDialog(true);
        return;
      }
    }

    setStatus('loading');
    const doneBy="MW Planner";
    const username = localStorage.getItem('username');
    const userData = await api.post('/api/sampleUserCredentials/getUserCredentials', { doneBy, username });
              
    const payload = {
      startPoint: formData.startPoint,
      endPoint: formData.endPoint,
      user: username,
      username: userData.data[0],
      password: userData.data[1]
    };

    try {
      const response = await api.post('/api/midstests/login-mids', payload);
      if (response.data === "Otp test") {
        setPendingPayload(payload);
        setShowOtpDialog(true);
        setStatus('idle');
      } else {
        const response = await api.post('/api/midstests/mwDprPage', payload);
        if (response.data === 'Failed') {
          setErrorMessage('Login failed. Please try again.');
          setShowErrorDialog(true);
          setStatus('idle');
          return;
        }
        setResult({ success: true, message: response.data });
        setStatus('complete');
        setFormData({ startPoint: '', endPoint: '' });
      }
      console.log('Response from server:', response);
    } catch (error) {
      setStatus('error');
      setResult({
        success: false,
        message: error.response?.data || 'Unknown error occurred'
      });
      console.error("Error in test submission: ", error);
    }
  };

  const handleOtpSubmit = async () => {
    if (!otp.trim()) {
      setErrorMessage('Please enter a valid OTP.');
      setShowErrorDialog(true);
      return;
    }
    setStatus('loading');
    try {
      const response = await api.post('/api/midstests/mwDprPage', {
        ...pendingPayload,
        otp
      });
      setResult({ success: true, message: response.data });
      setStatus('complete');
      setShowOtpDialog(false);
      setOtp('');
      setPendingPayload(null);
      setFormData({ startPoint: '', endPoint: '' });
      console.log('OTP submission response:', response);
    } catch (error) {
      setErrorMessage(error.response?.data || 'Invalid OTP. Please try again.');
      setShowErrorDialog(true);
      setStatus('idle');
      console.error("Error in OTP submission: ", error);
    }
  };

  const closeOtpDialog = () => {
    setShowOtpDialog(false);
    setOtp('');
    setPendingPayload(null);
    setStatus('idle');
  };

  const closeErrorDialog = () => {
    setShowErrorDialog(false);
    setErrorMessage('');
  };

  const LoadingDialog = () => (
    <div className="mwdpr-loading-overlay">
      <div className="mwdpr-loading-content">
        <FaSpinner className="mwdpr-loading-spinner" />
        <p className="mwdpr-loading-text">Executing Tests...</p>
      </div>
    </div>
  );

  const ResultDialog = ({ success, message, onClose }) => (
    <div className="mwdpr-loading-overlay">
      <div className="mwdpr-result-content">
        {success ? (
          <FaCheckCircle className="mwdpr-result-icon mwdpr-success-icon" />
        ) : (
          <FaTimesCircle className="mwdpr-result-icon mwdpr-error-icon" />
        )}
        <p className="mwdpr-result-message">{message}</p>
        <button className="mwdpr-retry-button" onClick={onClose}>
          {success ? 'Close' : 'Retry'}
        </button>
      </div>
    </div>
  );

  return (
    <Layout>
      <div className="mwdpr-container">
        <div className="background-blobs">
          <div className="blob telecom-blob-1"></div>
          <div className="blob telecom-blob-2"></div>
          <div className="blob telecom-blob-3"></div>
        </div>
        
        <div className="mwdpr-main-content">
          <div className="mwdpr-header">
            <h1>Telecom DPR Track</h1>
            <p>Monitor and analyze deployment progress reports</p>
            
            <div className="stats-container">
              <div className="stat-card">
                <FiBarChart className="stat-icon" />
                <div>
                  <h3>Success Rate</h3>
                  <p>{stats.successRate}%</p>
                </div>
              </div>
              
              <div className="stat-card">
                <FiClock className="stat-icon" />
                <div>
                  <h3>Avg. Duration</h3>
                  <p>{stats.avgDuration}</p>
                </div>
              </div>
              
              <div className="stat-card">
                <FiTrendingUp className="stat-icon" />
                <div>
                  <h3>Total Runs</h3>
                  <p>{stats.totalRuns}</p>
                </div>
              </div>
            </div>
          </div>
          
          <div className="mwdpr-glass-card">
            <div className="card-header">
              <h2>DPR Sequence Validation</h2>
              <p>Select start and end points to validate deployment sequence</p>
            </div>
            
            <form onSubmit={handleSubmit}>
              <div className="mwdpr-scenario-grid">
                <div className="mwdpr-form-group">
                  <div className="mwdpr-input-header">
                    <span className="mwdpr-input-label">Start Point</span>
                    <div className="mwdpr-select-wrapper">
                      <select
                        value={formData.startPoint}
                        onChange={(e) => setFormData({ ...formData, startPoint: e.target.value })}
                        className="mwdpr-glass-select"
                        aria-label="Start Point"
                      >
                        <option value="">Select Start Point</option>
                        {Object.entries(statusOptions).map(([category, options]) => (
                          <optgroup key={category} label={category}>
                            {options.map((option, index) => (
                              <option key={index} value={option}>{option}</option>
                            ))}
                          </optgroup>
                        ))}
                      </select>
                      <div className="mwdpr-select-arrow"></div>
                    </div>
                  </div>
                </div>

                <div className="mwdpr-form-group">
                  <div className="mwdpr-input-header">
                    <span className="mwdpr-input-label">End Point</span>
                    <div className="mwdpr-select-wrapper">
                      <select
                        value={formData.endPoint}
                        onChange={(e) => setFormData({ ...formData, endPoint: e.target.value })}
                        className="mwdpr-glass-select"
                        aria-label="End Point"
                      >
                        <option value="">Select End Point</option>
                        {Object.entries(statusOptions).map(([category, options]) => (
                          <optgroup key={category} label={category}>
                            {options.map((option, index) => (
                              <option key={index} value={option}>{option}</option>
                            ))}
                          </optgroup>
                        ))}
                      </select>
                      <div className="mwdpr-select-arrow"></div>
                    </div>
                  </div>
                </div>
              </div>

              <button type="submit" className="mwdpr-gradient-button" disabled={status === 'loading'}>
                Validate DPR Sequence
                <div className="mwdpr-button-hover-effect"></div>
              </button>
            </form>
          </div>

          <div className="sequence-info">
            <div className="sequence-header">
              <h3>Deployment Sequence Flow</h3>
              <p>Status must progress from top to bottom</p>
            </div>
            
            <div className="sequence-flow">
              {Object.entries(statusOptions).map(([category, options]) => (
                <div key={category} className="sequence-category">
                  <div className="category-header">{category}</div>
                  <div className="status-sequence">
                    {options.map((status, idx) => (
                      <div 
                        key={status} 
                        className={`status-item ${formData.startPoint === status ? 'selected-start' : ''} ${formData.endPoint === status ? 'selected-end' : ''}`}
                      >
                        <div className="status-bullet"></div>
                        <div className="status-name">{status}</div>
                      </div>
                    ))}
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Loading, OTP, and Error dialogs would go here */}
        
 {status === 'loading' && <LoadingDialog />}
        {status === 'complete' && (
          <ResultDialog
            success={true}
            message={result.message}
            onClose={() => setStatus('idle')}
          />
        )}
        {status === 'error' && (
          <ResultDialog
            success={false}
            message="An error occurred. Please try again."
            onClose={() => setStatus('idle')}
          />
        )}

        {showOtpDialog && (
          <div className="mwdpr-dialog-overlay" onClick={closeOtpDialog}>
            <div
              className="mwdpr-dialog-box mwdpr-slide-in"
              onClick={(e) => e.stopPropagation()}
              role="dialog"
              aria-labelledby="otp-dialog-title"
            >
              <div className="mwdpr-dialog-header">
                <h2 id="otp-dialog-title">Enter OTP</h2>
                <button
                  onClick={closeOtpDialog}
                  className="mwdpr-close-btn"
                  aria-label="Close OTP dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="mwdpr-dialog-content">
                <p>Please enter the OTP sent to your registered email or phone.</p>
                <input
                  type="text"
                  value={otp}
                  onChange={(e) => setOtp(e.target.value)}
                  placeholder="Enter OTP"
                  className="mwdpr-otp-input"
                  aria-label="OTP input"
                  autoFocus
                  onKeyPress={(e) => e.key === 'Enter' && handleOtpSubmit()}
                />
                <div className="mwdpr-dialog-buttons">
                  <button
                    onClick={handleOtpSubmit}
                    className="mwdpr-submit-btn"
                    aria-label="Submit OTP"
                  >
                    <FiCheck /> Submit
                  </button>
                  <button
                    onClick={closeOtpDialog}
                    className="mwdpr-cancel-btn"
                    aria-label="Cancel"
                  >
                    <FiX /> Cancel
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

        {showErrorDialog && (
          <div className="mwdpr-dialog-overlay" onClick={closeErrorDialog}>
            <div
              className="mwdpr-dialog-box mwdpr-slide-in"
              onClick={(e) => e.stopPropagation()}
              role="alertdialog"
              aria-labelledby="error-dialog-title"
            >
              <div className="mwdpr-dialog-header">
                <h2 id="error-dialog-title">Error</h2>
                <button
                  onClick={closeErrorDialog}
                  className="mwdpr-close-btn"
                  aria-label="Close error dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="mwdpr-dialog-content">
                <div className="mwdpr-error-icon">
                  <FiAlertCircle size={40} />
                </div>
                <p>{errorMessage}</p>
                <div className="mwdpr-dialog-buttons">
                  <button
                    onClick={closeErrorDialog}
                    className="mwdpr-close-error-btn"
                    aria-label="Close error"
                    autoFocus
                    onKeyPress={(e) => e.key === 'Enter' && closeErrorDialog()}
                  >
                    <FiX /> Close
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}
        
      </div>
    </Layout>
  );
};

const MWDPRPage = () => {
  return (
    <div className="mwdpr-page-container">
      <TestForm />
    </div>
  );
};

export default MWDPRPage;

