import React from 'react';
import AlbumCard from './AlbumCard';
import LoadingSpinner from '../Common/LoadingSpinner';
import ErrorMessage from '../Common/ErrorMessage';
import './Album.css';

const AlbumList = ({ 
  albums,
  loading = false,
  error = null,
  favorites = {},
  onToggleFavorite,
  viewMode = 'grid',
  compact = false,
  className = ''
}) => {
  if (loading) {
    return (
      <div className="loading-container">
        <LoadingSpinner size="large" />
        <p>Loading albums...</p>
      </div>
    );
  }

  if (error) {
    return (
      <ErrorMessage 
        title="Failed to load albums"
        message={error}
        type="error"
      />
    );
  }

  if (!albums || albums.length === 0) {
    return (
      <div className="empty-state">
        <div className="empty-state-icon">🎵</div>
        <h3 className="empty-state-title">No albums found</h3>
        <p className="empty-state-description">
          Try adjusting your search or filters to find what you're looking for.
        </p>
      </div>
    );
  }

  if (viewMode === 'list') {
    return (
      <div className={`album-list ${className}`}>
        {albums.map((album) => (
          <div key={album.id} className="album-list-item">
            <div className="album-list-image">
              <div style={{
                width: '100%',
                height: '100%',
                background: 'linear-gradient(135deg, #e53935 0%, #ab000d 100%)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                color: 'white'
              }}>
                🎵
              </div>
            </div>
            <div className="album-list-content">
              <h4 className="album-list-title">{album.album}</h4>
              <div className="album-list-meta">
                <span>{album.year}</span>
                <span>Chart: {album.chartPosition === '-' ? 'N/A' : `#${album.chartPosition}`}</span>
              </div>
            </div>
            <div className="album-list-actions">
              <button
                className="album-list-button"
                onClick={() => onToggleFavorite && onToggleFavorite(album.id, favorites[album.id])}
                style={{
                  background: 'none',
                  border: 'none',
                  color: favorites[album.id] ? '#ff4081' : '#757575',
                  cursor: 'pointer',
                  fontSize: '20px'
                }}
              >
                {favorites[album.id] ? '♥' : '♡'}
              </button>
              <button
                className="album-list-button"
                onClick={() => window.location.href = `/albums/${album.id}`}
                style={{
                  background: '#e53935',
                  color: 'white',
                  border: 'none',
                  padding: '8px 16px',
                  borderRadius: '4px',
                  cursor: 'pointer'
                }}
              >
                View
              </button>
            </div>
          </div>
        ))}
      </div>
    );
  }

  return (
    <div className={`album-grid ${compact ? 'compact' : ''} ${className}`}>
      {albums.map((album) => (
        <AlbumCard
          key={album.id}
          album={album}
          isFavorite={favorites[album.id]}
          onToggleFavorite={onToggleFavorite}
          compact={compact}
        />
      ))}
    </div>
  );
};

export default AlbumList;