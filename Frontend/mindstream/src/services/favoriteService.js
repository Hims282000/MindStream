import api from './api';

export const favoriteService = {
  // Get user's favorites
  getUserFavorites: async (userId) => {
    try {
      const response = await api.get(`/favorites/user/${userId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Failed to fetch favorites' };
    }
  },

  // Add to favorites
  addToFavorites: async (userId, albumId) => {
    try {
      const response = await api.post('/favorites/add', { userId, tvShowId: albumId });
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Failed to add to favorites' };
    }
  },

  // Remove from favorites
  removeFromFavorites: async (userId, albumId) => {
    try {
      const response = await api.delete('/favorites/remove', {
        data: { userId, tvShowId: albumId }
      });
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Failed to remove from favorites' };
    }
  },

  // Check if album is favorited
  checkFavorite: async (userId, albumId) => {
    try {
      const response = await api.get(`/favorites/check?userId=${userId}&tvShowId=${albumId}`);
      return response.data;
    } catch (error) {
      throw error.response?.data || { message: 'Failed to check favorite status' };
    }
  }
};