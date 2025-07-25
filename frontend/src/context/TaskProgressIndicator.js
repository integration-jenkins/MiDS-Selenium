import React, { useContext } from 'react';
import { FiX } from 'react-icons/fi';
import { TaskContext } from './TaskContext';

const TaskProgressIndicator = () => {
  const { tasks, cancelTask } = useContext(TaskContext);

  if (tasks.length === 0) return null;

  return (
    <div className="fixed bottom-4 right-4 w-80 max-h-96 overflow-y-auto z-50">
      {tasks.map((task) => (
        <div
          key={task.id}
          className="task-item bg-white dark:bg-gray-800 rounded-lg shadow-lg p-4 mb-2 flex items-center justify-between animate-slide-in"
        >
          <div className="flex items-center space-x-3">
            <div className="task-spinner"></div>
            <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
              Running {task.name}...
            </span>
          </div>
          <button
            onClick={() => cancelTask(task.id)}
            className="text-gray-500 dark:text-gray-300 hover:text-red-500 dark:hover:text-red-400"
            aria-label={`Cancel ${task.name}`}
          >
            <FiX size={20} />
          </button>
        </div>
      ))}
    </div>
  );
};

export default TaskProgressIndicator;