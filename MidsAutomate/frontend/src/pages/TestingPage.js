import React,{ useState } from 'react';
import { FaHistory, FaCogs, FaFlask, FaClipboardList, FaDownload } from 'react-icons/fa';
import '../css/TestingPage.css';
import { FaTable, FaToggleOn } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';
import SimpleRingLoader from './AdvanceLoader';


const TestingPage = () => {
    const navigate = useNavigate();
    const [showDialog, setShowDialog] = useState(false);
    const [actionType, setActionType] = useState('');
    const [downloadPath, setDownloadPath] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');  
    const handleMidsClick=()=>{
        navigate('/midstest'); 
      }
      const handleMWClick=()=>{
        navigate('/mwdpr'); 
      }
      const handleShowHistory=()=>{
        navigate('/testreports'); 
      }
      const handleBasicTestClick = () => setShowDialog(true);
      const handleTestAction = async () => {
        try {
           setLoading(true);
          const username = localStorage.getItem('username');
          console.log('Username:', username);
          if(actionType === 'page-test') {
            let username = localStorage.getItem('username');
            if(!username) {
              username='guest';
            }
            const payload = {
              user:username,
            }
            const response = await api.post('/api/basic-report/page-test', payload);
            console.log('Page test response:', response.data);
            navigate('/basic-test-report');
          } else {
            if(!downloadPath) {
              throw new Error('Please provide download path');
            }
            const payload = {
              user:username,
              directory:downloadPath
            }
            const response = await api.post('/api/basic-report/report-download', payload);
            console.log('Download report response:', response.data);
            //don't delete this code i will
            {/* 
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'test-report.pdf');
            document.body.appendChild(link);
            link.click();
            link.remove();
            */}
          }
        } catch (err) {
          setError(err.message || 'Failed to perform action');
        } finally {
          setLoading(false);
          setShowDialog(false);
        }
      };
  return (
    <div className="testing-container">
      {loading && <SimpleRingLoader />}
      <div className="background-blobs">
        {/* Same blob background as previous designs */}
      </div>

      <nav className="glass-nav">
       
        <h1 className="page-title gradient-text">MIDS TESTING</h1>
        <div className="nav-left">
          <button className="history-button" onClick={handleShowHistory}>
            <FaTable /> 
            Show History
          </button>
        </div>
      </nav>

      <div className="main-content">
        <div className="testing-categories">
          {/* Basic Testing Card */}
          <div className="glass-card testing-card">
            <div className="card-header" >
              <FaFlask className="card-icon" />
              <h3>Basic Testing</h3>
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
              <button className="action-button" onClick={handleBasicTestClick}>
        Run Basic Tests
      </button>
            </div>
          </div>

          {/* MW DPR Page Testing Card */}
          <div className="glass-card testing-card">
            <div className="card-header">
              <FaCogs className="card-icon" />
              <h3>MW DPR Page Testing</h3>
            </div>
            <div className="card-content">
              <div className="stat-item">
                <span>Active Scenarios:</span>
                <span className="stat-value">15</span>
              </div>
              <div className="stat-item">
                <span>Pending Tests:</span>
                <span className="stat-value warning">3</span>
              </div>
              <button className="action-button" onClick={handleMWClick}>
                Configure Scenarios
              </button>
            </div>
          </div>

          {/* Mids Test Management Card */}
          <div className="glass-card testing-card">
            <div className="card-header">
              <FaClipboardList className="card-icon" />
              <h3>Mids Test Management</h3>
            </div>
            <div className="card-content">
              <div className="stat-item">
                <span>Total Test Cases:</span>
                <span className="stat-value">247</span>
              </div>
              <div className="stat-item">
                <span>Execution Coverage:</span>
                <span className="stat-value">92%</span>
              </div>
              <button className="action-button" onClick={handleMidsClick}>
                Manage Test Plans
              </button>
            </div>
          </div>
        </div>
      </div>


      {showDialog && (
        <div className="test-dialog-overlay">
          <div className="test-dialog-content">
            <h3>Select Test Action</h3>
            
            <div className="dialog-options">
              <div className="dialog-option" onClick={() => setActionType('page-test')}>
                
                <span>Page Testing</span>
                {actionType === 'page-test' && <div className="selection-indicator" />}
              </div>
              
              <div className="dialog-option" onClick={() => setActionType('download')}>
                <FaDownload />
                <span>Download Report</span>
                {actionType === 'download' && <div className="selection-indicator" />}
              </div>
            </div>

            {actionType === 'download' && (
              <div className="path-input">
                <input
                  type="text"
                  placeholder="Enter download directory path..."
                  value={downloadPath}
                  onChange={(e) => setDownloadPath(e.target.value)}
                />
              </div>
            )}

            <div className="dialog-actions">
              <button onClick={() => setShowDialog(false)}>Cancel</button>
              <button onClick={handleTestAction} disabled={!actionType || loading}>
                {loading ? 'Processing...' : 'Confirm'}
              </button>
            </div>

            {error && <div className="dialog-error">{error}</div>}
          </div>
        </div>
      )}

    </div>
  );
};

export default TestingPage;