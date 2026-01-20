import React from 'react';
import { Link } from 'react-router-dom';
import { 
  Delete, 
  CalendarToday, 
  TrendingUp, 
  Favorite,
  MusicNote
} from '@mui/icons-material';
import LoadingSpinner from '../Common/LoadingSpinner';
import ErrorMessage from '../Common/ErrorMessage';
import './Favorites.css';

const FavoritesList = ({ 
  favorites,
  loading = false,
  error = null,
  onRemoveFavorite,
  viewMode = 'grid',
  className = ''
}) => {
  if (loading) {
    return (
      <div className="loading-container">
        <LoadingSpinner size="large" />
        <p>Loading favorites...</p>
      </div>
    );
  }

  if (error) {
    return (
      <ErrorMessage 
        title="Failed to load favorites"
        message={error}
        type="error"
      />
    );
  }

  if (!favorites || favorites.length === 0) {
    return (
      <div className="favorites-empty">
        <div className="favorites-empty-icon">⭐</div>
        <h3 className="favorites-empty-title">No favorites yet</h3>
        <p className="favorites-empty-description">
          You haven't added any albums to your favorites. Start exploring the collection!
        </p>
        <Link to="/albums" className="favorites-empty-action">
          <MusicNote style={{ fontSize: '20px' }} />
          Browse Albums
        </Link>
      </div>
    );
  }

  if (viewMode === 'list') {
    return (
      <div className={`favorites-list ${className}`}>
        {favorites.map((favorite) => (
          <div key={favorite.id} className="favorite-list-item favorited">
            <div className="favorite-list-image">
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
              <div className="favorite-list-favorite-badge">
                <Favorite style={{ fontSize: '12px' }} />
              </div>
            </div>
            <div className="favorite-list-content">
              <h4 className="favorite-list-title">{favorite.tvShow?.album || favorite.album}</h4>
              <div className="favorite-list-meta">
                <span>
                  <CalendarToday style={{ fontSize: '14px', marginRight: '4px', verticalAlign: 'middle' }} />
                  {favorite.tvShow?.year || favorite.year}
                </span>
                <span>
                  <TrendingUp style={{ fontSize: '14px', marginRight: '4px', verticalAlign: 'middle' }} />
                  Chart: {favorite.tvShow?.chartPosition === '-' || favorite.chartPosition === '-' 
                    ? 'N/A' 
                    : `#${favorite.tvShow?.chartPosition || favorite.chartPosition}`}
                </span>
              </div>
              {favorite.addedAt && (
                <div className="favorite-list-added">
                  <Favorite style={{ fontSize: '12px', color: '#ff4081' }} />
                  Added {new Date(favorite.addedAt).toLocaleDateString()}
                </div>
              )}
            </div>
            <div className="favorite-list-actions">
              <button
                className="favorite-list-remove"
                onClick={() => onRemoveFavorite && onRemoveFavorite(
                  favorite.tvShow?.id || favorite.id,
                  favorite.tvShow?.album || favorite.album
                )}
              >
                <Delete style={{ fontSize: '18px' }} />
              </button>
              <Link
                to={`/albums/${favorite.tvShow?.id || favorite.id}`}
                style={{
                  background: '#e53935',
                  color: 'white',
                  textDecoration: 'none',
                  padding: '8px 16px',
                  borderRadius: '6px',
                  fontSize: '14px',
                  fontWeight: '500'
                }}
              >
                View
              </Link>
            </div>
          </div>
        ))}
      </div>
    );
  }

  return (
    <div className={`favorites-grid ${className}`}>
      {favorites.map((favorite) => (
        <div key={favorite.id} className="favorite-card favorited">
          <div className="favorite-card-header">
            <div style={{
              width: '100%',
              height: '100%',
              background: 'linear-gradient(135deg, #e53935 0%, #ab000d 100%)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: 'white'
            }}>
              <MusicNote style={{ fontSize: '48px', opacity: 0.8 }} />
            </div>
            <div className="favorite-card-badges">
              <span className="favorite-card-badge favorite">
                <Favorite style={{ fontSize: '10px', marginRight: '2px' }} />
                Favorite
              </span>
              {favorite.tvShow?.chartPosition !== '-' && favorite.tvShow?.chartPosition && (
                <span className="favorite-card-badge chart">
                  #{favorite.tvShow.chartPosition}
                </span>
              )}
            </div>
            <button
              className="favorite-card-remove"
              onClick={() => onRemoveFavorite && onRemoveFavorite(
                favorite.tvShow?.id || favorite.id,
                favorite.tvShow?.album || favorite.album
              )}
            >
              <Delete style={{ fontSize: '18px' }} />
            </button>
          </div>
          <div className="favorite-card-content">
            <h3 className="favorite-card-title">
              {favorite.tvShow?.album || favorite.album}
            </h3>
            <div className="favorite-card-meta">
              <div className="favorite-card-year">
                <CalendarToday style={{ fontSize: '16px' }} />
                <span>{favorite.tvShow?.year || favorite.year}</span>
              </div>
              <div className="favorite-card-chart">
                <TrendingUp style={{ fontSize: '16px' }} />
                <span>
                  {favorite.tvShow?.chartPosition === '-' || favorite.chartPosition === '-' 
                    ? 'N/A' 
                    : `#${favorite.tvShow?.chartPosition || favorite.chartPosition}`}
                </span>
              </div>
            </div>
            {favorite.addedAt && (
              <div className="favorite-card-added">
                <Favorite className="favorite-card-added-icon" />
                <span>Added {new Date(favorite.addedAt).toLocaleDateString()}</span>
              </div>
            )}
            <div className="favorite-card-actions">
              <Link
                to={`/albums/${favorite.tvShow?.id || favorite.id}`}
                className="favorite-card-button favorite-card-button-primary"
              >
                View Details
              </Link>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default FavoritesList;