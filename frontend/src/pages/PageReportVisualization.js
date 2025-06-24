import { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import Layout from "../components/Layout";
import "../css/PageReportVisualization.css";
import { FiX } from "react-icons/fi";
import { Bar, Pie, Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  ArcElement,
  Title,
  Tooltip,
  Legend
);

const PageReportVisualization = () => {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedReport, setSelectedReport] = useState(null);
  const maxRetries = 3;
  const retryDelay = 2000;

  const fetchReports = async (retryCount = 0) => {
    setLoading(true);
    try {
      const response = await api.get("/api/basic-report/all");
      console.log("Fetched page report visualizations:", response.data);
      setReports(response.data);
      setError(null);
    } catch (err) {
      console.error("Error fetching page report visualizations:", err);
      if (retryCount < maxRetries) {
        setTimeout(() => fetchReports(retryCount + 1), retryDelay);
      } else {
        setError("Failed to fetch reports. Please try again later.");
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchReports();
  }, []);

  const totalTests = reports.length;
  const successRate = (
    (reports.filter((report) => report.lastExecutionStatus === "Success").length /
      totalTests) *
      100 || 0
  ).toFixed(1);
  const avgExecutionTime = (
    reports.reduce((sum, report) => sum + report.averageTimeTakenMS, 0) /
      totalTests || 0
  ).toFixed(2);

  const handleRowClick = (report) => {
    setSelectedReport(report);
  };

  const closeDialog = () => {
    setSelectedReport(null);
  };

  // Bar Chart Data: Average Time Taken by Page
  const barData = {
    labels: reports.map((report) => report.pageName),
    datasets: [
      {
        label: "Average Time Taken (sec)",
        data: reports.map((report) => (report.averageTimeTakenMS / 1000).toFixed(2)),
        backgroundColor: "rgba(37, 99, 235, 0.6)",
        borderColor: "rgba(37, 99, 235, 1)",
        borderWidth: 1,
      },
    ],
  };

  const barOptions = {
    responsive: true,
    plugins: {
      legend: { position: "top", labels: { color: "#64748b" } },
      title: { display: true, text: "Average Time Taken by Page", color: "#64748b" },
      tooltip: { backgroundColor: "rgba(30, 41, 59, 0.9)", titleColor: "#fff", bodyColor: "#fff" },
    },
    scales: {
      x: { ticks: { color: "#64748b" }, grid: { display: false } },
      y: { ticks: { color: "#64748b" }, grid: { color: "rgba(255, 255, 255, 0.1)" } },
    },
  };

  // Pie Chart Data: Test Status Distribution
  const statusCounts = reports.reduce(
    (acc, report) => {
      acc[report.lastExecutionStatus] = (acc[report.lastExecutionStatus] || 0) + 1;
      return acc;
    },
    {}
  );

  const pieData = {
    labels: Object.keys(statusCounts),
    datasets: [
      {
        label: "Test Status",
        data: Object.values(statusCounts),
        backgroundColor: ["rgba(16, 185, 129, 0.6)", "rgba(239, 68, 68, 0.6)"],
        borderColor: ["rgba(16, 185, 129, 1)", "rgba(239, 68, 68, 1)"],
        borderWidth: 1,
      },
    ],
  };

  const pieOptions = {
    responsive: true,
    plugins: {
      legend: { position: "top", labels: { color: "#64748b" } },
      title: { display: true, text: "Test Status Distribution", color: "#64748b" },
      tooltip: { backgroundColor: "rgba(30, 41, 59, 0.9)", titleColor: "#fff", bodyColor: "#fff" },
    },
  };

  // Line Chart Data: Number of Tests Over Time
  const testsByDate = reports.reduce((acc, report) => {
    const date = new Date(report.lastExecutionDate).toLocaleDateString();
    acc[date] = (acc[date] || 0) + report.noOfTest;
    return acc;
  }, {});

  const lineData = {
    labels: Object.keys(testsByDate).sort(),
    datasets: [
      {
        label: "Number of Tests",
        data: Object.values(testsByDate),
        fill: false,
        borderColor: "rgba(37, 99, 235, 1)",
        tension: 0.1,
        pointBackgroundColor: "rgba(37, 99, 235, 1)",
        pointBorderColor: "#fff",
        pointHoverBackgroundColor: "#fff",
        pointHoverBorderColor: "rgba(37, 99, 235, 1)",
      },
    ],
  };

  const lineOptions = {
    responsive: true,
    plugins: {
      legend: { position: "top", labels: { color: "#64748b" } },
      title: { display: true, text: "Tests Performed Over Time", color: "#64748b" },
      tooltip: { backgroundColor: "rgba(30, 41, 59, 0.9)", titleColor: "#fff", bodyColor: "#fff" },
    },
    scales: {
      x: { ticks: { color: "#64748b" }, grid: { display: false } },
      y: { ticks: { color: "#64748b" }, grid: { color: "rgba(255, 255, 255, 0.1)" } },
    },
  };

  return (
    <Layout title="Page Report Visualization">
      <div className="pagereportviz-container">
        <div className="pagereportviz-header">
          <p>Visual insights into basic test report performance</p>
        </div>

        {loading ? (
          <div className="pagereportviz-loading">Loading reports...</div>
        ) : error ? (
          <div className="pagereportviz-error">{error}</div>
        ) : reports.length === 0 ? (
          <div className="pagereportviz-no-reports">No reports found.</div>
        ) : (
          <>
            <div className="pagereportviz-stats-grid">
              <div className="pagereportviz-stat-card">
                <h3>Total Tests</h3>
                <p>{totalTests}</p>
              </div>
              <div className="pagereportviz-stat-card">
                <h3>Success Rate</h3>
                <p>{successRate}%</p>
              </div>
              <div className="pagereportviz-stat-card">
                <h3>Average Execution Time</h3>
                <p>{avgExecutionTime} ms</p>
              </div>
            </div>

            <div className="pagereportviz-charts-grid">
              <div className="pagereportviz-chart-card">
                <Bar data={barData} options={barOptions} aria-label="Bar chart of average time taken by page" />
              </div>
              <div className="pagereportviz-chart-card">
                <Pie data={pieData} options={pieOptions} aria-label="Pie chart of test status distribution" />
              </div>
              <div className="pagereportviz-chart-card">
                <Line data={lineData} options={lineOptions} aria-label="Line chart of tests performed over time" />
              </div>
            </div>

            <div className="pagereportviz-table-container">
              <table className="pagereportviz-table">
                <thead>
                  <tr>
                    <th>Page Name</th>
                    <th>Last Execution Status</th>
                    <th>Average Time Taken (sec)</th>
                    <th>No. of Tests</th>
                  </tr>
                </thead>
                <tbody>
                  {reports.map((report) => (
                    <tr
                      key={report.basicTestId}
                      onClick={() => handleRowClick(report)}
                      className="pagereportviz-table-row"
                      tabIndex={0}
                      onKeyPress={(e) => e.key === 'Enter' && handleRowClick(report)}
                      role="button"
                      aria-label={`View details for ${report.pageName}`}
                    >
                      <td>{report.pageName}</td>
                      <td>
                        <span
                          className={`pagereportviz-status-badge pagereportviz-status-${report.lastExecutionStatus.toLowerCase()}`}
                        >
                          {report.lastExecutionStatus}
                        </span>
                      </td>
                      <td>{(report.averageTimeTakenMS / 1000).toFixed(2)}</td>
                      <td>{report.noOfTest}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {selectedReport && (
              <div className="pagereportviz-dialog-overlay" onClick={closeDialog}>
                <div
                  className="pagereportviz-dialog-box pagereportviz-slide-in"
                  onClick={(e) => e.stopPropagation()}
                >
                  <div className="pagereportviz-dialog-header">
                    <h2>{selectedReport.pageName} Details</h2>
                    <button
                      onClick={closeDialog}
                      className="pagereportviz-close-btn"
                      aria-label="Close dialog"
                    >
                      <FiX size={24} />
                    </button>
                  </div>
                  <div className="pagereportviz-dialog-content">
                    <div className="pagereportviz-detail-grid">
                      <div className="pagereportviz-detail-card">
                        <label>Performance</label>
                        <div className="pagereportviz-metric-group">
                          <div className="pagereportviz-metric">
                            <span className="pagereportviz-metric-label">Last Execution</span>
                            <span className="pagereportviz-metric-value">
                              {selectedReport.lastTimeTakenMS} ms
                            </span>
                          </div>
                          <div className="pagereportviz-metric">
                            <span className="pagereportviz-metric-label">Average</span>
                            <span className="pagereportviz-metric-value">
                              {selectedReport.averageTimeTakenMS} ms
                            </span>
                          </div>
                        </div>
                      </div>

                      <div className="pagereportviz-detail-card pagereportviz-status-card">
                        <label>Current Status</label>
                        <div
                          className={`pagereportviz-status-indicator pagereportviz-status-${selectedReport.lastExecutionStatus.toLowerCase()}`}
                        >
                          {selectedReport.lastExecutionStatus}
                        </div>
                      </div>

                      <div className="pagereportviz-detail-card">
                        <label>Last Executed</label>
                        <p className="pagereportviz-detail-text">
                          {new Date(selectedReport.lastExecutionDate).toLocaleString()}
                        </p>
                        <p className="pagereportviz-detail-text">
                          By {selectedReport.lastExecutionBy}
                        </p>
                      </div>

                      <div className="pagereportviz-detail-card">
                        <label>Test Statistics</label>
                        <div className="pagereportviz-progress-bar">
                          <div
                            className="pagereportviz-progress-fill"
                            style={{ width: `${Math.min((selectedReport.noOfTest / 10) * 100, 100)}%` }}
                          ></div>
                          <span>{selectedReport.noOfTest} tests performed</span>
                        </div>
                      </div>
                    </div>

                    <div className="pagereportviz-comments-section">
                      <h4>Comments</h4>
                      <p className="pagereportviz-comment-text">
                        {selectedReport.comments || "No comments provided."}
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            )}
          </>
        )}
      </div>
    </Layout>
  );
};

export default PageReportVisualization;