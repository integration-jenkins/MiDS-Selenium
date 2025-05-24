import { createContext, useContext, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    // const [user, setUser] = useState(null);
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [loading, setLoading] = useState(true); // Add loading state
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('jwtToken');
        // const role = localStorage.getItem('userRole');
        if (token) {
            // setUser({ role });
            setIsAuthenticated(true);
        } else {
            setIsAuthenticated(false);
        }
        setLoading(false); // Update loading state after check
    }, []);

    const login = (token, role) => {
        localStorage.setItem('jwtToken', token);
        // setUser({ role });
        setIsAuthenticated(true);
        navigate('/dashboard'); 
    };

    const logout = () => {
        localStorage.removeItem('jwtToken');
        // localStorage.removeItem('userRole');
        // setUser(null);
        setIsAuthenticated(false);
        navigate('/login');
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);