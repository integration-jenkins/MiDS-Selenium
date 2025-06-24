import { useEffect, useState, useRef } from "react";
import api from "../api/axiosConfig";
import Layout from "../components/Layout";
import "../css/BasicTestReports.css";
import { FiX, FiAlertCircle } from "react-icons/fi";
import { FaSpinner } from "react-icons/fa";

const BasicTestReports = () => {
  const [reports, setReports] = useState([]);
  const [selectedReport, setSelectedReport] = useState(null);
  const [imageLoading, setImageLoading] = useState(false);
  const [imageError, setImageError] = useState(null);
  const tableRef = useRef(null);
  const [columnWidths, setColumnWidths] = useState({});
  const isDragging = useRef(null);

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
    console.log("Selected report:", report);
    setSelectedReport(report);
    if (report.imgPath) {
      setImageLoading(true);
      setImageError(null);
    } else {
      setImageLoading(false);
      setImageError("No screenshot available.");
    }
  };

  const closeDialog = () => {
    setSelectedReport(null);
    setImageLoading(false);
    setImageError(null);
  };

  const headers = [
    { key: 'sno', label: 'SNo', minWidth: 50 },
    { key: 'pageName', label: 'Page Name', minWidth: 150 },
    { key: 'lastTimeTaken', label: 'Last Time Taken (sec)', minWidth: 100 },
    { key: 'averageTimeTaken', label: 'Average Time Taken (sec)', minWidth: 100 },
    { key: 'comments', label: 'Comments', minWidth: 200 },
    { key: 'lastExecutionDate', label: 'Last Execution Date', minWidth: 150 },
    { key: 'lastExecutionStatus', label: 'Last Execution Status', minWidth: 100 },
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
    if (tableRef.current && reports.length > 0) {
      const newWidths = {};
      headers.forEach(({ key, minWidth }) => {
        const cells = tableRef.current.querySelectorAll(`td[data-column="${key}"], th[data-column="${key}"]`);
        let maxWidth = minWidth;
        cells.forEach(cell => {
          const width = cell.getBoundingClientRect().width;
          maxWidth = Math.max(maxWidth, width);
        });
        newWidths[key] = Math.min(maxWidth + 20, 400);
      });
      setColumnWidths(newWidths);
    }
  }, [reports]);

  const getImageUrl = (imgPath) => {
    if (!imgPath) {
      console.warn("imgPath is null or undefined");
      return null;
    }
    const filename = imgPath.split(/[\\/]/).pop();
    const url = `/api/images/${encodeURIComponent(filename)}`;
    console.log("Constructed image URL:", url);
    return url;
  };

  // Test API call when report is selected
  useEffect(() => {
    if (selectedReport && selectedReport.imgPath) {
      const url = getImageUrl(selectedReport.imgPath);
      if (url) {
        console.log("Attempting to fetch image from:", url);
        api.get(url, { responseType: 'blob' })
          .then(response => {
            console.log("Image fetch successful:", response);
            setImageLoading(false);
          })
          .catch(error => {
            console.error("Image fetch failed:", error);
            setImageLoading(false);
            setImageError("Failed to load screenshot.");
          });
      }
    }
  }, [selectedReport]);

  return (
    <Layout title="Basic Test Reports">
      <div className="basictestreports-container">
        <div className="background-blobs"></div>
        <div className="basictestreports-header">
          <p>Overview of all basic test execution reports</p>
        </div>

        <div className="basictestreports-stats-grid">
          <div className="basictestreports-stat-card">
            <h3>Total Tests</h3>
            <p>{totalTests}</p>
          </div>
          <div className="basictestreports-stat-card">
            <h3>Success Rate</h3>
            <p>{successRate}%</p>
          </div>
        </div>

        {reports.length === 0 ? (
          <div className="basictestreports-no-reports">No reports found.</div>
        ) : (
          <div className="basictestreports-table-container">
            <table className="basictestreports-table" ref={tableRef}>
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
                        className="basictestreports-resize-handle"
                        onMouseDown={(e) => startResizing(e, key)}
                        aria-hidden="true"
                      ></div>
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {reports.map((report, index) => (
                  <tr
                    key={report.basicTestId}
                    onClick={() => handleRowClick(report)}
                    className="basictestreports-table-row"
                    tabIndex={0}
                    onKeyPress={(e) => e.key === 'Enter' && handleRowClick(report)}
                    role="button"
                    aria-label={`View details for ${report.pageName}`}
                  >
                    <td data-column="sno">{index + 1}</td>
                    <td data-column="pageName">{report.pageName}</td>
                    <td data-column="lastTimeTaken">{(report.lastTimeTakenMS / 1000).toFixed(2)}</td>
                    <td data-column="averageTimeTaken">{(report.averageTimeTakenMS / 1000).toFixed(2)}</td>
                    <td data-column="comments">{report.comments}</td>
                    <td data-column="lastExecutionDate">{new Date(report.lastExecutionDate).toLocaleString()}</td>
                    <td data-column="lastExecutionStatus">
                      <span
                        className={`basictestreports-status-badge basictestreports-status-${report.lastExecutionStatus.toLowerCase()}`}
                      >
                        {report.lastExecutionStatus}
                      </span>
                    </td>
                    <td data-column="lastExecutionBy">{report.lastExecutionBy}</td>
                    <td data-column="noOfTest">{report.noOfTest}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {selectedReport && (
          <div className="basictestreports-dialog-overlay" onClick={closeDialog}>
            <div
              className="basictestreports-dialog-box basictestreports-slide-in"
              onClick={(e) => e.stopPropagation()}
              role="dialog"
              aria-labelledby="dialog-title"
            >
              <div className="basictestreports-dialog-header">
                <h2 id="dialog-title">{selectedReport.pageName} Details</h2>
                <button
                  onClick={closeDialog}
                  className="basictestreports-close-btn"
                  aria-label="Close dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="basictestreports-dialog-content">
                <div className="basictestreports-detail-grid">
                  <div className="basictestreports-detail-card">
                    <label>Performance</label>
                    <div className="basictestreports-metric-group">
                      <div className="basictestreports-metric">
                        <span className="basictestreports-metric-label">Last Execution</span>
                        <span className="basictestreports-metric-value">
                          {selectedReport.lastTimeTakenMS}ms
                        </span>
                      </div>
                      <div className="basictestreports-metric">
                        <span className="basictestreports-metric-label">Average</span>
                        <span className="basictestreports-metric-value">
                          {selectedReport.averageTimeTakenMS}ms
                        </span>
                      </div>
                    </div>
                  </div>

                  <div className="basictestreports-detail-card basictestreports-status-card">
                    <label>Current Status</label>
                    <div
                      className={`basictestreports-status-indicator basictestreports-status-${selectedReport.lastExecutionStatus.toLowerCase()}`}
                    >
                      {selectedReport.lastExecutionStatus}
                    </div>
                  </div>

                  <div className="basictestreports-detail-card">
                    <label>Last Executed</label>
                    <p className="basictestreports-detail-text">
                      {new Date(selectedReport.lastExecutionDate).toLocaleString()}
                    </p>
                    <p className="basictestreports-detail-text">
                      By {selectedReport.lastExecutionBy}
                    </p>
                  </div>

                  <div className="basictestreports-detail-card">
                    <label>Test Statistics</label>
                    <div className="basictestreports-progress-bar">
                      <div
                        className="basictestreports-progress-fill"
                        style={{ width: `${Math.min((selectedReport.noOfTest / 10) * 100, 100)}%` }}
                      ></div>
                      <span>{selectedReport.noOfTest} tests performed</span>
                    </div>
                  </div>
                </div>

                <div className="basictestreports-image-section">
                  <h4>Screenshot</h4>
                  {imageLoading && !imageError && (
                    <div className="basictestreports-image-loading">
                      <FaSpinner className="basictestreports-spinner" />
                      <p>Loading image...</p>
                    </div>
                  )}
                  {imageError && (
                    <div className="basictestreports-image-error">
                      <FiAlertCircle size={40} />
                      <p>{imageError}</p>
                    </div>
                  )}
                  {!imageLoading && !imageError && selectedReport.imgPath && (
                    <img
                      src={getImageUrl(selectedReport.imgPath)}
                      alt={`Screenshot for ${selectedReport.pageName}`}
                      className="basictestreports-screenshot"
                      onLoad={() => {
                        console.log("Image loaded successfully");
                        setImageLoading(false);
                      }}
                      onError={() => {
                        console.error("Image failed to load");
                        setImageLoading(false);
                        setImageError("Failed to load screenshot.");
                      }}
                    />
                  )}
                  {!imageLoading && !imageError && !selectedReport.imgPath && (
                    <div className="basictestreports-image-error">
                      <FiAlertCircle size={40} />
                      <p>No screenshot available.</p>
                    </div>
                  )}
                </div>

                <div className="basictestreports-comments-section">
                  <h4>Comments</h4>
                  <p className="basictestreports-comment-text">{selectedReport.comments || 'No comments provided.'}</p>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </Layout>
  );
};

export default BasicTestReports;