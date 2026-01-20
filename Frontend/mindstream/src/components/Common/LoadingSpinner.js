import React from 'react';
import './Common.css';

const LoadingSpinner = ({ size = 'medium', color = 'primary', variant = 'circle', className = '' }) => {
  const sizeClass = `loading-spinner-${size}`;
  const colorClass = `loading-spinner-${color}`;
  
  if (variant === 'dots') {
    return (
      <div className={`loading-spinner loading-spinner-dots ${sizeClass} ${colorClass} ${className}`}>
        <div></div>
        <div></div>
        <div></div>
      </div>
    );
  }
  
  return (
    <div className={`loading-spinner ${sizeClass} ${colorClass} ${className}`}>
      <svg
        className="loading-spinner-circle"
        viewBox="0 0 50 50"
        style={{ width: '100%', height: '100%' }}
      >
        <circle
          cx="25"
          cy="25"
          r="20"
          fill="none"
          stroke="currentColor"
          strokeWidth="4"
          strokeLinecap="round"
        />
      </svg>
    </div>
  );
};

export default LoadingSpinner;