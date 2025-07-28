import React, { useState, useContext } from 'react';
import { FaHistory, FaCogs, FaFlask, FaClipboardList, FaDownload } from 'react-icons/fa';
import { FiX, FiCheck, FiAlertCircle, FiClock, FiBarChart2, FiUser, FiShield, FiCalendar, FiSun, FiMoon } from 'react-icons/fi';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';
import SimpleRingLoader from './AdvanceLoader';
import Layout from "../components/Layout";
import { ThemeContext } from '../context/ThemeContext'; // Import ThemeContext
import '../css/BasicTest.css';

const BasicTest = () => {
  const { theme, toggleTheme } = useContext(ThemeContext); // Use ThemeContext
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [showOtpDialog, setShowOtpDialog] = useState(false);
  const [otp, setOtp] = useState('');
  const [testName, setTestName] = useState('');
  const [showErrorDialog, setShowErrorDialog] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [activeTab, setActiveTab] = useState('all');

  // Mock data for test statistics
  const [testStats] = useState({
    pageRender: {
      totalRuns: 128,
      successRate: 98.4,
      avgDuration: "2.4s",
      lastRun: "2 hours ago"
    },
    downloadReport: {
      totalRuns: 92,
      successRate: 96.7,
      avgDuration: "4.1s",
      lastRun: "3 hours ago"
    },
    overall: {
      totalTests: 420,
      successRate: 97.8,
      failureRate: 2.2
    }
  });

  const [recentTests] = useState([
    { id: 1, name: "Page Render Test", status: "success", time: "12:45 PM", date: "Today" },
    { id: 2, name: "Download Report Test", status: "success", time: "11:30 AM", date: "Today" },
    { id: 3, name: "Page Render Test", status: "failed", time: "10:15 AM", date: "Today" },
    { id: 4, name: "Download Report Test", status: "success", time: "9:00 AM", date: "Today" },
    { id: 5, name: "Page Render Test", status: "success", time: "4:30 PM", date: "Yesterday" }
  ]);

  const handleBasicPageRenderTest = async () => {
    try {
      setLoading(true);
      const testNaam = 'Page Render Test';
      setTestName(testNaam);
      const username = localStorage.getItem('username');
      const payload = { user: username, testName: testNaam };
      const response = await api.post('/api/basic-report/login', payload);
      if (response.data === 'Failed') {
        setErrorMessage('Login failed. Please try again.');
        setShowErrorDialog(true);
      } else {
        setShowOtpDialog(true);
      }
    } catch (err) {
      setErrorMessage(err.message || 'Failed to perform login action');
      setShowErrorDialog(true);
    } finally {
      setLoading(false);
    }
  };

  const handleOtpSubmit = async () => {
    if (!otp.trim()) {
      setErrorMessage('Please enter a valid OTP.');
      setShowErrorDialog(true);
      return;
    }
    try {
      setLoading(true);
      const username = localStorage.getItem('username');
      const payload = { user: username, otp: otp };
      
      let response;
      if (testName === 'Page Render Test') {
        response = await api.post('/api/basic-report/otp-check', payload);
      } else if (testName === 'Download Report Test') {
        response = await api.post('/api/basic-report/report-download', payload);
      }

      if (response.data === 'Failed') {
        setErrorMessage('Invalid OTP. Please try again.');
        setShowErrorDialog(true);
      } else if (response.data === 'Success') {
        setShowOtpDialog(false);
        setOtp('');
        if (testName === 'Page Render Test') {
          navigate('/reports/page-render-report');
        } else if (testName === 'Download Report Test') {
          navigate('/reports/download-report');
        }
      } else {
        if (testName === 'Page Render Test') {
          navigate('/reports/page-render-report');
        } else if (testName === 'Download Report Test') {
          navigate('/reports/download-report');
        }
      }
    } catch (err) {
      setErrorMessage(err.message || 'Failed to verify OTP');
      setShowErrorDialog(true);
    } finally {
      setLoading(false);
    }
  };

  const handleBasicDownloadReportTest = async () => {
    try {
      setLoading(true);
      const testName = 'Download Report Test';
      setTestName(testName);
      const username = localStorage.getItem('username');
      const payload = { user: username, testName: testName };
      const response = await api.post('/api/basic-report/login', payload);
      if (response.data === 'Failed') {
        setErrorMessage('Login failed. Please try again.');
        setShowErrorDialog(true);
      } else {
        setShowOtpDialog(true);
      }
    } catch (err) {
      setErrorMessage(err.message || 'Failed to perform download action');
      setShowErrorDialog(true);
    } finally {
      setLoading(false);
    }
  };

  const closeOtpDialog = () => {
    setShowOtpDialog(false);
    setOtp('');
  };

  const closeErrorDialog = () => {
    setShowErrorDialog(false);
    setErrorMessage('');
  };

  return (
    <Layout>
      {loading && <SimpleRingLoader />}
      <div className="basic-test-page">

        {/* Background elements */}
        <div className="background-blobs">
          <div className="blob blob-1"></div>
          <div className="blob blob-2"></div>
          <div className="blob blob-3"></div>
        </div>
        
        <div className="main-content">
          <div className="page-header">
            <h1>Quality Assurance Dashboard</h1>
            <p>Monitor and execute critical tests to ensure application stability</p>
          </div>
          
          {/* Statistics Cards */}
          <div className="stats-grid">
            <div className="stat-card">
              <div className="stat-icon">
                <FiBarChart2 />
              </div>
              <div className="stat-content">
                <h3>Total Tests</h3>
                <span className="stat-value">{testStats.overall.totalTests}</span>
              </div>
            </div>
            
            <div className="stat-card">
              <div className="stat-icon success">
                <FiCheck />
              </div>
              <div className="stat-content">
                <h3>Success Rate</h3>
                <span className="stat-value">{testStats.overall.successRate}%</span>
              </div>
            </div>
            
            <div className="stat-card">
              <div className="stat-icon warning">
                <FiAlertCircle />
              </div>
              <div className="stat-content">
                <h3>Failure Rate</h3>
                <span className="stat-value">{testStats.overall.failureRate}%</span>
              </div>
            </div>
            
            <div className="stat-card">
              <div className="stat-icon">
                <FiUser />
              </div>
              <div className="stat-content">
                <h3>Active Testers</h3>
                <span className="stat-value">24</span>
              </div>
            </div>
          </div>
          
          <div className="content-columns">
            {/* Left column - Test Cards */}
            <div className="testing-column">
              <div className="section-header">
                <h2>Test Suites</h2>
                <p>Execute critical tests to validate core functionality</p>
              </div>
              
              <div className="testing-cards">
                <div className="glass-card testing-card">
                  <div className="card-header">
                    <div className="card-icon">
                      <FaFlask />
                    </div>
                    <h3>Page Rendering Test</h3>
                  </div>
                  <div className="card-content">
                    <div className="stat-item">
                      <FiClock />
                      <span>Last Run:</span>
                      <span className="stat-value">{testStats.pageRender.lastRun}</span>
                    </div>
                    <div className="stat-item">
                      <FiBarChart2 />
                      <span>Success Rate:</span>
                      <span className="stat-value success">{testStats.pageRender.successRate}%</span>
                    </div>
                    <div className="stat-item">
                      <FiShield />
                      <span>Total Runs:</span>
                      <span className="stat-value">{testStats.pageRender.totalRuns}</span>
                    </div>
                    <button className="action-button" onClick={handleBasicPageRenderTest}>
                      Run Page Render Tests
                    </button>
                  </div>
                </div>

                <div className="glass-card testing-card">
                  <div className="card-header">
                    <div className="card-icon">
                      <FaDownload />
                    </div>
                    <h3>Download Report Test</h3>
                  </div>
                  <div className="card-content">
                    <div className="stat-item">
                      <FiClock />
                      <span>Last Run:</span>
                      <span className="stat-value">{testStats.downloadReport.lastRun}</span>
                    </div>
                    <div className="stat-item">
                      <FiBarChart2 />
                      <span>Success Rate:</span>
                      <span className="stat-value success">{testStats.downloadReport.successRate}%</span>
                    </div>
                    <div className="stat-item">
                      <FiShield />
                      <span>Total Runs:</span>
                      <span className="stat-value">{testStats.downloadReport.totalRuns}</span>
                    </div>
                    <button className="action-button" onClick={handleBasicDownloadReportTest}>
                      Run Download Report Test
                    </button>
                  </div>
                </div>
                
                <div className="glass-card testing-card">
                  <div className="card-header">
                    <div className="card-icon">
                      <FaClipboardList />
                    </div>
                    <h3>Coming Soon</h3>
                  </div>
                  <div className="card-content">
                    <p>Additional test suites will be added in future releases</p>
                    <button className="action-button disabled" disabled>
                      Coming Soon
                    </button>
                  </div>
                </div>
              </div>
            </div>
            
            {/* Right column - Recent Activity */}
            <div className="activity-column">
              <div className="section-header">
                <h2>Recent Test Activity</h2>
                <div className="tabs">
                  <button 
                    className={`tab ${activeTab === 'all' ? 'active' : ''}`}
                    onClick={() => setActiveTab('all')}
                  >
                    All Tests
                  </button>
                  <button 
                    className={`tab ${activeTab === 'success' ? 'active' : ''}`}
                    onClick={() => setActiveTab('success')}
                  >
                    Successful
                  </button>
                  <button 
                    className={`tab ${activeTab === 'failed' ? 'active' : ''}`}
                    onClick={() => setActiveTab('failed')}
                  >
                    Failed
                  </button>
                </div>
              </div>
              
              <div className="activity-list">
                {recentTests
                  .filter(test => activeTab === 'all' || 
                          (activeTab === 'success' && test.status === 'success') || 
                          (activeTab === 'failed' && test.status === 'failed'))
                  .map(test => (
                    <div key={test.id} className="activity-item">
                      <div className={`status-indicator ${test.status}`}></div>
                      <div className="activity-details">
                        <h4>{test.name}</h4>
                        <div className="activity-meta">
                          <span className="time">
                            <FiClock /> {test.time}
                          </span>
                          <span className="date">
                            <FiCalendar /> {test.date}
                          </span>
                        </div>
                      </div>
                      <div className={`status-badge ${test.status}`}>
                        {test.status === 'success' ? 'Success' : 'Failed'}
                      </div>
                    </div>
                  ))}
              </div>
              
              <div className="insights-section">
                <div className="section-header">
                  <h2>Testing Insights</h2>
                </div>
                <div className="insights-content">
                  <div className="insight-card">
                    <h4>Performance Trends</h4>
                    <div className="progress-bar">
                      <div className="progress-fill" style={{ width: '85%' }}></div>
                      <span>85% Success Rate (Last 7 Days)</span>
                    </div>
                  </div>
                  <div className="insight-card">
                    <h4>Common Failure Points</h4>
                    <ul>
                      <li>Network latency issues</li>
                      <li>Third-party API timeouts</li>
                      <li>Large report generation</li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* OTP Dialog */}
        {showOtpDialog && (
          <div className="dialog-overlay" onClick={closeOtpDialog}>
            <div
              className="dialog-box slide-in"
              onClick={(e) => e.stopPropagation()}
              role="dialog"
              aria-labelledby="otp-dialog-title"
            >
              <div className="dialog-header">
                <h2 id="otp-dialog-title">Security Verification</h2>
                <button
                  onClick={closeOtpDialog}
                  className="close-btn"
                  aria-label="Close OTP dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="dialog-content">
                <div className="otp-illustration">
                  <div className="phone-icon">📱</div>
                  <div className="otp-message">
                    <p>We've sent a verification code to your registered device</p>
                  </div>
                </div>
                <input
                  type="text"
                  value={otp}
                  onChange={(e) => setOtp(e.target.value)}
                  placeholder="Enter 6-digit code"
                  className="otp-input"
                  aria-label="OTP input"
                  autoFocus
                  onKeyPress={(e) => e.key === 'Enter' && handleOtpSubmit()}
                />
                <div className="dialog-buttons">
                  <button
                    onClick={handleOtpSubmit}
                    className="submit-btn"
                    aria-label="Submit OTP"
                  >
                    <FiCheck /> Verify & Continue
                  </button>
                  <button
                    onClick={closeOtpDialog}
                    className="cancel-btn"
                    aria-label="Cancel"
                  >
                    <FiX /> Cancel
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Error Dialog */}
        {showErrorDialog && (
          <div className="dialog-overlay" onClick={closeErrorDialog}>
            <div
              className="dialog-box slide-in"
              onClick={(e) => e.stopPropagation()}
              role="alertdialog"
              aria-labelledby="error-dialog-title"
            >
              <div className="dialog-header">
                <h2 id="error-dialog-title">Operation Failed</h2>
                <button
                  onClick={closeErrorDialog}
                  className="close-btn"
                  aria-label="Close error dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="dialog-content">
                <div className="error-icon">
                  <FiAlertCircle size={40} />
                </div>
                <p>{errorMessage}</p>
                <div className="dialog-buttons">
                  <button
                    onClick={closeErrorDialog}
                    className="close-error-btn"
                    aria-label="Close error"
                    autoFocus
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

export default BasicTest;