import React, { useState, useEffect } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, 
         TextField, Button, Tabs, Tab, Box, Snackbar, Alert } from '@mui/material';
import api from '../api/axiosConfig';
import '../css/RoleCredentialsDialog.css';

const RoleCredentialsDialog = ({ open, onClose, missingRoles }) => {
  const [activeTab, setActiveTab] = useState(0);
  const [credentials, setCredentials] = useState({});
  const [circle, setCircle] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [notification, setNotification] = useState({ open: false, message: '', severity: 'success' });

  useEffect(() => {
    if (open && missingRoles && missingRoles.length > 0) {
      const initialCreds = {};
      missingRoles.forEach(role => {
        initialCreds[role] = { username: '', password: '' };
      });
      setCredentials(initialCreds);
      setActiveTab(0); // Reset to first tab when dialog opens
    }
    console.log('Missing Roles:', missingRoles);
  }, [missingRoles, open]);

  const handleRoleChange = (role, field, value) => {
    setCredentials(prev => ({
      ...prev,
      [role]: { ...prev[role], [field]: value }
    }));
  };

  const handleSubmit = async () => {
    try {
      setIsSubmitting(true);
      
      // Validate inputs
      if (!circle.trim()) {
        showNotification('Circle is required', 'error');
        return;
      }
      
      for (const role of missingRoles) {
        if (!credentials[role]?.username || !credentials[role]?.password) {
          showNotification(`Please complete credentials for ${role}`, 'error');
          return;
        }
      }
      
      // Format the payload as a list of credential objects as expected by the API
      const payload = Object.entries(credentials).map(([role, creds]) => ({
        userName: creds.username,
        password: creds.password,
        doneBy: role,
        circle: circle.trim()
      }));
  
      // The API endpoint expects a direct array in the request body
      const response = await api.post('/api/sampleUserCredentials/addUser', payload);
      
      if (response.status === 200 || response.status === 201) {
        showNotification('Credentials saved successfully!', 'success');
        setTimeout(() => onClose(true), 1500); // Close after showing success message
      } else {
        // Handle specific error responses based on the API
        const errorMessage = response.data || 'Error saving credentials';
        showNotification(errorMessage, 'error');
      }
    } catch (error) {
      console.error('Error saving credentials:', error);
      // Extract the error message from the response if available
      const errorMessage = error.response?.data || error.message || 'Error saving credentials';
      showNotification(errorMessage, 'error');
    } finally {
      setIsSubmitting(false);
    }
  };
  
  const showNotification = (message, severity = 'success') => {
    setNotification({ open: true, message, severity });
  };
  
  const handleCloseNotification = () => {
    setNotification({ ...notification, open: false });
  };

  return (
    <>
      <Dialog 
        open={open} 
        onClose={() => onClose(false)} 
        maxWidth="md" 
        fullWidth
        PaperProps={{ className: "credentials-dialog" }}
      >
        <DialogTitle className="dialog-header">
          <div className="gradient-text">Add Required Credentials</div>
          <div className="subtitle">Missing credentials for {missingRoles?.length || 0} roles</div>
        </DialogTitle>

        <DialogContent>
          <div className="circle-input">
            <TextField
              label="Circle"
              value={circle}
              onChange={(e) => setCircle(e.target.value)}
              fullWidth
              margin="normal"
              required
            />
          </div>

          {missingRoles && missingRoles.length > 0 && (
            <>
              <Tabs 
                value={activeTab} 
                onChange={(e, newVal) => setActiveTab(newVal)}
                variant="scrollable"
                scrollButtons="auto"
              >
                {missingRoles.map((role) => (
                  <Tab key={role} label={role} />
                ))}
              </Tabs>

              <Box sx={{ pt: 3 }}>
                {missingRoles.map((role, index) => (
                  <div key={role} hidden={activeTab !== index}>
                    <div className="credential-grid">
                      <TextField
                        label="Username"
                        value={credentials[role]?.username || ''}
                        onChange={(e) => handleRoleChange(role, 'username', e.target.value)}
                        fullWidth
                        margin="normal"
                        required
                      />
                      <TextField
                        label="Password"
                        type="password"
                        value={credentials[role]?.password || ''}
                        onChange={(e) => handleRoleChange(role, 'password', e.target.value)}
                        fullWidth
                        margin="normal"
                        required
                      />
                    </div>
                  </div>
                ))}
              </Box>
            </>
          )}
        </DialogContent>

        <DialogActions>
          <Button onClick={() => onClose(false)}>Cancel</Button>
          <Button 
            onClick={handleSubmit}
            variant="contained" 
            color="primary"
            disabled={isSubmitting || !circle || Object.values(credentials).some(c => !c?.username || !c?.password)}
          >
            {isSubmitting ? 'Saving...' : 'Save All Credentials'}
          </Button>
        </DialogActions>
      </Dialog>
      
      <Snackbar 
        open={notification.open} 
        autoHideDuration={6000} 
        onClose={handleCloseNotification}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert onClose={handleCloseNotification} severity={notification.severity}>
          {notification.message}
        </Alert>
      </Snackbar>
    </>
  );
};

export default RoleCredentialsDialog;