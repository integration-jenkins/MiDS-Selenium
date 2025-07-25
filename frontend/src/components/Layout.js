import React, { useState, useEffect, useRef } from 'react';
import { FaTachometerAlt, FaRobot, FaNetworkWired, FaServer, FaCog,FaChartLine, FaSignOutAlt, FaBell, FaUserCircle, FaSearch, FaMoon, FaSun, FaBars } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import '../css/Layout.css';
import { useAuth } from '../context/AuthContext';


const Layout = ({ children, title }) => {

  //sample Data
   const notifications = [
    { id: 1, title: 'Test Completed', description: 'SR to SP automation test completed', time: '10 mins ago', status: 'success' },
    { id: 2, title: 'Performance Alert', description: 'API response time increased', time: '45 mins ago', status: 'warning' },
    { id: 3, title: 'New Version', description: 'Update v0.0.1 available', time: '2 hours ago', status: 'info' }
  ];

  const [darkMode, setDarkMode] = useState(false);
  const [isSidebarCollapsed, setIsSidebarCollapsed] = useState(false);
  const [sidebarWidth, setSidebarWidth] = useState(280);
  const isDragging = useRef(false);
  const sidebarRef = useRef(null);
    const { logout } = useAuth();

  const toggleDarkMode = () => {
    setDarkMode(!darkMode);
  };

  const toggleSidebar = () => {
    setIsSidebarCollapsed(!isSidebarCollapsed);
  };

  const handleMouseDown = (e) => {
    isDragging.current = true;
    e.preventDefault();
  };

  const handleMouseMove = (e) => {
    if (isDragging.current && !isSidebarCollapsed) {
      const newWidth = e.clientX;
      if (newWidth >= 200 && newWidth <= 400) {
        setSidebarWidth(newWidth);
      }
    }
  };

  const handleMouseUp = () => {
    isDragging.current = false;
  };

  useEffect(() => {
    document.addEventListener('mousemove', handleMouseMove);
    document.addEventListener('mouseup', handleMouseUp);
    return () => {
      document.removeEventListener('mousemove', handleMouseMove);
      document.removeEventListener('mouseup', handleMouseUp);
    };
  }, []);

  return (
    <div className={`layout-dashboard ${darkMode ? 'layout-dark' : 'layout-light'}`}>
      <div className="layout-dashboard-layout">
        {/* Sidebar */}
        <div
          className={`layout-sidebar ${isSidebarCollapsed ? 'layout-sidebar-collapsed' : ''}`}
          style={{ width: isSidebarCollapsed ? '0' : `${sidebarWidth}px` }}
          ref={sidebarRef}
        >
          <div className="layout-sidebar-header">
            <div className="layout-logo">
              <div className="layout-logo-icon">T</div>
              <h2>Mids<span>Automation</span></h2>
            </div>
          </div>
          
          <div className="layout-menu-group">
            <h3>MAIN</h3>
            <Link to="/dashboard" className="layout-menu-item active">
              <FaTachometerAlt className="layout-icon" />
              <span>Dashboard</span>
            </Link>
            {/* <Link to="/profile" className="layout-menu-item">
              <FaUserCircle className="layout-icon" />
              <span>Profile</span>
            </Link> */}
          </div>
          
          
          <div className="advanceDashboard-menu-group">
                      <h3>AUTOMATION TESTING</h3>
          <div className="treeview">
              <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
                  e.preventDefault();
                  const menuElement = e.currentTarget.nextElementSibling;
                  menuElement.style.display = menuElement.style.display === 'none' ? 'block' : 'none';
                }}>
                  <FaRobot className="advanceDashboard-icon" />
                  <span>Test Scenarios</span>
                </a>
                <ul className="treeview-menu" style={{ display: 'none' }}>
                  <li><a href="/automation-testing/basic-test"><span>Basic Test</span></a></li>
                  <li><a href="/automation-testing/mw-dpr-track"><span>Dpr Track</span></a></li>
                  <li><a href="/automation-testing/mids-test"><span>Mids Test</span></a></li>
                </ul>
            </div>
            <div className="treeview">
              <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
                  e.preventDefault();
                  const menuElement = e.currentTarget.nextElementSibling;
                  menuElement.style.display = menuElement.style.display === 'none' ? 'block' : 'none';
                }}>
                <FaChartLine className="advanceDashboard-icon" />
                <span>Testing Reports</span>
              </a>
              <ul className="treeview-menu" style={{ display: 'none' }}>
                <li><a href="/reports/page-render-report"><span>Page Render Report</span></a></li>
                <li><a href="/reports/download-report"><span>Download Files Report</span></a></li>
                <li><a href="/reports/mids-tests-report"><span>Mids Test Report</span></a></li>
              </ul>
            </div>
            <div className="treeview">
              <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
                  e.preventDefault();
                  const menuElement = e.currentTarget.nextElementSibling;
                  menuElement.style.display = menuElement.style.display === 'none' ? 'block' : 'none';
                }}>
                <FaChartLine className="advanceDashboard-icon" />
                <span>Visualize Reports</span>
              </a>
              <ul className="treeview-menu" style={{ display: 'none' }}>
                <li><a href="/automation-testing/page-performance-dashboard"><span>Visualize Pages Report</span></a></li>
                <li><a href="#"><span>Visualize Download Report</span></a></li>
                <li><a href="#"><span>Visualize Mids Report</span></a></li>
              </ul>
            </div>
          </div>
          
          
                  
          <div className="advanceDashboard-menu-group">
                      <h3>SETTING</h3>
          <div className="treeview">
              <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
                  e.preventDefault();
                  const menuElement = e.currentTarget.nextElementSibling;
                  menuElement.style.display = menuElement.style.display === 'none' ? 'block' : 'none';
                }}>
                  <FaRobot className="advanceDashboard-icon" />
                  <span>Profile Management</span>
                </a>
                <ul className="treeview-menu" style={{ display: 'none' }}>
                  <li><a href="/profile-management/profile-modify"><span>Profile Modify</span></a></li>
                  <li><a href="#"><span>User Report</span></a></li>
                  <li><a href="#"><span>Notifications</span></a></li>
                  <li><a href="#"><span>Delete Account</span></a></li>
                </ul>
            </div>
            <div className="treeview">
              <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
                  e.preventDefault();
                  const menuElement = e.currentTarget.nextElementSibling;
                  menuElement.style.display = menuElement.style.display === 'none' ? 'block' : 'none';
                }}>
                <FaChartLine className="advanceDashboard-icon" />
                <span>Manage Tester Id Credentials</span>
              </a>
              <ul className="treeview-menu" style={{ display: 'none' }}>
                <li><a href="/profile-management/profile-modify"><span>Modify them</span></a></li>
                <li><a href="#"><span>Delete All</span></a></li>
              </ul>
            </div>
            <div className="treeview">
             <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
                  e.preventDefault();
                  const menuElement = e.currentTarget.nextElementSibling;
                  menuElement.style.display = menuElement.style.display === 'none' ? 'block' : 'none';
                }}>
                <FaCog className="advanceDashboard-icon" />
                <span>Configuration</span>
              </a>
              <ul className="treeview-menu" style={{ display: 'none' }}>
                <li><a href="#"><span>General Settings</span></a></li>
                <li><a href="#"><span>Environment</span></a></li>
                <li><a href="/setting/feedback"><span>Feedback</span></a></li>
          
              </ul>
            </div>
          </div>
          
          <div className="layout-menu-group">
            <Link to="/login" onClick={logout} className="layout-menu-item logout">
              <FaSignOutAlt className="layout-icon" />
              <span>Logout</span>
            </Link>
          </div>
          
          {/* <div className="layout-sidebar-footer">
            <div className="layout-version">v0.0.1</div>
            <div className="layout-status-indicator">
              <div className="layout-status-dot"></div>
              <span>Operational</span>
            </div>
          </div> */}
          
          <div
            className="layout-resize-handle"
            onMouseDown={handleMouseDown}
          ></div>
        </div>
        
        {/* Main Content */}
        <div
          className="layout-main-content"
          style={{ marginLeft: isSidebarCollapsed ? '0' : `${sidebarWidth}px` }}
        >
          {/* Header */}
          <div
            className="layout-header"
            style={{ left: isSidebarCollapsed ? '0' : `${sidebarWidth}px` }}
          >
            <div className="layout-header-left">
              <button
                className="layout-sidebar-toggle"
                onClick={toggleSidebar}
                aria-label="Toggle sidebar"
              >
                <FaBars />
              </button>
              <div className="layout-search-bar">
                <div className="layout-search-container">
                  <FaSearch className="layout-icon" />
                  <input type="text" placeholder="Search test cases, scenarios..." />
                </div>
              </div>
            </div>
            
            <div className="layout-header-controls">
              <button
                className="layout-theme-toggle"
                onClick={toggleDarkMode}
                aria-label="Toggle theme"
              >
                {darkMode ? <FaSun /> : <FaMoon />}
              </button>
              
              <div className="advanceDashboard-dropdown">
                              <button className="advanceDashboard-notification-btn" aria-expanded="false">
                                <FaBell />
                                <span className="advanceDashboard-notification-badge">3</span>
                              </button>
                              <div className="advanceDashboard-dropdown-menu">
                                {notifications.map(notification => (
                                  <div key={notification.id} className="advanceDashboard-dropdown-item">
                                    <div className="advanceDashboard-notification-content">
                                      <h5>{notification.title}</h5>
                                      <p>{notification.description}</p>
                                      <p className="advanceDashboard-notification-time">{notification.time}</p>
                                    </div>
                                  </div>
                                ))}
                                <div className="advanceDashboard-dropdown-footer">
                                  <a href="#" className="advanceDashboard-view-all">View all</a>
                                </div>
                              </div>
                            </div>
              
              <button className="layout-profile-btn">
                 <div className="advanceDashboard-dropdown">
                                <a id="userSettings" className="advanceDashboard-dropdown-toggle" href="#!" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                  <FaUserCircle />
                                   <span>Admin User</span>
                                </a>
                                <div className="advanceDashboard-dropdown-menu">
                                  <a className="advanceDashboard-dropdown-item" href="profile.html"><FaUserCircle className="advanceDashboard-icon" />Profile</a>
                                  <a className="advanceDashboard-dropdown-item" href="settings.html"><FaCog className="advanceDashboard-icon" />Account Settings</a>
                                  <a className="advanceDashboard-dropdown-item" href="login.html"><FaSignOutAlt className="advanceDashboard-icon" />Logout</a>
                                </div>
                  </div>
              </button>
            </div>
          </div>
          
          {/* Content */}
          <div className="layout-content">
            <h1 className="layout-title">{title}</h1>
            {children}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Layout;