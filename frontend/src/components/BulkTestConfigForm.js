import React, { useState } from 'react';
import api from '../api/axiosConfig';
import { useNavigate } from 'react-router-dom';
import MidsTestSideSection from '../components/MidsTestSideSection';

import {
    Box,
    TextField,
    Button,
    Typography,
    Paper,
    Grid,
    Divider,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    LinearProgress,
    Alert
} from '@mui/material';

const BulkTestConfigForm = () => {
    const initialData = {
        hopId: "",
        circle: "",
        partnerAllocationName: "",
        dismantleUser: {
            "Circle MW Planner": ["", ""],
            "Circle Operation Team": ["", ""],
            "Circle Deployment Team": ["", ""],
            "Circle I&C Partner": ["", ""]
        },
        filePath: "",
        department: ""
    };

    const [formData, setFormData] = useState(initialData);
    const [selectedDepartment, setSelectedDepartment] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const circles = ['HAR', 'PB', 'ASM', 'KOL', 'ROTN', 'MP', 'HP', 'NE', 'ROB',
        'AP', 'KK', 'MAH', 'GUJ', 'BIH', 'KER', 'MUM', 'JH', 'JK',
        'ORI', 'UPE', 'UPW', 'DEL', 'RAJ', 'CHN'];
    const departments = Object.keys(formData.dismantleUser);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleDepartmentChange = (e) => {
        setSelectedDepartment(e.target.value);
        setFormData(prev => ({
            ...prev,
            department: e.target.value
        }));
    };

    const handleUserCredentialChange = (field, index, value) => {
        setFormData(prev => {
            const updatedDismantleUser = { ...prev.dismantleUser };
            updatedDismantleUser[field][index] = value;
            return {
                ...prev,
                dismantleUser: updatedDismantleUser
            };
        });
    };

    const navigate = useNavigate();

    const moveToResultView = (response) => {
        navigate('/dismantle/dashboard/form/testReport', {
            state: {
                resp: response
            }
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setSuccess(null);
        setIsLoading(true);

        try {
            // Validate at least one department has credentials
            const hasCredentials = Object.values(formData.dismantleUser).some(
                ([username, password]) => username && password
            );

            if (!hasCredentials) {
                throw new Error("Please enter credentials for at least one department");
            }

            const response = await api.post('/api/dismantleTest/bulkTest', formData);
            console.log(response);

            if (response.data && Object.keys(response.data).length > 0) {
                setSuccess("Configuration submitted successfully!");
                setTimeout(() => moveToResultView(response.data), 1500);
            }
        } catch (err) {
            console.error(err);
            setError(err.message || "Failed to submit configuration");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <Box sx={{ display: 'flex' }}>
            <MidsTestSideSection />
            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    p: 3,
                    marginLeft: '250px', // Adjust based on your sidebar width
                    maxWidth: 'calc(100% - 250px)' // Adjust based on your sidebar width
                }}
            >
                <Paper elevation={3} sx={{ p: 4, borderRadius: 2 }}>
                    <Typography variant="h4" gutterBottom sx={{ mb: 3, fontWeight: 600, color: 'primary.main' }}>
                        Dismantle Bulk Upload Configuration
                    </Typography>
                    <Button
                        variant="outlined"
                        onClick={() => navigate(-1)}
                        sx={{
                            minWidth: 'auto',
                            padding: '8px 16px'
                        }}
                    >
                        Back
                    </Button>

                    {error && (
                        <Alert severity="error" sx={{ mb: 3 }}>
                            {error}
                        </Alert>
                    )}

                    {success && (
                        <Alert severity="success" sx={{ mb: 3 }}>
                            {success}
                        </Alert>
                    )}

                    <form onSubmit={handleSubmit}>
                        {/* Basic Information Section */}
                        <Box sx={{ mb: 4 }}>
                            <Typography variant="h6" gutterBottom sx={{ fontWeight: 500 }}>
                                Basic Information
                            </Typography>
                            <Divider sx={{ mb: 3 }} />

                            <Grid container spacing={3}>
                                <Grid item xs={12} md={6}>
                                    <FormControl fullWidth size="medium" sx={{
                                        paddingRight: '75px',
                                        height: '70px',
                                        '& .MuiOutlinedInput-root': { height: '56px' }
                                    }} required>
                                        <InputLabel>Circle</InputLabel>
                                        <Select
                                            label="Circle"
                                            name="circle"
                                            value={formData.circle}
                                            onChange={handleChange}
                                        >
                                            {circles.map(code => (
                                                <MenuItem key={code} value={code}>{code}</MenuItem>
                                            ))}
                                        </Select>
                                    </FormControl>
                                </Grid>

                                <Grid item xs={12} md={6}>
                                    <FormControl fullWidth size="medium" sx={{
                                        paddingRight: '125px',
                                        height: '70px',
                                        '& .MuiOutlinedInput-root': { height: '56px' }
                                    }} required>
                                        <InputLabel>Department</InputLabel>
                                        <Select
                                            label="Department"
                                            value={selectedDepartment}
                                            onChange={handleDepartmentChange}
                                        >
                                            {departments.map(dept => (
                                                <MenuItem key={dept} value={dept}>{dept}</MenuItem>
                                            ))}
                                        </Select>
                                    </FormControl>
                                </Grid>

                                <Grid item xs={12}>
                                    <TextField
                                        fullWidth
                                        label="File Path"
                                        name="filePath"
                                        value={formData.filePath}
                                        onChange={handleChange}
                                        variant="outlined"
                                        sx={{
                                            height: '70px',
                                            '& .MuiOutlinedInput-root': { height: '56px' }
                                        }}
                                        size="small"
                                        required
                                    />
                                </Grid>
                            </Grid>
                        </Box>

                        {/* Dismantle User Credentials Section */}
                        <Box sx={{ mb: 4 }}>
                            <Typography variant="h6" gutterBottom sx={{ fontWeight: 500 }}>
                                Department Credentials
                            </Typography>
                            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                                Enter credentials for the departments that will perform dismantling
                            </Typography>
                            <Divider sx={{ mb: 3 }} />

                            <Grid container spacing={3}>
                                {departments.map((department) => (
                                    <React.Fragment key={department}>
                                        <Grid item xs={12}>
                                            <Typography variant="subtitle1" sx={{ fontWeight: 500 }}>
                                                {department}
                                            </Typography>
                                        </Grid>
                                        <Grid item xs={12} md={6}>
                                            <TextField
                                                fullWidth
                                                label="Username"
                                                value={formData.dismantleUser[department][0]}
                                                onChange={(e) => handleUserCredentialChange(department, 0, e.target.value)}
                                                variant="outlined"
                                                size="small"
                                            />
                                        </Grid>
                                        <Grid item xs={12} md={6}>
                                            <TextField
                                                fullWidth
                                                label="Password"
                                                type="password"
                                                value={formData.dismantleUser[department][1]}
                                                onChange={(e) => handleUserCredentialChange(department, 1, e.target.value)}
                                                variant="outlined"
                                                size="small"
                                            />
                                        </Grid>
                                    </React.Fragment>
                                ))}
                            </Grid>
                        </Box>

                        {/* Submit Button */}
                        <Box sx={{ mt: 2 }}>
                            <Button
                                type="submit"
                                variant="contained"
                                size="large"
                                fullWidth
                                disabled={isLoading}
                                sx={{
                                    py: 1.5,
                                    fontWeight: 500,
                                    fontSize: '1rem'
                                }}
                            >
                                {isLoading ? 'Processing...' : 'Submit Configuration'}
                            </Button>
                            {isLoading && (
                                <Box sx={{ width: '100%', mt: 2 }}>
                                    <LinearProgress />
                                </Box>
                            )}
                        </Box>
                    </form>
                </Paper>
            </Box>
        </Box>
    );
};

export default BulkTestConfigForm;