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
const AppRoutes = () => {
    const { isAuthenticated, loading } = useAuth();

    // Show loading state while checking authentication
    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <Routes>
            <Route path="/" element={<Navigate to="/login" />} />
            <Route path="/dpr" element={isAuthenticated ? <DPRPage /> : <Navigate to="/login" />} />
            <Route path="/dprform" element={isAuthenticated ? <DPRForm /> : <Navigate to="/login" />} />
            <Route path="/login" element={isAuthenticated ? <Navigate to="/dashboard" /> : <Login />} />
            <Route path="/dashboard" element={isAuthenticated ? <Dashboard /> : <Navigate to="/login" />} />
            <Route path="/signup" element={isAuthenticated ? <Navigate to="/dashboard" /> : <Signup />} />
            <Route path="/midstest" element={isAuthenticated ? <MidsTest /> : <Navigate to="/login" />} />
            <Route path="/testing" element={isAuthenticated ? <TestingPage /> : <Navigate to="/login" />} />
            <Route path="/mwdpr" element={isAuthenticated ? <MWDPRPage /> : <Navigate to="/login" />} />
            <Route path="/testreports" element={isAuthenticated ? <TestReports /> : <Navigate to="/login" />} />
            <Route path="/download-report" element={isAuthenticated ? <DownloadReportTests /> : <Navigate to="/login" />} />
            <Route path="/basic-test-report" element={isAuthenticated ? <BasicTestReports /> : <Navigate to="/login" />} />
            <Route path="/hypnotic-loader" element={isAuthenticated ? <AdvanceLoader /> : <Navigate to="/login" />} />
            

            {/* Add more routes as needed */}
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