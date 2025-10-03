import "../css/DismantleDashboard.css";
import api from '../api/axiosConfig';
import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from "react";
import MidsTestSideSection from '../components/MidsTestSideSection';

const DismantleDashboard = () => {
    const navigate = useNavigate();
    const [tableData, setTableData] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [selectedRow, setSelectedRow] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isDetailLoading, setIsDetailLoading] = useState(false);
    const [detailedData, setDetailedData] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [userInitials, setUserInitials] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            setIsLoading(true);
            try {
                const response = await api.get('/api/dismantleTest/testResult');
                setTableData(response.data);
            } catch (error) {
                console.error("Error fetching data:", error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchData();
    }, []);

    const getDetailedData = async (viewReportId) => {
        try {
            setIsDetailLoading(true);
            console.log("viewId = ", viewReportId)
            const resp = await api.get(`/api/dismantleTest/viewReport?viewReportId=${viewReportId}`);
            console.log("resp : ", resp.data)
            return resp.data;
        } catch (error) {
            console.error("Error fetching detailed data:", error);
            return null;
        } finally {
            setIsDetailLoading(false);
        }
    };

    const handleDetailsClick = async (row) => {
        setSelectedRow(row);
        const data = await getDetailedData(row.viewReport);
        console.log("Data = ", data);
        setDetailedData(data);
        console.log("SetData = ", detailedData)
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setSelectedRow(null);
        setDetailedData(null);
    };

    const handleButtonClick = () => {
        navigate('/dismantle/dashboard/form');
    };

    const moveToBulkUploadTest = () => {
        navigate('/dismantle/dashboard/bulkTest')
    }

    useEffect(() => {
        const username = localStorage.getItem('username') || '';
        setUserInitials(getInitials(username));
    }, []);

    const getInitials = (name) => {
        if (!name) return 'GU';
        return name
            .split(' ')
            .filter(part => part.length > 0)
            .map(part => part[0])
            .join('')
            .toUpperCase();
    };

    return (
        <div className="app-container">
            <div className="sidebar-container">
                <MidsTestSideSection />
            </div>

            <div className="main-content-container">
                {/* Full-width Navbar */}
                <header className="header">
                    <div className="header-container">
                        <h1>Dismantle Automation Testing</h1>
                        <div className="user-info">
                            <span className="user-name">{localStorage.getItem('username') || 'Guest User'}</span>
                            <div className="user-avatar">{userInitials}</div>
                        </div>
                    </div>
                </header>

                <main className="main-content">
                    {/* Introduction Section */}
                    <section className="intro-section">
                        <h2>Dismantle Management Dashboard</h2>
                        <p>
                            Perform dismantle testing for various workflows and components. Enter test details, validate data, and track progress efficiently and Easily carry out dismantle testing across different workflows. Validate component data and monitor testing steps with accuracy.
                        </p>
                    </section>

                    {/* Main Content Area - Side by Side Layout */}
                    <div className="content-grid">
                        {/* Table Section - Left Side */}
                        <section className="table-section">
                            <div className="section-header">
                                <h3>Test Result</h3>
                                <div className="stats-and-search">
                                    <div className="test-stats">
                                        <span className="stat total">Total: {tableData.length}</span>
                                        <span className="stat passed">Passed: {
                                            tableData.filter(item => item.testStatus?.toLowerCase() === 'all test passed').length
                                        }</span>
                                        <span className="stat failed">Failed: {
                                            tableData.filter(item => item.testStatus?.toLowerCase() === 'test failed').length
                                        }</span>
                                    </div>
                                    <div className="search-container">
                                        <input
                                            type="text"
                                            placeholder="Search by Test ID..."
                                            value={searchTerm}
                                            onChange={(e) => setSearchTerm(e.target.value)}
                                            className="search-input"
                                        />
                                    </div>
                                </div>
                            </div>
                            <div className="table-container">
                                {isLoading ? (
                                    <div className="loading-message">Loading data...</div>
                                ) : (
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>Test ID</th>
                                                <th>Test Type</th>
                                                <th>Test Status</th>
                                                <th>Completion Time (Min)</th>
                                                <th>Passed Cases</th>
                                                <th>Remark</th>
                                                <th>Details</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {tableData.length > 0 ? (
                                                tableData
                                                    .filter(row => {
                                                        const testId = row.testId ? row.testId.toString().toLowerCase() : '';
                                                        const search = searchTerm.toLowerCase();
                                                        return testId.includes(search);
                                                    })
                                                    .map((row, index) => (
                                                        <tr key={row.id || index}>
                                                            <td>{row.testId || '-'}</td>
                                                            <td>{row.testType || '-'}</td>
                                                            <td>
                                                                <span className={`status ${row.testStatus?.toLowerCase().includes('fail') ? 'failed' : 'passed'}`}>
                                                                    {row.testStatus || '-'}
                                                                </span>
                                                            </td>
                                                            <td>{row.completionTime ? (row.completionTime / 1000).toFixed(2) : '-'}</td>
                                                            <td>{row.totalPassCase || '-'}</td>
                                                            <td>{row.remark || '-'}</td>
                                                            <td>
                                                                <button
                                                                    className="details-button"
                                                                    onClick={() => handleDetailsClick(row)}
                                                                >
                                                                    View Details
                                                                </button>
                                                            </td>
                                                        </tr>
                                                    ))
                                            ) : (
                                                <tr>
                                                    <td colSpan="7" className="no-data-message">
                                                        {isLoading ? 'Loading...' : 'No data available'}
                                                    </td>
                                                </tr>
                                            )}
                                        </tbody>
                                    </table>
                                )}
                            </div>
                        </section>

                        <section className="groups-section">
                            <div className="section-header">
                                <h3>Testing Section</h3>
                            </div>
                            <div className="groups-grid">
                                <div className="group-card">
                                    <h4>Workflow Testing</h4>
                                    <div className="group-details">
                                        <p><span>All necessary details must be valid to test the functionality of the dismantle plan</span></p>
                                    </div>
                                    <button className="configure-button" onClick={handleButtonClick}>Test</button>
                                </div>

                                <div className="group-card">
                                    <h4>Bulk Upload Test</h4>
                                    <div className="group-details">
                                        <p><span>To start bulk upload testing, please ensure that the uploaded plan is in a valid stage</span></p>
                                    </div>
                                    <button className="configure-button" onClick={moveToBulkUploadTest}>Test</button>
                                </div>
                            </div>
                        </section>
                    </div>

                    <section className="features-section">
                        <h2>Important Points In Testing</h2>
                        <div className="features-grid">
                            <div className="feature-card">
                                <p>For optimal alignment during testing, ensure the browser window is resized to 50% or less of the screen width</p>
                            </div>
                            <div className="feature-card">
                                <p>Please ensure all fields contain valid data.</p>
                            </div>
                            <div className="feature-card">
                                <p>All usernames and passwords must be correct, and ensure that details of all involved users are properly filled in</p>
                            </div>
                        </div>
                    </section>

                    {/* Modal for showing detailed table */}
                    {isModalOpen && selectedRow && (
                        <div className="modal-overlay">
                            <div className="modal-content">
                                <div className="modal-header">
                                    <h3>Test Details for {selectedRow.testId}</h3>
                                    <button className="close-button" onClick={closeModal}>×</button>
                                </div>
                                <div className="modal-body">
                                    {isDetailLoading ? (
                                        <div className="loading-message">Loading detailed data...</div>
                                    ) : (
                                        <table className="details-table">
                                            <thead>
                                                <tr>
                                                    <th>Test ID</th>
                                                    <th>Department Name</th>
                                                    <th>Status</th>
                                                    <th>Test Date</th>
                                                    <th>Dismantle Status</th>
                                                    <th>Remark</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {detailedData && detailedData.length > 0 ? (
                                                    detailedData.map((data, index) => (
                                                        <tr key={index}>
                                                            <td>{data.testId}</td>
                                                            <td>{data.departmentName}</td>
                                                            <td className={`status ${data.testStatus?.toLowerCase().includes('fail') ? 'failed' : 'passed'}`}>
                                                                {data.testStatus}
                                                            </td>
                                                            <td>{data.testDate}</td>
                                                            <td>{data.dismantleStatus}</td>
                                                            <td>{data.remark}</td>
                                                        </tr>
                                                    ))
                                                ) : (
                                                    <tr>
                                                        <td colSpan="6" className="no-data-message">
                                                            No detailed data available
                                                        </td>
                                                    </tr>
                                                )}
                                            </tbody>
                                        </table>
                                    )}
                                </div>
                            </div>
                        </div>
                    )}
                </main>
            </div>
        </div>
    );
};

export default DismantleDashboard;