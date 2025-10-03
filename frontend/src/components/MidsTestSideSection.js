import { useAuth } from '../context/AuthContext';
import {
  FaTachometerAlt, FaRobot, FaChartLine, FaCog, FaSignOutAlt,
  FaChevronDown, FaChevronRight
} from 'react-icons/fa';
import { useState } from 'react';

const MidsTestSideSection = () => {
  const { logout } = useAuth();
  const [openMenus, setOpenMenus] = useState({});

  const toggleMenu = (menuName) => {
    setOpenMenus(prev => ({
      ...prev,
      [menuName]: !prev[menuName]
    }));
  };

  return (
    <div className="advanceDashboard-sidebar" style={{
      width: '265px',
      minHeight: '100vh',
      position: 'fixed',
      left: 0,
      top: 0,
      backgroundColor: '#2c3e50',
      color: '#ecf0f1',
      overflowY: 'auto',
      zIndex: 1000,
      boxSizing: 'border-box',
      padding: '0 0 20px 0'
    }}>
      <div className="advanceDashboard-sidebar-header" style={{ padding: '20px 15px' }}>
        <div className="advanceDashboard-logo" style={{ display: 'flex', alignItems: 'center' }}>
          <div className="advanceDashboard-logo-icon" style={{
            width: '40px',
            height: '40px',
            borderRadius: '8px',
            backgroundColor: '#3498db',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            marginRight: '10px',
            fontWeight: 'bold',
            fontSize: '20px'
          }}>T</div>
          <h2 style={{ margin: 0, fontSize: '18px', fontWeight: '600' }}>
            Mids<span style={{ color: '#3498db' }}>Automation</span>
          </h2>
        </div>
      </div>

      <div style={{ padding: '0 15px', marginBottom: '20px' }}>
        <div className="advanceDashboard-menu-group" style={{ marginBottom: '20px' }}>
          <h3 style={{
            fontSize: '12px',
            textTransform: 'uppercase',
            color: '#95a5a6',
            margin: '0 0 10px 5px',
            fontWeight: '600'
          }}>MAIN</h3>
          <a href="/dashboard" className="advanceDashboard-menu-item" style={{
            display: 'flex',
            alignItems: 'center',
            padding: '10px 15px',
            borderRadius: '5px',
            color: '#ecf0f1',
            textDecoration: 'none',
            transition: 'all 0.3s',
            backgroundColor: '#3498db'
          }}>
            <FaTachometerAlt style={{ marginRight: '10px', fontSize: '16px' }} />
            <span>Dashboard</span>
          </a>
        </div>

        <div className="advanceDashboard-menu-group" style={{ marginBottom: '20px' }}>
          <h3 style={{
            fontSize: '12px',
            textTransform: 'uppercase',
            color: '#95a5a6',
            margin: '0 0 10px 5px',
            fontWeight: '600'
          }}>AUTOMATION TESTING</h3>

          <div className="treeview" style={{ marginBottom: '5px' }}>
            <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
              e.preventDefault();
              toggleMenu('testScenarios');
            }} style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              padding: '10px 15px',
              borderRadius: '5px',
              color: '#ecf0f1',
              textDecoration: 'none',
              transition: 'all 0.3s'
            }}>
              <div style={{ display: 'flex', alignItems: 'center' }}>
                <FaRobot style={{ marginRight: '10px', fontSize: '16px' }} />
                <span>Test Scenarios</span>
              </div>
              {openMenus.testScenarios ?
                <FaChevronDown style={{ fontSize: '12px' }} /> :
                <FaChevronRight style={{ fontSize: '12px' }} />
              }
            </a>
            {openMenus.testScenarios && (
              <ul style={{
                listStyle: 'none',
                padding: '0',
                margin: '5px 0 10px 20px',
                borderLeft: '1px solid #34495e'
              }}>
                <li style={{ marginBottom: '5px' }}><a href="/automation-testing/basic-test" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Basic Test</span></a></li>
                <li style={{ marginBottom: '5px' }}><a href="/automation-testing/mw-dpr-track" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Dpr Track</span></a></li>
                <li style={{ marginBottom: '5px' }}><a href="/automation-testing/mids-test" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Mids Test</span></a></li>
                <li style={{ marginBottom: '5px' }}><a href="/dismantle/dashboard" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Dismantle Testing</span></a></li>
                <li style={{ marginBottom: '5px' }}><a href="/trafficShifting/dashboard" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Traffic Shifting Testing</span></a></li>
              </ul>
            )}
          </div>

          <div className="treeview" style={{ marginBottom: '5px' }}>
            <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
              e.preventDefault();
              toggleMenu('testingReports');
            }} style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              padding: '10px 15px',
              borderRadius: '5px',
              color: '#ecf0f1',
              textDecoration: 'none',
              transition: 'all 0.3s'
            }}>
              <div style={{ display: 'flex', alignItems: 'center' }}>
                <FaChartLine style={{ marginRight: '10px', fontSize: '16px' }} />
                <span>Testing Reports</span>
              </div>
              {openMenus.testingReports ?
                <FaChevronDown style={{ fontSize: '12px' }} /> :
                <FaChevronRight style={{ fontSize: '12px' }} />
              }
            </a>
            {openMenus.testingReports && (
              <ul style={{
                listStyle: 'none',
                padding: '0',
                margin: '5px 0 10px 20px',
                borderLeft: '1px solid #34495e'
              }}>
                <li style={{ marginBottom: '5px' }}><a href="/reports/page-render-report" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Page Render Report</span></a></li>
                <li style={{ marginBottom: '5px' }}><a href="/reports/download-report" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Download Files Report</span></a></li>
                <li style={{ marginBottom: '5px' }}><a href="/reports/mids-tests-report" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Mids Test Report</span></a></li>
              </ul>
            )}
          </div>
        </div>

        <div className="advanceDashboard-menu-group" style={{ marginBottom: '20px' }}>
          <h3 style={{
            fontSize: '12px',
            textTransform: 'uppercase',
            color: '#95a5a6',
            margin: '0 0 10px 5px',
            fontWeight: '600'
          }}>SETTING</h3>

          <div className="treeview" style={{ marginBottom: '5px' }}>
            <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
              e.preventDefault();
              toggleMenu('profileManagement');
            }} style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              padding: '10px 15px',
              borderRadius: '5px',
              color: '#ecf0f1',
              textDecoration: 'none',
              transition: 'all 0.3s'
            }}>
              <div style={{ display: 'flex', alignItems: 'center' }}>
                <FaRobot style={{ marginRight: '10px', fontSize: '16px' }} />
                <span>Profile Management</span>
              </div>
              {openMenus.profileManagement ?
                <FaChevronDown style={{ fontSize: '12px' }} /> :
                <FaChevronRight style={{ fontSize: '12px' }} />
              }
            </a>
            {openMenus.profileManagement && (
              <ul style={{
                listStyle: 'none',
                padding: '0',
                margin: '5px 0 10px 20px',
                borderLeft: '1px solid #34495e'
              }}>
                <li style={{ marginBottom: '5px' }}><a href="/profile-management/profile-modify" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Profile Modify</span></a></li>
                <li style={{ marginBottom: '5px' }}><a href="#" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>User Report</span></a></li>
                <li style={{ marginBottom: '5px' }}><a href="#" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Delete Account</span></a></li>
              </ul>
            )}
          </div>

          <div className="treeview" style={{ marginBottom: '5px' }}>
            <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
              e.preventDefault();
              toggleMenu('testerCredentials');
            }} style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              padding: '10px 15px',
              borderRadius: '5px',
              color: '#ecf0f1',
              textDecoration: 'none',
              transition: 'all 0.3s'
            }}>
              <div style={{ display: 'flex', alignItems: 'center' }}>
                <FaChartLine style={{ marginRight: '10px', fontSize: '16px' }} />
                <span>Manage Tester Id Credentials</span>
              </div>
              {openMenus.testerCredentials ?
                <FaChevronDown style={{ fontSize: '12px' }} /> :
                <FaChevronRight style={{ fontSize: '12px' }} />
              }
            </a>
            {openMenus.testerCredentials && (
              <ul style={{
                listStyle: 'none',
                padding: '0',
                margin: '5px 0 10px 20px',
                borderLeft: '1px solid #34495e'
              }}>
                <li style={{ marginBottom: '5px' }}><a href="/profile-management/profile-modify" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Modify them</span></a></li>
                 <li style={{ marginBottom: '5px' }}><a href="#" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Test User Management</span></a></li>
                <li style={{ marginBottom: '5px' }}><a href="#" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Delete All</span></a></li>
              </ul>
            )}
          </div>

          <div className="treeview" style={{ marginBottom: '5px' }}>
            <a href="#" className="advanceDashboard-menu-item" onClick={(e) => {
              e.preventDefault();
              toggleMenu('configuration');
            }} style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              padding: '10px 15px',
              borderRadius: '5px',
              color: '#ecf0f1',
              textDecoration: 'none',
              transition: 'all 0.3s'
            }}>
              <div style={{ display: 'flex', alignItems: 'center' }}>
                <FaCog style={{ marginRight: '10px', fontSize: '16px' }} />
                <span>Configuration</span>
              </div>
              {openMenus.configuration ?
                <FaChevronDown style={{ fontSize: '12px' }} /> :
                <FaChevronRight style={{ fontSize: '12px' }} />
              }
            </a>
            {openMenus.configuration && (
              <ul style={{
                listStyle: 'none',
                padding: '0',
                margin: '5px 0 10px 20px',
                borderLeft: '1px solid #34495e'
              }}>
                <li style={{ marginBottom: '5px' }}><a href="#" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>General Settings</span></a></li>
                <li style={{ marginBottom: '5px' }}><a href="#" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Environment</span></a></li>
                <li style={{ marginBottom: '5px' }}><a href="/setting/feedback" style={{
                  display: 'block',
                  padding: '8px 15px',
                  color: '#bdc3c7',
                  textDecoration: 'none',
                  borderRadius: '3px',
                  fontSize: '14px',
                  transition: 'all 0.2s'
                }}><span>Feedback</span></a></li>
              </ul>
            )}
          </div>
        </div>

        <div className="advanceDashboard-menu-group">
          <a href="/login" onClick={logout} className="advanceDashboard-menu-item" style={{
            display: 'flex',
            alignItems: 'center',
            padding: '10px 15px',
            borderRadius: '5px',
            color: '#e74c3c',
            textDecoration: 'none',
            transition: 'all 0.3s'
          }}>
            <FaSignOutAlt style={{ marginRight: '10px', fontSize: '16px' }} />
            <span>Logout</span>
          </a>
        </div>
      </div>

      <div className="advanceDashboard-sidebar-footer" style={{
        padding: '15px',
        borderTop: '1px solid #34495e',
        marginTop: 'auto',
        position: 'absolute',
        bottom: '0',
        width: '100%',
        boxSizing: 'border-box'
      }}>
        <div className="advanceDashboard-version" style={{
          fontSize: '12px',
          color: '#95a5a6',
          marginBottom: '5px'
        }}>v0.0.1</div>
        <div className="advanceDashboard-status-indicator" style={{
          display: 'flex',
          alignItems: 'center',
          fontSize: '12px',
          color: '#95a5a6'
        }}>
          <div className="advanceDashboard-status-dot" style={{
            width: '8px',
            height: '8px',
            borderRadius: '50%',
            backgroundColor: '#2ecc71',
            marginRight: '5px'
          }}></div>
          <span>Operational</span>
        </div>
      </div>
    </div>
  );
};

export default MidsTestSideSection;