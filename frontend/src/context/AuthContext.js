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
        //Get Local store time as the token valid 10hours only
        const localStoreTime = localStorage.getItem('localStoreTime');
        const currentTime = new Date().getTime();
        const difference = currentTime - localStoreTime;
        if (token && difference < 36000000) {
            setIsAuthenticated(true);
        } else {
            localStorage.removeItem('jwtToken');
            localStorage.removeItem('localStoreTime');
            localStorage.removeItem('username');
            setIsAuthenticated(false);
        }
        setLoading(false); // Update loading state after check
    }, []);

    const login = (token, role) => {
        localStorage.setItem('jwtToken', token);
        localStorage.setItem('localStoreTime', new Date().getTime());
        // setUser({ role });
        setIsAuthenticated(true);
        navigate('/dashboard'); 
    };

    const logout = () => {
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('localStoreTime');
        localStorage.removeItem('username');
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