import { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import "../css/DownloadReportTests.css";

const DownloadReportTests = () => {
  const [reports, setReports] = useState([]);
  const [expandedNames, setExpandedNames] = useState([]);

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

  // Function to toggle the expanded state of a name cell
  const toggleNameExpansion = (id) => {
    setExpandedNames((prev) =>
      prev.includes(id) ? prev.filter((rowId) => rowId !== id) : [...prev, id]
    );
  };

  const totalTests = reports.length;
    let count = 1;
  const successRate = (
    (reports.filter((report) => report.downloadReportTestStatus === "Success").length /
      totalTests) *
      100 || 0
  ).toFixed(1);

  return (
    <div className="reports-container">
      <div className="reports-header">
        <h1>Download Report Tests</h1>
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
              <th>SNo.</th>
              <th>Report Name</th>
              <th>Last Time Taken (sec)</th>
              <th>Average Time Taken (sec)</th>
              <th>Download Report Test Status</th>
              <th>Downloaded Report Path</th>
              <th>Comments</th>
              <th>Last Execution Date</th>
              <th>Last Execution By</th>
              <th>No. of Tests</th>
            </tr>
          </thead>
          <tbody>
            {reports.map((report) => (
              <tr key={report.downloadReportTestID}>
                <td title={report.downloadReportTestID}>{count++}</td>
                <td
                  className={`name-cell ${expandedNames.includes(report.downloadReportTestID) ? 'expanded' : ''}`}
                  onClick={() => toggleNameExpansion(report.downloadReportTestID)}
                >
                  {report.reportName}
                </td>
                <td title={report.lastTimeTakenMS}>{report.lastTimeTakenMS/1000}</td>
                <td title={report.averageTimeTakenMS}>{(report.averageTimeTakenMS)/1000}</td>
                <td>
                  <span
                    className={`status-badge status-${report.downloadReportTestStatus.toLowerCase()}`}
                    title={report.downloadReportTestStatus}
                  >
                    {report.downloadReportTestStatus}
                  </span>
                </td>
                <td title={report.downloadedReportPath}>{report.downloadedReportPath}</td>
                <td title={report.comments}>{report.comments}</td>
                <td title={new Date(report.lastExecutionDate).toLocaleString()}>
                  {new Date(report.lastExecutionDate).toLocaleString()}
                </td>
                <td title={report.lastExecutionBy}>{report.lastExecutionBy}</td>
                <td title={report.noOfTest}>{report.noOfTest}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default DownloadReportTests;