// Format date to readable string
export const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

// Format chart position
export const formatChartPosition = (position) => {
  if (position === '-' || position === null || position === undefined) {
    return 'Not Charted';
  }
  return `#${position}`;
};

// Get color based on chart position
export const getChartColor = (position) => {
  if (position === '-' || position === null || position === undefined) {
    return '#757575';
  }
  const numPosition = parseInt(position);
  if (numPosition <= 10) return '#4caf50';
  if (numPosition <= 50) return '#ff9800';
  return '#f44336';
};

// Get album type based on year
export const getAlbumType = (year) => {
  if (year < 2002) return 'Early Years';
  if (year < 2007) return 'Peak Years';
  return 'Later Years';
};

// Truncate text with ellipsis
export const truncateText = (text, maxLength) => {
  if (!text) return '';
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + '...';
};

// Debounce function
export const debounce = (func, delay) => {
  let timeoutId;
  return (...args) => {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => func.apply(null, args), delay);
  };
};

// Format number with commas
export const formatNumber = (num) => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

// Capitalize first letter
export const capitalize = (str) => {
  if (!str) return '';
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
};

// Generate random color
export const getRandomColor = () => {
  const colors = [
    '#e53935', '#d81b60', '#8e24aa', '#5e35b1', '#3949ab',
    '#1e88e5', '#039be5', '#00acc1', '#00897b', '#43a047',
    '#7cb342', '#c0ca33', '#fdd835', '#ffb300', '#fb8c00'
  ];
  return colors[Math.floor(Math.random() * colors.length)];
};

// Sort albums by various criteria
export const sortAlbums = (albums, sortBy = 'year') => {
  const sorted = [...albums];
  
  switch (sortBy) {
    case 'year':
      return sorted.sort((a, b) => b.year - a.year);
    case 'year-asc':
      return sorted.sort((a, b) => a.year - b.year);
    case 'title':
      return sorted.sort((a, b) => a.album.localeCompare(b.album));
    case 'chart':
      return sorted.sort((a, b) => {
        const aPos = a.chartPosition === '-' ? 999 : parseInt(a.chartPosition);
        const bPos = b.chartPosition === '-' ? 999 : parseInt(b.chartPosition);
        return aPos - bPos;
      });
    default:
      return sorted;
  }
};

// Filter albums by year range
export const filterByYearRange = (albums, startYear, endYear) => {
  return albums.filter(album => 
    album.year >= startYear && album.year <= endYear
  );
};

// Search albums by query
export const searchAlbums = (albums, query) => {
  if (!query.trim()) return albums;
  
  const searchTerm = query.toLowerCase();
  return albums.filter(album => 
    album.album.toLowerCase().includes(searchTerm)
  );
};

// Calculate album statistics
export const calculateAlbumStats = (albums) => {
  if (!albums || albums.length === 0) {
    return {
      total: 0,
      earliestYear: 0,
      latestYear: 0,
      averageYear: 0,
      chartedCount: 0
    };
  }

  const years = albums.map(a => a.year);
  const chartedAlbums = albums.filter(a => a.chartPosition !== '-');
  
  return {
    total: albums.length,
    earliestYear: Math.min(...years),
    latestYear: Math.max(...years),
    averageYear: Math.round(years.reduce((a, b) => a + b, 0) / years.length),
    chartedCount: chartedAlbums.length,
    chartedPercentage: Math.round((chartedAlbums.length / albums.length) * 100)
  };
};

// Group albums by year
export const groupAlbumsByYear = (albums) => {
  const groups = {};
  albums.forEach(album => {
    if (!groups[album.year]) {
      groups[album.year] = [];
    }
    groups[album.year].push(album);
  });
  
  return Object.entries(groups)
    .map(([year, albums]) => ({ year: parseInt(year), albums, count: albums.length }))
    .sort((a, b) => b.year - a.year);
};

// Validate email
export const isValidEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

// Validate password strength
export const validatePassword = (password) => {
  const errors = [];
  
  if (password.length < 6) {
    errors.push('Password must be at least 6 characters');
  }
  if (!/[A-Z]/.test(password)) {
    errors.push('Password must contain at least one uppercase letter');
  }
  if (!/[0-9]/.test(password)) {
    errors.push('Password must contain at least one number');
  }
  
  return {
    isValid: errors.length === 0,
    errors
  };
};

// Get initials from name
export const getInitials = (name) => {
  if (!name) return '';
  return name
    .split(' ')
    .map(word => word[0])
    .join('')
    .toUpperCase()
    .slice(0, 2);
};

// Create slug from text
export const createSlug = (text) => {
  return text
    .toLowerCase()
    .replace(/[^\w\s-]/g, '')
    .replace(/[\s_-]+/g, '-')
    .replace(/^-+|-+$/g, '');
};

// Parse query parameters from URL
export const parseQueryParams = (search) => {
  const params = new URLSearchParams(search);
  const result = {};
  
  for (const [key, value] of params.entries()) {
    result[key] = value;
  }
  
  return result;
};

// Create query string from object
export const createQueryString = (params) => {
  const searchParams = new URLSearchParams();
  
  Object.entries(params).forEach(([key, value]) => {
    if (value !== null && value !== undefined && value !== '') {
      searchParams.append(key, value);
    }
  });
  
  return searchParams.toString();
};