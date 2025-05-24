import { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import "../css/TestReports.css";

const TestReports = () => {
  const [reports, setReports] = useState([]);

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

  return (
    <div className="reports-container">
      <div className="reports-header">
        <h1>MIDS Test Reports</h1>
        <div className="stats-grid">
          <div className="stat-card">
            <h3>Total Tests</h3>
            <p>{reports.length}</p>
          </div>
          <div className="stat-card">
            <h3>Success Rate</h3>
            <p>
              {(
                (reports.filter((report) => report.status === "Success")
                  .length /
                  reports.length) *
                  100 || 0
              ).toFixed(1)}
              %
            </p>
          </div>
        </div>
      </div>

      <table className="reports-table">
        <thead>
          <tr>
            <th>Test ID</th>
            <th>Test Name</th>
            <th>Test User</th>
            <th>Status</th>
            <th>Test Time taken</th>
            <th>Test Date</th>
            <th>Test Comment</th>
          </tr>
        </thead>
        <tbody>
          {reports.map((report) => (
            <tr>
              <td>{report.testId}</td>
              <td>{report.testName}</td>
              <td>{report.testUser}</td>
              <td>
                <span className={`status-badge ${report.status}`}>
                  {report.status}
                </span>
              </td>
              <td>{report.timeTakenSec}</td>
              <td>{report.testDate}</td>
              <td>{report.comments}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default TestReports;
