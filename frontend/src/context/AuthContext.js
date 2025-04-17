// src/context/AuthContext.js
import { createContext, useContext, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('jwtToken');
        const role = localStorage.getItem('userRole');

        console.log('useEffect triggered:', { token, role });

        if (token && role) {
            setUser((prevUser) => {
                if (prevUser?.role !== role) {
                    console.log('Updating user state');
                    return { role };
                }
                return prevUser;
            });

            setIsAuthenticated((prevAuth) => {
                if (!prevAuth) {
                    console.log('Updating isAuthenticated state');
                    return true;
                }
                return prevAuth;
            });
        }
    }, []); // Runs only once/ Add an empty dependency array

    const login = (token, role) => {
        localStorage.setItem('jwtToken', token);
        localStorage.setItem('userRole', role);
        setUser({ role });
        setIsAuthenticated(true);
        navigate('/dpr');
    };

    const logout = () => {
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('userRole');
        setUser(null);
        setIsAuthenticated(false);
        navigate('/login');
    };

    return (
        <AuthContext.Provider value={{ user, isAuthenticated, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);