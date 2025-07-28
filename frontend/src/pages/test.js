import React, { useState, useContext } from 'react';
import { FaFlask, FaDownload } from 'react-icons/fa';
import { FiX, FiCheck, FiAlertCircle } from 'react-icons/fi';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import api from '../api/axiosConfig';
import Layout from '../components/Layout';
import { TaskContext } from '../context/TaskContext';
import '../css/TestingPage.css';


const BasicTestPage = () => {
  const navigate = useNavigate();
  const { addTask, removeTask } = useContext(TaskContext);
  const [showOtpDialog, setShowOtpDialog] = useState(false);
  const [otp, setOtp] = useState('');
  const [showErrorDialog, setShowErrorDialog] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [testName, setTestName] = useState('');

  const handleBasicPageRenderTest = async () => {
    const source = axios.CancelToken.source();
    const taskId = `page-render-${Date.now()}`;
    addTask('Page Render Test', source);
    try {
      const testNaam = 'Page Render Test';
      setTestName(testNaam);
      const username = localStorage.getItem('username');
      const payload = { user: username, testName: testNaam };
      const response = await api.post('/api/basic-report/login', payload, {
        cancelToken: source.token,
      });
      console.log('Page test response:', response.data);
      if (response.data === 'Failed') {
        setErrorMessage('Login failed. Please try again.');
        setShowErrorDialog(true);
      } else {
        setShowOtpDialog(true);
      }
    } catch (err) {
      if (axios.isCancel(err)) {
        console.log('Page Render Test cancelled');
      } else {
        setErrorMessage(err.message || 'Failed to perform login action');
        setShowErrorDialog(true);
      }
    } finally {
      removeTask(taskId);
    }
  };

  const handleOtpSubmit = async () => {
    if (!otp.trim()) {
      setErrorMessage('Please enter a valid OTP.');
      setShowErrorDialog(true);
      return;
    }
    const source = axios.CancelToken.source();
    const taskId = `otp-submit-${Date.now()}`;
    addTask('OTP Verification', source);
    try {
      const username = localStorage.getItem('username');
      const payload = { user: username, otp: otp };
      let response;
      if (testName === 'Page Render Test') {
        response = await api.post('/api/basic-report/otp-check', payload, {
          cancelToken: source.token,
        });
      } else if (testName === 'Download Report Test') {
        response = await api.post('/api/basic-report/report-download', payload, {
          cancelToken: source.token,
        });
      }
      console.log('OTP check response:', response.data);
      if (response.data === 'Failed') {
        setErrorMessage('Invalid OTP. Please try again.');
        setShowErrorDialog(true);
      } else {
        setShowOtpDialog(false);
        setOtp('');
        if (testName === 'Page Render Test') {
          navigate('/reports/page-render-report');
        } else if (testName === 'Download Report Test') {
          navigate('/reports/download-report');
        }
      }
    } catch (err) {
      if (axios.isCancel(err)) {
        console.log('OTP Verification cancelled');
      } else {
        setErrorMessage(err.message || 'Failed to verify OTP');
        setShowErrorDialog(true);
      }
    } finally {
      removeTask(taskId);
    }
  };

  const handleBasicDownloadReportTest = async () => {
    const source = axios.CancelToken.source();
    const taskId = `download-report-${Date.now()}`;
    addTask('Download Report Test', source);
    try {
      const testNaam = 'Download Report Test';
      setTestName(testNaam);
      const username = localStorage.getItem('username');
      const payload = { user: username, testName: testNaam };
      const response = await api.post('/api/basic-report/login', payload, {
        cancelToken: source.token,
      });
      console.log('Download report response:', response.data);
      if (response.data === 'Failed') {
        setErrorMessage('Login failed. Please try again.');
        setShowErrorDialog(true);
      } else {
        setShowOtpDialog(true);
      }
    } catch (err) {
      if (axios.isCancel(err)) {
        console.log('Download Report Test cancelled');
      } else {
        setErrorMessage(err.message || 'Failed to perform download action');
        setShowErrorDialog(true);
      }
    } finally {
      removeTask(taskId);
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
      <div className="test-background-gradient"></div>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="test-card bg-white dark:bg-gray-800 rounded-2xl shadow-lg p-6 transform hover:scale-105 transition-transform duration-300">
            <div className="flex items-center space-x-4">
              <div className="p-3 bg-blue-100 dark:bg-blue-900 rounded-full">
                <FaFlask className="text-blue-600 dark:text-blue-300 text-2xl" />
              </div>
              <h3 className="text-xl font-semibold text-gray-900 dark:text-gray-100">Page Rendering Test</h3>
            </div>
            <div className="mt-4 space-y-3">
              <div className="flex justify-between text-sm text-gray-600 dark:text-gray-300">
                <span>Last Run:</span>
                <span>2h ago</span>
              </div>
              <div className="flex justify-between text-sm">
                <span>Success Rate:</span>
                <span className="text-green-500 font-medium">98%</span>
              </div>
              <button
                onClick={handleBasicPageRenderTest}
                className="w-full bg-gradient-to-r from-blue-500 to-purple-600 text-white py-2 rounded-lg font-medium hover:from-blue-600 hover:to-purple-700 transition-colors duration-300 flex items-center justify-center space-x-2"
              >
                <FaFlask />
                <span>Run Page Render Tests</span>
              </button>
            </div>
          </div>

          <div className="test-card bg-white dark:bg-gray-800 rounded-2xl shadow-lg p-6 transform hover:scale-105 transition-transform duration-300">
            <div className="flex items-center space-x-4">
              <div className="p-3 bg-teal-100 dark:bg-teal-900 rounded-full">
                <FaDownload className="text-teal-600 dark:text-teal-300 text-2xl" />
              </div>
              <h3 className="text-xl font-semibold text-gray-900 dark:text-gray-100">Download Report Test</h3>
            </div>
            <div className="mt-4 space-y-3">
              <div className="flex justify-between text-sm text-gray-600 dark:text-gray-300">
                <span>Last Run:</span>
                <span>2h ago</span>
              </div>
              <div className="flex justify-between text-sm">
                <span>Success Rate:</span>
                <span className="text-green-500 font-medium">98%</span>
              </div>
              <button
                onClick={handleBasicDownloadReportTest}
                className="w-full bg-gradient-to-r from-teal-500 to-cyan-600 text-white py-2 rounded-lg font-medium hover:from-teal-600 hover:to-cyan-700 transition-colors duration-300 flex items-center justify-center space-x-2"
              >
                <FaDownload />
                <span>Run Download Report Test</span>
              </button>
            </div>
          </div>
        </div>

        {showOtpDialog && (
          <div className="test-dialog-overlay" onClick={closeOtpDialog}>
            <div
              className="test-dialog-box bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-6 max-w-md w-full transform scale-95 animate-scale-in"
              onClick={(e) => e.stopPropagation()}
              role="dialog"
              aria-labelledby="otp-dialog-title"
            >
              <div className="flex justify-between items-center mb-4">
                <h2 id="otp-dialog-title" className="text-lg font-semibold text-gray-900 dark:text-gray-100">Enter OTP</h2>
                <button
                  onClick={closeOtpDialog}
                  className="text-gray-500 dark:text-gray-300 hover:text-gray-700 dark:hover:text-gray-100"
                  aria-label="Close OTP dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="space-y-4">
                <p className="text-sm text-gray-600 dark:text-gray-300">Please enter the OTP sent to your registered email or phone.</p>
                <input
                  type="text"
                  value={otp}
                  onChange={(e) => setOtp(e.target.value)}
                  placeholder="Enter OTP"
                  className="w-full p-3 rounded-lg border border-gray-300 dark:border-gray-600 bg-gray-50 dark:bg-gray-700 text-gray-900 dark:text-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  aria-label="OTP input"
                  autoFocus
                  onKeyPress={(e) => e.key === 'Enter' && handleOtpSubmit()}
                />
                <div className="flex space-x-3">
                  <button
                    onClick={handleOtpSubmit}
                    className="flex-1 bg-gradient-to-r from-blue-500 to-purple-600 text-white py-2 rounded-lg font-medium hover:from-blue-600 hover:to-purple-700 transition-colors duration-300 flex items-center justify-center space-x-2"
                    aria-label="Submit OTP"
                  >
                    <FiCheck />
                    <span>Submit</span>
                  </button>
                  <button
                    onClick={closeOtpDialog}
                    className="flex-1 bg-gray-200 dark:bg-gray-600 text-gray-700 dark:text-gray-200 py-2 rounded-lg font-medium hover:bg-gray-300 dark:hover:bg-gray-500 transition-colors duration-300 flex items-center justify-center space-x-2"
                    aria-label="Cancel"
                  >
                    <FiX />
                    <span>Cancel</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

        {showErrorDialog && (
          <div className="test-dialog-overlay" onClick={closeErrorDialog}>
            <div
              className="test-dialog-box bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-6 max-w-md w-full transform scale-95 animate-scale-in"
              onClick={(e) => e.stopPropagation()}
              role="alertdialog"
              aria-labelledby="error-dialog-title"
            >
              <div className="flex justify-between items-center mb-4">
                <h2 id="error-dialog-title" className="text-lg font-semibold text-gray-900 dark:text-gray-100">Error</h2>
                <button
                  onClick={closeErrorDialog}
                  className="text-gray-500 dark:text-gray-300 hover:text-gray-700 dark:hover:text-gray-100"
                  aria-label="Close error dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="space-y-4">
                <div className="flex justify-center">
                  <FiAlertCircle size={40} className="text-red-500" />
                </div>
                <p className="text-sm text-gray-600 dark:text-gray-300 text-center">{errorMessage}</p>
                <button
                  onClick={closeErrorDialog}
                  className="w-full bg-gray-200 dark:bg-gray-600 text-gray-700 dark:text-gray-200 py-2 rounded-lg font-medium hover:bg-gray-300 dark:hover:bg-gray-500 transition-colors duration-300 flex items-center justify-center space-x-2"
                  aria-label="Close error"
                  autoFocus
                  onKeyPress={(e) => e.key === 'Enter' && closeErrorDialog()}
                >
                  <FiX />
                  <span>Close</span>
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </Layout>
  );
};

export default BasicTestPage;