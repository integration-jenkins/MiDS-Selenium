import React, { useState, useEffect } from 'react';
import { FiCheck, FiPlay, FiSettings, FiBox } from 'react-icons/fi';
import { MdAutoMode, MdOutlineComputer } from 'react-icons/md';
import '../css/MidsTest.css';
import Layout from "../components/Layout";
import api from '../api/axiosConfig'; 
import { FiX, FiAlertCircle } from 'react-icons/fi';

const MidsTest = () => {
  const [selectedTests, setSelectedTests] = useState([]);
  const [showModeDialog, setShowModeDialog] = useState(false);
  const [showExecutionDialog, setShowExecutionDialog] = useState(false);
  const [executionMode, setExecutionMode] = useState('');
  const [testData, setTestData] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [testExecutionTypes, setTestExecutionTypes] = useState({});
  const [showOtpDialog, setShowOtpDialog] = useState(false);
  const [otp, setOtp] = useState('');
  const [showErrorDialog, setShowErrorDialog] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [pendingExecution, setPendingExecution] = useState(null);

  const [manualExecutionState, setManualExecutionState] = useState({
    active: false,
    currentTestIndex: 0,
    tests: [],
    results: {},
    showManualDialog: false,
  });
  const [isDialogOpen, setIsDialogOpen] = useState(false);
    const [formData, setFormData] = useState({
        testName: "",
  priority: "",
  pageName: "",
  successfulCount: 0,
  failedCount: 0,
  lastExecutionUser: null,
  lastExecutionDate: null,
  executionStatus: "",
  executionType: "",
  DoneBy: "",
    });

  const [state, setState] = useState({
    selectedTests: new Set(),
    darkMode: false,
    executionMode: null,
    showExecutionDialog: false,
    batchSelections: {},
    columnSort: { field: 'priority', order: 'desc' }
  });

  useEffect(() => {
    fetchTestData();
  }, []);

  const fetchTestData = async () => {
    try {
      setLoading(true);
      const response = await api.get('/api/midstests');
      const transformedData = transformBackendData(response.data);
      setTestData(transformedData);
      setLoading(false);
    } catch (err) {
      setError('Failed to fetch test data');
      setLoading(false);
    }
  };

  const transformBackendData = (backendData) => {
    return backendData.reduce((acc, test) => {
      const category = test.pageName;
      if (!acc[category]) acc[category] = [];
      acc[category].push({
        id: test.id,
        name: test.testName,
        success: test.successfulCount || 0,
        failure: test.failedCount || 0,
        priority: test.priority.toLowerCase(),
        doneBy: test.doneBy || 'N/A',
        status: test.executionStatus,
        lastExecutionUser: test.lastExecutionUser || 'N/A',
        lastExecutionDate: test.lastExecutionDate || null,
        executionType: test.executionType 
      });
      return acc;
    }, {});
  };

  const toggleTestSelection = (testId) => {
    setSelectedTests(prev => 
      prev.includes(testId) 
        ? prev.filter(id => id !== testId) 
        : [...prev, testId]
    );
  };

  const handleExecute = (mode) => {
    setExecutionMode(mode);
    setShowModeDialog(false);
    setShowExecutionDialog(true);
  };

  const handlePassExecute = async (mode) => {
    try {
      setLoading(true);
      if (mode === 'automatic') {
        const groupedTests = selectedTests.reduce((acc, testId) => {
          const test = Object.values(testData).flat().find((t) => t.id === testId);
          if (!acc[test.doneBy]) acc[test.doneBy] = [];
          acc[test.doneBy].push({ id: test.id, testName: test.name, executionType: "automatic" });
          return acc;
        }, {});

        const username = localStorage.getItem('username');
        const executionPromises = Object.entries(groupedTests).map(async ([doneBy, tests]) => {
          const userData = await api.post('/api/sampleUserCredentials/getUserCredentials', { doneBy, username });
          const tempPayload = {
            user: username,
            username: userData.data[0],
            password: userData.data[1]
          };
          const tempResponse = await api.post('/api/midstests/login-mids', tempPayload);
          if (tempResponse.data === "Otp test") {
            setPendingExecution({ mode, tests, testerName: username || 'Unknown', doneBy });
            setShowOtpDialog(true);
            return null;
          } else {
            const payload = {
              executionMode: mode,
              tests,
              testerName: username || 'Unknown',
              user: username,
            };
            const response = await api.post('/api/midstests/executeTests', payload);
            return response.data;
          }
        });

        const results = await Promise.all(executionPromises.filter(p => p !== null));
        console.log('All Execution Results:', results);
      } else if (mode === "manual") {
        const manualTests = selectedTests.map(testId => {
          const test = Object.values(testData).flat().find(t => t.id === testId);
          return {
            id: test.id,
            name: test.name,
            category: Object.entries(testData).find(([cat, tests]) => 
              tests.some(t => t.id === testId)
            )[0],
            executionType: 'Manual',
            doneBy: test.doneBy,
          };
        });
        setManualExecutionState({
          active: true,
          currentTestIndex: 0,
          tests: manualTests,
          results: {},
          showManualDialog: true,
        });
        setShowExecutionDialog(false);
      } else {
        const automaticTests = [];
        const manualTests = [];
        selectedTests.forEach(testId => {
          const execType = testExecutionTypes[testId] || 'Automatic';
          const test = Object.values(testData).flat().find(t => t.id === testId);
          if (execType === 'Automatic') {
            automaticTests.push(test);
          } else {
            manualTests.push(test);
          }
        });

        const groupedAutomaticTests = automaticTests.reduce((acc, test) => {
          if (!acc[test.doneBy]) acc[test.doneBy] = [];
          acc[test.doneBy].push({ id: test.id, testName: test.name, executionType: "automatic" });
          return acc;
        }, {});

        const username = localStorage.getItem('username');
        const executionPromises = Object.entries(groupedAutomaticTests).map(async ([doneBy, tests]) => {
          const userData = await api.post('/api/sampleUserCredentials/getUserCredentials', { doneBy, username });
          const payload = {
            executionMode: mode,
            userName: userData.data[0],
            password: userData.data[1],
            tests,
            testerName: username || 'Unknown',
          };
          const response = await api.post('/api/midstests/executeTests', payload);
          return response.data;
        });

        const results = await Promise.all(executionPromises);
        console.log('All Execution Results:', results);

        if (manualTests.length > 0) {
          const manualTestData = manualTests.map(test => ({
            id: test.id,
            name: test.name,
            category: Object.entries(testData).find(([cat, tests]) => 
              tests.some(t => t.id === test.id)
            )[0],
            executionType: 'Manual',
            doneBy: test.doneBy,
          }));
          setManualExecutionState({
            active: true,
            currentTestIndex: 0,
            tests: manualTestData,
            results: {},
            showManualDialog: true,
          });
          setShowExecutionDialog(false);
        }
      }
    } catch (error) {
      setErrorMessage(error.message || 'Failed to execute tests');
      setShowErrorDialog(true);
    } finally {
      setLoading(false);
      if (!showOtpDialog) {
        setShowExecutionDialog(false);
        setSelectedTests([]);
        setTestExecutionTypes({});
        await fetchTestData();
      }
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
      const { mode, tests, testerName, doneBy } = pendingExecution;
      const username = localStorage.getItem('username');
      const payload = {
        executionMode: mode,
        tests,
        testerName,
        otp,
        user: username,
      };
      const response = await api.post('/api/midstests/executeTests', payload);
      console.log(`Execution Response for doneBy ${doneBy}:`, response.data);
      setShowOtpDialog(false);
      setOtp('');
      setPendingExecution(null);
      setShowExecutionDialog(false);
      setSelectedTests([]);
      setTestExecutionTypes({});
      await fetchTestData();
    } catch (error) {
      setErrorMessage(error.message || 'Invalid OTP. Please try again.');
      setShowErrorDialog(true);
    } finally {
      setLoading(false);
    }
  };

  const closeOtpDialog = () => {
    setShowOtpDialog(false);
    setOtp('');
    setPendingExecution(null);
  };

  const closeErrorDialog = () => {
    setShowErrorDialog(false);
    setErrorMessage('');
  };

  const handleExecutionTypeChange = (testId, type) => {
    setTestExecutionTypes(prev => ({
      ...prev,
      [testId]: type
    }));
  };

  const handleManualTestAction = (action, result = null, comments = '') => {
    const currentTest = manualExecutionState.tests[manualExecutionState.currentTestIndex];
    const newResults = { ...manualExecutionState.results };
    
    if (action === 'record') {
      newResults[currentTest.id] = {
        status: result,
        comments,
        timestamp: new Date().toISOString(),
      };
    }
    
    if (action === 'next' || action === 'record') {
      if (manualExecutionState.currentTestIndex < manualExecutionState.tests.length - 1) {
        setManualExecutionState(prev => ({
          ...prev,
          currentTestIndex: prev.currentTestIndex + 1,
          results: newResults,
        }));
      } else {
        setManualExecutionState(prev => ({
          ...prev,
          active: false,
          showManualDialog: false,
          results: newResults,
        }));
        console.log('Manual execution completed:', newResults);
        fetchTestData();
      }
    } else if (action === 'skip') {
      newResults[currentTest.id] = {
        status: 'skipped',
        comments: 'Test skipped by user',
        timestamp: new Date().toISOString(),
      };
      setManualExecutionState(prev => ({
        ...prev,
        currentTestIndex: prev.currentTestIndex + 1,
        results: newResults,
      }));
    } else if (action === 'close') {
      setManualExecutionState(prev => ({
        ...prev,
        active: false,
        showManualDialog: false,
      }));
      fetchTestData();
    }
  };

  if (loading) return <div className="midstest-loading">Loading...</div>;
  if (error) return <div className="midstest-error">{error}</div>;

  


const showDialog = () => {
        setIsDialogOpen(true);
    };

    const closeDialog = () => {
        setIsDialogOpen(false);
    };


const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post('/api/midstests/add-mids-test',formData);
            if (response.ok) {
                const result = await response.json();
                console.log('Test added successfully:', result);
                closeDialog();
            } else {
                console.error('Failed to add test');
            }
        } catch (error) {
            console.error('Error:', error);
        }
    };

const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };



  return (
    <Layout >
      <div className="midstest-container">
        <div className="background-blobs"></div>
        <div className="midstest-header">
          <h1 className="midstest-title">MIDS Test Management</h1>
          <div className="midstest-actions">
            
<button className="midstest-settings-btn" aria-label="Settings" onClick={showDialog}>
                <FiSettings />
            </button>

          </div>
        </div>

        {Object.entries(testData).map(([category, tests]) => (
          <div key={category} className="midstest-category-panel">
            <div className="midstest-category-header">
              <FiBox className="midstest-category-icon" />
              <h2>{category}</h2>
            </div>
            <div className="midstest-table-container">
              <table className="midstest-table">
                <thead>
                  <tr>
                    <th><FiCheck /></th>
                    <th>Test Name</th>
                    <th>Success Rate</th>
                    <th>Failure Rate</th>
                    <th>Priority</th>
                    <th>Last Execution User</th>
                    <th>Last Execution Date</th>
                    <th>Execution Status</th>
                    <th>Execution Type</th>
                  </tr>
                </thead>
                <tbody>
                  {tests.map(test => {
                    const total = test.success + test.failure;
                    const successRate = total > 0 ? ((test.success / total) * 100).toFixed(2) : "0.00";
                    const failureRate = total > 0 ? ((test.failure / total) * 100).toFixed(2) : "0.00";
                    return (
                      <tr 
                        key={test.id} 
                        className="midstest-table-row"
                        onClick={() => toggleTestSelection(test.id)}
                        tabIndex={0}
                        role="button"
                        aria-label={`Select ${test.name}`}
                      >
                        <td>
                          <input 
                            type="checkbox"
                            checked={selectedTests.includes(test.id)}
                            onChange={() => {}}
                            className="midstest-checkbox"
                            aria-label={`Select ${test.name}`}
                          />
                        </td>
                        <td>{test.name}</td>
                        <td className="midstest-success">{successRate}%</td>
                        <td className="midstest-failure">{failureRate}%</td>
                        <td>
                          <span className={`midstest-priority midstest-priority-${test.priority}`}>
                            {test.priority}
                          </span>
                        </td>
                        <td>{test.lastExecutionUser}</td>
                        <td>{test.lastExecutionDate ? new Date(test.lastExecutionDate).toLocaleString() : 'N/A'}</td>
                        <td>{test.status}</td>
                        <td>{test.executionType}</td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        ))}

        {selectedTests.length > 0 && (
          <button 
            className="midstest-execute-btn"
            onClick={() => setShowModeDialog(true)}
            aria-label={`Execute ${selectedTests.length} selected tests`}
          >
            <FiPlay />
            Execute Selected ({selectedTests.length})
          </button>
        )}

        {showModeDialog && (
          <div className="midstest-dialog-overlay" onClick={() => setShowModeDialog(false)}>
            <div
              className="midstest-dialog-box midstest-slide-in"
              onClick={(e) => e.stopPropagation()}
              role="dialog"
              aria-labelledby="mode-dialog-title"
            >
              <div className="midstest-dialog-header">
                <h2 id="mode-dialog-title">Select Execution Mode</h2>
                <button
                  onClick={() => setShowModeDialog(false)}
                  className="midstest-close-btn"
                  aria-label="Close mode dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="midstest-dialog-content">
                <button 
                  className="midstest-dialog-option"
                  onClick={() => handleExecute('automatic')}
                >
                  <MdAutoMode className="midstest-option-icon" />
                  Fully Automatic
                </button>
                <button 
                  className="midstest-dialog-option"
                  onClick={() => handleExecute('auto+manual')}
                >
                  <MdOutlineComputer className="midstest-option-icon" />
                  Automatic + Manual
                </button>
                <button 
                  className="midstest-dialog-option"
                  onClick={() => handleExecute('manual')}
                >
                  <FiPlay className="midstest-option-icon" />
                  Manual Execution
                </button>
              </div>
            </div>
          </div>
        )}

        {showExecutionDialog && (
          <div className="midstest-dialog-overlay" onClick={() => setShowExecutionDialog(false)}>
            <div
              className="midstest-dialog-box midstest-slide-in"
              onClick={(e) => e.stopPropagation()}
              role="dialog"
              aria-labelledby="execution-dialog-title"
            >
              <div className="midstest-dialog-header">
                <h2 id="execution-dialog-title">Confirm Execution</h2>
                <button
                  onClick={() => setShowExecutionDialog(false)}
                  className="midstest-close-btn"
                  aria-label="Close execution dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="midstest-dialog-content">
                <div className="midstest-table-container">
                  <table className="midstest-table">
                    <thead>
                      <tr>
                        <th>Test Name</th>
                        {executionMode === 'auto+manual' && <th>Execution Type</th>}
                        <th>Priority</th>
                      </tr>
                    </thead>
                    <tbody>
                      {selectedTests.map(testId => {
                        const test = Object.values(testData).flat().find(t => t.id === testId);
                        return (
                          <tr key={testId}>
                            <td>{test.name}</td>
                            {executionMode === 'auto+manual' && (
                              <td>
                                <select 
                                  className="midstest-select"
                                  value={testExecutionTypes[testId] || 'Automatic'}
                                  onChange={(e) => handleExecutionTypeChange(testId, e.target.value)}
                                  disabled={test.executionType !== 'Both'}
                                  aria-label={`Execution type for ${test.name}`}
                                >
                                  <option value="Automatic" disabled={test.executionType === 'Manual'}>
                                    Automatic
                                  </option>
                                  <option value="Manual" disabled={test.executionType === 'Automatic'}>
                                    Manual
                                  </option>
                                </select>
                                {test.executionType !== 'Both' && (
                                  <span className="midstest-tooltip">
                                    ({test.executionType} only)
                                  </span>
                                )}
                              </td>
                            )}
                            <td>
                              <span className={`midstest-priority midstest-priority-${test.priority}`}>
                                {test.priority}
                              </span>
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
                <button 
                  className="midstest-execute-btn"
                  onClick={() => handlePassExecute(executionMode)}
                >
                  <FiPlay className="midstest-btn-icon" />
                  Start Execution
                </button>
              </div>
            </div>
          </div>
        )}

        {manualExecutionState.showManualDialog && (
          <div className="midstest-dialog-overlay" onClick={() => handleManualTestAction('close')}>
            <div
              className="midstest-manual-dialog midstest-slide-in"
              onClick={(e) => e.stopPropagation()}
              role="dialog"
              aria-labelledby="manual-dialog-title"
            >
              <div className="midstest-manual-sidebar">
                <h2 id="manual-dialog-title">Manual Test Execution</h2>
                <div className="midstest-test-list">
                  {manualExecutionState.tests.map((test, index) => (
                    <div
                      key={test.id}
                      className={`midstest-test-item ${index === manualExecutionState.currentTestIndex ? 'active' : ''}`}
                      onClick={() => setManualExecutionState(prev => ({ ...prev, currentTestIndex: index }))}
                      role="button"
                      tabIndex={0}
                      aria-label={`Go to ${test.name}`}
                    >
                      <span className="midstest-test-arrow">{index === manualExecutionState.currentTestIndex ? '→' : ''}</span>
                      {test.name}
                      {manualExecutionState.results[test.id] && (
                        <span className={`midstest-status midstest-status-${manualExecutionState.results[test.id].status}`}>
                          {manualExecutionState.results[test.id].status}
                        </span>
                      )}
                    </div>
                  ))}
                </div>
                <button
                  className="midstest-close-btn"
                  onClick={() => handleManualTestAction('close')}
                  aria-label="Close manual execution dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="midstest-manual-content">
                <div className="midstest-progress">
                  <div className="midstest-progress-track">
                    <div 
                      className="midstest-progress-bar" 
                      style={{ width: `${((manualExecutionState.currentTestIndex + 1) / manualExecutionState.tests.length) * 100}%` }}
                    ></div>
                  </div>
                  <div className="midstest-progress-text">
                    Test {manualExecutionState.currentTestIndex + 1} of {manualExecutionState.tests.length}
                  </div>
                </div>
                <div className="midstest-test-details">
                  <div className="midstest-test-meta">
                    <span>ID: {manualExecutionState.tests[manualExecutionState.currentTestIndex]?.id}</span>
                    <span>
                      Priority: <span className={`midstest-priority midstest-priority-${manualExecutionState.tests[manualExecutionState.currentTestIndex]?.priority}`}>
                        {manualExecutionState.tests[manualExecutionState.currentTestIndex]?.priority}
                      </span>
                    </span>
                  </div>
                  <h3>{manualExecutionState.tests[manualExecutionState.currentTestIndex]?.name}</h3>
                  <div className="midstest-test-category">
                    Category: {manualExecutionState.tests[manualExecutionState.currentTestIndex]?.category}
                  </div>
                </div>
                <div className="midstest-test-instructions">
                  <h4>Test Steps</h4>
                  <ol>
                    <li>Navigate to the application login page</li>
                    <li>Enter valid credentials in username field</li>
                    <li>Enter valid credentials in password field</li>
                    <li>Click the 'Login' button</li>
                    <li>Verify successful redirection to dashboard</li>
                    <li>Check user profile information is displayed correctly</li>
                  </ol>
                </div>
                <div className="midstest-test-actions">
                  <div className="midstest-result-actions">
                    <button 
                      className="midstest-action-btn midstest-pass"
                      onClick={() => handleManualTestAction('record', 'passed')}
                      aria-label="Mark test as passed"
                    >
                      <FiCheck /> Pass
                    </button>
                    <button 
                      className="midstest-action-btn midstest-fail"
                      onClick={() => handleManualTestAction('record', 'failed')}
                      aria-label="Mark test as failed"
                    >
                      <FiX /> Fail
                    </button>
                  </div>
                  <div className="midstest-comment-section">
                    <label>Comments</label>
                    <textarea 
                      placeholder="Add comments about test execution..."
                      className="midstest-comment-input"
                      onChange={(e) => handleManualTestAction('record', null, e.target.value)}
                      aria-label="Test comments"
                    ></textarea>
                  </div>
                  <div className="midstest-secondary-actions">
                    <button 
                      className="midstest-secondary-btn midstest-skip"
                      onClick={() => handleManualTestAction('skip')}
                      aria-label="Skip test"
                    >
                      Skip Test
                    </button>
                    <button 
                      className="midstest-secondary-btn midstest-attach"
                      onClick={() => document.getElementById('test-attachment').click()}
                      aria-label="Attach file"
                    >
                      Attach File
                    </button>
                    <input type="file" id="test-attachment" style={{ display: 'none' }} />
                  </div>
                </div>
                <div className="midstest-navigation">
                  <button 
                    className="midstest-nav-btn"
                    disabled={manualExecutionState.currentTestIndex === 0}
                    onClick={() => setManualExecutionState(prev => ({
                      ...prev,
                      currentTestIndex: prev.currentTestIndex - 1
                    }))}
                    aria-label="Previous test"
                  >
                    Previous
                  </button>
                  <button 
                    className="midstest-nav-btn midstest-primary"
                    onClick={() => handleManualTestAction('next')}
                    aria-label={manualExecutionState.currentTestIndex < manualExecutionState.tests.length - 1 ? "Next test" : "Finish execution"}
                  >
                    {manualExecutionState.currentTestIndex < manualExecutionState.tests.length - 1 
                      ? "Next Test" 
                      : "Finish Execution"}
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

        {showOtpDialog && (
          <div className="midstest-dialog-overlay" onClick={closeOtpDialog}>
            <div
              className="midstest-dialog-box midstest-slide-in"
              onClick={(e) => e.stopPropagation()}
              role="dialog"
              aria-labelledby="otp-dialog-title"
            >
              <div className="midstest-dialog-header">
                <h2 id="otp-dialog-title">Enter OTP</h2>
                <button
                  onClick={closeOtpDialog}
                  className="midstest-close-btn"
                  aria-label="Close OTP dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="midstest-dialog-content">
                <p>Please enter the OTP sent to your registered email or phone.</p>
                <input
                  type="text"
                  value={otp}
                  onChange={(e) => setOtp(e.target.value)}
                  placeholder="Enter OTP"
                  className="midstest-otp-input"
                  aria-label="OTP input"
                  autoFocus
                  onKeyPress={(e) => e.key === 'Enter' && handleOtpSubmit()}
                />
                <div className="midstest-dialog-buttons">
                  <button
                    onClick={handleOtpSubmit}
                    className="midstest-submit-btn"
                    aria-label="Submit OTP"
                  >
                    <FiCheck /> Submit
                  </button>
                  <button
                    onClick={closeOtpDialog}
                    className="midstest-cancel-btn"
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
          <div className="midstest-dialog-overlay" onClick={closeErrorDialog}>
            <div
              className="midstest-dialog-box midstest-slide-in"
              onClick={(e) => e.stopPropagation()}
              role="alertdialog"
              aria-labelledby="error-dialog-title"
            >
              <div className="midstest-dialog-header">
                <h2 id="error-dialog-title">Error</h2>
                <button
                  onClick={closeErrorDialog}
                  className="midstest-close-btn"
                  aria-label="Close error dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="midstest-dialog-content">
                <div className="midstest-error-icon">
                  <FiAlertCircle size={40} />
                </div>
                <p>{errorMessage}</p>
                <div className="midstest-dialog-buttons">
                  <button
                    onClick={closeErrorDialog}
                    className="midstest-close-error-btn"
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

        
            

{isDialogOpen && (
  <div className="midstest-dialog-overlay" onClick={closeDialog}>
    <div
      id="dialogBox"
      className="midstest-dialog-box"
      onClick={(e) => e.stopPropagation()}
      role="dialog"
      aria-labelledby="dialog-title"
    >
      <h2 id="dialog-title">Test Management Settings</h2>
      <form onSubmit={handleSubmit}>
        <label htmlFor="testName">Test Name:</label>
        <input
          type="text"
          id="testName"
          name="testName"
          value={formData.testName}
          onChange={handleChange}
          required
        />
        <label htmlFor="priority">Priority:</label>
        <input
          type="text"
          id="priority"
          name="priority"
          value={formData.priority}
          onChange={handleChange}
          required
        />
        <label htmlFor="pageName">PageName:</label>
        <input
          type="text"
          id="pageName"
          name="pageName"
          value={formData.pageName}
          onChange={handleChange}
          required
        />
        <label htmlFor="executionType">ExecutionType:</label>
        <input
          type="text"
          id="executionType"
          name="executionType"
          value={formData.executionType}
          onChange={handleChange}
          required
        />
        <label htmlFor="DoneBy">DoneBy:</label>
        <input
          type="text"
          id="DoneBy"
          name="DoneBy"
          value={formData.DoneBy}
          onChange={handleChange}
          required
        />

        <div className="midstest-dialog-buttons">
          <button type="submit">Submit</button>
          <button type="button" onClick={closeDialog}>Close</button>
        </div>
      </form>
    </div>
  </div>
)}

        


      </div>


      
    </Layout>
  );
};

export default MidsTest;