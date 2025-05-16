import React, { useState } from 'react';
import '../css/MWDpr.css'; // Adjust the path as necessary
import { FaTable, FaToggleOn } from 'react-icons/fa';
import api from '../api/axiosConfig';
import { useNavigate } from 'react-router-dom';
import { FaCheckCircle, FaTimesCircle } from 'react-icons/fa'; 
import { FaSpinner } from 'react-icons/fa';

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
  const [formData, setFormData] = useState({
    startPoint: '',
    endPoint: ''
  });
  const navigate = useNavigate();
  const handleShowHistory=()=>{
    navigate('/testreports'); 
  }
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
    try {
      const response = await api.post('/api/midstests/mwDprPage', {
        startPoint: formData.startPoint,
        endPoint: formData.endPoint,
        userId: userId
      });
      setResult({ success: true, message: response.data });
      console.log('Response from server:', response);//Two option sucess or failure
      setStatus('complete');
    } catch (error) {
      setStatus('error');
      setResult({
        success: false,
        message: error.response?.data || 'Unknown error occurred'
      });
      console.error("Error in test submission: ", error);
    }
  };
  const LoadingDialog = () => (
    <div className="loading-overlay">
      <div className="loading-content">
        <FaSpinner className="loading-spinner" />
        <p className="loading-text">Executing Tests...</p>
      </div>
    </div>
  );
  const ResultDialog = ({ success, message, onClose }) => (
    <div className="loading-overlay">
      <div className="result-content">
        {success ? (
          <FaCheckCircle className="result-icon success-icon" />
        ) : (
          <FaTimesCircle className="result-icon error-icon" />
        )}
        <p className="result-message">{message}</p>
        <button className="retry-button" onClick={onClose}>
          {success ? 'Close' : 'Retry'}
        </button>
      </div>
    </div>
  );

  return (
    <div className="test-container">
      <div className="glass-nav">
        <h2 className="gradient-text">MW DPR Automation</h2>
        <div className="table-toggle" onClick={handleShowHistory}>
          <FaTable /> {showTable ? 'Hide History' : 'Show History'}
        </div>
      </div>

      <div className="main-content">
        <div className="glass-cardd form-wrapper">
         

          <form onSubmit={handleSubmit}>
            <div className="scenario-grid">
              <div className="form-group">
                <div className="input-header">
                  <span className="input-label">Start Point</span>
                  <div className="select-wrapper">
                    <select
                      value={formData.startPoint}
                      onChange={(e) => setFormData({ ...formData, startPoint: e.target.value })}
                      className="glass-select"
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
                    <div className="select-arrow"></div>
                  </div>
                </div>
              </div>

              <div className="form-group">
                <div className="input-header">
                  <span className="input-label">End Point</span>
                  <div className="select-wrapper">
                    <select
                      value={formData.endPoint}
                      onChange={(e) => setFormData({ ...formData, endPoint: e.target.value })}
                      className="glass-select"
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
                    <div className="select-arrow"></div>
                  </div>
                </div>
              </div>
            </div>

            <button type="submit" className="gradient-button" disabled={status === 'loading'}>
              Run Test Scenario
              <div className="button-hover-effect"></div>
            </button>
          </form>
        </div>

{/* 
        {showTable && (
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
        )}
  */}
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
    </div>
  );
};

const MWDPRPage = () => {
  return (
    <div className="container">
      <TestForm />
    </div>
  );
};

export default MWDPRPage;