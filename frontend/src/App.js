import React from 'react';
import DPRForm from './components/DPRForm';
import DPRPage from './components/DPRPage';
import Login from './pages/Login';
import Signup from './pages/SignUp';
import Dashboard from './components/Dashboard';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import MidsTest from './pages/MidsTest';
import MWDPRPage from './pages/MWDPRPage';
import TestingPage from './pages/TestingPage';
import TestReports from './pages/TestReports';
import BasicTestReports from './pages/BasicTestReports';
import DownloadReportTests from './pages/DownloadReportTests';
import AdvanceLoader from './pages/AdvanceLoader';
import ProfileManagement from './pages/ProfileManagement';
import FeedbackPage from './pages/FeedbackPage';
import PagePerformanceDashboard from './pages/PagePerformanceDashboard';
import AdvancedDashboard from './pages/AdvancedDashboard';
import PageReportVisualization from './pages/PageReportVisualization';
import BasicTestPage from './pages/BasicTest';
const AppRoutes = () => {
    const { isAuthenticated, loading } = useAuth();
    

    // Show loading state while checking authentication
    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <Routes>
            <Route path="/" element={<Navigate to="/login" />} />
            <Route path="/login" element={isAuthenticated ? <Navigate to="/dashboard" /> : <Login />} />
            {/* <Route path="/dashboard" element={isAuthenticated ? <Dashboard /> : <Navigate to="/login" />} /> */}
            <Route path="/signup" element={isAuthenticated ? <Navigate to="/dashboard" /> : <Signup />} />
            <Route path="/automation-testing/mids-test" element={isAuthenticated ? <MidsTest /> : <Navigate to="/login" />} />
            <Route path="/automation-testing" element={isAuthenticated ? <TestingPage /> : <Navigate to="/login" />} />
            <Route path="/automation-testing/mw-dpr-track" element={isAuthenticated ? <MWDPRPage /> : <Navigate to="/login" />} />
            <Route path="/reports/mids-tests-report" element={isAuthenticated ? <TestReports /> : <Navigate to="/login" />} />
            <Route path="/reports/download-report" element={isAuthenticated ? <DownloadReportTests /> : <Navigate to="/login" />} />
            <Route path="/reports/page-render-report" element={isAuthenticated ? <BasicTestReports /> : <Navigate to="/login" />} />
//          <Route path="/hypnotic-loader" element={isAuthenticated ? <AdvanceLoader /> : <Navigate to="/login" />} />
            <Route path="/automation-testing/page-performance-dashboard" element={isAuthenticated ? <PagePerformanceDashboard /> : <Navigate to="/login" />} />
            
            <Route path="/profile-management/profile-modify" element={isAuthenticated ? <ProfileManagement /> : <Navigate to="/login" />} />
            <Route path="/setting/feedback" element={isAuthenticated ? <FeedbackPage /> : <Navigate to="/login" />} />
            <Route path="/dashboard" element={isAuthenticated ?<AdvancedDashboard /> : <Navigate to="/login" />} />
            <Route path="/page-report-visualization" element={isAuthenticated?<PageReportVisualization />: <Navigate to="/login" />} />
            <Route path="/automation-testing/basic-test" element={true ?<BasicTestPage/>: <Navigate to="/login" />}/>
        </Routes>
    );
};

const App = () => {
    return (
        <Router>
            <AuthProvider>
                <AppRoutes />
            </AuthProvider>
        </Router>
    );
};

export default App;