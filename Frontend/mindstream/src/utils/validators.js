// Validate user registration data
export const validateRegistration = (data) => {
  const errors = {};

  if (!data.username || data.username.trim().length < 3) {
    errors.username = 'Username must be at least 3 characters';
  }

  if (!data.email || !isValidEmail(data.email)) {
    errors.email = 'Please enter a valid email address';
  }

  if (!data.password || data.password.length < 6) {
    errors.password = 'Password must be at least 6 characters';
  }

  if (data.password !== data.confirmPassword) {
    errors.confirmPassword = 'Passwords do not match';
  }

  if (!data.fullName || data.fullName.trim().length < 2) {
    errors.fullName = 'Please enter your full name';
  }

  return {
    isValid: Object.keys(errors).length === 0,
    errors
  };
};

// Validate user login data
export const validateLogin = (data) => {
  const errors = {};

  if (!data.username || data.username.trim().length === 0) {
    errors.username = 'Username is required';
  }

  if (!data.password || data.password.length === 0) {
    errors.password = 'Password is required';
  }

  return {
    isValid: Object.keys(errors).length === 0,
    errors
  };
};

// Validate album data
export const validateAlbum = (data) => {
  const errors = {};

  if (!data.album || data.album.trim().length === 0) {
    errors.album = 'Album title is required';
  }

  if (!data.year || isNaN(data.year) || data.year < 1900 || data.year > new Date().getFullYear()) {
    errors.year = 'Please enter a valid year';
  }

  if (data.chartPosition && data.chartPosition !== '-') {
    const chartNum = parseInt(data.chartPosition);
    if (isNaN(chartNum) || chartNum < 1 || chartNum > 200) {
      errors.chartPosition = 'Chart position must be between 1 and 200';
    }
  }

  return {
    isValid: Object.keys(errors).length === 0,
    errors
  };
};

// Validate search query
export const validateSearchQuery = (query) => {
  if (!query || query.trim().length === 0) {
    return {
      isValid: false,
      error: 'Search query cannot be empty'
    };
  }

  if (query.trim().length < 2) {
    return {
      isValid: false,
      error: 'Search query must be at least 2 characters'
    };
  }

  return {
    isValid: true,
    error: null
  };
};

// Validate year filter
export const validateYearFilter = (year) => {
  if (!year) return { isValid: true };

  const yearNum = parseInt(year);
  const currentYear = new Date().getFullYear();

  if (isNaN(yearNum) || yearNum < 1900 || yearNum > currentYear) {
    return {
      isValid: false,
      error: `Year must be between 1900 and ${currentYear}`
    };
  }

  return { isValid: true };
};

// Validate chart position
export const validateChartPosition = (position) => {
  if (position === '-' || position === '') return { isValid: true };

  const posNum = parseInt(position);
  if (isNaN(posNum) || posNum < 1 || posNum > 200) {
    return {
      isValid: false,
      error: 'Chart position must be between 1 and 200 or "-" for not charted'
    };
  }

  return { isValid: true };
};

// Validate email format
export const isValidEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

// Validate password strength
export const validatePasswordStrength = (password) => {
  const requirements = {
    length: password.length >= 6,
    uppercase: /[A-Z]/.test(password),
    lowercase: /[a-z]/.test(password),
    number: /[0-9]/.test(password),
    special: /[!@#$%^&*(),.?":{}|<>]/.test(password)
  };

  const strength = Object.values(requirements).filter(Boolean).length;
  let message = '';

  if (strength <= 2) message = 'Weak';
  else if (strength <= 4) message = 'Medium';
  else message = 'Strong';

  return {
    strength,
    message,
    requirements
  };
};

// Validate username
export const validateUsername = (username) => {
  if (!username || username.trim().length < 3) {
    return {
      isValid: false,
      error: 'Username must be at least 3 characters'
    };
  }

  if (!/^[a-zA-Z0-9_.-]+$/.test(username)) {
    return {
      isValid: false,
      error: 'Username can only contain letters, numbers, dots, hyphens, and underscores'
    };
  }

  return { isValid: true };
};

// Validate full name
export const validateFullName = (name) => {
  if (!name || name.trim().length < 2) {
    return {
      isValid: false,
      error: 'Full name must be at least 2 characters'
    };
  }

  if (!/^[a-zA-Z\s]+$/.test(name)) {
    return {
      isValid: false,
      error: 'Full name can only contain letters and spaces'
    };
  }

  return { isValid: true };
};

// Validate numeric range
export const validateNumericRange = (value, min, max) => {
  const num = parseInt(value);
  
  if (isNaN(num)) {
    return {
      isValid: false,
      error: 'Please enter a valid number'
    };
  }

  if (num < min || num > max) {
    return {
      isValid: false,
      error: `Value must be between ${min} and ${max}`
    };
  }

  return { isValid: true };
};

// Validate required field
export const validateRequired = (value, fieldName) => {
  if (!value || value.toString().trim().length === 0) {
    return {
      isValid: false,
      error: `${fieldName} is required`
    };
  }

  return { isValid: true };
};

// Validate array not empty
export const validateArrayNotEmpty = (array, fieldName) => {
  if (!Array.isArray(array) || array.length === 0) {
    return {
      isValid: false,
      error: `${fieldName} cannot be empty`
    };
  }

  return { isValid: true };
};