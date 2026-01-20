import api from './api';

export const albumService = {
  // Get all albums
  getAllAlbums: async (userId = null) => {
    try {
      const url = userId ? `/tvshows?userId=${userId}` : '/tvshows';
      const response = await api.get(url);
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Failed to fetch albums' };
    }
  },

  // Get album by ID
  getAlbumById: async (id, userId = null) => {
    try {
      const url = userId ? `/tvshows/${id}?userId=${userId}` : `/tvshows/${id}`;
      const response = await api.get(url);
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Album not found' };
    }
  },

  // Search albums
  searchAlbums: async (query, userId = null) => {
    try {
      const url = userId ? `/tvshows/search?query=${query}&userId=${userId}` : `/tvshows/search?query=${query}`;
      const response = await api.get(url);
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Search failed' };
    }
  },

  // Get albums by year
  getAlbumsByYear: async (year, userId = null) => {
    try {
      const url = userId ? `/tvshows/year/${year}?userId=${userId}` : `/tvshows/year/${year}`;
      const response = await api.get(url);
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Failed to filter by year' };
    }
  },

  // Load initial data
  loadInitialData: async () => {
    try {
      const response = await api.post('/tvshows/load-initial-data');
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Failed to load initial data' };
    }
  },

  // Add new album (admin)
  addAlbum: async (albumData) => {
    try {
      const response = await api.post('/tvshows', albumData);
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Failed to add album' };
    }
  }
};