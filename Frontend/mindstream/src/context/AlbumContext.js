import React, { createContext, useState, useContext, useCallback } from 'react';
import { albumService } from '../services/albumService';

const AlbumContext = createContext({});

export const useAlbums = () => useContext(AlbumContext);

export const AlbumProvider = ({ children }) => {
  const [albums, setAlbums] = useState([]);
  const [currentAlbum, setCurrentAlbum] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Fetch all albums
  const fetchAlbums = useCallback(async (userId = null) => {
    try {
      setLoading(true);
      setError(null);
      const response = await albumService.getAllAlbums(userId);
      if (response.data) {
        setAlbums(response.data);
      }
      return response.data;
    } catch (err) {
      setError(err.message || 'Failed to fetch albums');
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  // Fetch single album by ID
  const fetchAlbumById = useCallback(async (id, userId = null) => {
    try {
      setLoading(true);
      setError(null);
      const response = await albumService.getAlbumById(id, userId);
      if (response.data) {
        setCurrentAlbum(response.data);
      }
      return response.data;
    } catch (err) {
      setError(err.message || 'Album not found');
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  // Search albums
  const searchAlbums = useCallback(async (query, userId = null) => {
    try {
      setLoading(true);
      setError(null);
      const response = await albumService.searchAlbums(query, userId);
      if (response.data) {
        setAlbums(response.data);
      }
      return response.data;
    } catch (err) {
      setError(err.message || 'Search failed');
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  // Get albums by year
  const getAlbumsByYear = useCallback(async (year, userId = null) => {
    try {
      setLoading(true);
      setError(null);
      const response = await albumService.getAlbumsByYear(year, userId);
      if (response.data) {
        setAlbums(response.data);
      }
      return response.data;
    } catch (err) {
      setError(err.message || 'Failed to filter by year');
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  // Clear current album
  const clearCurrentAlbum = useCallback(() => {
    setCurrentAlbum(null);
  }, []);

  // Clear error
  const clearError = useCallback(() => {
    setError(null);
  }, []);

  const value = {
    albums,
    currentAlbum,
    loading,
    error,
    fetchAlbums,
    fetchAlbumById,
    searchAlbums,
    getAlbumsByYear,
    clearCurrentAlbum,
    clearError
  };

  return (
    <AlbumContext.Provider value={value}>
      {children}
    </AlbumContext.Provider>
  );
};

export default AlbumContext;
