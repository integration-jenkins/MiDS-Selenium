import React, { useState, useEffect } from 'react';
import MidsTestSideSection from '../components/MidsTestSideSection';
import api from '../api/axiosConfig';
import "../css/TsShiftingDashboard.css";
import { Alert } from '@mui/material';

const TsShiftingDashboard = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const [userInitials, setUserInitials] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    planId: '',
    circle: '',
    path: '',
    checkHold: 'Yes'
  });
  const [testData, setTestData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [apiStatus, setApiStatus] = useState({ message: '', type: '' });

  // State for managing users
  const [users, setUsers] = useState([{ department: '', name: '', password: '' }]);
  const [showUserModal, setShowUserModal] = useState(false);

  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [selectedDetails, setSelectedDetails] = useState([]);
  const [selectedTest, setSelectedTest] = useState(null);
  const [userDetails, setUserDestails] = useState([]);
  const [selectedUsers, setSelectedUsers] = useState([]);

  const [bulkDataResult, setBulkDataResult] = useState({});
  const [bulkUploadTable, setBulkUploadTable] = useState(false);

  const [testType, setTestType] = useState("");



  useEffect(() => {
    const username = localStorage.getItem('username') || '';
    setUserInitials(getInitials(username));
    fetchData();
  }, []);

  const getInitials = (name) => {
    if (!name) return 'AU';
    return name
      .split(' ')
      .filter(part => part.length > 0)
      .map(part => part[0])
      .join('')
      .toUpperCase();
  };

  const filteredData = testData.filter(test => {
    const matchesSearch = searchTerm === '' ||
      (test.testId && test.testId.toLowerCase().includes(searchTerm.toLowerCase()));

    const matchesTestType = testType === '' || test.testType === testType;

    return matchesSearch && matchesTestType;
  });


  // Calculate statistics
  const totalTests = filteredData.length;
  const passedTests = filteredData.filter(test => test.testStatus === 'PASSED').length;
  const failedTests = filteredData.filter(test => test.testStatus === 'FAILED').length;
  const passRate = totalTests > 0 ? ((passedTests / totalTests) * 100).toFixed(2) : '0.00';

  // Filter data based on search term
  // const filteredData = testData.filter(test =>
  //   (test.testId && test.testId.toLowerCase().includes(searchTerm.toLowerCase()))
  // );


  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleUserInputChange = (userType, index, value) => {
    setFormData(prev => ({
      ...prev,
      dismantleUser: {
        ...prev.dismantleUser,
        [userType]: prev.dismantleUser[userType].map((item, i) =>
          i === index ? value : item
        )
      }
    }));
  };

  const testWorkFlow = async () => {
    setShowModal(true);
    setApiStatus({ message: '', type: '' });
    setSelectedUsers([]); // Reset selected users when modal opens
    await viewAllusers(); // Fetch users when modal opens
  };

  const [bulkUploadTab, setBulkUploadTab] = useState(false);

  const bulkUploadTest = async () => {
    setBulkUploadTab(true);
    setApiStatus({ message: '', type: '' });
    setSelectedUsers([]); // Reset selected users when modal opens
    await viewAllusers();
  }

  const submitForm = async () => {
    console.log("form data = ", formData);

    const { planId, circle, path, checkHold } = formData;

    // check top-level fields
    if (!planId || !circle || !path || !checkHold) {
      alert("Please fill all required fields!");
      return;
    }

    setLoading(true);
    setApiStatus({ message: '', type: '' });

    console.log("Send Data = ", formData)

    try {
      const response = await api.post('/api/trafficShifting/workflow', formData);

      if (response.status !== 200) {
        throw new Error('Network response was not ok');
      }

      console.log("Api resp = ", response);
      setApiStatus({
        message: 'Test workflow executed successfully!',
        type: 'success'
      });

      // Refresh the test data after a short delay
      setTimeout(() => {
        fetchData();
        setShowModal(false);
      }, 1500);
    } catch (error) {
      console.error('Error executing workflow:', error);
      setApiStatus({
        message: 'Failed to execute workflow test. Please try again.',
        type: 'error'
      });
    } finally {
      setLoading(false);
    }
  };

  const fetchData = async () => {
    try {
      const response = await api.get('/api/trafficShifting/testResult');
      setTestData(response.data);
      console.log("fetch Test Data = ", response.data);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  const closeModal = () => {
    setShowModal(false);
    setBulkUploadTab(false);
    setApiStatus({ message: '', type: '' });
  };

  // Open the Add Test Users modal
  const openUserModal = () => {
    setShowUserModal(true);
  };

  // Close the Add Test Users modal
  const closeUserModal = () => {
    setShowUserModal(false);
  };

  // Add a new empty user form
  const addNewUser = () => {
    setUsers([...users, { department: '', name: '', password: '' }]);
  };

  // Remove a user from the list
  const removeUser = (index) => {
    if (users.length <= 1) return;
    const newUsers = [...users];
    newUsers.splice(index, 1);
    setUsers(newUsers);
  };

  // Handle input changes for user fields
  const handleUserChange = (index, field, value) => {
    const newUsers = [...users];
    newUsers[index][field] = value;
    setUsers(newUsers);
  };

  const saveUsers = () => {
    console.log('Saved users:', users);

    // Validate all user fields are filled
    const incompleteUsers = users.filter(user =>
      !user.department || !user.name || !user.password
    );

    if (incompleteUsers.length > 0) {
      alert('Please fill all fields for all users before saving.');
      return;
    }

    saveUserData(users);
    closeUserModal();
  };

  // fatch the all user with all details
  const viewAllusers = async () => {
    const data = await fetchUserData();
    setUserDestails(data);
    console.log("fetch user Details = ", userDetails);
  };

  const fetchUserData = async () => {
    try {
      const response = await api.get('api/trafficShifting/getUsers');
      console.log("fetch user = ", response.data);
      return response.data; // return the fresh data
    } catch (error) {
      console.error("Error fetching data:", error);
      return [];
    }
  };

  // Function to open details modal
  const openDetailsModal = (test) => {
    setSelectedTest(test);
    setSelectedDetails(test.trafficShiftingResultData || []);
    setShowDetailsModal(true);
  };

  // Function to close details modal
  const closeDetailsModal = () => {
    setShowDetailsModal(false);
    setSelectedDetails([]);
    setSelectedTest(null);
  };

  const saveUserData = async (users) => {
    try {
      const resp = await api.put('api/trafficShifting/setUser', users);
      console.log(resp);

      if (resp.data) {
        alert(resp.data);
      } else {
        alert("There some issue to save user!")
      }

    } catch (error) {
      console.log(error);

      // Show error message to user
      alert('Failed to save users. Please try again.');

      // If you want to show the specific error message from the server:
      if (error.response && error.response.data && error.response.data.message) {
        alert(`Error: ${error.response.data.message}`);
      }
    }

  };

  // Function to handle user selection
  const handleUserSelect = (user) => {
    // Check if user is already selected
    if (!selectedUsers.some(u => u.name === user.name && u.department === user.department)) {
      setSelectedUsers(prev => [...prev, user]);

      // Update the formData with the selected user
      setFormData(prev => ({
        ...prev,
        dismantleUser: {
          ...prev.dismantleUser,
          name: [...(prev.dismantleUser?.name || []), user.name],
          department: [...(prev.dismantleUser?.department || []), user.department],
          password: [...(prev.dismantleUser?.password || []), user.password]
        }
      }));
    }
  };

  // Function to remove a selected user
  const removeSelectedUser = (index) => {
    setSelectedUsers(prev => prev.filter((_, i) => i !== index));

    // Update the formData to remove the user
    setFormData(prev => ({
      ...prev,
      dismantleUser: {
        name: prev.dismantleUser?.name.filter((_, i) => i !== index) || [],
        department: prev.dismantleUser?.department.filter((_, i) => i !== index) || [],
        password: prev.dismantleUser?.password.filter((_, i) => i !== index) || []
      }
    }));
  };

  // this verifies the Sheet and fetches all results
  const verifyUploadSheet = async (e) => {
    try {
      setLoading(true);
      let tem = {
        path: formData.path,
        circle: formData.circle,
      };

      console.log("Verify sheet:", tem);

      const resp = await api.post("/api/trafficShifting/testBulkUploadSheet", tem);

      console.log(resp);

      if (resp.data && resp.data.length > 0) {
        setBulkDataResult(resp.data);
        setBulkUploadTable(true);
      } else {
        alert("All column holds valid values, You Sheet is Valid, you can use it!");
      }
    } catch (error) {
      console.error("Error verifying sheet:", error);
      alert("Failed to verify the sheet!");
    } finally {
      setLoading(false);
    }
  };

  const startBulkUploadTest = async () => {
    try {
      if (formData.path.length == 0 || formData.circle.length == 0) {
        alert("Fill the All required Details before the test");
        return;
      } else if (!formData.dismantleUser ||
        Object.keys(formData.dismantleUser).length === 0 ||
        // If dismantleUser has multiple departments/names/passwords (arrays with length > 1)
        (Array.isArray(formData.dismantleUser.department) && formData.dismantleUser.department.length > 1) ||
        (Array.isArray(formData.dismantleUser.name) && formData.dismantleUser.name.length > 1) ||
        (Array.isArray(formData.dismantleUser.password) && formData.dismantleUser.password.length > 1)) {
        alert("Select exactly one user for bulkUpload Testing");
        return;
      }

      // Prepare the data in the correct format for backend
      const requestData = {
        circle: formData.circle,
        dismantleUser: {
          department: formData.dismantleUser.department,
          name: formData.dismantleUser.name,
          password: formData.dismantleUser.password
        },
        path: formData.path,
        planId: formData.planId || ""
      };

      console.log("RequestData = ", requestData);

      setLoading(true);

      const resp = await api.post("/api/trafficShifting/bulkUploadTest", requestData);
      console.log("BulkUploadTest Result = ", resp);

      setApiStatus({
        message: 'Test Bulk upload executed successfully!',
        type: 'success'
      });

      setTimeout(
        () => {
          setBulkUploadTab(false);
        }, 1500
      )




    } catch (error) {
      console.error("Error:", error);
      alert("There is some issue, Try after some time!");
      setApiStatus({
        message: 'Test Bulk upload executed Failed!',
        type: 'success'
      });
      setTimeout(
        () => {
          setBulkUploadTab(false);
        }, 1500
      )
    } finally {
      setLoading(false);
    }
  }

  const removeTestUser = async () => {
    try {
      console.log("remove users = " + formData.dismantleUser.department)
    } catch (error) {
      alert("There issue when remove the user")
    }
  }

  return (
    <div className="dashboard-container">
      <MidsTestSideSection />
      {/* Main Content */}
      <div className={`main-content ${!isSidebarOpen ? 'expanded' : ''}`}>
        {/* Navbar */}
        <header className="navbar">
          <div className="navbar-left">
            <h1>Traffic Shifting Dashboard</h1>
          </div>
          <div className="navbar-right">
            <div className="user-info">
              <span className="user-name">{localStorage.getItem('username') || 'Admin User'}</span>
              <div className="user-avatar">
                {userInitials}
              </div>
            </div>
          </div>
        </header>

        {/* Dashboard Content */}
        <div className="dashboard-content">
          {/* Header Section */}
          <div className="header-section">
            <h2>Traffic Shifting Test Execution Overview</h2>
            <p>This page is designed to validate the complete execution lifecycle of traffic shifting plans. It ensures smooth testing, identifies potential issues, and helps improve overall code quality and system performance.</p>
          </div>

          {/* Statistics Cards */}
          <div className="stats-container">
            <div className="stat-card">
              <div className="stat-icon" style={{ backgroundColor: '#3498db20', color: '#3498db' }}>
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M3 13h8V3H3v10zm0 8h8v-6H3v6zm10 0h8V11h-8v10zm0-18v6h8V3h-8z" />
                </svg>
              </div>
              <div className="stat-info">
                <h3>Total Tests</h3>
                <span className="stat-number">{totalTests}</span>
              </div>
            </div>

            <div className="stat-card">
              <div className="stat-icon" style={{ backgroundColor: '#4caf5020', color: '#4caf50' }}>
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M9 16.2L4.8 12l-1.4 1.4L9 19 21 7l-1.4-1.4L9 16.2z" />
                </svg>
              </div>
              <div className="stat-info">
                <h3>Passed Tests</h3>
                <span className="stat-number">{passedTests}</span>
              </div>
            </div>

            <div className="stat-card">
              <div className="stat-icon" style={{ backgroundColor: '#f4433620', color: '#f44336' }}>
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z" />
                </svg>
              </div>
              <div className="stat-info">
                <h3>Failed Tests</h3>
                <span className="stat-number">{failedTests}</span>
              </div>
            </div>

            <div className="stat-card">
              <div className="stat-icon" style={{ backgroundColor: '#ff980020', color: '#ff9800' }}>
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M11 2v20c-5.07-.5-9-4.79-9-10s3.93-9.5 9-10zm2.03 0v8.99H22c-.47-4.74-4.24-8.52-8.97-8.99zm0 11.01V22c4.74-.47 8.5-4.25 8.97-8.99h-8.97z" />
                </svg>
              </div>
              <div className="stat-info">
                <h3>Pass Rate</h3>
                <span className="stat-number">{passRate}%</span>
              </div>
            </div>
          </div>

          {/* Main Content Area */}
          <div className="content-area">
            {/* Table Section */}
            <div className="table-section">
              <div className="table-header">
                <h3>Test Execution History</h3>
                <div className="search-container">
                  <input
                    type="text"
                    placeholder="Search tests..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                  />
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                    <path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" />
                  </svg>
                </div>

                <div className="filter-container">
                  <select
                    value={testType}
                    onChange={(e) => setTestType(e.target.value)}
                    className="test-type-filter"
                  >
                    <option value="">All Test Types</option>
                    <option value="BULK_UPLOAD_TEST">Bulk Upload Test</option>
                    <option value="WORK_FLOW_TEST">Work Flow Test</option>
                  </select>
                </div>

              </div>

              <div className="table-container">
                <table>
                  <thead>
                    <tr>
                      <th>Test Type</th>
                      <th>Test Id</th>
                      <th>Test Status</th>
                      <th>Total Case Pass</th>
                      <th>Completion Time (Sec) </th>
                      <th>Test Remark</th>
                      <th>View Result</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredData.length > 0 ? (
                      filteredData.map((test) => (
                        <tr key={test.id || test.testId}>
                          <td>{test.testType || 'N/A'}</td>
                          <td>{test.testId || 'N/A'}</td>
                          <td>
                            <span className={`status-badge ${test.testStatus ? test.testStatus.toLowerCase() : 'unknown'}`}>
                              {test.testStatus || 'Unknown'}
                            </span>
                          </td>

                          <td>{test.totalPassCase || '0'}</td>
                          <td>{test.completionTime || 'N/A'}</td>

                          <td>{test.remark || 'No remarks'}</td>
                          <td>
                            <button
                              className="submit-button"
                              onClick={() => openDetailsModal(test)}
                              title="View Details"
                              disabled={!test.trafficShiftingResultData || test.trafficShiftingResultData.length === 0}
                            >
                              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
                                <path d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z" />
                              </svg>
                              View
                            </button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="7" style={{ textAlign: 'center', padding: '20px' }}>
                          {testData.length === 0 ? 'No test data available' : 'No matching tests found'}
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>

            {/* Side Boxes Section */}
            <div className="side-section">
              <div className="info-box" style={{ backgroundColor: '#e3f2fd', borderLeft: '4px solid #2196f3' }}>
                <h3>Workflow testing</h3>
                <p>Review detailed analysis of test results, identify patterns, and track performance metrics over time.</p>
                <button className="action-button" style={{ backgroundColor: '#2196f3' }}
                  onClick={testWorkFlow}>
                  Test workflow
                </button>
              </div>

              <div className="info-box" style={{ backgroundColor: '#f1f8e9', borderLeft: '4px solid #4caf50' }}>
                <h3>Bulk Upload Test</h3>
                <p>This page validates bulk upload sheets and ensures the bulk upload functionality is tested thoroughly against all required parameters.</p>
                <button className="action-button" style={{ backgroundColor: '#4caf50' }}
                  onClick={bulkUploadTest}>
                  Bulk Upload Test
                </button>
              </div>
            </div>
          </div>
        </div>

        {/* Footer */}
        <footer className="dashboard-footer">
          <p>© Test Shifting Dashboard. All rights reserved. | Designed for better test management and analysis.</p>
        </footer>
      </div>

      {/* Modal for workflow testing */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>Workflow Test Configuration</h3>
              <button className="submit-button" onClick={openUserModal}
                style={{ marginLeft: "auto", marginRight: "10px" }}
              >Add Test Users</button>
              <button className="close-button" onClick={closeModal} aria-label="Close">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z" />
                </svg>
              </button>
            </div>

            {apiStatus.message && (
              <div className={`api-status ${apiStatus.type}`}>
                {apiStatus.message}
              </div>
            )}

            <div className="modal-body">
              <div className="form-row">
                <div className="form-group">
                  <label>Plan ID</label>
                  <input
                    type="text"
                    name="planId"
                    value={formData.planId}
                    onChange={handleInputChange}
                    placeholder="Enter plan ID"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Circle</label>
                  <input
                    type="text"
                    name="circle"
                    value={formData.circle}
                    onChange={handleInputChange}
                    placeholder="Enter circle"
                    required
                  />
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Path</label>
                  <input
                    type="text"
                    name="path"
                    value={formData.path}
                    onChange={handleInputChange}
                    placeholder="Enter file path"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Check Hold</label>
                  <select
                    name="checkHold"
                    value={formData.checkHold}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="Yes">Yes</option>
                    <option value="Hold">Hold</option>
                  </select>
                </div>
              </div>

              {/* Add the user selection section */}
              <div className="form-row">
                <div className="form-group">
                  <label>Select Users</label>
                  <div className="user-selection-container">
                    {/* Show selected users */}
                    {selectedUsers.map((user, index) => (
                      <div key={index} className="selected-user-tag">
                        {user.name}/{user.department}
                        <button
                          type="button"
                          className="remove-user-tag"
                          onClick={() => removeSelectedUser(index)}
                        >
                          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="14" height="14">
                            <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z" />
                          </svg>
                        </button>
                      </div>
                    ))}

                    {/* Show available users to select */}
                    {userDetails.length > 0 ? (
                      <div className="available-users">
                        {userDetails
                          .filter(user => !selectedUsers.some(u => u.name === user.name && u.department === user.department))
                          .map((user, index) => (
                            <button
                              key={index}
                              type="button"
                              className="user-select-button"
                              onClick={() => handleUserSelect(user)}
                            >
                              {user.name}/{user.department}
                            </button>
                          ))
                        }
                      </div>
                    ) : userDetails.length === 0 ? (
                      // Show message when userDetails is empty array (no users available)
                      <div className="no-users-message">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
                          <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z" />
                        </svg>
                        No users available. Please add users first.
                      </div>
                    ) : (
                      // Show loading only when userDetails is null/undefined (still loading)
                      <div className="loading-users">
                        <span className="spinner-small"></span>
                        Loading users...
                      </div>
                    )}
                  </div>
                </div>
              </div>
            </div>

            <div className="modal-footer">
              <button className="submit-button" onClick={removeTestUser}
                style={{ marginLeft: "auto", marginRight: "10px" }}
              >Remove Test Users</button>
              <button className="submit-button" onClick={submitForm} disabled={loading}>
                {loading ? (
                  <>
                    <span className="spinner"></span>
                    Processing...
                  </>
                ) : 'Run Test'}
              </button>
            </div>
          </div>
        </div>
      )}


      {/* bulkUpload Test section */}

      {bulkUploadTab && (
        <div className="modal-overlay">
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>Bulk Upload Test Configuration</h3>
              <button className="submit-button" onClick={openUserModal}
                style={{ marginLeft: "auto", marginRight: "10px" }}
              >Add Test Users</button>
              <button className="close-button" onClick={closeModal} aria-label="Close">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z" />
                </svg>
              </button>
            </div>

            {apiStatus.message && (
              <div className={`api-status ${apiStatus.type}`}>
                {apiStatus.message}
              </div>
            )}

            <div className="modal-body">
              <div className="form-row">
                <div className="form-group">
                  <label>Circle</label>
                  <input
                    type="text"
                    name="circle"
                    value={formData.circle}
                    onChange={handleInputChange}
                    placeholder="Enter circle"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Path</label>
                  <input
                    type="text"
                    name="path"
                    value={formData.path}
                    onChange={handleInputChange}
                    placeholder="Enter file path"
                    required
                  />
                </div>
              </div>

              {/* Add the user selection section */}
              <div className="form-row">
                <div className="form-group">
                  <label>Select Users</label>
                  <div className="user-selection-container">
                    {/* Show selected users */}
                    {selectedUsers.map((user, index) => (
                      <div key={index} className="selected-user-tag">
                        {user.name}/{user.department}
                        <button
                          type="button"
                          className="remove-user-tag"
                          onClick={() => removeSelectedUser(index)}
                        >
                          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="14" height="14">
                            <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z" />
                          </svg>
                        </button>
                      </div>
                    ))}

                    {/* Show available users to select */}
                    {userDetails.length > 0 ? (
                      <div className="available-users">
                        {userDetails
                          .filter(user => !selectedUsers.some(u => u.name === user.name && u.department === user.department))
                          .map((user, index) => (
                            <button
                              key={index}
                              type="button"
                              className="user-select-button"
                              onClick={() => handleUserSelect(user)}
                            >
                              {user.name}/{user.department}
                            </button>
                          ))
                        }
                      </div>
                    ) : userDetails.length === 0 ? (
                      // Show message when userDetails is empty array (no users available)
                      <div className="no-users-message">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
                          <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z" />
                        </svg>
                        No users available. Please add users first.
                      </div>
                    ) : (
                      // Show loading only when userDetails is null/undefined (still loading)
                      <div className="loading-users">
                        <span className="spinner-small"></span>
                        Loading users...
                      </div>
                    )}
                  </div>
                </div>
              </div>
            </div>

            <div className="modal-footer">
              <button className="submit-button" onClick={verifyUploadSheet} disabled={loading}>
                {loading ? (
                  <>
                    <span className="spinner"></span>
                    Processing...
                  </>
                ) : 'Verify Upload Sheet'}
              </button>
              <button className="submit-button" onClick={startBulkUploadTest} disabled={loading}>
                {loading ? (
                  <>
                    <span className="spinner"></span>
                    Processing...
                  </>
                ) : 'Run Test'}
              </button>
            </div>
          </div>
        </div>
      )}

      {bulkUploadTable && (
        <div className="modal-overlay">
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>Bulk Upload Verification Results</h3>
              <button className="close-button" onClick={() => setBulkUploadTable(false)} aria-label="Close">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z" />
                </svg>
              </button>
            </div>

            <div className="modal-body">
              <div className="details-table-container">
                <h4>Sheet Validation Results</h4>
                <table className="details-table">
                  <thead>
                    <tr>
                      <th>Row Number</th>
                      <th>Column Name</th>
                      <th>Remark</th>
                    </tr>
                  </thead>
                  <tbody>
                    {bulkDataResult.length > 0 ? (
                      bulkDataResult.map((detail, index) => (
                        <tr key={index}>
                          <td>{detail.rowNumber || 'N/A'}</td>
                          <td>{detail.columnName || 'N/A'}</td>
                          <td>{detail.errorMessage || 'No remarks'}</td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="3" style={{ textAlign: 'center', padding: '20px' }}>
                          No validation errors found
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      )}



      {showDetailsModal && selectedTest && (
        <div className="modal-overlay">
          <div className="modal details-modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>Test Plan Details - {selectedTest.uniquePlanId || selectedTest.testId || 'N/A'}</h3>
              <button className="close-button" onClick={closeDetailsModal} aria-label="Close">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z" />
                </svg>
              </button>
            </div>

            <div className="modal-body">
              {/* Summary Information */}
              <div className="summary-info">
                <div className="summary-item">
                  <span className="summary-label">Test Status:</span>
                  <span className={`summary-value status-${selectedTest.testStatus ? selectedTest.testStatus.toLowerCase() : 'unknown'}`}>
                    {selectedTest.testStatus || 'N/A'}
                  </span>
                </div>
                <div className="summary-item">
                  <span className="summary-label">Completion Time:</span>
                  <span className="summary-value">{selectedTest.completionTime || 'N/A'} seconds</span>
                </div>
                <div className="summary-item">
                  <span className="summary-label">Total Pass Cases:</span>
                  <span className="summary-value">{selectedTest.totalPassCase || '0'}</span>
                </div>
                <div className="summary-item">
                  <span className="summary-label">Total Date:</span>
                  <span className="summary-value">{selectedTest.testDate || 'N'}</span>
                </div>
                <div className="summary-item">
                  <span className="summary-label">Remark:</span>
                  <span className="summary-value">{selectedTest.remark || 'No remarks'}</span>
                </div>
              </div>

              {/* Detailed Results Table */}
              <div className="details-table-container">
                <h4>Traffic Shifting Results</h4>
                <table className="details-table">
                  <thead>
                    <tr>
                      <th>Plan ID</th>
                      <th>Plan Status</th>
                      <th>Test Status</th>
                      <th>User Name</th>
                      <th>Department</th>
                      <th>Remark</th>
                    </tr>
                  </thead>
                  <tbody>
                    {selectedDetails.length > 0 ? (
                      selectedDetails.map((detail, index) => (
                        <tr key={index}>
                          <td>{detail.planId || 'N/A'}</td>
                          <td>
                            <span className={`status-badge ${detail.planStatus ? detail.planStatus.toLowerCase() : 'unknown'}`}>
                              {detail.planStatus || 'N/A'}
                            </span>
                          </td>
                          <td>
                            <span className={`status-badge ${detail.testStatus ? detail.testStatus.toLowerCase() : 'unknown'}`}>
                              {detail.testStatus || 'N/A'}
                            </span>
                          </td>
                          <td>{detail.userName || 'N/A'}</td>
                          <td>{detail.department || 'N/A'}</td>
                          <td>{detail.remark || 'No remarks'}</td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="6" style={{ textAlign: 'center', padding: '20px' }}>
                          No detailed results available
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>

            <div className="modal-footer">
              <button className="submit-button" onClick={closeDetailsModal}>
                Close
              </button>
            </div>
          </div>
        </div>
      )}

      {/* ADD TEST USERS TAB */}
      {showUserModal && (
        <div className="modal-overlay">
          <div className="modal user-modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>Add Test Users</h3>
              <button className="close-button" onClick={closeUserModal} aria-label="Close">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z" />
                </svg>
              </button>
            </div>

            <div className="modal-body">
              <div className="users-list">
                {users.map((user, index) => (
                  <div key={index} className="user-row">
                    <div className="form-row">
                      <h3>User No.{index + 1}</h3>
                      <div className="form-group">
                        <label>Department</label>
                        <select
                          value={user.department}
                          onChange={(e) => handleUserChange(index, 'department', e.target.value)}
                          required
                        >
                          <option value="">Select Department</option>
                          <option value="Circle MW Planner">Circle MW Planner</option>
                          <option value="Circle Operation Team">Circle Operation Team</option>
                          <option value="Circle Deployment Team">Circle Deployment Team</option>
                          <option value="Circle DINC Team">Circle DINC Team</option>
                        </select>
                      </div>
                      <div className="form-group">
                        <label>Name</label>
                        <input
                          type="text"
                          value={user.name}
                          onChange={(e) => handleUserChange(index, 'name', e.target.value)}
                          placeholder="Enter name"
                        />
                      </div>
                      <div className="form-group">
                        <label>Password</label>
                        <input
                          type="password"
                          value={user.password}
                          onChange={(e) => handleUserChange(index, 'password', e.target.value)}
                          placeholder="Enter password"
                        />
                      </div>
                    </div>
                    {users.length > 1 && (
                      <button
                        className="remove-user-button"
                        onClick={() => removeUser(index)}
                        aria-label="Remove user"
                      >
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                          <path d="M19 13H5v-2h14v2z" />
                        </svg>
                      </button>
                    )}
                  </div>
                ))}
              </div>

              <button className="add-user-button" onClick={addNewUser}>
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z" />
                </svg>
                Add Another User
              </button>
            </div>

            <div className="modal-footer">
              <button className="submit-button" onClick={saveUsers}>
                Save Users
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default TsShiftingDashboard;