import { useEffect, useState, useRef } from "react";
import api from "../api/axiosConfig";
import Layout from "../components/Layout";
import "../css/TestReports.css";
import { FiX, FiCheck, FiAlertCircle } from "react-icons/fi";

const TestReports = () => {
  const [reports, setReports] = useState([]);
  const [expandedComments, setExpandedComments] = useState([]);
  const [selectedReport, setSelectedReport] = useState(null);
  const [showConfirmDialog, setShowConfirmDialog] = useState(null);
  const tableRef = useRef(null);
  const [columnWidths, setColumnWidths] = useState({});
  const isDragging = useRef(null);

  useEffect(() => {
    const fetchReports = async () => {
      try {
        const response = await api.get("/api/midstests/all");
        console.log("Fetched reports:", response.data);
        setReports(response.data);
      } catch (error) {
        console.error("Error fetching reports:", error);
      }
    };
    fetchReports();
  }, []);

  const totalTests = reports.length;
  const successRate = (
    (reports.filter((report) => report.status === "Success").length /
      totalTests) *
      100 || 0
  ).toFixed(1);

  const toggleCommentExpansion = (id) => {
    setExpandedComments((prev) =>
      prev.includes(id) ? prev.filter((rowId) => rowId !== id) : [...prev, id]
    );
  };

  const handleRowClick = (report) => {
    setShowConfirmDialog(report);
  };

  const confirmViewDetails = () => {
    setSelectedReport(showConfirmDialog);
    setShowConfirmDialog(null);
  };

  const cancelConfirmDialog = () => {
    setShowConfirmDialog(null);
  };

  const closeDialog = () => {
    setSelectedReport(null);
  };

  const headers = [
    { key: 'testId', label: 'Test ID', minWidth: 80 },
    { key: 'testName', label: 'Test Name', minWidth: 150 },
    { key: 'testUser', label: 'Test User', minWidth: 100 },
    { key: 'status', label: 'Status', minWidth: 100 },
    { key: 'timeTakenSec', label: 'Test Time Taken (sec)', minWidth: 100 },
    { key: 'testDate', label: 'Test Date', minWidth: 150 },
    { key: 'comments', label: 'Test Comment', minWidth: 300 },
  ];

  const startResizing = (e, columnKey) => {
    e.preventDefault();
    isDragging.current = { columnKey, startX: e.clientX, startWidth: columnWidths[columnKey] || headers.find(h => h.key === columnKey).minWidth };
  };

  const resizeColumn = (e) => {
    if (!isDragging.current) return;
    const { columnKey, startX, startWidth } = isDragging.current;
    const newWidth = Math.max(startWidth + (e.clientX - startX), headers.find(h => h.key === columnKey).minWidth);
    setColumnWidths(prev => ({ ...prev, [columnKey]: Math.min(newWidth, 600) }));
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
        newWidths[key] = Math.min(maxWidth + 20, 600); // Add padding, cap at 600px
      });
      setColumnWidths(newWidths);
    }
  }, [reports]);

  const truncateComment = (comment, maxLength = 100) => {
    if (!comment) return '';
    return comment.length > maxLength ? `${comment.substring(0, maxLength)}...` : comment;
  };

  return (
    <Layout title="MIDS Test Reports">
      <div className="testreport-container">
        <div className="testreport-header">
          <p>Overview of all MIDS test execution reports</p>
        </div>

        <div className="testreport-stats-grid">
          <div className="testreport-stat-card">
            <h3>Total Tests</h3>
            <p>{totalTests}</p>
          </div>
          <div className="testreport-stat-card">
            <h3>Success Rate</h3>
            <p>{successRate}%</p>
          </div>
        </div>

        {reports.length === 0 ? (
          <div className="testreport-no-reports">No reports found.</div>
        ) : (
          <div className="testreport-table-container">
            <table className="testreport-table" ref={tableRef}>
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
                        className="testreport-resize-handle"
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
                    key={report.testId}
                    onClick={() => handleRowClick(report)}
                    className="testreport-table-row"
                    tabIndex={0}
                    onKeyPress={(e) => e.key === 'Enter' && handleRowClick(report)}
                    role="button"
                    aria-label={`View details for ${report.testName}`}
                  >
                    <td data-column="testId" title={report.testId}>{report.testId}</td>
                    <td data-column="testName" title={report.testName}>{report.testName}</td>
                    <td data-column="testUser" title={report.testUser}>{report.testUser}</td>
                    <td data-column="status">
                      <span
                        className={`testreport-status-badge testreport-status-${report.status.toLowerCase()}`}
                        title={report.status}
                      >
                        {report.status}
                      </span>
                    </td>
                    <td data-column="timeTakenSec" title={report.timeTakenSec}>
                      {report.timeTakenSec}
                    </td>
                    <td data-column="testDate" title={report.testDate}>
                      {new Date(report.testDate).toLocaleString()}
                    </td>
                    <td
                      data-column="comments"
                      className={`testreport-comment-cell ${expandedComments.includes(report.testId) ? 'testreport-expanded' : ''}`}
                      onClick={(e) => {
                        e.stopPropagation(); // Prevent row click when toggling comment
                        toggleCommentExpansion(report.testId);
                      }}
                      title={report.comments}
                    >
                      {expandedComments.includes(report.testId)
                        ? report.comments
                        : truncateComment(report.comments)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {showConfirmDialog && (
          <div className="testreport-dialog-overlay" onClick={cancelConfirmDialog}>
            <div
              className="testreport-confirm-dialog testreport-slide-in"
              onClick={(e) => e.stopPropagation()}
            >
              <div className="testreport-dialog-header">
                <h2>Confirm Action</h2>
                <button
                  onClick={cancelConfirmDialog}
                  className="testreport-close-btn"
                  aria-label="Close confirmation dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="testreport-dialog-content">
                <p>
                  Do you want to view details for the test report "
                  {showConfirmDialog.testName}"?
                </p>
                <div className="testreport-confirm-buttons">
                  <button
                    onClick={confirmViewDetails}
                    className="testreport-confirm-btn"
                    aria-label="Confirm view details"
                  >
                    <FiCheck /> Yes
                  </button>
                  <button
                    onClick={cancelConfirmDialog}
                    className="testreport-cancel-btn"
                    aria-label="Cancel"
                  >
                    <FiX /> No
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

        {selectedReport && (
          <div className="testreport-dialog-overlay" onClick={closeDialog}>
            <div
              className="testreport-dialog-box testreport-slide-in"
              onClick={(e) => e.stopPropagation()}
            >
              <div className="testreport-dialog-header">
                <h2>{selectedReport.testName} Details</h2>
                <button
                  onClick={closeDialog}
                  className="testreport-close-btn"
                  aria-label="Close dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="testreport-dialog-content">
                <div className="testreport-detail-grid">
                  <div className="testreport-detail-card">
                    <label>Test Performance</label>
                    <div className="testreport-metric-group">
                      <div className="testreport-metric">
                        <span className="testreport-metric-label">Time Taken</span>
                        <span className="testreport-metric-value">
                          {selectedReport.timeTakenSec} sec
                        </span>
                      </div>
                    </div>
                  </div>

                  <div className="testreport-detail-card testreport-status-card">
                    <label>Current Status</label>
                    <div
                      className={`testreport-status-indicator testreport-status-${selectedReport.status.toLowerCase()}`}
                    >
                      {selectedReport.status}
                    </div>
                  </div>

                  <div className="testreport-detail-card">
                    <label>Test Execution</label>
                    <p className="testreport-detail-text">
                      Date: {new Date(selectedReport.testDate).toLocaleString()}
                    </p>
                    <p className="testreport-detail-text">
                      By: {selectedReport.testUser}
                    </p>
                  </div>
                </div>

                <div className="testreport-comments-section">
                  <h4>Comments</h4>
                  <div className="testreport-comment-text">
                    {selectedReport.comments || 'No comments provided.'}
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </Layout>
  );
};

export default TestReports;