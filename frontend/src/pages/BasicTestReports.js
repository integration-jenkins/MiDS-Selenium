import { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import "../css/BasicTestReports.css";
import { FiSun, FiMoon, FiX } from "react-icons/fi";

const BasicTestReports = () => {
  const [reports, setReports] = useState([]);
  const [isDarkMode, setIsDarkMode] = useState(false);
  const [selectedReport, setSelectedReport] = useState(null);

  useEffect(() => {
    const fetchReports = async () => {
      try {
        const response = await api.get("/api/basic-report/all");
        console.log("Fetched basic test reports:", response.data);
        setReports(response.data);
      } catch (error) {
        console.error("Error fetching basic test reports:", error);
      }
    };
    fetchReports();
  }, []);

  const totalTests = reports.length;
  const successRate = (
    (reports.filter((report) => report.lastExecutionStatus === "Success").length /
      totalTests) *
      100 || 0
  ).toFixed(1);

  const handleRowClick = (report) => {
    setSelectedReport(report);
  };

  const closeDialog = () => {
    setSelectedReport(null);
  };

  return (
    <div className={`reports-container ${isDarkMode ? 'dark-mode' : 'light-mode'}`}>
      <button
        className="theme-toggle"
        onClick={() => setIsDarkMode(!isDarkMode)}
        aria-label="Toggle theme"
      >
       {isDarkMode ? <FiSun size={24} /> : <FiMoon size={24} />}
       </button>
      <div className="reports-header">
        <h1>Basic Test Reports</h1>
        <div className="stats-grid">
          <div className="stat-card">
            <h3>Total Tests</h3>
            <p>{totalTests}</p>
          </div>
          <div className="stat-card">
            <h3>Success Rate</h3>
            <p>{successRate}%</p>
          </div>
        </div>
      </div>

      {reports.length === 0 ? (
        <div className="no-reports">No reports found.</div>
      ) : (
        <table className="reports-table">
          <thead>
            <tr>
              <th>SNo</th>
              <th>Page Name</th>
              <th>Last Time Taken (sec)</th>
              <th>Average Time Taken (sec)</th>
              <th>Comments</th>
              <th>Last Execution Date</th>
              <th>Last Execution Status</th>
              <th>Last Execution By</th>
              <th>No. of Tests</th>
            </tr>
          </thead>
          <tbody>
            {reports.map((report, index) => (
              <tr key={report.basicTestId} onClick={() => handleRowClick(report)}>
                <td>{index + 1}</td>
                <td>{report.pageName}</td>
                <td>{report.lastTimeTakenMS/1000}</td>
                <td>{report.averageTimeTakenMS/1000}</td>
                <td>{report.comments}</td>
                <td>{new Date(report.lastExecutionDate).toLocaleString()}</td>
                <td>
                  <span
                    className={`status-badge status-${report.lastExecutionStatus.toLowerCase()}`}
                  >
                    {report.lastExecutionStatus}
                  </span>
                </td>
                <td>{report.lastExecutionBy}</td>
                <td>{report.noOfTest}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
{selectedReport && (
        <div className="dialog-overlay">
          <div className="dialog-box slide-in">
            <div className="dialog-header">
              <h2>{selectedReport.pageName} Details</h2>
              <button onClick={closeDialog} className="close-btn">
                <FiX size={24} />
              </button>
            </div>
            <div className="dialog-content">
              <div className="detail-grid">
                <div className="detail-card">
                  <label>Performance</label>
                  <div className="metric-group">
                    <div className="metric">
                      <span className="metric-label">Last Execution</span>
                      <span className="metric-value">
                        {selectedReport.lastTimeTakenMS}ms
                      </span>
                    </div>
                    <div className="metric">
                      <span className="metric-label">Average</span>
                      <span className="metric-value">
                        {selectedReport.averageTimeTakenMS}ms
                      </span>
                    </div>
                  </div>
                </div>

                <div className="detail-card status-card">
                  <label>Current Status</label>
                  <div className={`status-indicator ${selectedReport.lastExecutionStatus.toLowerCase()}`}>
                    {selectedReport.lastExecutionStatus}
                  </div>
                </div>

                <div className="detail-card">
                  <label>Last Executed</label>
                  <p className="detail-text">
                    {new Date(selectedReport.lastExecutionDate).toLocaleString()}
                  </p>
                  <p className="detail-text">
                    By {selectedReport.lastExecutionBy}
                  </p>
                </div>

                <div className="detail-card">
                  <label>Test Statistics</label>
                  <div className="progress-bar">
                    <div 
                      className="progress-fill"
                      style={{ width: `${(selectedReport.noOfTest / 10) * 100}%` }}
                    ></div>
                    <span>{selectedReport.noOfTest} tests performed</span>
                  </div>
                </div>
              </div>
              
              <div className="comments-section">
                <h4>Comments</h4>
                <p className="comment-text">{selectedReport.comments}</p>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default BasicTestReports;