import React from 'react';
import { Link } from 'react-router-dom';
import { 
  CalendarToday, 
  TrendingUp, 
  Favorite, 
  FavoriteBorder,
  MusicNote 
} from '@mui/icons-material';
import './Album.css';

const AlbumCard = ({ 
  album,
  isFavorite = false,
  onToggleFavorite,
  compact = false,
  className = ''
}) => {
  const handleFavoriteClick = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (onToggleFavorite) {
      onToggleFavorite(album.id, isFavorite);
    }
  };

  return (
    <Link to={`/albums/${album.id}`} className={`album-card ${compact ? 'compact' : ''} ${className}`}>
      <div className="album-image-container">
        <div className="album-image-placeholder">
          <MusicNote className="album-image-placeholder-icon" />
        </div>
        
        <div className="album-badges">
          {album.chartPosition !== '-' && (
            <span className="album-badge chart">
              #{album.chartPosition}
            </span>
          )}
        </div>
        
        <button 
          className="album-card-remove"
          onClick={handleFavoriteClick}
        >
          {isFavorite ? (
            <Favorite style={{ color: '#ff4081', fontSize: '16px' }} />
          ) : (
            <FavoriteBorder style={{ fontSize: '16px' }} />
          )}
        </button>
      </div>
      
      <div className="album-content">
        <h3 className="album-title">{album.album}</h3>
        <p className="album-artist">MindStream</p>
        
        <div className="album-meta">
          <div className="album-year">
            <CalendarToday className="album-year-icon" />
            <span>{album.year}</span>
          </div>
          <div className="album-chart">
            <TrendingUp className="album-chart-icon" />
            <span className="album-chart-number">
              {album.chartPosition === '-' ? 'N/A' : `#${album.chartPosition}`}
            </span>
          </div>
        </div>
        
        <div className="album-actions">
          <button 
            className="album-button album-button-primary"
            onClick={(e) => {
              e.preventDefault();
              window.location.href = `/albums/${album.id}`;
            }}
          >
            View Details
          </button>
        </div>
      </div>
    </Link>
  );
};

export default AlbumCard;