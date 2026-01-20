import React, { useState } from 'react';
import { Search, Clear } from '@mui/icons-material';
import './Common.css';

const SearchFilter = ({ 
  placeholder = 'Search...',
  value = '',
  onChange,
  onClear,
  className = ''
}) => {
  const [internalValue, setInternalValue] = useState(value);

  const handleChange = (e) => {
    const newValue = e.target.value;
    setInternalValue(newValue);
    if (onChange) {
      onChange(newValue);
    }
  };

  const handleClear = () => {
    setInternalValue('');
    if (onClear) {
      onClear();
    }
    if (onChange) {
      onChange('');
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();
    }
  };

  return (
    <div className={`search-filter-container ${className}`}>
      <div className="search-filter-input-group">
        <Search className="search-filter-icon" />
        <input
          type="text"
          className="search-filter-input"
          placeholder={placeholder}
          value={internalValue}
          onChange={handleChange}
          onKeyPress={handleKeyPress}
        />
        {internalValue && (
          <button className="search-filter-clear" onClick={handleClear}>
            <Clear />
          </button>
        )}
      </div>
    </div>
  );
};

export default SearchFilter;