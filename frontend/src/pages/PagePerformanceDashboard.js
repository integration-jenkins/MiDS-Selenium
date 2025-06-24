import React from 'react';
import { BarChart, Bar, PieChart, Pie, Cell, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import '../css/PagePerformanceDashboard.css';

const PagePerformanceDashboard = () => {
  // Sample data (you would replace this with your API data)
  const pageData = [
    { id: 1, pageName: "Login Page", lastTime: 30.027, avgTime: 24.812, status: "Failed", tests: 36 },
    { id: 2, pageName: "Dashboard Page", lastTime: 45.386, avgTime: 71.049, status: "Success", tests: 12 },
    { id: 3, pageName: "Deployment Dashboard", lastTime: 30.505, avgTime: 55.258, status: "Success", tests: 11 },
    { id: 4, pageName: "RAN MW Page", lastTime: 33.624, avgTime: 21.769, status: "Success", tests: 9 },
    { id: 5, pageName: "POP Info Page", lastTime: 26.778, avgTime: 23.306, status: "Failed", tests: 9 },
    { id: 6, pageName: "DPR Report Page", lastTime: 0.006, avgTime: 13.439, status: "Failed", tests: 9 },
    { id: 7, pageName: "Traffic Upload", lastTime: 0.011, avgTime: 180.485, status: "Failed", tests: 8 },
    { id: 8, pageName: "Traffic Report", lastTime: 0.005, avgTime: 2.379, status: "Failed", tests: 8 },
  ];

  // Calculate statistics
  const successCount = pageData.filter(page => page.status === "Success").length;
  const failureCount = pageData.filter(page => page.status === "Failed").length;
  const totalPages = pageData.length;
  const avgPageLoad = (pageData.reduce((sum, page) => sum + page.avgTime, 0) / totalPages).toFixed(2);
  
  // Prepare data for charts
  const performanceData = [...pageData]
    .sort((a, b) => b.avgTime - a.avgTime)
    .slice(0, 8)
    .map(page => ({
      name: page.pageName,
      "Last Time": page.lastTime,
      "Avg Time": page.avgTime
    }));

  const statusData = [
    { name: "Success", value: successCount },
    { name: "Failed", value: failureCount }
  ];

  const testCountData = [...pageData]
    .sort((a, b) => b.tests - a.tests)
    .slice(0, 6)
    .map(page => ({
      name: page.pageName,
      tests: page.tests
    }));

  const COLORS = ['#00C49F', '#FF8042', '#FFBB28', '#0088FE', '#FF0000', '#00FF00'];
  const STATUS_COLORS = { "Success": "#00C49F", "Failed": "#FF8042" };

  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="custom-tooltip">
          <p className="tooltip-label">{payload[0].payload.name}</p>
          <p className="tooltip-value">Last Time: {payload[0].value}s</p>
          <p className="tooltip-value">Avg Time: {payload[1].value}s</p>
        </div>
      );
    }
    return null;
  };

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1 className="gradient-text">Page Performance Dashboard</h1>
        <p>Visual analysis of page load times and test results</p>
      </div>
      
      <div className="summary-cards">
        <div className="glass-card summary-card">
          <div className="card-value">{totalPages}</div>
          <div className="card-label">Total Pages</div>
        </div>
        
        <div className="glass-card summary-card">
          <div className="card-value">{successCount}</div>
          <div className="card-label">Successful Pages</div>
        </div>
        
        <div className="glass-card summary-card">
          <div className="card-value">{failureCount}</div>
          <div className="card-label">Failed Pages</div>
        </div>
        
        <div className="glass-card summary-card">
          <div className="card-value">{avgPageLoad}s</div>
          <div className="card-label">Avg Load Time</div>
        </div>
      </div>
      
      <div className="charts-grid">
        <div className="glass-card chart-card">
          <h3>Page Load Performance (Top 8)</h3>
          <div className="chart-container">
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={performanceData}>
                <XAxis dataKey="name" angle={-45} textAnchor="end" height={60} />
                <YAxis label={{ value: 'Seconds', angle: -90, position: 'insideLeft' }} />
                <Tooltip content={<CustomTooltip />} />
                <Legend />
                <Bar dataKey="Last Time" fill="#8884d8" />
                <Bar dataKey="Avg Time" fill="#82ca9d" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
        
        <div className="glass-card chart-card">
          <h3>Success/Failure Distribution</h3>
          <div className="chart-container">
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={statusData}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  outerRadius={100}
                  fill="#8884d8"
                  dataKey="value"
                  label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                >
                  {statusData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={STATUS_COLORS[entry.name]} />
                  ))}
                </Pie>
                <Tooltip />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>
        
        <div className="glass-card chart-card">
          <h3>Most Tested Pages</h3>
          <div className="chart-container">
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={testCountData}>
                <XAxis dataKey="name" angle={-45} textAnchor="end" height={60} />
                <YAxis label={{ value: 'Test Count', angle: -90, position: 'insideLeft' }} />
                <Tooltip />
                <Legend />
                <Bar dataKey="tests" fill="#0088FE" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
        
        <div className="glass-card chart-card">
          <h3>Page Status Overview</h3>
          <div className="status-list">
            {pageData.map(page => (
              <div key={page.id} className="status-item">
                <div className="page-name">{page.pageName}</div>
                <div className={`status-badge ${page.status.toLowerCase()}`}>
                  {page.status}
                </div>
                <div className="time-info">
                  <div>Last: {page.lastTime}s</div>
                  <div>Avg: {page.avgTime}s</div>
                </div>
                <div className="test-count">{page.tests} tests</div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default PagePerformanceDashboard;