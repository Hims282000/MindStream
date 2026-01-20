// API Configuration
export const API_CONFIG = {
  BASE_URL: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
  TIMEOUT: 30000,
  RETRY_ATTEMPTS: 3,
  RETRY_DELAY: 1000,
};

// API Endpoints
export const API_ENDPOINTS = {
  // Auth Endpoints
  AUTH: {
    REGISTER: '/auth/register',
    LOGIN: '/auth/login',
    LOGOUT: '/auth/logout',
    REFRESH_TOKEN: '/auth/refresh',
    FORGOT_PASSWORD: '/auth/forgot-password',
    RESET_PASSWORD: '/auth/reset-password',
  },
  
  // Album Endpoints
  ALBUMS: {
    GET_ALL: '/tvshows',
    GET_BY_ID: '/tvshows/:id',
    SEARCH: '/tvshows/search',
    BY_YEAR: '/tvshows/year/:year',
    ADD: '/tvshows',
    UPDATE: '/tvshows/:id',
    DELETE: '/tvshows/:id',
    LOAD_INITIAL_DATA: '/tvshows/load-initial-data',
  },
  
  // Favorite Endpoints
  FAVORITES: {
    GET_USER_FAVORITES: '/favorites/user/:userId',
    ADD: '/favorites/add',
    REMOVE: '/favorites/remove',
    CHECK: '/favorites/check',
    COUNT: '/favorites/count/:userId',
  },
  
  // User Endpoints
  USERS: {
    GET_PROFILE: '/users/:userId',
    UPDATE_PROFILE: '/users/:userId',
    CHANGE_PASSWORD: '/users/:userId/change-password',
    DELETE_ACCOUNT: '/users/:userId',
  },
};

// Application Constants
export const APP_CONSTANTS = {
  APP_NAME: 'White Stripes Album Collection',
  APP_DESCRIPTION: 'Track and collect your favorite White Stripes albums',
  APP_VERSION: '1.0.0',
  
  // Pagination
  ITEMS_PER_PAGE: 12,
  MAX_PAGE_BUTTONS: 5,
  
  // Search & Filter
  SEARCH_DEBOUNCE: 300,
  MIN_SEARCH_LENGTH: 2,
  
  // Years
  MIN_YEAR: 1997,
  MAX_YEAR: new Date().getFullYear(),
  
  // Chart Positions
  MAX_CHART_POSITION: 200,
};

// Validation Constants
export const VALIDATION_CONSTANTS = {
  USERNAME: {
    MIN_LENGTH: 3,
    MAX_LENGTH: 30,
    PATTERN: /^[a-zA-Z0-9_.-]+$/,
  },
  PASSWORD: {
    MIN_LENGTH: 6,
    MAX_LENGTH: 100,
  },
  EMAIL: {
    PATTERN: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
  },
  FULL_NAME: {
    MIN_LENGTH: 2,
    MAX_LENGTH: 100,
  },
  ALBUM_TITLE: {
    MIN_LENGTH: 1,
    MAX_LENGTH: 200,
  },
};

// Storage Keys
export const STORAGE_KEYS = {
  TOKEN: 'token',
  USER: 'user',
  THEME: 'theme',
  LANGUAGE: 'language',
  FAVORITES: 'favorites',
  RECENT_SEARCHES: 'recent_searches',
  SETTINGS: 'app_settings',
};

// Theme Constants
export const THEME_CONSTANTS = {
  THEMES: {
    LIGHT: 'light',
    DARK: 'dark',
    SYSTEM: 'system',
  },
  COLORS: {
    PRIMARY: '#e53935',
    SECONDARY: '#212121',
    SUCCESS: '#4caf50',
    WARNING: '#ff9800',
    ERROR: '#f44336',
    INFO: '#2196f3',
    FAVORITE: '#ff4081',
  },
};

// Navigation Constants
export const NAVIGATION_CONSTANTS = {
  ROUTES: {
    HOME: '/',
    ALBUMS: '/albums',
    ALBUM_DETAIL: '/albums/:id',
    FAVORITES: '/favorites',
    DASHBOARD: '/dashboard',
    LOGIN: '/login',
    REGISTER: '/register',
    NOT_FOUND: '*',
  },
  MENU_ITEMS: [
    { path: '/', label: 'Home', icon: 'home' },
    { path: '/albums', label: 'Albums', icon: 'album' },
    { path: '/favorites', label: 'Favorites', icon: 'favorite' },
    { path: '/dashboard', label: 'Dashboard', icon: 'dashboard' },
  ],
};

// Album Types
export const ALBUM_TYPES = {
  STUDIO: 'studio',
  LIVE: 'live',
  COMPILATION: 'compilation',
  EP: 'ep',
  SINGLE: 'single',
  DEMO: 'demo',
  BOX_SET: 'box_set',
};

// Chart Position Categories
export const CHART_CATEGORIES = {
  TOP_10: 'top_10',
  TOP_50: 'top_50',
  TOP_100: 'top_100',
  NOT_CHARTED: 'not_charted',
};

// Error Messages
export const ERROR_MESSAGES = {
  // Network Errors
  NETWORK_ERROR: 'Network error. Please check your connection.',
  TIMEOUT_ERROR: 'Request timed out. Please try again.',
  SERVER_ERROR: 'Server error. Please try again later.',
  
  // Auth Errors
  INVALID_CREDENTIALS: 'Invalid username or password.',
  USER_EXISTS: 'Username or email already exists.',
  TOKEN_EXPIRED: 'Session expired. Please login again.',
  UNAUTHORIZED: 'You are not authorized to perform this action.',
  
  // Album Errors
  ALBUM_NOT_FOUND: 'Album not found.',
  ALBUM_EXISTS: 'Album already exists.',
  INVALID_ALBUM_DATA: 'Invalid album data.',
  
  // Favorite Errors
  FAVORITE_EXISTS: 'Album is already in favorites.',
  FAVORITE_NOT_FOUND: 'Favorite not found.',
  
  // General Errors
  VALIDATION_ERROR: 'Please check your input and try again.',
  UNKNOWN_ERROR: 'An unexpected error occurred. Please try again.',
};

// Success Messages
export const SUCCESS_MESSAGES = {
  // Auth Success
  REGISTRATION_SUCCESS: 'Registration successful! You can now login.',
  LOGIN_SUCCESS: 'Login successful!',
  LOGOUT_SUCCESS: 'Logged out successfully.',
  PASSWORD_RESET: 'Password reset successfully.',
  
  // Album Success
  ALBUM_ADDED: 'Album added successfully.',
  ALBUM_UPDATED: 'Album updated successfully.',
  ALBUM_DELETED: 'Album deleted successfully.',
  
  // Favorite Success
  FAVORITE_ADDED: 'Added to favorites.',
  FAVORITE_REMOVED: 'Removed from favorites.',
  
  // Profile Success
  PROFILE_UPDATED: 'Profile updated successfully.',
  PASSWORD_CHANGED: 'Password changed successfully.',
};

// Date & Time Formats
export const DATE_FORMATS = {
  DISPLAY_DATE: 'MMM DD, YYYY',
  DISPLAY_DATE_TIME: 'MMM DD, YYYY hh:mm A',
  API_DATE: 'YYYY-MM-DD',
  API_DATE_TIME: 'YYYY-MM-DDTHH:mm:ssZ',
  RELATIVE_TIME: 'relative',
};

// Localization
export const LOCALIZATION = {
  DEFAULT_LANGUAGE: 'en',
  SUPPORTED_LANGUAGES: ['en'],
  LANGUAGE_NAMES: {
    en: 'English',
  },
};

// Feature Flags
export const FEATURE_FLAGS = {
  ENABLE_REGISTRATION: true,
  ENABLE_SOCIAL_LOGIN: false,
  ENABLE_DARK_MODE: true,
  ENABLE_OFFLINE_MODE: false,
  ENABLE_EXPORT_DATA: true,
  ENABLE_SHARING: true,
};

// Performance Constants
export const PERFORMANCE_CONSTANTS = {
  IMAGE_LAZY_LOAD_THRESHOLD: 0.5,
  DEBOUNCE_DELAY: 300,
  THROTTLE_DELAY: 100,
  CACHE_DURATION: 5 * 60 * 1000, // 5 minutes
};

// Export all constants
export default {
  API_CONFIG,
  API_ENDPOINTS,
  APP_CONSTANTS,
  VALIDATION_CONSTANTS,
  STORAGE_KEYS,
  THEME_CONSTANTS,
  NAVIGATION_CONSTANTS,
  ALBUM_TYPES,
  CHART_CATEGORIES,
  ERROR_MESSAGES,
  SUCCESS_MESSAGES,
  DATE_FORMATS,
  LOCALIZATION,
  FEATURE_FLAGS,
  PERFORMANCE_CONSTANTS,
};  