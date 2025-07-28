import { useEffect, useState, useRef } from "react";
import api from "../api/axiosConfig";
import Layout from "../components/Layout";
import { FiX, FiDownload, FiClock, FiBarChart2, FiUser, FiChevronDown, FiChevronUp, FiSearch, FiFilter } from "react-icons/fi";
import '../css/DownloadReportTests.css';
const DownloadReportTests = () => {
  const [reports, setReports] = useState([]);
  const [filteredReports, setFilteredReports] = useState([]);
  const [selectedReport, setSelectedReport] = useState(null);
  const [expandedNames, setExpandedNames] = useState([]);
  const [sortConfig, setSortConfig] = useState({ key: null, direction: 'ascending' });
  const [statusFilter, setStatusFilter] = useState('all');
  const [searchTerm, setSearchTerm] = useState('');
  const [activeTab, setActiveTab] = useState('all');

  useEffect(() => {
    const fetchReports = async () => {
      try {
        const response = await api.get("/api/basic-report/all-download-reports");
        setReports(response.data);
        setFilteredReports(response.data);
      } catch (error) {
        console.error("Error fetching download report tests:", error);
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
        report.downloadReportTestStatus.toLowerCase() === statusFilter.toLowerCase()
      );
    }
    
    // Apply search filter
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      result = result.filter(report => 
        report.reportName.toLowerCase().includes(term) ||
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
  const successCount = reports.filter(report => report.downloadReportTestStatus === "Success").length;
  const failureCount = reports.filter(report => report.downloadReportTestStatus === "Failed").length;
  const successRate = totalTests > 0 ? ((successCount / totalTests) * 100).toFixed(1) : 0;
  
  const headers = [
    { key: 'sno', label: 'ID', minWidth: 50, sortable: false },
    { key: 'reportName', label: 'Report Name', minWidth: 150, sortable: true },
    { key: 'lastTimeTakenMS', label: 'Time Taken (sec)', minWidth: 100, sortable: true },
    { key: 'averageTimeTakenMS', label: 'Avg Time (sec)', minWidth: 100, sortable: true },
    { key: 'downloadReportTestStatus', label: 'Status', minWidth: 100, sortable: true },
    { key: 'noOfSuccessTestCount', label: 'Success Count', minWidth: 100, sortable: true },
    { key: 'noOfTest', label: 'Total Tests', minWidth: 100, sortable: true },
  ];

  return (
    <Layout >
      <div className="download-report">
        <div className="background-blobs">
          <div className="blob blob-1"></div>
          <div className="blob blob-2"></div>
          <div className="blob blob-3"></div>
        </div>
        
        <div className="header">
          <div className="header-icon">
            <FiDownload />
          </div>
          <h1>Report Download Dashboard</h1>
          <p>Comprehensive overview of all report download test executions</p>
        </div>
        
        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-icon total">
              <FiBarChart2 />
            </div>
            <div className="stat-content">
              <h3>Total Tests</h3>
              <p>{totalTests}</p>
            </div>
          </div>
          
          <div className="stat-card">
            <div className="stat-icon success">
              <FiClock />
            </div>
            <div className="stat-content">
              <h3>Success Rate</h3>
              <p>{successRate}%</p>
            </div>
          </div>
          
          <div className="stat-card">
            <div className="stat-icon success-count">
              <FiUser />
            </div>
            <div className="stat-content">
              <h3>Successful</h3>
              <p>{successCount}</p>
            </div>
          </div>
          
          <div className="stat-card">
            <div className="stat-icon failure">
              <FiUser />
            </div>
            <div className="stat-content">
              <h3>Failed</h3>
              <p>{failureCount}</p>
            </div>
          </div>
        </div>
        
        <div className="controls">
          <div className="search">
            <FiSearch className="search-icon" />
            <input
              type="text"
              placeholder="Search reports..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          
          <div className="filters">
            <div className="filter-group">
              <label><FiFilter /> Status:</label>
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
        
        <div className="tabs">
          <button 
            className={`tab ${activeTab === 'all' ? 'active' : ''}`}
            onClick={() => setActiveTab('all')}
          >
            All Reports
          </button>
          <button 
            className={`tab ${activeTab === 'recent' ? 'active' : ''}`}
            onClick={() => setActiveTab('recent')}
          >
            Recent Tests
          </button>
          <button 
            className={`tab ${activeTab === 'fastest' ? 'active' : ''}`}
            onClick={() => setActiveTab('fastest')}
          >
            Fastest Downloads
          </button>
        </div>

        {filteredReports.length === 0 ? (
          <div className="no-reports">
            <div className="no-reports-icon">📊</div>
            <h3>No Reports Found</h3>
            <p>Try adjusting your filters or run new tests</p>
          </div>
        ) : (
          <div className="table-container">
            <table className="table">
              <thead>
                <tr>
                  {headers.map(({ key, label, sortable }) => (
                    <th
                      key={key}
                      onClick={sortable ? () => requestSort(key) : undefined}
                      className={sortable ? 'sortable' : ''}
                    >
                      <div className="header-content">
                        {label}
                        {sortable && <div className="sort-icon">{getSortIcon(key)}</div>}
                      </div>
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {filteredReports.map((report, index) => (
                  <tr
                    key={report.downloadReportTestID}
                    onClick={() => handleRowClick(report)}
                    className="table-row"
                  >
                    <td>{index + 1}</td>
                    <td
                      className="name-cell"
                      onClick={(e) => {
                        e.stopPropagation();
                        toggleNameExpansion(report.downloadReportTestID);
                      }}
                    >
                      <div className="report-name">
                        {report.reportName}
                        <button className="expand-btn">
                          {expandedNames.includes(report.downloadReportTestID) ? 
                            <FiChevronUp /> : <FiChevronDown />}
                        </button>
                      </div>
                      
                      {expandedNames.includes(report.downloadReportTestID) && (
                        <div className="expanded-details">
                          <div>
                            <strong>Path:</strong> 
                            <span>{report.downloadedReportPath || 'Not available'}</span>
                          </div>
                          <div>
                            <strong>Comments:</strong> 
                            <span>{report.comments || 'No comments'}</span>
                          </div>
                        </div>
                      )}
                    </td>
                    <td>{(report.lastTimeTakenMS / 1000).toFixed(2)}</td>
                    <td>{(report.averageTimeTakenMS / 1000).toFixed(2)}</td>
                    <td>
                      <span className={`status-badge status-${report.downloadReportTestStatus.toLowerCase()}`}>
                        {report.downloadReportTestStatus}
                      </span>
                    </td>
                    <td>{report.noOfSuccessTestCount}</td>
                    <td>{report.noOfTest}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {selectedReport && (
          <div className="dialog-overlay" onClick={closeDialog}>
            <div
              className="dialog-box"
              onClick={(e) => e.stopPropagation()}
            >
              <div className="dialog-header">
                <h2>{selectedReport.reportName} Details</h2>
                <button
                  onClick={closeDialog}
                  className="close-btn"
                  aria-label="Close dialog"
                >
                  <FiX size={24} />
                </button>
              </div>
              <div className="dialog-content">
                <div className="detail-grid">
                  <div className="detail-card">
                    <label>Performance Metrics</label>
                    <div className="metric-group">
                      <div className="metric">
                        <span>Last Execution:</span>
                        <span className="value">{(selectedReport.lastTimeTakenMS / 1000).toFixed(2)} sec</span>
                      </div>
                      <div className="metric">
                        <span>Average Time:</span>
                        <span className="value">{(selectedReport.averageTimeTakenMS / 1000).toFixed(2)} sec</span>
                      </div>
                    </div>
                  </div>

                  <div className="detail-card status-card">
                    <label>Current Status</label>
                    <div className={`status-indicator status-${selectedReport.downloadReportTestStatus.toLowerCase()}`}>
                      {selectedReport.downloadReportTestStatus}
                    </div>
                  </div>

                  <div className="detail-card">
                    <label>Execution Details</label>
                    <div className="detail-item">
                      <span>By:</span>
                      <span>{selectedReport.lastExecutionBy}</span>
                    </div>
                    <div className="detail-item">
                      <span>Date:</span>
                      <span>{new Date(selectedReport.lastExecutionDate).toLocaleString()}</span>
                    </div>
                  </div>

                  <div className="detail-card">
                    <label>Test Statistics</label>
                    <div className="progress-container">
                      <div className="progress-labels">
                        <span>Success Rate:</span>
                        <span className="success-rate">
                          {selectedReport.noOfTest > 0 ? 
                            ((selectedReport.noOfSuccessTestCount / selectedReport.noOfTest) * 100).toFixed(1) + '%' : 
                            'N/A'}
                        </span>
                      </div>
                      <div className="progress-bar">
                        <div 
                          className="progress-fill"
                          style={{ 
                            width: selectedReport.noOfTest > 0 ? 
                              `${(selectedReport.noOfSuccessTestCount / selectedReport.noOfTest) * 100}%` : 
                              '0%' 
                          }}
                        ></div>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="path-section">
                  <label>Downloaded Report Path</label>
                  <div className="path-value">
                    {selectedReport.downloadedReportPath || 'No path provided'}
                  </div>
                </div>

                <div className="comments-section">
                  <label>Comments</label>
                  <div className="comments-value">
                    {selectedReport.comments || 'No comments provided'}
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

export default DownloadReportTests;