import React from 'react';
import { ErrorOutline, Close } from '@mui/icons-material';
import './Common.css';

const ErrorMessage = ({ 
  type = 'primary',
  title,
  message,
  onClose,
  className = ''
}) => {
  const typeClass = `error-message-${type}`;
  
  return (
    <div className={`error-message ${typeClass} ${className}`}>
      <ErrorOutline className="error-message-icon" />
      <div className="error-message-content">
        {title && <div className="error-message-title">{title}</div>}
        <div className="error-message-description">{message}</div>
      </div>
      {onClose && (
        <button className="error-message-close" onClick={onClose}>
          <Close />
        </button>
      )}
    </div>
  );
};

export default ErrorMessage;