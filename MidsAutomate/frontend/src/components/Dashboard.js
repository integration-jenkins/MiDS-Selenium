import React, { useState } from 'react';
import { FaUserCircle, FaChartLine, FaRobot, FaMoon, FaSun, FaCog, FaComments } from 'react-icons/fa';
import { LineChart, Line, BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import '../css/Dashboard.css';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useEffect } from 'react';
import RoleCredentialsDialog from '../components/RoleCredentialsDialog';
import api from '../api/axiosConfig';
const Dashboard = () => {
  const [isDarkMode, setIsDarkMode] = useState(false);
  const [menuOpen, setMenuOpen] = useState(false);
  const [showCredDialog, setShowCredDialog] = useState(false);
  const [missingRoles, setMissingRoles] = useState([]);
  
  const navigate = useNavigate();
   const { logout } = useAuth();
  const handleTestCardClick=()=>{
    navigate('/testing'); 
  }
  const roles = [
    'MS Partner',
    'MW Planner',
    'Operation Team',
    'Deployment Team',
    'I&C Partner'
  ];
  // useEffect(() => {
  //   const checkCredentials = async () => {
  //     await checkMissingCredentials();
  //   };
  //   checkCredentials();
  // }, [showCredDialog, missingRoles, navigate]);
  useEffect(() => {
    // Check credentials when component mounts
    checkMissingCredentials();
  }, []); // Don't include showCredDialog or missingRoles to prevent infinite loops
  

  const checkMissingCredentials = async () => {
    try {
      const allExistingRoles = new Set();
      let credentialCount = 0;
      // Check credentials for each role
      for (const role of roles) {
        const response = await api.post('/api/sampleUserCredentials/getUserCredentials', { doneBy: role });
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
       } else {
        //  navigate('/dashboard');
       }

    } catch (error) {
      console.error('Failed to check credentials:', error);
    }
  };

  const chartData = [
    { name: 'Jan', efficiency: 65, engagement: 75 },
    { name: 'Feb', efficiency: 78, engagement: 82 },
    { name: 'Mar', efficiency: 82, engagement: 88 },
    { name: 'Apr', efficiency: 90, engagement: 85 },
    { name: 'May', efficiency: 88, engagement: 90 },
  ];

  const testResults = [
    { scenario: 'SR to SP', success: 98, failures: 2 },
    { scenario: 'MO Delivery', success: 95, failures: 5 },
    { scenario: 'PHY-AT', success: 92, failures: 8 },
    { scenario: 'TS Complete', success: 99, failures: 1 },
  ];

  return (
    <div className={`dashboard-container ${isDarkMode ? 'dark' : 'light'}`}>
      <div className="dashboard-background">
        <div className="gradient-overlay"></div>
        <div className="particle-effect"></div>
      </div>

      <nav className="dashboard-nav">
  <div className="nav-brand">
    <h1 className="gradient-logo">MIDS AUTOMATION</h1>
  </div>

  <div className="nav-controls">
   

    <div className="nav-menu">
      <button className="nav-item">
        <FaComments className="nav-icon" />
      </button>
    
      <button className="nav-item">
        <FaCog className="nav-icon" />
      </button>
      <div 
        className="nav-item profile-menu" 
        onMouseEnter={() => setMenuOpen(true)}
        onMouseLeave={() => setMenuOpen(false)}
      >
        <FaUserCircle className="nav-icon" />
        {menuOpen && (
          <div className="dropdown-menu">
            <button className="menu-item">Profile</button>
            <button className="menu-item">Settings</button>
            <button className="menu-item" onClick={logout}>Logout</button>
          </div>
        )}
      </div>
      <button 
      className="theme-of-toggle" 
      onClick={() => setIsDarkMode(!isDarkMode)}
      aria-label="Toggle theme"
    >
      {isDarkMode ? <FaSun /> : <FaMoon />}
    </button>

    </div>
  </div>
</nav>

      <main className="dashboard-content">
        <div className="metrics-grid">
          <div className="metric-card primary"  onClick={handleTestCardClick}>
            <div className="card-header">
              <FaRobot className="card-icon" />
              <h2>Automation Testing</h2>
            </div>
            <div className="card-stats">
              <div className="stat">
                <div className="stat-value">98%</div>
                <div className="stat-label">Success Rate</div>
              </div>
              <div className="stat">
                <div className="stat-value">45s</div>
                <div className="stat-label">Avg. Runtime</div>
              </div>
            </div>
            <div className="card-glow"></div>
          </div>

          <div className="metric-card secondary">
            <div className="card-header">
              <FaChartLine className="card-icon" />
              <h2>Automation Analytics</h2>
            </div>
            <div className="card-stats">
              <div className="stat">
                <div className="stat-value">32%</div>
                <div className="stat-label">Efficiency Gain</div>
              </div>
              <div className="stat">
                <div className="stat-value">1.4x</div>
                <div className="stat-label">Speed Boost</div>
              </div>
            </div>
            <div className="card-glow"></div>
          </div>
        </div>

        <div className="visualization-grid">
          <div className="chart-card">
            <h3>Performance Trends</h3>
            <div className="chart-container">
              <ResponsiveContainer width="100%" height={300}>
                <LineChart data={chartData}>
                  <XAxis 
                    dataKey="name" 
                    stroke={isDarkMode ? '#e2e8f0' : '#1e293b'} 
                    tickLine={false}
                  />
                  <YAxis 
                    stroke={isDarkMode ? '#e2e8f0' : '#1e293b'} 
                    tickLine={false}
                  />
                  <Tooltip 
                    content={({ payload }) => (
                      <div className="custom-tooltip">
                        {payload?.map((entry, index) => (
                          <div key={index} className="tooltip-item">
                            <span className="tooltip-label">{entry.name}</span>
                            <span className="tooltip-value">{entry.value}%</span>
                          </div>
                        ))}
                      </div>
                    )}
                  />
                  <Line 
                    type="monotone" 
                    dataKey="efficiency" 
                    stroke="url(#efficiencyGradient)"
                    strokeWidth={2}
                    dot={false}
                  />
                  <Line 
                    type="monotone" 
                    dataKey="engagement" 
                    stroke="url(#engagementGradient)"
                    strokeWidth={2}
                    dot={false}
                  />
                  <defs>
                    <linearGradient id="efficiencyGradient" x1="0" y1="0" x2="1" y2="0">
                      <stop offset="0%" stopColor="#6366f1" />
                      <stop offset="100%" stopColor="#8b5cf6" />
                    </linearGradient>
                    <linearGradient id="engagementGradient" x1="0" y1="0" x2="1" y2="0">
                      <stop offset="0%" stopColor="#10b981" />
                      <stop offset="100%" stopColor="#059669" />
                    </linearGradient>
                  </defs>
                </LineChart>
              </ResponsiveContainer>
            </div>
          </div>

          <div className="chart-card">
            <h3>Test Success Rates</h3>
            <div className="chart-container">
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={testResults}>
                  <XAxis 
                    dataKey="scenario" 
                    stroke={isDarkMode ? '#e2e8f0' : '#1e293b'} 
                    tickLine={false}
                  />
                  <YAxis 
                    stroke={isDarkMode ? '#e2e8f0' : '#1e293b'} 
                    tickLine={false}
                  />
                  <Tooltip 
                    content={({ payload }) => (
                      <div className="custom-tooltip">
                        {payload?.map((entry, index) => (
                          <div key={index} className="tooltip-item">
                            <span className="tooltip-label">{entry.name}</span>
                            <span className="tooltip-value">{entry.value}%</span>
                          </div>
                        ))}
                      </div>
                    )}
                  />
                  <Bar 
                    dataKey="success" 
                    fill="url(#successGradient)"
                    radius={[4, 4, 0, 0]}
                  />
                  <Bar 
                    dataKey="failures" 
                    fill="url(#failureGradient)"
                    radius={[4, 4, 0, 0]}
                  />
                  <defs>
                    <linearGradient id="successGradient" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="0%" stopColor="#6366f1" />
                      <stop offset="100%" stopColor="#4f46e5" />
                    </linearGradient>
                    <linearGradient id="failureGradient" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="0%" stopColor="#f472b6" />
                      <stop offset="100%" stopColor="#db2777" />
                    </linearGradient>
                  </defs>
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>
        </div>

        <div className="status-grid">
          <div className="status-card system-health">
            <h3>System Health</h3>
            <div className="health-metrics">
              <div className="metric">
                <div className="metric-label">API Response</div>
                <div className="metric-value success">98ms</div>
              </div>
              <div className="metric">
                <div className="metric-label">Test Queue</div>
                <div className="metric-value warning">12 pending</div>
              </div>
            </div>
          </div>

          <div className="status-card recent-activity">
            <h3>Recent Activity</h3>
            <div className="activity-list">
              <div className="activity-item success">
                <div className="activity-icon">✓</div>
                <div className="activity-text">SR → SP test completed (0.8s)</div>
              </div>
              <div className="activity-item failed">
                <div className="activity-icon">✕</div>
                <div className="activity-text">PHY-AT validation failed (retry)</div>
              </div>
            </div>
          </div>
        </div>
      </main>
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

export default Dashboard;