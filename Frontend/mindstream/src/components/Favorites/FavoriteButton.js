import React from 'react';
import { Favorite, FavoriteBorder } from '@mui/icons-material';
import './Favorites.css';

const FavoriteButton = ({ 
  isFavorite = false,
  onClick,
  size = 'medium',
  showLabel = false,
  className = ''
}) => {
  const sizes = {
    small: { icon: 20, padding: '4px' },
    medium: { icon: 24, padding: '8px' },
    large: { icon: 32, padding: '12px' }
  };

  const { icon: iconSize, padding } = sizes[size];

  const handleClick = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (onClick) {
      onClick();
    }
  };

  return (
    <button
      className={`favorite-button ${className}`}
      onClick={handleClick}
      style={{
        display: 'flex',
        alignItems: 'center',
        gap: '6px',
        background: isFavorite ? 'rgba(255, 64, 129, 0.1)' : 'transparent',
        border: `2px solid ${isFavorite ? '#ff4081' : '#e0e0e0'}`,
        borderRadius: '8px',
        padding,
        cursor: 'pointer',
        transition: 'all 0.3s ease',
        color: isFavorite ? '#ff4081' : '#757575'
      }}
    >
      {isFavorite ? (
        <Favorite style={{ fontSize: iconSize }} />
      ) : (
        <FavoriteBorder style={{ fontSize: iconSize }} />
      )}
      {showLabel && (
        <span style={{ 
          fontWeight: '500',
          fontSize: size === 'small' ? '12px' : '14px'
        }}>
          {isFavorite ? 'Favorited' : 'Add to Favorites'}
        </span>
      )}
    </button>
  );
};

export default FavoriteButton;