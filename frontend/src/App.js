import React from 'react';
import DPRForm from './components/DPRForm';
import DPRPage from './components/DPRPage';
import Login from './components/Login';
import Dashboard from './components/Dashboard';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';

const AppRoutes = () => {
    const { isAuthenticated } = useAuth();

    return (

        <Routes>

            <Route path="/dpr" element={ isAuthenticated ?<DPRPage /> :<Navigate to="/login" />} />
            <Route path="/dprform" element={isAuthenticated ? <DPRForm /> : <Navigate to="/login" />} />
            <Route path="/login" element={isAuthenticated ? <Navigate to="/dpr" /> : <Login />} />
            <Route path="/dashboard" element={isAuthenticated ? <Dashboard /> : <Navigate to="/login" />} />
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