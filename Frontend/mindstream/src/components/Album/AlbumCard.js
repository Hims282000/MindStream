import React from 'react';
import { Link } from 'react-router-dom';
import { 
  Box,
  Card,
  CardContent,
  Typography,
  Chip,
  IconButton,
  Tooltip,
  useTheme
} from '@mui/material';
import { 
  CalendarToday, 
  TrendingUp, 
  Favorite, 
  FavoriteBorder,
  MusicNote,
  ArrowForward
} from '@mui/icons-material';

const AlbumCard = ({ 
  album,
  isFavorite = false,
  onToggleFavorite,
  compact = false,
  className = ''
}) => {
  const theme = useTheme();

  const handleFavoriteClick = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (onToggleFavorite) {
      onToggleFavorite(album.id, isFavorite);
    }
  };

  return (
    <Card 
      className={`album-card ${className}`}
      component={Link}
      to={`/albums/${album.id}`}
      sx={{
        display: 'flex',
        flexDirection: 'column',
        height: '100%',
        textDecoration: 'none',
        position: 'relative',
        overflow: 'visible',
        bgcolor: 'background.paper',
        '&:hover': {
          transform: 'translateY(-8px)',
          '& .album-action-button': {
            opacity: 1,
            transform: 'translateY(0)',
          }
        }
      }}
    >

      <Box 
        sx={{ 
          position: 'relative', 
          pt: '100%',
          background: `linear-gradient(135deg, ${theme.palette.primary.main} 0%, ${theme.palette.secondary.dark} 100%)`,
          borderRadius: '16px',
          m: 1.5,
          mb: 0,
          boxShadow: 'inset 0 0 20px rgba(0,0,0,0.2)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          overflow: 'hidden'
        }}
      >
        <Box 
          sx={{ 
            position: 'absolute', 
            top: 0, 
            left: 0, 
            width: '100%', 
            height: '100%', 
            display: 'flex', 
            alignItems: 'center', 
            justifyContent: 'center' 
          }}
        >
          <MusicNote sx={{ fontSize: 80, color: 'rgba(255,255,255,0.7)' }} />
        </Box>


        {album.chartPosition !== '-' && (
          <Chip
            label={`#${album.chartPosition}`}
            size="small"
            sx={{
              position: 'absolute',
              top: 12,
              left: 12,
              bgcolor: 'rgba(0,0,0,0.6)',
              color: '#fff',
              backdropFilter: 'blur(4px)',
              fontWeight: 'bold',
              height: '24px'
            }}
          />
        )}


        <Tooltip title={isFavorite ? "Remove from favorites" : "Add to favorites"}>
          <IconButton
            onClick={handleFavoriteClick}
            sx={{
              position: 'absolute',
              top: 12,
              right: 12,
              bgcolor: 'white',
              width: 32,
              height: 32,
              '&:hover': { bgcolor: 'white', transform: 'scale(1.1)' },
              transition: 'transform 0.2s',
              boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
            }}
          >
            {isFavorite ? (
              <Favorite sx={{ fontSize: 20, color: '#ec4899' }} />
            ) : (
              <FavoriteBorder sx={{ fontSize: 20, color: 'text.secondary' }} />
            )}
          </IconButton>
        </Tooltip>


        <Box
          className="album-action-button"
          sx={{
            position: 'absolute',
            bottom: 16,
            right: 16,
            opacity: 0,
            transform: 'translateY(10px)',
            transition: 'all 0.3s ease',
            bgcolor: 'white',
            borderRadius: '50%',
            p: 1,
            display: 'flex',
            boxShadow: '0 4px 12px rgba(0,0,0,0.2)'
          }}
        >
          <ArrowForward color="primary" />
        </Box>
      </Box>


      <CardContent sx={{ flex: 1, px: 2.5, pb: 2, pt: 2 }}>
        <Typography 
          variant="h6" 
          component="h3" 
          sx={{ 
            fontWeight: 700, 
            lineHeight: 1.3,
            mb: 0.5,
            fontSize: '1rem',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            display: '-webkit-box',
            WebkitLineClamp: 2,
            WebkitBoxOrient: 'vertical',
            color: 'text.primary'
          }}
        >
          {album.album}
        </Typography>
        
        <Typography variant="body2" color="primary.main" fontWeight={500} sx={{ mb: 2 }}>
          MindStream
        </Typography>

        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mt: 'auto' }}>
           <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5, color: 'text.secondary' }}>
             <CalendarToday sx={{ fontSize: 14 }} />
             <Typography variant="caption" fontWeight={500}>
               {album.year}
             </Typography>
           </Box>

           <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5, color: '#f59e0b' }}>
             <TrendingUp sx={{ fontSize: 16 }} />
             {album.chartPosition !== '-' && (
               <Typography variant="caption" fontWeight={700}>
                 Top {album.chartPosition <= 10 ? '10' : album.chartPosition <= 50 ? '50' : '100'}
               </Typography>
             )}
           </Box>
        </Box>
      </CardContent>
    </Card>
  );
};

export default AlbumCard;