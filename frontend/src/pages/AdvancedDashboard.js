// src/pages/AdvancedDashboard.js
import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../api/axiosConfig';
import RoleCredentialsDialog from '../components/RoleCredentialsDialog';

import { 
  FaTachometerAlt, FaRobot, FaNetworkWired, FaServer, FaChartLine, FaCog, FaSignOutAlt,
  FaBell, FaUserCircle, FaSearch, FaMoon, FaSun, FaClock, FaChartPie, FaMicrochip,
  FaExclamationTriangle, FaCheck, FaTimes, FaGithub, FaLinkedin, FaTwitter
} from 'react-icons/fa';
import { 
  LineChart, Line, BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, 
  CartesianGrid, Legend, RadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis, Radar
} from 'recharts';
import '../css/AdvancedDashboard.css';

const AdvancedDashboard = () => {
  const [darkMode, setDarkMode] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [notificationsOpen, setNotificationsOpen] = useState(false);
  const [profileOpen, setProfileOpen] = useState(false);
  const [missingRoles, setMissingRoles] = useState([]);  
  const [activeTab, setActiveTab] = useState('daily');
  const [showCredDialog, setShowCredDialog] = useState(false);
  const roles = [
    'MS Partner',
    'MW Planner',
    'Operation Team',
    'Deployment Team',
    'I&C Partner'
  ];

  
  const { logout } = useAuth();
  useEffect(() => {
      checkMissingCredentials();
    }, []); 

    const checkMissingCredentials = async () => {
    try {
      const allExistingRoles = new Set();
      let credentialCount = 0;
      // Check credentials for each role
      for (const role of roles) {
        const response = await api.post('/api/sampleUserCredentials/getUserCredentials', { doneBy: role,username: localStorage.getItem('username') });
        console.log('Response for role:', role, response.data);
        if (response.data.length > 0) {
          allExistingRoles.add(role);
          credentialCount++;
        }
      }
      console.log('All existing roles:', allExistingRoles);
       // Determine missing roles
       const missing = roles.filter(role => !allExistingRoles.has(role));
       setMissingRoles(missing);
 
       // Show dialog if missing roles exist
       if (missing.length > 0) {
         setShowCredDialog(true);
         console.log('Missing roles:', missing);
       } 

    } catch (error) {
      console.error('Failed to check credentials:', error);
    }
  };
     
  
  // Mock data
  const data = {
    automationTests: 1250,
    successRate: 98.2,
    executionTime: 45,
    coverage: 85,
    networkHealth: 92,
    systemLoad: 42,
    anomalyCount: 3,
    predictions: [
      { name: 'Next 24h', value: 95 },
      { name: 'Next 7d', value: 92 },
      { name: 'Next 30d', value: 88 }
    ]
  };
  
  const metrics = [
    {
      id: 1,
      title: 'Automation Tests',
      value: data.automationTests,
      change: '+15%',
      icon: <FaRobot />,
      color: 'advanceDashboard-blue'
    },
    {
      id: 2,
      title: 'Success Rate',
      value: `${data.successRate}%`,
      change: '+2.3%',
      icon: <FaChartLine />,
      color: 'advanceDashboard-green'
    },
    {
      id: 3,
      title: 'Execution Time',
      value: `${data.executionTime}s`,
      change: '-5s',
      icon: <FaClock />,
      color: 'advanceDashboard-purple'
    },
    {
      id: 4,
      title: 'Coverage',
      value: `${data.coverage}%`,
      change: '+8%',
      icon: <FaChartPie />,
      color: 'advanceDashboard-orange'
    },
    {
      id: 5,
      title: 'Network Health',
      value: `${data.networkHealth}%`,
      change: '+3%',
      icon: <FaNetworkWired />,
      color: 'advanceDashboard-teal'
    },
    {
      id: 6,
      title: 'System Load',
      value: `${data.systemLoad}%`,
      change: '-7%',
      icon: <FaMicrochip />,
      color: 'advanceDashboard-indigo'
    },
    {
      id: 7,
      title: 'Anomalies Detected',
      value: data.anomalyCount,
      change: '-2',
      icon: <FaExclamationTriangle />,
      color: 'advanceDashboard-red'
    }
  ];

  // Chart data
  const dailyData = [
    { name: '00:00', success: 98, time: 42 },
    { name: '04:00', success: 96, time: 45 },
    { name: '08:00', success: 97, time: 43 },
    { name: '12:00', success: 95, time: 48 },
    { name: '16:00', success: 99, time: 41 },
    { name: '20:00', success: 98, time: 40 },
  ];
  
  const networkData = [
    { name: 'Mon', latency: 42, throughput: 85, health: 92 },
    { name: 'Tue', latency: 38, throughput: 89, health: 95 },
    { name: 'Wed', latency: 45, throughput: 82, health: 88 },
    { name: 'Thu', latency: 40, throughput: 91, health: 93 },
    { name: 'Fri', latency: 48, throughput: 87, health: 85 },
    { name: 'Sat', latency: 41, throughput: 93, health: 90 },
    { name: 'Sun', latency: 43, throughput: 88, health: 92 },
  ];
  
  const testData = [
    { id: 1, scenario: 'SR to SP Conversion', status: 'Passed', duration: '32s', timestamp: '2023-11-15 10:23:45' },
    { id: 2, scenario: 'MO Delivery', status: 'Passed', duration: '28s', timestamp: '2023-11-15 09:45:12' },
    { id: 3, scenario: 'PHY-AT Validation', status: 'Failed', duration: '41s', timestamp: '2023-11-15 08:32:56' },
    { id: 4, scenario: 'TS Complete', status: 'Passed', duration: '38s', timestamp: '2023-11-14 16:45:23' },
    { id: 5, scenario: 'Network Handover', status: 'Warning', duration: '45s', timestamp: '2023-11-14 14:12:34' },
  ];
  
  const systemData = [
    { time: '00:00', cpu: 42, memory: 65, network: 85 },
    { time: '04:00', cpu: 38, memory: 62, network: 88 },
    { time: '08:00', cpu: 65, memory: 75, network: 82 },
    { time: '12:00', cpu: 72, memory: 78, network: 79 },
    { time: '16:00', cpu: 68, memory: 72, network: 85 },
    { time: '20:00', cpu: 55, memory: 68, network: 90 },
  ];
  
  const anomalies = [
    { id: 1, type: 'Network Latency', severity: 'High', detected: '2 hours ago', status: 'Pending' },
    { id: 2, type: 'API Timeout', severity: 'Medium', detected: '5 hours ago', status: 'Investigating' },
    { id: 3, type: 'Test Failure', severity: 'Critical', detected: 'Yesterday', status: 'Resolved' },
    { id: 4, type: 'Resource Spike', severity: 'Medium', detected: 'Nov 12', status: 'Resolved' },
  ];
  
  const testCoverageData = [
    { subject: 'SR to SP', A: 95, fullMark: 100 },
    { subject: 'MO Delivery', A: 88, fullMark: 100 },
    { subject: 'PHY-AT', A: 92, fullMark: 100 },
    { subject: 'TS Complete', A: 98, fullMark: 100 },
    { subject: 'Handover', A: 85, fullMark: 100 },
    { subject: 'Security', A: 90, fullMark: 100 },
  ];
  
  const notifications = [
    { id: 1, title: 'Test Completed', description: 'SR to SP automation test completed', time: '10 mins ago', status: 'success' },
    { id: 2, title: 'Performance Alert', description: 'API response time increased', time: '45 mins ago', status: 'warning' },
    { id: 3, title: 'New Version', description: 'Update v0.0.1 available', time: '2 hours ago', status: 'info' }
  ];

  useEffect(() => {
    // Simulate data loading
    setTimeout(() => {
      setIsLoading(false);
    }, 1500);
  }, []);

  const toggleDarkMode = () => {
    setDarkMode(!darkMode);
  };

  const currentData = activeTab === 'daily' ? dailyData : 
                     activeTab === 'weekly' ? dailyData : dailyData;

  return (
    <div className={`advanceDashboard-dashboard ${darkMode ? 'advanceDashboard-dark' : 'advanceDashboard-light'}`}>
      <div className="advanceDashboard-dashboard-layout">
        {/* Sidebar */}
        <div className="advanceDashboard-sidebar">
          <div className="advanceDashboard-sidebar-header">
            <div className="advanceDashboard-logo">
              <div className="advanceDashboard-logo-icon">T</div>
              <h2>Mids<span>Automation</span></h2>
            </div>
          </div>
          
          <div className="advanceDashboard-menu-group">
            <h3>MAIN</h3>
            <a href="/dashboard" className="advanceDashboard-menu-item active">
              <FaTachometerAlt className="advanceDashboard-icon" />
              <span>Dashboard</span>
            </a>
          </div>


<div className="advanceDashboard-menu-group">
            <h3>AUTOMATION TESTING</h3>
<div className="treeview">
    <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
        e.preventDefault();
        const menuElement = e.currentTarget.nextElementSibling;
        menuElement.style.display = menuElement.style.display === 'none' ? 'block' : 'none';
      }}>
        <FaRobot className="advanceDashboard-icon" />
        <span>Test Scenarios</span>
      </a>
      <ul className="treeview-menu" style={{ display: 'none' }}>
        <li><a href="/automation-testing/basic-test"><span>Basic Test</span></a></li>
        <li><a href="/automation-testing/mw-dpr-track"><span>Dpr Track</span></a></li>
        <li><a href="/automation-testing/mids-test"><span>Mids Test</span></a></li>
      </ul>
  </div>
  <div className="treeview">
    <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
        e.preventDefault();
        const menuElement = e.currentTarget.nextElementSibling;
        menuElement.style.display = menuElement.style.display === 'none' ? 'block' : 'none';
      }}>
      <FaChartLine className="advanceDashboard-icon" />
      <span>Testing Reports</span>
    </a>
    <ul className="treeview-menu" style={{ display: 'none' }}>
      <li><a href="/reports/page-render-report"><span>Page Render Report</span></a></li>
      <li><a href="/reports/download-report"><span>Download Files Report</span></a></li>
      <li><a href="/reports/mids-tests-report"><span>Mids Test Report</span></a></li>
    </ul>
  </div>
</div>


          
          {/* <div className="advanceDashboard-menu-group">
            <h3>AUTOMATION</h3>
<div className="treeview">
    <a href="#" className="advanceDashboard-menu-item">
      <FaRobot className="advanceDashboard-icon" />
      <span>Test Scenarios</span>
    </a>
    <ul className="treeview-menu">
      <li><a href="#"><span>Create Test</span></a></li>
      <li><a href="#"><span>Test List</span></a></li>
      <li><a href="#"><span>Test Groups</span></a></li>
    </ul>
  </div>
  <div className="treeview">
    <a href="#" className="advanceDashboard-menu-item">
      <FaChartLine className="advanceDashboard-icon" />
      <span>Execution History</span>
    </a>
    <ul className="treeview-menu">
      <li><a href="#"><span>Recent Runs</span></a></li>
      <li><a href="#"><span>Reports</span></a></li>
      <li><a href="#"><span>Analytics</span></a></li>
    </ul>
  </div>
  <div className="treeview">
    <a href="#" className="advanceDashboard-menu-item">
      <FaCog className="advanceDashboard-icon" />
      <span>Configuration</span>
    </a>
    <ul className="treeview-menu">
      <li><a href="#"><span>General Settings</span></a></li>
      <li><a href="#"><span>Environment</span></a></li>
      <li><a href="#"><span>Parameters</span></a></li>
    </ul>
  </div>
</div> */}


          
          {/* <div className="advanceDashboard-menu-group">
            <h3>NETWORK</h3>
            <a href="#" className="advanceDashboard-menu-item">
              <FaNetworkWired className="advanceDashboard-icon" />
              <span>Health Monitoring</span>
            </a>
            <a href="#" className="advanceDashboard-menu-item">
              <FaServer className="advanceDashboard-icon" />
              <span>Infrastructure</span>
            </a>
          </div> */}

          
<div className="advanceDashboard-menu-group">
            <h3>SETTING</h3>
<div className="treeview">
    <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
        e.preventDefault();
        const menuElement = e.currentTarget.nextElementSibling;
        menuElement.style.display = menuElement.style.display === 'none' ? 'block' : 'none';
      }}>
        <FaRobot className="advanceDashboard-icon" />
        <span>Profile Management</span>
      </a>
      <ul className="treeview-menu" style={{ display: 'none' }}>
        <li><a href="/profile-management/profile-modify"><span>Profile Modify</span></a></li>
        <li><a href="#"><span>User Report</span></a></li>
        <li><a href="#"><span>Delete Account</span></a></li>
      </ul>
  </div>
  <div className="treeview">
    <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
        e.preventDefault();
        const menuElement = e.currentTarget.nextElementSibling;
        menuElement.style.display = menuElement.style.display === 'none' ? 'block' : 'none';
      }}>
      <FaChartLine className="advanceDashboard-icon" />
      <span>Manage Tester Id Credentials</span>
    </a>
    <ul className="treeview-menu" style={{ display: 'none' }}>
      <li><a href="/profile-management/profile-modify"><span>Modify them</span></a></li>
      <li><a href="#"><span>Delete All</span></a></li>
    </ul>
  </div>
  <div className="treeview">
   <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
        e.preventDefault();
        const menuElement = e.currentTarget.nextElementSibling;
        menuElement.style.display = menuElement.style.display === 'none' ? 'block' : 'none';
      }}>
      <FaCog className="advanceDashboard-icon" />
      <span>Configuration</span>
    </a>
    <ul className="treeview-menu" style={{ display: 'none' }}>
      <li><a href="#"><span>General Settings</span></a></li>
      <li><a href="#"><span>Environment</span></a></li>
      <li><a href="/setting/feedback"><span>Feedback</span></a></li>

    </ul>
  </div>
</div>
          
          <div className="advanceDashboard-menu-group">
            <a href="/login" onClick={logout} className="advanceDashboard-menu-item logout">
              <FaSignOutAlt className="advanceDashboard-icon" />
              <span>Logout</span>
            </a>
          </div>
          
          <div className="advanceDashboard-sidebar-footer">
            <div className="advanceDashboard-version">v0.0.1</div>
            <div className="advanceDashboard-status-indicator">
              <div className="advanceDashboard-status-dot"></div>
              <span>Operational</span>
            </div>
          </div>
        </div>
        
        {/* Main Content */}
        <div className="advanceDashboard-main-content">
          {/* Header */}
          <div className="advanceDashboard-header">
            <div className="advanceDashboard-search-bar">
              <div className="advanceDashboard-search-container">
                <FaSearch className="advanceDashboard-icon" />
                <input type="text" placeholder="Search test cases, scenarios..." />
              </div>
            </div>
            
            <div className="advanceDashboard-header-controls">
              <button 
                className="advanceDashboard-theme-toggle" 
                onClick={toggleDarkMode}
                aria-label="Toggle theme"
              >
                {darkMode ? <FaSun /> : <FaMoon />}
              </button>
              
              <div className="advanceDashboard-dropdown">
                <button className="advanceDashboard-notification-btn" aria-expanded="false">
                  <FaBell />
                  <span className="advanceDashboard-notification-badge">3</span>
                </button>
                <div className="advanceDashboard-dropdown-menu">
                  {notifications.map(notification => (
                    <div key={notification.id} className="advanceDashboard-dropdown-item">
                      <div className="advanceDashboard-notification-content">
                        <h5>{notification.title}</h5>
                        <p>{notification.description}</p>
                        <p className="advanceDashboard-notification-time">{notification.time}</p>
                      </div>
                    </div>
                  ))}
                  <div className="advanceDashboard-dropdown-footer">
                    <a href="#" className="advanceDashboard-view-all">View all</a>
                  </div>
                </div>
              </div>
              
              <button className="advanceDashboard-profile-btn">
                {/* <div className="advanceDashboard-avatar">
                  <FaUserCircle />
                </div> */}
                <div className="advanceDashboard-dropdown">
                <a id="userSettings" className="advanceDashboard-dropdown-toggle" href="#!" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                  <FaUserCircle />
                   <span>Admin User</span>
                </a>
                <div className="advanceDashboard-dropdown-menu">
                  <a className="advanceDashboard-dropdown-item" href="profile.html"><FaUserCircle className="advanceDashboard-icon" />Profile</a>
                  <a className="advanceDashboard-dropdown-item" href="settings.html"><FaCog className="advanceDashboard-icon" />Account Settings</a>
                  <a className="advanceDashboard-dropdown-item" href="login.html"><FaSignOutAlt className="advanceDashboard-icon" />Logout</a>
                </div>
              </div>
                
               
              </button>
            </div>
          </div>
          
          {/* Dashboard Content */}
          <div className="advanceDashboard-content">
            {isLoading ? (
              <div className="advanceDashboard-loading-container">
                <div className="advanceDashboard-spinner"></div>
                <p>Loading mids automation data...</p>
              </div>
            ) : (
              <>
                {/* Metrics Grid */}
                <div className="advanceDashboard-metrics-grid">
                  {metrics.map(metric => (
                    <div key={metric.id} className={`advanceDashboard-metric-card ${metric.color}`}>
                      <div className="advanceDashboard-metric-icon">
                        {metric.icon}
                      </div>
                      <h3>{metric.value}</h3>
                      <p>{metric.title}</p>
                      <div className={`advanceDashboard-metric-change ${metric.change.startsWith('+') ? 'advanceDashboard-positive' : 'advanceDashboard-negative'}`}>
                        {metric.change}
                      </div>
                      <div className="advanceDashboard-metric-glow"></div>
                    </div>
                  ))}
                </div>
                
                {/* Performance Charts */}
                <div className="advanceDashboard-section">
                  <div className="advanceDashboard-section-header">
                    <h3>Test Performance Analytics</h3>
                    <div className="advanceDashboard-tabs">
                      <button 
                        className={`advanceDashboard-tab ${activeTab === 'daily' ? 'advanceDashboard-active' : ''}`}
                        onClick={() => setActiveTab('daily')}
                      >
                        Daily
                      </button>
                      <button 
                        className={`advanceDashboard-tab ${activeTab === 'weekly' ? 'advanceDashboard-active' : ''}`}
                        onClick={() => setActiveTab('weekly')}
                      >
                        Weekly
                      </button>
                      <button 
                        className={`advanceDashboard-tab ${activeTab === 'monthly' ? 'advanceDashboard-active' : ''}`}
                        onClick={() => setActiveTab('monthly')}
                      >
                        Monthly
                      </button>
                    </div>
                  </div>
                  
                  <div className="advanceDashboard-chart-container">
                    <ResponsiveContainer width="100%" height="100%">
                      <BarChart data={currentData}>
                        <CartesianGrid strokeDasharray="3 3" stroke={darkMode ? "#444" : "#eee"} />
                        <XAxis dataKey="name" stroke={darkMode ? "#ccc" : "#666"} />
                        <YAxis stroke={darkMode ? "#ccc" : "#666"} domain={[30, 50]} />
                        <Tooltip 
                          contentStyle={{ 
                            backgroundColor: darkMode ? '#2d3748' : '#fff', 
                            borderColor: darkMode ? '#4a5568' : '#ddd' 
                          }}
                        />
                        <Legend />
                        <Bar 
                          dataKey="time" 
                          fill="#3b82f6" 
                          name="Execution Time (s)"
                          radius={[4, 4, 0, 0]}
                        />
                      </BarChart>
                    </ResponsiveContainer>
                  </div>
                </div>
                
                {/* Network Health and Recent Tests */}
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '25px' }}>
                  {/* Network Health */}
                  <div className="advanceDashboard-section">
                    <div className="advanceDashboard-section-header">
                      <h3>Network Health Monitoring</h3>
                      <a href="#">View Details</a>
                    </div>
                    
                    <div className="advanceDashboard-chart-container">
                      <ResponsiveContainer width="100%" height="100%">
                        <LineChart data={networkData}>
                          <CartesianGrid strokeDasharray="3 3" stroke={darkMode ? "#444" : "#eee"} />
                          <XAxis dataKey="name" stroke={darkMode ? "#ccc" : "#666"} />
                          <YAxis stroke={darkMode ? "#ccc" : "#666"} domain={[80, 100]} />
                          <Tooltip 
                            contentStyle={{ 
                              backgroundColor: darkMode ? '#2d3748' : '#fff', 
                              borderColor: darkMode ? '#4a5568' : '#ddd' 
                            }}
                          />
                          <Legend />
                          <Line 
                            type="monotone" 
                            dataKey="health" 
                            stroke="#10b981" 
                            strokeWidth={2} 
                            name="Network Health (%)"
                            dot={{ r: 4 }}
                            activeDot={{ r: 6, strokeWidth: 0 }}
                          />
                        </LineChart>
                      </ResponsiveContainer>
                    </div>
                  </div>
                  
                  {/* Recent Tests */}
                  <div className="advanceDashboard-section">
                    <div className="advanceDashboard-section-header">
                      <h3>Recent Test Executions</h3>
                      <a href="#">View All</a>
                    </div>
                    
                    <div className="advanceDashboard-table-container">
                      <table className="advanceDashboard-table">
                        <thead>
                          <tr>
                            <th>Test Scenario</th>
                            <th>Status</th>
                            <th>Duration</th>
                            <th>Timestamp</th>
                          </tr>
                        </thead>
                        <tbody>
                          {testData.map(test => (
                            <tr key={test.id}>
                              <td>{test.scenario}</td>
                              <td>
                                <span className={`advanceDashboard-status-badge ${test.status.toLowerCase()}`}>
                                  {test.status}
                                </span>
                              </td>
                              <td>{test.duration}</td>
                              <td>{test.timestamp}</td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
                
                {/* System Health and Anomaly Detection */}
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '25px' }}>
                  {/* System Health */}
                  <div className="advanceDashboard-section">
                    <div className="advanceDashboard-section-header">
                      <h3>System Health Monitoring</h3>
                      <a href="#">View Details</a>
                    </div>
                    
                    <div className="advanceDashboard-chart-container">
                      <ResponsiveContainer width="100%" height="100%">
                        <LineChart data={systemData}>
                          <CartesianGrid strokeDasharray="3 3" stroke={darkMode ? "#444" : "#eee"} />
                          <XAxis dataKey="time" stroke={darkMode ? "#ccc" : "#666"} />
                          <YAxis stroke={darkMode ? "#ccc" : "#666"} domain={[30, 90]} />
                          <Tooltip 
                            contentStyle={{ 
                              backgroundColor: darkMode ? '#2d3748' : '#fff', 
                              borderColor: darkMode ? '#4a5568' : '#ddd' 
                            }}
                          />
                          <Legend />
                          <Line 
                            type="monotone" 
                            dataKey="cpu" 
                            stroke="#ef4444" 
                            strokeWidth={2} 
                            name="CPU Usage (%)"
                          />
                          <Line 
                            type="monotone" 
                            dataKey="memory" 
                            stroke="#3b82f6" 
                            strokeWidth={2} 
                            name="Memory (%)"
                          />
                          <Line 
                            type="monotone" 
                            dataKey="network" 
                            stroke="#10b981" 
                            strokeWidth={2} 
                            name="Network (%)"
                          />
                        </LineChart>
                      </ResponsiveContainer>
                    </div>
                  </div>
                  
                  {/* Anomaly Detection */}
                  <div className="advanceDashboard-section">
                    <div className="advanceDashboard-section-header">
                      <h3>Anomaly Detection</h3>
                      <div>
                        <span className="advanceDashboard-status-badge advanceDashboard-failed" style={{ marginRight: '10px' }}>3 Critical</span>
                        <span className="advanceDashboard-status-badge advanceDashboard-warning">5 High</span>
                      </div>
                    </div>
                    
                    <div style={{ height: '300px', overflowY: 'auto' }}>
                      {anomalies.map(anomaly => (
                        <div key={anomaly.id} style={{ 
                          display: 'flex', 
                          alignItems: 'center', 
                          padding: '12px 0',
                          borderBottom: darkMode ? '1px solid #334155' : '1px solid #e2e8f0'
                        }}>
                          <div style={{ 
                            width: '32px', 
                            height: '32px', 
                            borderRadius: '50%', 
                            background: anomaly.severity === 'Critical' ? 'rgba(239, 68, 68, 0.1)' : 
                                        anomaly.severity === 'High' ? 'rgba(245, 158, 11, 0.1)' : 'rgba(59, 130, 246, 0.1)',
                            display: 'flex', 
                            alignItems: 'center', 
                            justifyContent: 'center',
                            marginRight: '15px',
                            color: anomaly.severity === 'Critical' ? '#ef4444' : 
                                  anomaly.severity === 'High' ? '#f59e0b' : '#3b82f6'
                          }}>
                            {anomaly.severity === 'Critical' ? <FaTimes /> : <FaExclamationTriangle />}
                          </div>
                          <div style={{ flex: 1 }}>
                            <div style={{ fontWeight: '500' }}>{anomaly.type}</div>
                            <div style={{ display: 'flex', gap: '10px', fontSize: '13px', color: darkMode ? '#94a3b8' : '#64748b' }}>
                              <span>{anomaly.severity} severity</span>
                              <span>Detected: {anomaly.detected}</span>
                            </div>
                          </div>
                          <div style={{ 
                            padding: '4px 10px', 
                            borderRadius: '20px', 
                            background: anomaly.status === 'Resolved' ? 'rgba(16, 185, 129, 0.1)' : 
                                        anomaly.status === 'Investigating' ? 'rgba(59, 130, 246, 0.1)' : 'rgba(245, 158, 11, 0.1)',
                            color: anomaly.status === 'Resolved' ? '#10b981' : 
                                  anomaly.status === 'Investigating' ? '#3b82f6' : '#f59e0b',
                            fontSize: '13px'
                          }}>
                            {anomaly.status}
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                </div>
                
                {/* Footer */}
                <div className="advanceDashboard-footer">
                  <div className="advanceDashboard-footer-content">
                    <div>
                      <h4>Mids Automation Dashboard</h4>
                      <p>Advanced analytics for mids test automation and network monitoring</p>
                      <div className="advanceDashboard-footer-links">
                        <a href="#">Privacy Policy</a>
                        <a href="#">Terms of Service</a>
                        <a href="#">Documentation</a>
                      </div>
                    </div>
                    
                    <div>
                      <div className="advanceDashboard-status-indicator" style={{ justifyContent: 'flex-end', marginBottom: '10px' }}>
                        <div className="advanceDashboard-status-dot"></div>
                        <span>Operational</span>
                      </div>
                      <div className="advanceDashboard-social-links">
                        <a href="#"><FaGithub /></a>
                        <a href="#"><FaLinkedin /></a>
                        <a href="#"><FaTwitter /></a>
                      </div>
                    </div>
                  </div>
                  
                  <div className="advanceDashboard-footer-bottom">
                    <p>© {new Date().getFullYear()} Mids Automation Inc. All rights reserved.</p>
                  </div>
                </div>
              </>
            )}
          </div>
        </div>
      </div>
       <RoleCredentialsDialog
  open={showCredDialog}
  missingRoles={missingRoles}  
  onClose={(success) => {
    setShowCredDialog(false);
    localStorage.removeItem('checkingCredentials');
    // Only now navigate to the dashboard
  }}
/>
    </div>
  );
};

export default AdvancedDashboard;