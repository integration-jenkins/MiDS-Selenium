import React, { useState } from 'react';
import { FaUserCircle, FaChartLine, FaRobot,FaMoon,FaSun, FaCog, FaComments, FaRegChartBar } from 'react-icons/fa';
import { LineChart, Line, BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import '../css/Dashboard.css';

const data = [
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

const Dashboard = () => {
    const [isDarkMode, setIsDarkMode] = useState(false);
    const [menuOpen, setMenuOpen] = useState(false);
  return (
    <div className={`dashboard-container ${isDarkMode ? 'dark' : 'light'}`}>
    <div className="background-blobs">
      {/* Same blob background as login */}
      </div>

      <nav className="glass-nav">
        <div className="nav-brand">
          <h1 className="dashboard-title">
            <span className="gradient-text">MIDS AUTOMATION</span>
          </h1>
        </div>
        
        <div className="nav-controls">
          <div className="theme-toggle" onClick={() => setIsDarkMode(!isDarkMode)}>
            {isDarkMode ? <FaSun /> : <FaMoon />}
          </div>
          
          <div className="nav-menu">
            <div className="nav-item" onMouseEnter={() => setMenuOpen(true)} onMouseLeave={() => setMenuOpen(false)}>
              <FaUserCircle className="nav-icon" />
              {menuOpen && (
                <div className="profile-menu">
                  <div className="menu-item">Profile</div>
                  <div className="menu-item">Settings</div>
                  <div className="menu-item">Logout</div>
                </div>
              )}
            </div>
            <div className="nav-item"><FaComments className="nav-icon" /></div>
            <div className="nav-item"><FaCog className="nav-icon" /></div>
          </div>
        </div>
      </nav>

      <div className="main-content">
        <div className="action-cards">
          <div className="glass-card main-action">
            <FaRobot className="action-icon" />
            <h2>Automation Testing</h2>
            <p>Run full scenario tests and validate workflows</p>
            <div className="stats">
              <div className="stat">
                <div className="stat-value">98%</div>
                <div className="stat-label">Success Rate</div>
              </div>
              <div className="stat">
                <div className="stat-value">45s</div>
                <div className="stat-label">Avg. Runtime</div>
              </div>
            </div>
          </div>

          <div className="glass-card main-action">
            <FaChartLine className="action-icon" />
            <h2>Automation Analytics</h2>
            <p>Analyze performance and optimization metrics</p>
            <div className="stats">
              <div className="stat">
                <div className="stat-value">32%</div>
                <div className="stat-label">Efficiency Gain</div>
              </div>
              <div className="stat">
                <div className="stat-value">1.4x</div>
                <div className="stat-label">Speed Boost</div>
              </div>
            </div>
          </div>
        </div>

        <div className="charts-grid">
          <div className="glass-card chart-card">
            <h3>Performance Trends</h3>
            <div className="chart-container">
              <ResponsiveContainer width="100%" height={300}>
                <LineChart data={data}>
                  <XAxis dataKey="name" stroke="#fff" />
                  <YAxis stroke="#fff" />
                  <Tooltip 
                    contentStyle={{ 
                      background: 'rgba(255, 255, 255, 0.1)',
                      border: 'none',
                      borderRadius: '8px',
                      backdropFilter: 'blur(10px)'
                    }}
                  />
                  <Line 
                    type="monotone" 
                    dataKey="efficiency" 
                    stroke="#6366f1" 
                    strokeWidth={2}
                    dot={{ fill: '#4f46e5' }}
                  />
                  <Line 
                    type="monotone" 
                    dataKey="engagement" 
                    stroke="#10b981" 
                    strokeWidth={2}
                    dot={{ fill: '#059669' }}
                  />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </div>

          <div className="glass-card chart-card">
            <h3>Test Success Rates</h3>
            <div className="chart-container">
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={testResults}>
                  <XAxis dataKey="scenario" stroke="#fff" />
                  <YAxis stroke="#fff" />
                  <Tooltip 
                    contentStyle={{ 
                      background: 'rgba(255, 255, 255, 0.1)',
                      border: 'none',
                      borderRadius: '8px',
                      backdropFilter: 'blur(10px)'
                    }}
                  />
                  <Bar 
                    dataKey="success" 
                    fill="#6366f1" 
                    radius={[4, 4, 0, 0]}
                  />
                  <Bar 
                    dataKey="failures" 
                    fill="#f472b6" 
                    radius={[4, 4, 0, 0]}
                  />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>
        </div>

        <div className="quick-info">
          <div className="glass-card system-health">
            <h3>System Health</h3>
            <div className="health-metric">
              <div className="metric-label">API Response</div>
              <div className="metric-value success">98ms</div>
            </div>
            <div className="health-metric">
              <div className="metric-label">Test Queue</div>
              <div className="metric-value warning">12 pending</div>
            </div>
          </div>

          <div className="glass-card recent-activity">
            <h3>Recent Activity</h3>
            <div className="activity-item">
              <div className="activity-icon success">✓</div>
              <div className="activity-text">
                SR → SP test completed (0.8s)
              </div>
            </div>
            <div className="activity-item">
              <div className="activity-icon failed">✕</div>
              <div className="activity-text">
                PHY-AT validation failed (retry)
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;