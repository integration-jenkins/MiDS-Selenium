import React, { useState } from 'react';
import { FaHistory, FaCogs, FaFlask, FaClipboardList, FaDownload } from 'react-icons/fa';
import '../css/TestingPage.css';
import '../css/BasicTestDialogs.css';
import { FaTable, FaToggleOn } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';
import SimpleRingLoader from './AdvanceLoader';
import Layout from "../components/Layout";
import { FiX, FiCheck, FiAlertCircle } from 'react-icons/fi';

const BasicTestPage = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [showOtpDialog, setShowOtpDialog] = useState(false);
  const [otp, setOtp] = useState('');
  const [testName, setTestName] = useState('');
  const [showErrorDialog, setShowErrorDialog] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleBasicPageRenderTest = async () => {
    try {
      setLoading(true);
      const testNaam = 'Page Render Test';
      setTestName(testNaam);
      const username = localStorage.getItem('username');
      const payload = { user: username,testName:testNaam };
      const response = await api.post('/api/basic-report/login', payload);
      console.log('Page test response:', response.data);
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
      console.log("otp",otp);
      const payload = { user: username, otp:otp };
      
      let response ;
      console.log("ok ",testName);
      if(testName === 'Page Render Test'){
        console.log("response"); 
        response= await api.post('/api/basic-report/otp-check', payload);
      }else if(testName === 'Download Report Test'){
        response= await api.post('/api/basic-report/report-download', payload);
      } 
          

      console.log('OTP check response:', response.data);
      if (response.data === 'Failed') {
        setErrorMessage('Invalid OTP. Please try again.');
        setShowErrorDialog(true);
      } else if(response.data === 'Success'){ 
        setShowOtpDialog(false);
        setOtp('');
        if(testName === 'Page Render Test'){
          navigate('/reports/page-render-report');
        }else if(testName === 'Download Report Test'){
          navigate('/reports/download-report');
        }
      }else{
        if(testName === 'Page Render Test'){
          navigate('/reports/page-render-report');
        }else if(testName === 'Download Report Test'){
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
      const payload = { user: username, testName:testName };
      const response = await api.post('/api/basic-report/login', payload);
      console.log('Download report response:', response.data);
      if (response.data === 'Failed') {
        setErrorMessage('Login failed. Please try again.');
        setShowErrorDialog(true);
      }else{
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
    <Layout title="Basic Test">
      {loading && <SimpleRingLoader />}
      <div className="background-blobs"></div>
      <div className="main-content">
        <div className="testing-categories">
          <div className="glass-card testing-card">
            <div className="card-header">
              <FaFlask className="card-icon" />
              <h3>Page Rendering Test</h3>
            </div>
            <div className="card-content">
              <div className="stat-item">
                <span>Last Run:</span>
                <span className="stat-value">2h ago</span>
              </div>
              <div className="stat-item">
                <span>Success Rate:</span>
                <span className="stat-value success">98%</span>
              </div>
              <button className="action-button" onClick={handleBasicPageRenderTest}>
                Run Page Render Tests
              </button>
            </div>
          </div>

          <div className="glass-card testing-card">
            <div className="card-header">
              <FaCogs className="card-icon" />
              <h3>Download Report Test</h3>
            </div>
            <div className="card-content">
              <div className="stat-item">
                <span>Last Run:</span>
                <span className="stat-value">2h ago</span>
              </div>
              <div className="stat-item">
                <span>Success Rate:</span>
                <span className="stat-value success">98%</span>
              </div>
              <button className="action-button" onClick={handleBasicDownloadReportTest}>
                Run Download Report Test
              </button>
            </div>
          </div>
        </div>
      </div>

      {showOtpDialog && (
        <div className="basictest-dialog-overlay" onClick={closeOtpDialog}>
          <div
            className="basictest-dialog-box basictest-slide-in"
            onClick={(e) => e.stopPropagation()}
            role="dialog"
            aria-labelledby="otp-dialog-title"
          >
            <div className="basictest-dialog-header">
              <h2 id="otp-dialog-title">Enter OTP</h2>
              <button
                onClick={closeOtpDialog}
                className="basictest-close-btn"
                aria-label="Close OTP dialog"
              >
                <FiX size={24} />
              </button>
            </div>
            <div className="basictest-dialog-content">
              <p>Please enter the OTP sent to your registered email or phone.</p>
              <input
                type="text"
                value={otp}
                onChange={(e) => setOtp(e.target.value)}
                placeholder="Enter OTP"
                className="basictest-otp-input"
                aria-label="OTP input"
                autoFocus
                onKeyPress={(e) => e.key === 'Enter' && handleOtpSubmit()}
              />
              <div className="basictest-dialog-buttons">
                <button
                  onClick={handleOtpSubmit}
                  className="basictest-submit-btn"
                  aria-label="Submit OTP"
                >
                  <FiCheck /> Submit
                </button>
                <button
                  onClick={closeOtpDialog}
                  className="basictest-cancel-btn"
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
        <div className="basictest-dialog-overlay" onClick={closeErrorDialog}>
          <div
            className="basictest-dialog-box basictest-slide-in"
            onClick={(e) => e.stopPropagation()}
            role="alertdialog"
            aria-labelledby="error-dialog-title"
          >
            <div className="basictest-dialog-header">
              <h2 id="error-dialog-title">Error</h2>
              <button
                onClick={closeErrorDialog}
                className="basictest-close-btn"
                aria-label="Close error dialog"
              >
                <FiX size={24} />
              </button>
            </div>
            <div className="basictest-dialog-content">
              <div className="basictest-error-icon">
                <FiAlertCircle size={40} />
              </div>
              <p>{errorMessage}</p>
              <div className="basictest-dialog-buttons">
                <button
                  onClick={closeErrorDialog}
                  className="basictest-close-error-btn"
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
    </Layout>
  );
};

export default BasicTestPage;