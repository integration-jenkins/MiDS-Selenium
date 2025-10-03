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
    LinearProgress
} from '@mui/material';

const TestConfigForm = () => {
    const [formData, setFormData] = useState({
        hopId: '',
        circle: '',
        partnerAllocationName: '',
        dismantleUser: {
            "Circle MW Planner": ['', ''],
            "Circle Operation Team": ['', ''],
            "Circle Deployment Team": ['', ''],
            "Circle I&C Partner": ['', '']
        },
        filePath: ''
    });

    const [isLoading, setIsLoading] = useState(false);
    const pcCodes = ['HAR', 'PB', 'ASM', 'KOL', 'ROTN', 'MP', 'HP', 'NE', 'ROB',
        'AP', 'KK', 'MAH', 'GUJ', 'BIH', 'KER', 'MUM', 'JH', 'JK',
        'ORI', 'UPE', 'UPW', 'DEL', 'RAJ', 'CHN'];
    const passwordPlaceholder = '••••••••';

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleDismantleUserChange = (field, index, value) => {
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

    const moveToResultView = (rseponce) => {
        navigate('/dismantle/dashboard/form/testReport', {
            state: {
                resp: rseponce
            }
        }
        );
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log('Form submitted:', formData);

        setIsLoading(true); // Show loading bar

        try {
            const resp = await api.post('/api/dismantleTest/workflow', formData);
            console.log(resp);
            if (resp.data && Object.keys(resp.data).length > 0) {
                moveToResultView(resp.data)
            }
        } catch (error) {
            console.log(error);
        } finally {
            setIsLoading(false); // Hide loading bar
        }
    };


    return (
        <div style={{ display: 'flex' }}>
            <MidsTestSideSection />
            <Box sx={{ maxWidth: 800, margin: 'auto', p: 3, flex: 1 }}>
                <Paper elevation={3} sx={{ p: 4, borderRadius: 2 }}>
                    <Box sx={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center',
                        mb: 3
                    }}>
                        <Typography variant="h4" sx={{ fontWeight: 600, color: 'primary.main' }}>
                            Test Configuration Form
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
                    </Box>

                    <form onSubmit={handleSubmit}>
                        {/* Basic Information Section */}
                        <Box sx={{ mb: 4 }}>
                            <Typography variant="h6" gutterBottom sx={{ fontWeight: 500 }}>
                                Basic Information
                            </Typography>
                            <Divider sx={{ mb: 3 }} />

                            <Grid container spacing={3}>
                                <Grid item xs={12} md={6}>
                                    <TextField
                                        fullWidth
                                        label="HOP ID"
                                        name="hopId"
                                        value={formData.hopId}
                                        onChange={handleChange}
                                        variant="outlined"
                                        size="small"
                                    />
                                </Grid>

                                <Grid item xs={12} md={6}>
                                    <FormControl fullWidth size="medium" sx={{ paddingRight: '75px' }}>
                                        <InputLabel>Circle</InputLabel>
                                        <Select
                                            label="Circle"
                                            name="circle"
                                            value={formData.circle}
                                            onChange={handleChange}
                                        >
                                            {pcCodes.map(code => (
                                                <MenuItem key={code} value={code}>{code}</MenuItem>
                                            ))}
                                        </Select>
                                    </FormControl>
                                </Grid>

                                <Grid item xs={12}>
                                    <TextField
                                        fullWidth
                                        label="Partner Allocation Name"
                                        name="partnerAllocationName"
                                        value={formData.partnerAllocationName}
                                        onChange={handleChange}
                                        variant="outlined"
                                        size="small"
                                    />
                                </Grid>

                                <Grid item xs={12}>
                                    <TextField
                                        fullWidth
                                        label="File Path"
                                        name="filePath"
                                        value={formData.filePath}
                                        onChange={handleChange}
                                        variant="outlined"
                                        size="small"
                                    />
                                </Grid>
                            </Grid>
                        </Box>

                        {/* Dismantle User Credentials Section */}
                        <Box sx={{ mb: 4 }}>
                            <Typography variant="h6" gutterBottom sx={{ fontWeight: 500 }}>
                                Dismantle User Credentials
                            </Typography>
                            <Divider sx={{ mb: 3 }} />

                            <Grid container spacing={3}>
                                {Object.entries(formData.dismantleUser).map(([field, [username, password]]) => (
                                    <React.Fragment key={field}>
                                        <Grid item xs={12}>
                                            <Typography variant="subtitle1" sx={{ mb: 1, fontWeight: 500 }}>
                                                {field}
                                            </Typography>
                                        </Grid>
                                        <Grid item xs={12} md={6}>
                                            <TextField
                                                fullWidth
                                                label="Username"
                                                value={username}
                                                onChange={(e) => handleDismantleUserChange(field, 0, e.target.value)}
                                                variant="outlined"
                                                size="small"
                                            />
                                        </Grid>
                                        <Grid item xs={12} md={6}>
                                            <TextField
                                                fullWidth
                                                label="Password"
                                                type="password"
                                                value={password}
                                                onChange={(e) => handleDismantleUserChange(field, 1, e.target.value)}
                                                placeholder={passwordPlaceholder}
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
        </div>
    );
};

export default TestConfigForm;