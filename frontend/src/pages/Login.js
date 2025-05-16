// src/components/Login.js
import React, { useState } from 'react';
import { FaGoogle, FaGithub, FaLock, FaUser, FaEye, FaEyeSlash } from 'react-icons/fa';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import '../css/Login.css';
// import instance from '../api/axios.js';

const Login = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [isDarkMode, setIsDarkMode] = useState(false);
  const [formData, setFormData] = useState({ username: '', password: '' });const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);
    try {
      const response = await fetch('http://localhost:6083/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: formData.username,
          password: formData.password,
        }),
      });
      //Store the username and password in local storage
      localStorage.setItem('username', formData.username);
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Login failed');
      }

      const data = await response.json();
      login(data.jwt); // Use the token and role from the response
      // navigate('/dashboard'); // Redirect to the dashboard
    } catch (err) {
      setError(err.message || 'Failed to authenticate. Please try again.');
    } finally {
      setIsLoading(false); // Reset loading state
    }


  };

  return (
      <div className={`login-container ${isDarkMode ? 'dark' : 'light'}`}>
        <div className="background-blobs">
          <div className="blob blob-1"></div>
          <div className="blob blob-2"></div>
          <div className="blob blob-3"></div>
        </div>

        <div className="theme-toggle" onClick={() => setIsDarkMode(!isDarkMode)}>
          <div className={`toggle-handle ${isDarkMode ? 'dark' : 'light'}`}></div>
        </div>

        <div className="glass-card">
          <div className="login-header">
            <h1>Welcome Back</h1>
            <p>Please login to continue</p>
          </div>

          <form className="login-form" onSubmit={handleSubmit}>
            {error && <div className="error-message">{error}</div>}

            <div className="input-group">
              <FaUser className="input-icon" />
              <input
                  type="text"
                  placeholder=" "
                  value={formData.username}
                  onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                  required
              />
              <label className="floating-label">Username</label>
            </div>

            <div className="input-group">
              <FaLock className="input-icon" />
              <input
                  type={showPassword ? 'text' : 'password'}
                  placeholder=" "
                  value={formData.password}
                  onChange={(e) => setFormData({...formData, password: e.target.value})}
                  required
              />
              <label className="floating-label">Password</label>
              <button
                  type="button"
                  className="password-toggle"
                  onClick={() => setShowPassword(!showPassword)}
                  aria-label="Toggle password visibility"
              >
                {showPassword ? <FaEyeSlash /> : <FaEye />}
              </button>
            </div>

            <button
                type="submit"
                className="login-button"
                disabled={isLoading}
            >
              {isLoading ? (
                  <div className="spinner"></div>
              ) : (
                  <>
                    Sign In
                    <div className="button-hover-effect"></div>
                  </>
              )}
            </button>

            <div className="social-login">
              <p>Or continue with</p>
              <div className="social-buttons">
                <button type="button" className="social-button google">
                  <FaGoogle />
                </button>
                <button type="button" className="social-button github">
                  <FaGithub />
                </button>
              </div>
            </div>
          </form>

          <div className="login-footer">
            <p>Don't have an account? <a href="/signup">Sign up</a></p>
            <a href="/forgot-password">Forgot password?</a>
          </div>
        </div>
      </div>
  );
};

export default Login;