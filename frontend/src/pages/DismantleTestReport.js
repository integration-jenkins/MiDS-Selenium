import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import MidsTestSideSection from '../components/MidsTestSideSection';

import {
    Box,
    Typography,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Button,
    Chip,
    Grid,
    Divider,
    useTheme,
    useMediaQuery
} from '@mui/material';

const DismantleTestReport = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    // Get the response data from navigation state
    const testResults = location.state?.resp || [];

    // Function to handle back navigation
    const handleBack = () => {
        navigate(-1);
    };

    // Status chip component
    const StatusChip = ({ status }) => (
        <Chip
            label={status}
            size="small"
            sx={{
                backgroundColor: status === 'PASS' ? '#4caf50' : '#f44336',
                color: 'white',
                fontWeight: 600,
                minWidth: 70,
                fontSize: '0.75rem'
            }}
        />
    );

    return (
        <Box sx={{ display: 'flex', minHeight: '100vh' }}>
            <MidsTestSideSection />
            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    p: 3,
                    ml: '250px',
                    width: 'calc(100% - 250px)',
                    backgroundColor: '#f5f5f5'
                }}
            >
                {/* Header Section */}
                <Box sx={{ mb: 3 }}>
                    <Box sx={{ 
                        display: 'flex', 
                        justifyContent: 'space-between', 
                        alignItems: 'center',
                        mb: 1
                    }}>
                        <Typography variant="h4" sx={{ 
                            fontWeight: 600, 
                            color: '#2c3e50'
                        }}>
                            Dismantle Test Report
                        </Typography>
                        <Button
                            variant="contained"
                            onClick={handleBack}
                            sx={{ 
                                backgroundColor: '#3498db',
                                '&:hover': { backgroundColor: '#2980b9' }
                            }}
                        >
                            Back to Dashboard
                        </Button>
                    </Box>
                    <Typography variant="body1" sx={{ 
                        color: '#7f8c8d',
                        mb: 1
                    }}>
                        Detailed results of the dismantle workflow testing
                    </Typography>
                    <Divider />
                </Box>

                {/* Summary Stats */}
                {testResults.length > 0 && (
                    <Grid container spacing={2} sx={{ mb: 3 }}>
                        <Grid item xs={6} sm={3}>
                            <Paper sx={{ p: 2, textAlign: 'center' }}>
                                <Typography variant="subtitle2" sx={{ fontWeight: 600, color: '#2c3e50' }}>
                                    Total Tests
                                </Typography>
                                <Typography variant="h6" sx={{ fontWeight: 700, color: '#2c3e50' }}>
                                    20
                                </Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={6} sm={3}>
                            <Paper sx={{ p: 2, textAlign: 'center' }}>
                                <Typography variant="subtitle2" sx={{ fontWeight: 600, color: '#2c3e50' }}>
                                    Passed
                                </Typography>
                                <Typography variant="h6" sx={{ fontWeight: 700, color: '#4caf50' }}>
                                    {testResults.filter(r => r.testStatus === 'PASS').length}
                                </Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={6} sm={3}>
                            <Paper sx={{ p: 2, textAlign: 'center' }}>
                                <Typography variant="subtitle2" sx={{ fontWeight: 600, color: '#2c3e50' }}>
                                    Failed
                                </Typography>
                                <Typography variant="h6" sx={{ fontWeight: 700, color: '#f44336' }}>
                                    {20 - (testResults.filter(r => r.testStatus === 'PASS').length)}
                                </Typography>
                            </Paper>
                        </Grid>
                        <Grid item xs={6} sm={3}>
                            <Paper sx={{ p: 2, textAlign: 'center' }}>
                                <Typography variant="subtitle2" sx={{ fontWeight: 600, color: '#2c3e50' }}>
                                    Success Rate
                                </Typography>
                                <Typography variant="h6" sx={{ fontWeight: 700, color: '#3498db' }}>
                                    {Math.round(
                                        (testResults.filter(r => r.testStatus === 'PASS').length / 20) * 100
                                    )}%
                                </Typography>
                            </Paper>
                        </Grid>
                    </Grid>
                )}

                {/* Results Table */}
                <Paper elevation={2} sx={{ overflow: 'hidden' }}>
                    <TableContainer>
                        <Table size={isMobile ? 'small' : 'medium'}>
                            <TableHead>
                                <TableRow sx={{ backgroundColor: '#e3f2fd' }}>
                                    <TableCell sx={{ fontWeight: 600, color: '#1976d2' }}>Status</TableCell>
                                    <TableCell sx={{ fontWeight: 600, color: '#1976d2' }}>Dismantle Step</TableCell>
                                    <TableCell sx={{ fontWeight: 600, color: '#1976d2' }}>User</TableCell>
                                    <TableCell sx={{ fontWeight: 600, color: '#1976d2' }}>Department</TableCell>
                                    <TableCell sx={{ fontWeight: 600, color: '#1976d2' }}>Remarks</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {testResults.length > 0 ? (
                                    testResults.map((result, index) => (
                                        <TableRow 
                                            key={result.id} 
                                            sx={{ 
                                                backgroundColor: index % 2 === 0 ? '#fafafa' : 'white'
                                            }}
                                        >
                                            <TableCell>
                                                <StatusChip status={result.testStatus} />
                                            </TableCell>
                                            <TableCell>{result.dismantleStatus}</TableCell>
                                            <TableCell sx={{ color: '#3498db' }}>{result.userName}</TableCell>
                                            <TableCell>{result.departmentName}</TableCell>
                                            <TableCell sx={{
                                                color: result.testStatus === 'FAIL' ? '#f44336' : '#7f8c8d',
                                                maxWidth: 300,
                                                whiteSpace: 'normal',
                                                wordBreak: 'break-word'
                                            }}>
                                                {result.remark}
                                            </TableCell>
                                        </TableRow>
                                    ))
                                ) : (
                                    <TableRow>
                                        <TableCell colSpan={5} align="center" sx={{ py: 4 }}>
                                            <Typography variant="body1" color="text.secondary">
                                                No test results available
                                            </Typography>
                                        </TableCell>
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Paper>
            </Box>
        </Box>
    );
};

export default DismantleTestReport;