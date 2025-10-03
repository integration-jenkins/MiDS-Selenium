import React, { useState } from 'react';
import '../css/MWDpr.css';
import { FaTable, FaToggleOn } from 'react-icons/fa';
import api from '../api/axiosConfig';
import { useNavigate } from 'react-router-dom';
import { FaCheckCircle, FaTimesCircle, FaSpinner } from 'react-icons/fa';
import Layout from "../components/Layout";
import { FiX, FiCheck, FiAlertCircle } from 'react-icons/fi';

const statusOptions = {
  SR_RFAI: ["SR Pending", "SP Pending", "SO Pending", "RFAI Pending"],
  Material: ["MO Pending", "Material Delivery Pending"],
  INC: ["HOP Alignment Pending/Phy-AT Pending", "HOP Alignment Pending/Phy-AT Raised", "HOP Alignment Pending/Phy-AT Rejected", "HOP Alignment Pending/Phy-AT Accept", "I&C Pending"],
  AT: ["PHY-AT RAISED/SOFT-AT PENDING", "PHY-AT REJECTED/SOFT-AT RAISED", "PHY-AT RAISED/SOFT-AT RAISED", "AT ACCEPTED", "PHY-AT PENDING/SOFT-AT ACCEPTED", "PHY-AT ACCEPTED/SOFT-AT PENDING", "PHY+SOFT AT PENDING", "PHY-AT ACCEPTED/SOFT-AT REJECTED", "PHY-AT PENDING/SOFT-AT RAISED", "PHY-AT RAISED/SOFT-AT REJECTED", "PHY-AT REJECTED/SOFT-AT REJECTED", "PHY-AT PENDING/SOFT-AT REJECTED", "PHY-AT REJECTED/SOFT-AT ACCEPTED", "PHY-AT ACCEPTED/SOFT-AT RAISED", "PHY-AT REJECTED/SOFT-AT PENDING", "PHY-AT RAISED/SOFT-AT ACCEPTED"],
  Traffic: ["TS Completed"],
  Cancel: ["Canceled", "Request for Cancellation", "Material Returned"],
  softUpgrade: ["Upgrade Pending", "Upgrade Completed"]
};

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
  const handleShowHistory = () => {
    navigate('/testreports');
  };
  const [testResults, setTestResults] = useState([]);
  let userId = localStorage.getItem('username');
  const [status, setStatus] = useState('idle');
  const [result, setResult] = useState(null);
  if (!userId) {
    userId = 'Guest';
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
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
    <Layout title="DPR Track">
      <div className="mwdpr-container">
        <div className="background-blobs"></div>
        <div className="mwdpr-main-content">
          <div className="mwdpr-glass-card">
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
                Run Test Scenario
                <div className="mwdpr-button-hover-effect"></div>
              </button>
            </form>
          </div>

          {/* {showTable && (
            <div className="glass-card results-table">
              <div className="table-header">
                <h3>Test History</h3>
                <span className="results-count">{testResults.length} results</span>
              </div>
              <div className="table-container">
                <table>
                  <tbody>
                    {testResults.map((test) => (
                      <tr key={test.id}>
                        <td>{test.id}</td>
                        <td>{test.startPoint}</td>
                        <td>{test.endPoint}</td>
                        <td>{test.executionTime}</td>
                        <td>
                          <span className={`status-chip ${test.status.toLowerCase()}`}>
                            {test.status}
                          </span>
                        </td>
                        <td>{test.date}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )} */}
        </div>

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