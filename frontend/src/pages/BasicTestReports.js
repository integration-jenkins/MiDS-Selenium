import { useEffect, useState, useRef } from "react";
import api from "../api/axiosConfig";
import Layout from "../components/Layout";
import { FiX, FiAlertCircle, FiClock, FiBarChart2, FiUser, FiTrendingUp, FiChevronDown, FiChevronUp,FiCheck } from "react-icons/fi";
import { FaSpinner, FaFilter, FaSearch } from "react-icons/fa";
import { MdOutlineScreenshot } from "react-icons/md";
import React from "react";
import '../css/BasicTestReports.css';
const BasicTestReports = () => {
  const [reports, setReports] = useState([]);
  const [filteredReports, setFilteredReports] = useState([]);
  const [selectedReport, setSelectedReport] = useState(null);
  const [imageLoading, setImageLoading] = useState(false);
  const [imageError, setImageError] = useState(null);
  const [imageBlobUrl, setImageBlobUrl] = useState(null);
  const tableRef = useRef(null);
  const [columnWidths, setColumnWidths] = useState({});
  const isDragging = useRef(null);
  const [sortConfig, setSortConfig] = useState({ key: null, direction: 'ascending' });
  const [statusFilter, setStatusFilter] = useState('all');
  const [searchTerm, setSearchTerm] = useState('');
  const [expandedRow, setExpandedRow] = useState(null);

  useEffect(() => {
    const fetchReports = async () => {
      try {
        const response = await api.get("/api/basic-report/all");
        console.log("Fetched basic test reports:", response.data);
        setReports(response.data);
        setFilteredReports(response.data);
      } catch (error) {
        console.error("Error fetching basic test reports:", error);
      }
    };
    fetchReports();
  }, []);

  useEffect(() => {
    // Filter and sort reports
    let result = [...reports];
    
    // Apply status filter
    if (statusFilter !== 'all') {
      result = result.filter(report => 
        report.lastExecutionStatus.toLowerCase() === statusFilter.toLowerCase()
      );
    }
    
    // Apply search filter
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      result = result.filter(report => 
        report.pageName.toLowerCase().includes(term) ||
        (report.comments && report.comments.toLowerCase().includes(term)) ||
        report.lastExecutionBy.toLowerCase().includes(term)
      );
    }
    
    // Apply sorting
    if (sortConfig.key) {
      result.sort((a, b) => {
        if (a[sortConfig.key] < b[sortConfig.key]) {
          return sortConfig.direction === 'ascending' ? -1 : 1;
        }
        if (a[sortConfig.key] > b[sortConfig.key]) {
          return sortConfig.direction === 'ascending' ? 1 : -1;
        }
        return 0;
      });
    }
    
    setFilteredReports(result);
  }, [reports, statusFilter, searchTerm, sortConfig]);

  const requestSort = (key) => {
    let direction = 'ascending';
    if (sortConfig.key === key && sortConfig.direction === 'ascending') {
      direction = 'descending';
    }
    setSortConfig({ key, direction });
  };

  const getSortIcon = (key) => {
    if (sortConfig.key !== key) return null;
    return sortConfig.direction === 'ascending' ? <FiChevronUp /> : <FiChevronDown />;
  };

  const totalTests = reports.length;
  const successRate = (
    (reports.filter((report) => report.lastExecutionStatus === "Success").length /
      totalTests) *
      100 || 0
  ).toFixed(1);

  const successCount = reports.filter(report => report.lastExecutionStatus === "Success").length;
  const failureCount = reports.filter(report => report.lastExecutionStatus === "Failed").length;

  const handleRowClick = (report, e) => {
    // Prevent expanding when clicking on expand button
    if (e.target.closest('.expand-button')) return;
    
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

  const toggleRowExpand = (id, e) => {
    e.stopPropagation();
    setExpandedRow(expandedRow === id ? null : id);
  };

  const closeDialog = () => {
    setSelectedReport(null);
    setImageLoading(false);
    setImageError(null);
    setImageBlobUrl(null);
  };

  const headers = [
    { key: 'sno', label: 'SNo', minWidth: 50 },
    { key: 'pageName', label: 'Page Name', minWidth: 150, sortable: true },
    { key: 'lastTimeTakenMS', label: 'Time Taken (sec)', minWidth: 100, sortable: true },
    { key: 'averageTimeTakenMS', label: 'Avg Time (sec)', minWidth: 100, sortable: true },
    { key: 'lastExecutionStatus', label: 'Status', minWidth: 100, sortable: true },
    { key: 'lastExecutionDate', label: 'Last Executed', minWidth: 150, sortable: true },
    { key: 'lastExecutionBy', label: 'Executed By', minWidth: 100, sortable: true },
    { key: 'noOfSuccessTestCount', label: 'Success Count', minWidth: 100, sortable: true },
    { key: 'noOfTest', label: 'Total Tests', minWidth: 100, sortable: true },
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
    const timestamp = Date.now();
    const url = `/api/images/${encodeURIComponent(filename)}?t=${timestamp}`;
    console.log("Constructed image URL with cache-busting:", url);
    return url;
  };

  useEffect(() => {
    if (selectedReport && selectedReport.imgPath) {
      const url = getImageUrl(selectedReport.imgPath);
      if (url) {
        console.log("Attempting to fetch image from:", url);
        api.get(url, { responseType: 'blob' })
          .then(response => {
            console.log("Image fetch successful:", response);
            const blobUrl = URL.createObjectURL(response.data);
            setImageBlobUrl(blobUrl);
            setImageLoading(false);
          })
          .catch(error => {
            console.error("Image fetch failed:", error);
            setImageLoading(false);
            setImageError("Failed to load screenshot.");
          });
      }
      return () => {
        if (imageBlobUrl) {
          URL.revokeObjectURL(imageBlobUrl);
          setImageBlobUrl(null);
        }
      };
    }
  }, [selectedReport]);

  return (
    <Layout>
      <div className="btr-container">
        <div className="btr-background-blobs">
          <div className="blob blob-1"></div>
          <div className="blob blob-2"></div>
          <div className="blob blob-3"></div>
        </div>
        
        <div className="btr-header">
          <h1>Test Performance Dashboard</h1>
          <p>Comprehensive overview of all basic test execution reports</p>
        </div>

        <div className="btr-stats-grid">
          <div className="btr-stat-card">
            <div className="stat-icon total">
              <FiBarChart2 />
            </div>
            <div className="stat-content">
              <h3>Total Tests</h3>
              <p>{totalTests}</p>
            </div>
          </div>
          
          <div className="btr-stat-card">
            <div className="stat-icon success">
              <FiTrendingUp />
            </div>
            <div className="stat-content">
              <h3>Success Rate</h3>
              <p>{successRate}%</p>
            </div>
          </div>
          
          <div className="btr-stat-card">
            <div className="stat-icon success-count">
              <FiCheck />
            </div>
            <div className="stat-content">
              <h3>Successful Tests</h3>
              <p>{successCount}</p>
            </div>
          </div>
          
          <div className="btr-stat-card">
            <div className="stat-icon failure">
              <FiAlertCircle />
            </div>
            <div className="stat-content">
              <h3>Failed Tests</h3>
              <p>{failureCount}</p>
            </div>
          </div>
        </div>

        <div className="btr-controls">
          <div className="btr-search">
            <FaSearch className="search-icon" />
            <input
              type="text"
              placeholder="Search reports..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          
          <div className="btr-filters">
            <div className="filter-group">
              <label><FaFilter /> Status:</label>
              <select 
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
              >
                <option value="all">All Statuses</option>
                <option value="Success">Success</option>
                <option value="Failed">Failed</option>
              </select>
            </div>
          </div>
        </div>

        {filteredReports.length === 0 ? (
          <div className="btr-no-reports">
            <FiAlertCircle className="no-reports-icon" />
            <p>No reports found matching your criteria</p>
          </div>
        ) : (
          <div className="btr-table-container">
            <table className="btr-table" ref={tableRef}>
              <thead>
                <tr>
                  <th></th>
                  {headers.map(({ key, label, sortable }) => (
                    <th
                      key={key}
                      data-column={key}
                      style={{ width: columnWidths[key] || 'auto' }}
                      aria-label={label}
                      onClick={sortable ? () => requestSort(key) : undefined}
                      className={sortable ? 'sortable' : ''}
                    >
                      <div className="header-content">
                        {label}
                        {sortable && <div className="sort-icon">{getSortIcon(key)}</div>}
                        {sortable && (
                          <div
                            className="btr-resize-handle"
                            onMouseDown={(e) => startResizing(e, key)}
                            aria-hidden="true"
                          ></div>
                        )}
                      </div>
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {filteredReports.map((report, index) => (
                  <React.Fragment key={report.basicTestId}>
                    <tr
                      onClick={(e) => handleRowClick(report, e)}
                      className={`btr-table-row ${expandedRow === report.basicTestId ? 'expanded' : ''}`}
                      tabIndex={0}
                      onKeyPress={(e) => e.key === 'Enter' && handleRowClick(report)}
                      role="button"
                      aria-label={`View details for ${report.pageName}`}
                    >
                      <td className="expand-cell">
                        <button 
                          className="expand-button"
                          onClick={(e) => toggleRowExpand(report.basicTestId, e)}
                          aria-label={expandedRow === report.basicTestId ? "Collapse details" : "Expand details"}
                        >
                          {expandedRow === report.basicTestId ? <FiChevronUp /> : <FiChevronDown />}
                        </button>
                      </td>
                      <td data-column="sno">{index + 1}</td>
                      <td data-column="pageName">{report.pageName}</td>
                      <td data-column="lastTimeTakenMS">{(report.lastTimeTakenMS / 1000).toFixed(2)}</td>
                      <td data-column="averageTimeTakenMS">{(report.averageTimeTakenMS / 1000).toFixed(2)}</td>
                      <td data-column="lastExecutionStatus">
                        <span
                          className={`btr-status-badge btr-status-${report.lastExecutionStatus.toLowerCase()}`}
                        >
                          {report.lastExecutionStatus}
                        </span>
                      </td>
                      <td data-column="lastExecutionDate">{new Date(report.lastExecutionDate).toLocaleDateString()}</td>
                      <td data-column="lastExecutionBy">{report.lastExecutionBy}</td>
                      <td data-column="noOfSuccessTestCount">{report.noOfSuccessTestCount}</td>
                      <td data-column="noOfTest">{report.noOfTest}</td>
                    </tr>
                    
                    {expandedRow === report.basicTestId && (
                      <tr className="expanded-details">
                        <td colSpan={headers.length + 1}>
                          <div className="details-content">
                            <div className="detail-item">
                              <label>Comments:</label>
                              <p>{report.comments || 'No comments provided'}</p>
                            </div>
                            <div className="detail-item">
                              <label>Execution Details:</label>
                              <p>Last executed by {report.lastExecutionBy} on {new Date(report.lastExecutionDate).toLocaleString()}</p>
                            </div>
                            <div className="detail-item">
                              <label>Performance Metrics:</label>
                              <div className="metrics">
                                <div className="metric">
                                  <span>Last Execution:</span>
                                  <span className="value">{(report.lastTimeTakenMS / 1000).toFixed(2)} sec</span>
                                </div>
                                <div className="metric">
                                  <span>Average:</span>
                                  <span className="value">{(report.averageTimeTakenMS / 1000).toFixed(2)} sec</span>
                                </div>
                                <div className="metric">
                                  <span>Success Rate:</span>
                                  <span className="value">
                                    {report.noOfTest > 0 
                                      ? ((report.noOfSuccessTestCount / report.noOfTest) * 100).toFixed(1) + '%' 
                                      : 'N/A'}
                                  </span>
                                </div>
                              </div>
                            </div>
                          </div>
                        </td>
                      </tr>
                    )}
                  </React.Fragment>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {selectedReport && (
          <div className="btr-dialog-overlay" onClick={closeDialog}>
            <div
              className="btr-dialog-box btr-slide-in"
              onClick={(e) => e.stopPropagation()}
              role="dialog"
              aria-labelledby="dialog-title"
            >
              <div className="btr-dialog-header">
                <h2 id="dialog-title">{selectedReport.pageName} Test Report</h2>
                <button
                  onClick={closeDialog}
                  className="btr-close-btn"
                  aria-label="Close dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="btr-dialog-content">
                <div className="btr-detail-grid">
                  <div className="btr-detail-card">
                    <label><FiClock /> Performance Metrics</label>
                    <div className="btr-metric-group">
                      <div className="btr-metric">
                        <span className="btr-metric-label">Last Execution Time</span>
                        <span className="btr-metric-value">
                          {(selectedReport.lastTimeTakenMS / 1000).toFixed(2)} sec
                        </span>
                      </div>
                      <div className="btr-metric">
                        <span className="btr-metric-label">Average Time</span>
                        <span className="btr-metric-value">
                          {(selectedReport.averageTimeTakenMS / 1000).toFixed(2)} sec
                        </span>
                      </div>
                    </div>
                  </div>

                  <div className="btr-detail-card btr-status-card">
                    <label><FiBarChart2 /> Current Status</label>
                    <div
                      className={`btr-status-indicator btr-status-${selectedReport.lastExecutionStatus.toLowerCase()}`}
                    >
                      {selectedReport.lastExecutionStatus}
                    </div>
                  </div>

                  <div className="btr-detail-card">
                    <label><FiUser /> Execution Details</label>
                    <p className="btr-detail-text">
                      {new Date(selectedReport.lastExecutionDate).toLocaleString()}
                    </p>
                    <p className="btr-detail-text">
                      By {selectedReport.lastExecutionBy}
                    </p>
                  </div>

                  <div className="btr-detail-card">
                    <label><FiTrendingUp /> Test Statistics</label>
                    <div className="btr-progress-bar">
                      <div
                        className="btr-progress-fill"
                        style={{ width: `${Math.min((selectedReport.noOfTest / 10) * 100, 100)}%` }}
                      ></div>
                      <span>{selectedReport.noOfTest} tests performed</span>
                    </div>
                    <div className="success-rate">
                      Success Rate: {selectedReport.noOfTest > 0 
                        ? ((selectedReport.noOfSuccessTestCount / selectedReport.noOfTest) * 100).toFixed(1) + '%' 
                        : 'N/A'}
                    </div>
                  </div>
                </div>

                <div className="btr-image-section">
                  <h4><MdOutlineScreenshot /> Screenshot</h4>
                  {imageLoading && !imageError && (
                    <div className="btr-image-loading">
                      <FaSpinner className="btr-spinner" />
                      <p>Loading image...</p>
                    </div>
                  )}
                  {imageError && (
                    <div className="btr-image-error">
                      <FiAlertCircle size={40} />
                      <p>{imageError}</p>
                    </div>
                  )}
                  {!imageLoading && !imageError && imageBlobUrl && (
                    <img
                      src={imageBlobUrl}
                      alt={`Screenshot for ${selectedReport.pageName}`}
                      className="btr-screenshot"
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
                  {!imageLoading && !imageError && !imageBlobUrl && !selectedReport.imgPath && (
                    <div className="btr-image-error">
                      <FiAlertCircle size={40} />
                      <p>No screenshot available.</p>
                    </div>
                  )}
                </div>

                <div className="btr-comments-section">
                  <h4>Comments</h4>
                  <p className="btr-comment-text">{selectedReport.comments || 'No comments provided.'}</p>
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