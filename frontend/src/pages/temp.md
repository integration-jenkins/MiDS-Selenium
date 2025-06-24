```bash
import React, { useState,useEffect } from 'react';
import { FiCheck, FiPlay, FiSearch, FiSettings, FiBox } from 'react-icons/fi';
import { MdAutoMode, MdOutlineComputer } from 'react-icons/md';
import '../css/MidsTest.css';
import {FiMoon, FiSun} from 'react-icons/fi';
import axios from 'axios';
import api from '../api/axiosConfig'; 
const MidsTest = () => {
  const [selectedTests, setSelectedTests] = useState([]);
  const [showModeDialog, setShowModeDialog] = useState(false);
  const [showExecutionDialog, setShowExecutionDialog] = useState(false);
  const [executionMode, setExecutionMode] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [testData, setTestData] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [testExecutionTypes, setTestExecutionTypes] = useState({});

  const [state, setState] = useState({
    selectedTests: new Set(),
    darkMode: false,
    executionMode: null,
    showExecutionDialog: false,
    searchQuery: '',
    batchSelections: {},
    columnSort: { field: 'priority', order: 'desc' }
  });
  // useEffect(() => {
  //   fetchTestData();
  //   if (showExecutionDialog && executionMode === 'auto+manual') {
  //     const initialTypes = {};
  //     selectedTests.forEach(testId => {
  //       const test = Object.values(testData).flat().find(t => t.id === testId);
  //       initialTypes[testId] = test.executionType === 'Manual' ? 'Manual' : 'Automatic';
  //     });
  //     setTestExecutionTypes(initialTypes);
  //   }
  // }, [showExecutionDialog, executionMode, selectedTests, testData]);
   useEffect(() => {
      fetchTestData();
    }, []);
  

const fetchTestData = async () => {
  try {
    setLoading(true);
    const response = await api.get('/api/midstests'); // Use configured axios instance
    // const response = await axios.get('/api/midstests');
    // console.log("response "+response.data);
    const transformedData = transformBackendData(response.data);
    // console.log(transformedData);
    setTestData(transformedData);
    setLoading(false);
  } catch (err) {
    // handleApiError(err);
    setError('Failed to fetch test data');
        setLoading(false);
  }
};

// Handle execution type change
  const handleExecutionTypeChange = (testId, type) => {
    setTestExecutionTypes(prev => ({
      ...prev,
      [testId]: type
    }));
  };
// Data transformation function
const transformBackendData = (backendData) => {
  return backendData.reduce((acc, test) => {
    const category = test.pageName;
    if (!acc[category]) acc[category] = [];
    acc[category].push({
      id: test.id,
      name: test.testName,
      success: test.successfulCount || 0,
      failure: test.failedCount || 0,
      priority: test.priority.toLowerCase(),
      doneBy: test.doneBy || 'N/A',
      status: test.executionStatus,
      lastExecutionUser: test.lastExecutionUser || 'N/A',
      lastExecutionDate: test.lastExecutionDate || null,
      executionType: test.executionType 
    });
    return acc;
  }, {});
};

  if (loading) return <div className="loading-spinner">Loading...</div>;
  if (error) return <div className="error-message">{error}</div>;

  const toggleTestSelection = (testId) => {
    setSelectedTests(prev => 
      prev.includes(testId) 
        ? prev.filter(id => id !== testId) 
        : [...prev, testId]
    );
  };

  const handleExecute = (mode) => {
    setExecutionMode(mode);
    setShowModeDialog(false);
    setShowExecutionDialog(true);
  };

  const handlePassExecute = async (mode) => {
    try {
      const exType = executionMode === 'auto+manual' ? 'Automatic' : 'Manual';
      if(mode=='Fully Automatic') {
      // Group selected tests by `doneBy`
      const groupedTests = selectedTests.reduce((acc, testId) => {
        const test = Object.values(testData).flat().find((t) => t.id === testId);
        if (!acc[test.doneBy]) acc[test.doneBy] = [];
        acc[test.doneBy].push({ id: test.id, testName: test.name ,executionType:exType});
        return acc;
      }, {});
  
      console.log('Grouped Tests by doneBy:', groupedTests);
  
      const executionPromises = Object.entries(groupedTests).map(async ([doneBy, tests]) => {
        console.log(`Fetching credentials for doneBy: ${doneBy}`);
        const userData = await api.post('/api/sampleUserCredentials/getUserCredentials', { doneBy });
        console.log('User Data:', userData.data);
        console.log(localStorage.getItem('username'));
        const payload = {
          executionMode: mode,
          userName: userData.data[0],
          password: userData.data[1],
          tests, // List of tests for this `doneBy`
          testerName: localStorage.getItem('username') || 'Unknown',
        };
  
        console.log('Payload for doneBy:', doneBy, payload);
  
        // const response = await api.post('/api/midstests/executeTests', payload);
        // console.log(`Execution Response for doneBy ${doneBy}:`, response.data);
        // return response.data;
        return "data"; // Simulating response for now
      });
      const results = await Promise.all(executionPromises);
      console.log('All Execution Results:', results);
      }else if(mode=="Fully Manual"){
            //In this senerio test case need to be pased one by one and result shown to resul
      }else{
        // Hybrid mode - separate by selected execution type
        const automaticTests = [];
        const manualTests = [];
        selectedTests.forEach(testId => {
        const executionType = testExecutionTypes[testId];
        const test = Object.values(testData).flat().find(t => t.id === testId);
         if (executionType === 'Automatic') {
          automaticTests.push({
            id: test.id,
            testName: test.name,
            executionType: 'Automatic'
          });
        } else {
          manualTests.push({
            id: test.id,
            testName: test.name,
            executionType: 'Manual'
          });
        }
      });

      // Execute automatic tests in bulk
      if (automaticTests.length > 0) {
        const groupedAutoTests = automaticTests.reduce((acc, test) => {
          const testObj = Object.values(testData).flat().find(t => t.id === test.id);
          if (!acc[testObj.doneBy]) acc[testObj.doneBy] = [];
          acc[testObj.doneBy].push(test);
          return acc;
        }, {});
        
        const autoPromises = Object.entries(groupedAutoTests).map(async ([doneBy, tests]) => {
          const userData = await api.post('/api/sampleUserCredentials/getUserCredentials', { doneBy });
          const payload = {
            executionMode: 'Automatic',
            userName: userData.data[0],
            password: userData.data[1],
            tests,
            testerName: localStorage.getItem('username') || 'Unknown',
          };
          return "data";//api.post('/api/midstests/executeTests', payload);
        });

        await Promise.all(autoPromises);
      } 
      
      // Execute manual tests individually
      if (manualTests.length > 0) {
        // Start manual execution flow
        console.log("Starting manual execution for:", manualTests);
        // This would typically open a manual test runner UI
        // For now, we'll simulate execution
        manualTests.forEach(test => {
          console.log(`Manual execution started for: ${test.testName}`);
          // In a real app, you would open a test execution form
        });
      }
      
      
      
          }
    } catch (error) {
      console.error('Error during execution:', error);
    }
  };

  return (
    <div className={`mids-container ${state.darkMode ? 'dark' : 'light'}`}>
    
      {/* Header Section */}
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold gradient-text">MIDS Test Management</h1>
        <div className="flex items-center gap-4">
          <div className="glass-panel flex items-center px-4 py-2 rounded-lg">
            <FiSearch className="mr-2" />
            <input
              type="text"
              placeholder="Search tests..."
              className="bg-transparent outline-none"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>
          <div className="theme-toggle" onClick={() => setState(p => ({...p, darkMode: !p.darkMode}))}>
            {state.darkMode ? <FiSun /> : <FiMoon />}
          </div>
          <button className="glass-panel p-3 rounded-lg">
            <FiSettings />
          </button>
        </div>
      </div>

      {/* Test Categories Grid */}
      {Object.entries(testData).map(([category, tests]) => (
        <div key={category} className="glass-panel mb-8 p-6 rounded-xl">
          <div className="flex items-center gap-3 mb-4">
            <FiBox className="text-xl" />
            <h2 className="text-xl font-semibold">{category}</h2>
          </div>
          
          <table className="w-full">
            <thead>
              <tr className="text-left border-b border-opacity-20">
                <th className="pb-3 w-8"><FiCheck /></th>
                <th className="pb-3">Test Name</th>
                <th className="pb-3">Success Rate</th>
                <th className="pb-3">Failure Rate</th>
                <th className="pb-3">Priority</th>
                <th className="pb-3">Last Execution User</th>
                <th className="pb-3">Last Execution Date</th>
                <th className="pb-3">Execution Status</th>
                <th className="pb-3">Execution Type</th>
              </tr>
            </thead>
            <tbody>
              {tests.map(test => {
              const total = test.success + test.failure;
              const successRate = total > 0 ? ((test.success / total) * 100).toFixed(2) : "0.00";
              const failureRate = total > 0 ? ((test.failure / total) * 100).toFixed(2) : "0.00";
              return (
            <tr 
              key={test.id} 
              className="hover:bg-opacity-10 transition-colors cursor-pointer"
              onClick={() => toggleTestSelection(test.id)}
            >
              <td className="py-3">
                <input 
              type="checkbox"
              checked={selectedTests.includes(test.id)}
              onChange={() => {}}
              className="glass-checkbox"
                />
              </td>
              <td className="py-3">{test.name}</td>
              <td className="py-3 text-green-400">{successRate}%</td>
              <td className="py-3 text-red-400">{failureRate}%</td>
              <td className="py-3">
                <span className={`priority-tag ${test.priority.toLowerCase()}`}>
              {test.priority}
                </span>
              </td>
              <td className="py-3">{test.lastExecutionUser}</td>
              <td className="py-3">
                {test.lastExecutionDate || 'N/A'}
              </td>
              <td className="py-3">{test.status }</td>
              <td className="py-3">
                {test.executionType }
              </td>
            </tr>
              );
            })}
            </tbody>
          </table>
            {/* Floating Action Button */}
      {selectedTests.length > 0 && (
        <button 
          className="floating-execute-btn"
          onClick={() => setShowModeDialog(true)}
        >
          <FiPlay />
          Execute Selected ({selectedTests.length})
        </button>
      )}
        </div>
      ))}

      {/* Execution Mode Dialog */}
      {showModeDialog && (
        <div className="dialog-overlay">
          <div className="glass-panel p-8 rounded-xl max-w-md">
            <h3 className="text-xl font-bold mb-6">Select Execution Mode</h3>
            <div className="space-y-4">
              <button 
                className="dialog-option"
                onClick={() => handleExecute('automatic')}
              >
                <MdAutoMode className="mr-2" />
                Fully Automatic
              </button>
              <button 
                className="dialog-option"
                onClick={() => handleExecute('auto+manual')}
              >
                <MdOutlineComputer className="mr-2" />
                Automatic + Manual
              </button>
              <button 
                className="dialog-option"
                onClick={() => handleExecute('manual')}
              >
                <FiPlay className="mr-2" />
                Manual Execution
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Confirm Dialog */}
      {showExecutionDialog && (
        <div className="dialog-overlay">
          <div className="glass-panel p-8 rounded-xl max-w-3xl">
            <h3 className="text-xl font-bold mb-6">Confirm Execution</h3>
            <table className="w-full mb-6">
              <thead>
                <tr className="text-left border-b border-opacity-20">
                  <th className="pb-3">Test Name</th>
                  {executionMode === 'auto+manual' && <th className="pb-3">Execution Type</th>}
                  <th className="pb-3">Priority</th>
                </tr>
              </thead>
              <tbody>
                {selectedTests.map(testId => {
                  const test = Object.values(testData)
                    .flat()
                    .find(t => t.id === testId);
                  return (
                    <tr key={testId}>
                      <td className="py-3">{test.name}</td>
                      {executionMode === 'auto+manual' && (
                        <td className="py-3">
                          <select 
                          className="glass-select"
                          value={testExecutionTypes[testId] || 'Automatic'}
                          onChange={(e) => handleExecutionTypeChange(testId, e.target.value)}
                          disabled={test.executionType !== 'Both'}
                        >
                          <option 
                            value="Automatic" 
                            disabled={test.executionType === 'Manual'}
                          >
                            Automatic
                          </option>
                          <option 
                            value="Manual" 
                            disabled={test.executionType === 'Automatic'}
                          >
                            Manual
                          </option>
                        </select>
                        {/* Tooltip for disabled options */}
                        {test.executionType !== 'Both' && (
                          <span className="text-xs text-gray-500 ml-2">
                            ({test.executionType} only)
                          </span>
                        )}
                        </td>
                      )}
                      <td className="py-3">
                        <span className={`priority-tag ${test.priority.toLowerCase()}`}>
                          {test.priority}
                        </span>
                      </td>
                    </tr>
                  )}
                )}
              </tbody>
            </table>
            <button className="execute-btn" onClick={() => handlePassExecute(executionMode)}>
              <FiPlay className="mr-2" />
              Start Execution
            </button>
          </div>
        </div>
      )}

    
    </div>
  );
};

export default MidsTest;
```