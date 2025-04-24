import React from 'react';
import DPRForm from './components/DPRForm';
import DPRPage from './components/DPRPage';
import Login from './pages/Login';
import Signup from './pages/SignUp';
import Dashboard from './components/Dashboard';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import MidsTest from './pages/MidsTest';
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