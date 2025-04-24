import React, { useState } from 'react';
import { FiCheck, FiPlay, FiSearch, FiSettings, FiBox } from 'react-icons/fi';
import { MdAutoMode, MdOutlineComputer } from 'react-icons/md';
import '../css/MidsTest.css';
import {FiMoon, FiSun} from 'react-icons/fi';

const MidsTest = () => {
  const [selectedTests, setSelectedTests] = useState([]);
  const [showModeDialog, setShowModeDialog] = useState(false);
  const [showExecutionDialog, setShowExecutionDialog] = useState(false);
  const [executionMode, setExecutionMode] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [state, setState] = useState({
    selectedTests: new Set(),
    darkMode: false,
    executionMode: null,
    showExecutionDialog: false,
    searchQuery: '',
    batchSelections: {},
    columnSort: { field: 'priority', order: 'desc' }
  });

  // Sample test data
  const testCategories = {
    'MW Plan Tracking': [
      { id: 1, name: 'PHY AT Manual Rejection', success: 98, failure: 2, priority: 'High' },
      { id: 2, name: 'AT Status Report Sync', success: 95, failure: 5, priority: 'Medium' },
      { id: 3, name: 'Bulk AT Status Upload (Reject)', success: 92, failure: 8, priority: 'Critical' },
    ],
    'LB Report': [
      { id: 4, name: 'Load Balancing Validation', success: 99, failure: 1, priority: 'High' },
      { id: 5, name: 'Traffic Distribution Test', success: 97, failure: 3, priority: 'Medium' },
    ],
  };
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
      {Object.entries(testCategories).map(([category, tests]) => (
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
              </tr>
            </thead>
            <tbody>
              {tests.map(test => (
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
                  <td className="py-3 text-green-400">{test.success}%</td>
                  <td className="py-3 text-red-400">{test.failure}%</td>
                  <td className="py-3">
                    <span className={`priority-tag ${test.priority.toLowerCase()}`}>
                      {test.priority}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
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

      {/* Execution Dialog */}
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
                  <th className="pb-3">Page Name</th>
                </tr>
              </thead>
              <tbody>
                {selectedTests.map(testId => {
                  const test = Object.values(testCategories)
                    .flat()
                    .find(t => t.id === testId);
                  return (
                    <tr key={testId}>
                      <td className="py-3">{test.name}</td>
                      {executionMode === 'auto+manual' && (
                        <td className="py-3">
                          <select className="glass-select">
                            <option>Automatic</option>
                            <option>Manual</option>
                          </select>
                        </td>
                      )}
                      <td className="py-3">
                        <span className={`priority-tag ${test.priority.toLowerCase()}`}>
                          {test.priority}
                        </span>
                      </td>
                      <td className="py-3 opacity-70">{test.category}</td>
                    </tr>
                  )}
                )}
              </tbody>
            </table>
            <button className="execute-btn">
              <FiPlay className="mr-2" />
              Start Execution
            </button>
          </div>
        </div>
      )}

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
  );
};

export default MidsTest;