import { useEffect, useState, useRef } from "react";
import api from "../api/axiosConfig";
import Layout from "../components/Layout";
import "../css/DownloadReportTests.css";
import { FiX } from "react-icons/fi";

const DownloadReportTests = () => {
  const [reports, setReports] = useState([]);
  const [expandedNames, setExpandedNames] = useState([]);
  const [selectedReport, setSelectedReport] = useState(null);
  const tableRef = useRef(null);
  const [columnWidths, setColumnWidths] = useState({});
  const isDragging = useRef(null);

  useEffect(() => {
    const fetchReports = async () => {
      try {
        const response = await api.get("/api/basic-report/all-download-reports");
        console.log("Fetched download report tests:", response.data);
        setReports(response.data);
      } catch (error) {
        console.error("Error fetching download report tests:", error);
      }
    };
    fetchReports();
  }, []);

  const toggleNameExpansion = (id) => {
    setExpandedNames((prev) =>
      prev.includes(id) ? prev.filter((rowId) => rowId !== id) : [...prev, id]
    );
  };

  const handleRowClick = (report) => {
    setSelectedReport(report);
  };

  const closeDialog = () => {
    setSelectedReport(null);
  };

  const totalTests = reports.length;
  let count = 1;
  const successRate = (
    (reports.filter((report) => report.downloadReportTestStatus === "Success").length /
      totalTests) *
      100 || 0
  ).toFixed(1);

  const headers = [
    { key: 'sno', label: 'SNo.', minWidth: 50 },
    { key: 'reportName', label: 'Report Name', minWidth: 150 },
    { key: 'lastTimeTaken', label: 'Last Time Taken (sec)', minWidth: 100 },
    { key: 'averageTimeTaken', label: 'Average Time Taken (sec)', minWidth: 100 },
    { key: 'downloadReportTestStatus', label: 'Download Report Test Status', minWidth: 100 },
    { key: 'downloadedReportPath', label: 'Downloaded Report Path', minWidth: 200 },
    { key: 'comments', label: 'Comments', minWidth: 200 },
    { key: 'lastExecutionDate', label: 'Last Execution Date', minWidth: 150 },
    { key: 'lastExecutionBy', label: 'Last Execution By', minWidth: 100 },
    { key: 'noOfTest', label: 'No. of Tests', minWidth: 80 },
  ];

  const startResizing = (e, columnKey) => {
    e.preventDefault();
    isDragging.current = { columnKey, startX: e.clientX, startWidth: columnWidths[columnKey] || headers.find(h => h.key === columnKey).minWidth };
  };

  const resizeColumn = (e) => {
    if (!isDragging.current) return;
    const { columnKey, startX, startWidth } = isDragging.current;
    const newWidth = Math.max(startWidth + (e.clientX - startX), headers.find(h => h.key === columnKey).minWidth);
    setColumnWidths(prev => ({ ...prev, [columnKey]: Math.min(newWidth, 400) }));
  };

  const stopResizing = () => {
    isDragging.current = null;
  };

  useEffect(() => {
    document.addEventListener('mousemove', resizeColumn);
    document.addEventListener('mouseup', stopResizing);
    return () => {
      document.removeEventListener('mousemove', resizeColumn);
      document.removeEventListener('mouseup', stopResizing);
    };
  }, []);

  useEffect(() => {
    // Auto-resize columns based on content
    if (tableRef.current && reports.length > 0) {
      const newWidths = {};
      headers.forEach(({ key, minWidth }) => {
        const cells = tableRef.current.querySelectorAll(`td[data-column="${key}"], th[data-column="${key}"]`);
        let maxWidth = minWidth;
        cells.forEach(cell => {
          const width = cell.getBoundingClientRect().width;
          maxWidth = Math.max(maxWidth, width);
        });
        newWidths[key] = Math.min(maxWidth + 20, 400); // Add padding, cap at 400px
      });
      setColumnWidths(newWidths);
    }
  }, [reports]);

  return (
    <Layout title="Download Report Tests">
      <div className="downloadreport-container">
        <div className="downloadreport-header">
          <p>Overview of all download report test executions</p>
        </div>

        <div className="downloadreport-stats-grid">
          <div className="downloadreport-stat-card">
            <h3>Total Tests</h3>
            <p>{totalTests}</p>
          </div>
          <div className="downloadreport-stat-card">
            <h3>Success Rate</h3>
            <p>{successRate}%</p>
          </div>
        </div>

        {reports.length === 0 ? (
          <div className="downloadreport-no-reports">No reports found.</div>
        ) : (
          <div className="downloadreport-table-container">
            <table className="downloadreport-table" ref={tableRef}>
              <thead>
                <tr>
                  {headers.map(({ key, label }) => (
                    <th
                      key={key}
                      data-column={key}
                      style={{ width: columnWidths[key] || 'auto' }}
                      aria-label={label}
                    >
                      {label}
                      <div
                        className="downloadreport-resize-handle"
                        onMouseDown={(e) => startResizing(e, key)}
                        aria-hidden="true"
                      ></div>
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {reports.map((report) => (
                  <tr
                    key={report.downloadReportTestID}
                    onClick={() => handleRowClick(report)}
                    className="downloadreport-table-row"
                    tabIndex={0}
                    onKeyPress={(e) => e.key === 'Enter' && handleRowClick(report)}
                    role="button"
                    aria-label={`View details for ${report.reportName}`}
                  >
                    <td data-column="sno" title={report.downloadReportTestID}>{count++}</td>
                    <td
                      data-column="reportName"
                      className={`downloadreport-name-cell ${expandedNames.includes(report.downloadReportTestID) ? 'downloadreport-expanded' : ''}`}
                      onClick={(e) => {
                        e.stopPropagation(); // Prevent row click when toggling name
                        toggleNameExpansion(report.downloadReportTestID);
                      }}
                    >
                      {report.reportName}
                    </td>
                    <td data-column="lastTimeTaken" title={report.lastTimeTakenMS}>
                      {(report.lastTimeTakenMS / 1000).toFixed(2)}
                    </td>
                    <td data-column="averageTimeTaken" title={report.averageTimeTakenMS}>
                      {(report.averageTimeTakenMS / 1000).toFixed(2)}
                    </td>
                    <td data-column="downloadReportTestStatus">
                      <span
                        className={`downloadreport-status-badge downloadreport-status-${report.downloadReportTestStatus.toLowerCase()}`}
                        title={report.downloadReportTestStatus}
                      >
                        {report.downloadReportTestStatus}
                      </span>
                    </td>
                    <td data-column="downloadedReportPath" title={report.downloadedReportPath}>
                      {report.downloadedReportPath}
                    </td>
                    <td data-column="comments" title={report.comments}>
                      {report.comments}
                    </td>
                    <td data-column="lastExecutionDate" title={new Date(report.lastExecutionDate).toLocaleString()}>
                      {new Date(report.lastExecutionDate).toLocaleString()}
                    </td>
                    <td data-column="lastExecutionBy" title={report.lastExecutionBy}>
                      {report.lastExecutionBy}
                    </td>
                    <td data-column="noOfTest" title={report.noOfTest}>
                      {report.noOfTest}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {selectedReport && (
          <div className="downloadreport-dialog-overlay" onClick={closeDialog}>
            <div
              className="downloadreport-dialog-box downloadreport-slide-in"
              onClick={(e) => e.stopPropagation()} // Prevent closing when clicking inside dialog
            >
              <div className="downloadreport-dialog-header">
                <h2>{selectedReport.reportName} Details</h2>
                <button
                  onClick={closeDialog}
                  className="downloadreport-close-btn"
                  aria-label="Close dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="downloadreport-dialog-content">
                <div className="downloadreport-detail-grid">
                  <div className="downloadreport-detail-card">
                    <label>Performance</label>
                    <div className="downloadreport-metric-group">
                      <div className="downloadreport-metric">
                        <span className="downloadreport-metric-label">Last Execution</span>
                        <span className="downloadreport-metric-value">
                          {selectedReport.lastTimeTakenMS}ms
                        </span>
                      </div>
                      <div className="downloadreport-metric">
                        <span className="downloadreport-metric-label">Average</span>
                        <span className="downloadreport-metric-value">
                          {selectedReport.averageTimeTakenMS}ms
                        </span>
                      </div>
                    </div>
                  </div>

                  <div className="downloadreport-detail-card downloadreport-status-card">
                    <label>Current Status</label>
                    <div
                      className={`downloadreport-status-indicator downloadreport-status-${selectedReport.downloadReportTestStatus.toLowerCase()}`}
                    >
                      {selectedReport.downloadReportTestStatus}
                    </div>
                  </div>

                  <div className="downloadreport-detail-card">
                    <label>Last Executed</label>
                    <p className="downloadreport-detail-text">
                      {new Date(selectedReport.lastExecutionDate).toLocaleString()}
                    </p>
                    <p className="downloadreport-detail-text">
                      By {selectedReport.lastExecutionBy}
                    </p>
                  </div>

                  <div className="downloadreport-detail-card">
                    <label>Test Statistics</label>
                    <div className="downloadreport-progress-bar">
                      <div
                        className="downloadreport-progress-fill"
                        style={{ width: `${Math.min((selectedReport.noOfTest / 10) * 100, 100)}%` }}
                      ></div>
                      <span>{selectedReport.noOfTest} tests performed</span>
                    </div>
                  </div>
                </div>

                <div className="downloadreport-path-section">
                  <h4>Downloaded Report Path</h4>
                  <p className="downloadreport-path-text">
                    {selectedReport.downloadedReportPath || 'No path provided.'}
                  </p>
                </div>

                <div className="downloadreport-comments-section">
                  <h4>Comments</h4>
                  <p className="downloadreport-comment-text">
                    {selectedReport.comments || 'No comments provided.'}
                  </p>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </Layout>
  );
};

export default DownloadReportTests;