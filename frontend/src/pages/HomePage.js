// src/components/HomePage.js
import React from 'react';
import { FiActivity, FiServer, FiZap, FiBox, FiDatabase } from 'react-icons/fi';
import { useAuth } from '../context/AuthContext';
import '../css/HomePage.css';

const HomePage = () => {
  // Mock data
  const metrics = {
    executions: { current: '24.8K', trend: 12.5 },
    coverage: 98.4,
    efficiency: '2.4s',
    uptime: '99.99%'
  };

  const testStats = [
    { category: 'Unit', value: 70, color: '#6366f1' },
    { category: 'Integration', value: 20, color: '#10b981' },
    { category: 'E2E', value: 10, color: '#f59e0b' }
  ];

  return (
    <div className="automation-dashboard">
      {/* Floating Background Elements */}
      <div className="cyber-gradient"></div>
      <div className="particle-network"></div>
      
      {/* Main Grid */}
      <div className="dashboard-grid">
        
        {/* Header Section */}
        <header className="dashboard-header">
          <div className="branding">
            <span className="logo-mark"></span>
            <h1>Automation<span className="gradient-text">Nexus</span></h1>
          </div>
          <div className="global-actions">
            <button className="ai-pilot">
              <span className="neon-pulse"></span>
              AI Pilot
            </button>
            <div className="user-panel">
              <div className="user-avatar"></div>
            </div>
          </div>
        </header>

        {/* Primary Metrics */}
        <section className="metrics-panel">
          <div className="metric-card cyber">
            <FiZap className="metric-icon" />
            <div className="metric-data">
              <h3>Executions</h3>
              <div className="metric-value">{metrics.executions.current}</div>
              <div className="metric-trend positive">
                +{metrics.executions.trend}% <FiActivity />
              </div>
            </div>
            <div className="metric-graph"></div>
          </div>

          <div className="metric-card hologram">
            <FiDatabase className="metric-icon" />
            <div className="metric-data">
              <h3>Coverage</h3>
              <div className="metric-value">{metrics.coverage}%</div>
              <div className="coverage-map"></div>
            </div>
          </div>
        </section>

        {/* Automation Matrix */}
        <section className="automation-matrix">
          <div className="matrix-header">
            <h2>Test Orchestration</h2>
            <div className="environment-selector">
              <span className="env-dot production"></span>
              Production-Ready
            </div>
          </div>
          
          <div className="matrix-grid">
            <div className="matrix-card ai-optimizer">
              <FiBox className="matrix-icon" />
              <h3>AI Test Generation</h3>
              <div className="spark-bar"></div>
            </div>
            
            <div className="matrix-card realtime-monitor">
              <FiServer className="matrix-icon" />
              <h3>Infrastructure Health</h3>
              <div className="server-cluster">
                <div className="server-node"></div>
                <div className="server-node"></div>
                <div className="server-node"></div>
              </div>
            </div>
          </div>
        </section>

        {/* Analytics Nexus */}
        <section className="analytics-nexus">
          <div className="nexus-header">
            <h2>Intelligence Platform</h2>
            <div className="timeframe-selector">
              <button className="active">24H</button>
              <button>7D</button>
              <button>30D</button>
            </div>
          </div>
          
          <div className="nexus-grid">
            <div className="analytics-card deep-dive">
              <div className="chart-container">
                {/* Custom chart implementation */}
                <div className="pyramid-chart">
                  {testStats.map((stat, index) => (
                    <div 
                      key={index}
                      className="pyramid-layer"
                      style={{
                        width: `${stat.value}%`,
                        backgroundColor: stat.color,
                        zIndex: 3 - index
                      }}
                    >
                      <span className="layer-label">{stat.category}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>
            
            <div className="analytics-card risk-predictor">
              <div className="threat-level">
                <div className="risk-gauge"></div>
                <div className="risk-stats">
                  <h3 className="gradient-text">Low Risk</h3>
                  <p>Predicted Stability</p>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>
  );
};

export default HomePage;