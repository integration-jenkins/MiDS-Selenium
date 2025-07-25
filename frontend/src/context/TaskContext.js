import React, { createContext, useState, useCallback } from 'react';
import { v4 as uuidv4 } from 'uuid';

export const TaskContext = createContext();

export const TaskProvider = ({ children }) => {
  const [tasks, setTasks] = useState([]);

  const addTask = useCallback((taskName, cancelTokenSource) => {
    setTasks((prev) => [
      ...prev,
      { id: uuidv4(), name: taskName, cancelTokenSource },
    ]);
  }, []);

  const removeTask = useCallback((taskId) => {
    setTasks((prev) => prev.filter((task) => task.id !== taskId));
  }, []);

  const cancelTask = useCallback((taskId) => {
    setTasks((prev) => {
      const task = prev.find((t) => t.id === taskId);
      if (task && task.cancelTokenSource) {
        task.cancelTokenSource.cancel('Task cancelled by user');
      }
      return prev.filter((t) => t.id !== taskId);
    });
  }, []);

  return (
    <TaskContext.Provider value={{ tasks, addTask, removeTask, cancelTask }}>
      {children}
    </TaskContext.Provider>
  );
};