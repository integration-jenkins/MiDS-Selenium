import React, { useState } from 'react';
//DPR.css file in css dir which is in src dir where as in src component this code is written
import '../css/DPR.css'; // Adjust the path as necessary
import { FaTable, FaToggleOn, FaUserCircle } from 'react-icons/fa';
import axios from 'axios';
const roles = [
  { id: 1, name: 'MS Partner', color: '#4CAF50' },
  { id: 2, name: 'MW Planner', color: '#2196F3' },
  { id: 3, name: 'Operation Team', color: '#FF9800' },
  { id: 4, name: 'INC Partner', color: '#9C27B0' }
];

const testData = {
  successCount: 45,
  failCount: 5,
  successPercentage: 90
};
const statusOptions = {
  SR_RFAI: ["SR Pending", "SP Pending", "SO Pending", "RFAI Pending"],
  Material: ["MO Pending", "Material Delivery Pending"],
  INC: ["HOP Alignment Pending/Phy-AT Pending", "HOP Alignment Pending/Phy-AT Raised", "HOP Alignment Pending/Phy-AT Rejected", "HOP Alignment Pending/Phy-AT Accept", "I&C Pending"],
  AT: ["PHY-AT RAISED/SOFT-AT PENDING", "PHY-AT REJECTED/SOFT-AT RAISED", "PHY-AT RAISED/SOFT-AT RAISED", "AT ACCEPTED", "PHY-AT PENDING/SOFT-AT ACCEPTED", "PHY-AT ACCEPTED/SOFT-AT PENDING", "PHY+SOFT AT PENDING", "PHY-AT ACCEPTED/SOFT-AT REJECTED", "PHY-AT PENDING/SOFT-AT RAISED", "PHY-AT RAISED/SOFT-AT REJECTED", "PHY-AT REJECTED/SOFT-AT REJECTED", "PHY-AT PENDING/SOFT-AT REJECTED", "PHY-AT REJECTED/SOFT-AT ACCEPTED", "PHY-AT ACCEPTED/SOFT-AT RAISED", "PHY-AT REJECTED/SOFT-AT PENDING", "PHY-AT RAISED/SOFT-AT ACCEPTED"],
  Traffic: ["TS Completed"],
  Cancel: ["Canceled", "Request for Cancellation", "Material Returned"],
  softUpgrade: ["Upgrade Pending", "Upgrade Completed"]
};
const initialTestResults = [
  { id: 1, startPoint: 'SR Pending', endPoint: 'SP Pending', executionTime: '2s', status: 'Success', testedBy: 'user1', date: '2024-02-20' },
  { id: 2, startPoint: 'MO Pending', endPoint: 'Material Delivery', executionTime: '3s', status: 'Failed', testedBy: 'user2', date: '2024-02-21' }
];

const Toolbar = ({ onMenuToggle, menuOpen }) => (
  <div className="toolbar">
    <h2>DPR Automation</h2>
    <div className="menu-container">
      <div className="menu-icon" onClick={onMenuToggle}>⋮</div>
      <div className={`menu-dropdown ${menuOpen ? 'show' : ''}`}>
        <div>Home</div>
        <div>Profile</div>
        <div>Logout</div>
      </div>
    </div>
  </div>
);

const RoleCard = ({ role, onClick }) => (
  <div className="card" onClick={onClick}>
    <h3>{role.name}</h3>
    <div className="card-content">
      <p>Success: {testData.successCount}</p>
      <p>Failed: {testData.failCount}</p>
      <div className="status-circle" style={{ backgroundColor: role.color }}>
        {testData.successPercentage}%
      </div>
    </div>
  </div>
);

const TestForm = ({ role, onBack }) => {
  const [showTable, setShowTable] = useState(false);
  const [formData, setFormData] = useState({ 
    userId: '', 
    password: '',
    startPoint: '',
    endPoint: ''
  });
  const [testResults, setTestResults] = useState([]);

  const handleSubmit =  async (e) => {
    e.preventDefault();
    const newTest = {
      id: testResults.length + 1,
      ...formData,
      executionTime: `${Math.random() * 5}s`,
      status: Math.random() > 0.2 ? 'Success' : 'Failed',
      testedBy: formData.userId,
      date: new Date().toLocaleDateString()
    };
    try{
      const token = localStorage.getItem('jwtToken');
      console.log("Token: ", token);
      const response = await axios.post('http://localhost:6083/api/tests', {
        userId: formData.userId,
        startPoint: formData.startPoint,
        endPoint: formData.endPoint,
        executionTime: newTest.executionTime,
        status: newTest.status
      },
      {

        headers: {
          Authorization: `Bearer ${"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYXZpaSIsImlhdCI6MTc0NzAyMjcwOCwiZXhwIjoxNzQ3MDU4NzA4fQ.g0W9OuTbDgRFilz3BLrCOZDP5CXVYzJMlRjr3_FboIA"}` // Testing Purpose Checking it working or not
        }
      }

      );

      console.log('Response from server:', response.data);

      // Update the test results in the UI
      setTestResults([...testResults, newTest]);
      setFormData({ ...formData, startPoint: '', endPoint: '' });


    }catch (error){
        console.error("Error in test submission: ", error);
    }


  };

  return (
    <div className="test-container">
      <div className="form-header">
        <button className="back-btn" onClick={onBack}>&larr; Back</button>
        <h2>{role.name} Automation</h2>
        <div className="table-toggle" onClick={() => setShowTable(!showTable)}>
          <FaTable /> {showTable ? 'Hide History' : 'Show History'}
        </div>
      </div>

      <div className="form-wrapper">
        <div className="auth-panel">
          <div className="auth-header">
            <FaUserCircle size={24} />
            <h3>Authentication</h3>
          </div>
          <div className="form-group">
            <label>User ID</label>
            <input
              type="text"
              value={formData.userId}
              onChange={(e) => setFormData({...formData, userId: e.target.value})}
            />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              value={formData.password}
              onChange={(e) => setFormData({...formData, password: e.target.value})}
            />
          </div>
        </div>

        <div className="scenario-panel">
          <div className="mode-toggle">
            <div className="toggle-item active">
              <FaToggleOn /> Automatic
              <span className="tooltip">Full automation mode</span>
            </div>
            <div className="toggle-item disabled">
              Auto+Manual
              <span className="tooltip">Coming soon</span>
            </div>
            <div className="toggle-item disabled">
              Manual
              <span className="tooltip">Disabled</span>
            </div>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="scenario-selectors">
              <div className="form-group">
                <label>Start Point</label>
                <select
                  value={formData.startPoint}
                  onChange={(e) => setFormData({...formData, startPoint: e.target.value})}
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
              </div>

              <div className="form-group">
                <label>End Point</label>
                <select
                  value={formData.endPoint}
                  onChange={(e) => setFormData({...formData, endPoint: e.target.value})}
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
              </div>
            </div>
            <button type="submit" className="run-btn">Run Test Scenario</button>
          </form>
        </div>
      </div>

      {showTable && (
        <div className="results-table">
          <table>
            <thead>
              <tr>
                <th>#</th>
                <th>Start Point</th>
                <th>End Point</th>
                <th>Execution Time</th>
                <th>Status</th>
                <th>Tested By</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {testResults.map((test) => (
                <tr key={test.id}>
                  <td>{test.id}</td>
                  <td>{test.startPoint}</td>
                  <td>{test.endPoint}</td>
                  <td>{test.executionTime}</td>
                  <td>
                    <span className={`status-badge ${test.status.toLowerCase()}`}>
                      {test.status}
                    </span>
                  </td>
                  <td>{test.testedBy}</td>
                  <td>{test.date}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

const DPRPage = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const [selectedRole, setSelectedRole] = useState(null);

  return (
    <div className="container">
      <Toolbar 
        onMenuToggle={() => setMenuOpen(!menuOpen)} 
        menuOpen={menuOpen}
      />
      
      {!selectedRole ? (
        <div className="cards-container">
          {roles.map((role) => (
            <RoleCard 
              key={role.id} 
              role={role} 
              onClick={() => setSelectedRole(role)}
            />
          ))}
        </div>
      ) : (
        <TestForm 
          role={selectedRole} 
          onBack={() => setSelectedRole(null)}
        />
      )}
    </div>
  );
};

export default DPRPage;