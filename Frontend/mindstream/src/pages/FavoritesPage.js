import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Container,
  Grid,
  Card,
  CardContent,
  CardMedia,
  CardActions,
  Typography,
  Box,
  Button,
  IconButton,
  Chip,
  Alert,
  CircularProgress,
  Paper,
  Stack,
  Divider,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Tooltip,
  Zoom,
  Badge
} from '@mui/material';
import {
  Favorite as FavoriteIcon,
  Delete as DeleteIcon,
  Album as AlbumIcon,
  CalendarToday as CalendarIcon,
  TrendingUp as TrendingUpIcon,
  MusicNote as MusicNoteIcon,
  GridView as GridViewIcon,
  ViewList as ViewListIcon,
  Sort as SortIcon,
  FilterList as FilterListIcon,
  ClearAll as ClearAllIcon,
  Warning as WarningIcon
} from '@mui/icons-material';
import { favoriteService } from '../services/favoriteService';
import { useAuth } from '../context/AuthContext';

const FavoritesPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  
  const [favorites, setFavorites] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [viewMode, setViewMode] = useState('grid');
  const [sortBy, setSortBy] = useState('added');
  const [deleteDialog, setDeleteDialog] = useState(null);

  useEffect(() => {
    if (user) {
      loadFavorites();
    }
  }, [user]);

  const loadFavorites = async () => {
    try {
      setLoading(true);
      const response = await favoriteService.getUserFavorites(user.id);
      if (response.data) {
        const sortedFavorites = sortFavorites(response.data);
        setFavorites(sortedFavorites);
      }
    } catch (err) {
      setError(err.message || 'Failed to load favorites');
    } finally {
      setLoading(false);
    }
  };

  const sortFavorites = (favoritesList) => {
    return [...favoritesList].sort((a, b) => {
      switch (sortBy) {
        case 'added':
          return new Date(b.addedAt) - new Date(a.addedAt);
        case 'year':
          return b.tvShow.year - a.tvShow.year;
        case 'title':
          return a.tvShow.album.localeCompare(b.tvShow.album);
        case 'chart':
          const aChart = a.tvShow.chartPosition === '-' ? 999 : parseInt(a.tvShow.chartPosition);
          const bChart = b.tvShow.chartPosition === '-' ? 999 : parseInt(b.tvShow.chartPosition);
          return aChart - bChart;
        default:
          return 0;
      }
    });
  };

  const handleRemoveFavorite = async (albumId, albumTitle) => {
    try {
      await favoriteService.removeFromFavorites(user.id, albumId);
      setFavorites(favorites.filter(fav => fav.tvShow.id !== albumId));
      setDeleteDialog(null);
    } catch (err) {
      setError(err.message || 'Failed to remove favorite');
    }
  };

  const handleClearAll = async () => {
    if (window.confirm('Are you sure you want to remove all favorites?')) {
      try {
        // Remove each favorite individually
        for (const favorite of favorites) {
          await favoriteService.removeFromFavorites(user.id, favorite.tvShow.id);
        }
        setFavorites([]);
      } catch (err) {
        setError(err.message || 'Failed to clear favorites');
      }
    }
  };

  const handleSortChange = (newSort) => {
    setSortBy(newSort);
    const sorted = sortFavorites(favorites);
    setFavorites(sorted);
  };

  if (loading) {
    return (
      <Container sx={{ py: 8, textAlign: 'center' }}>
        <CircularProgress size={60} sx={{ color: '#e53935' }} />
        <Typography variant="h6" sx={{ mt: 2 }}>
          Loading your favorites...
        </Typography>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Header */}
      <Paper elevation={0} sx={{ p: 3, mb: 4, backgroundColor: 'transparent' }}>
        <Stack direction="row" justifyContent="space-between" alignItems="center">
          <Box>
            <Typography variant="h2" component="h1" gutterBottom sx={{ fontWeight: 'bold' }}>
              <FavoriteIcon sx={{ color: '#ff4081', mr: 1, verticalAlign: 'middle' }} />
              My Favorites
            </Typography>
            <Typography variant="h6" color="text.secondary">
              {favorites.length} album{favorites.length !== 1 ? 's' : ''} in your collection
            </Typography>
          </Box>
          
          <Badge badgeContent={favorites.length} color="error">
            <MusicNoteIcon sx={{ fontSize: 60, color: '#e53935', opacity: 0.3 }} />
          </Badge>
        </Stack>
      </Paper>

      {/* Controls */}
      <Paper elevation={2} sx={{ p: 3, mb: 4 }}>
        <Grid container spacing={2} alignItems="center">
          <Grid item xs={12} md={6}>
            <Stack direction="row" spacing={1}>
              <Tooltip title="Grid View">
                <IconButton
                  onClick={() => setViewMode('grid')}
                  color={viewMode === 'grid' ? 'primary' : 'default'}
                >
                  <GridViewIcon />
                </IconButton>
              </Tooltip>
              <Tooltip title="List View">
                <IconButton
                  onClick={() => setViewMode('list')}
                  color={viewMode === 'list' ? 'primary' : 'default'}
                >
                  <ViewListIcon />
                </IconButton>
              </Tooltip>
              <Divider orientation="vertical" flexItem />
              <Tooltip title="Sort by Date Added">
                <IconButton
                  onClick={() => handleSortChange('added')}
                  color={sortBy === 'added' ? 'primary' : 'default'}
                >
                  <SortIcon />
                </IconButton>
              </Tooltip>
              <Tooltip title="Sort by Year">
                <IconButton
                  onClick={() => handleSortChange('year')}
                  color={sortBy === 'year' ? 'primary' : 'default'}
                >
                  <CalendarIcon />
                </IconButton>
              </Tooltip>
              <Tooltip title="Sort by Chart">
                <IconButton
                  onClick={() => handleSortChange('chart')}
                  color={sortBy === 'chart' ? 'primary' : 'default'}
                >
                  <TrendingUpIcon />
                </IconButton>
              </Tooltip>
            </Stack>
          </Grid>
          <Grid item xs={12} md={6}>
            <Stack direction="row" spacing={1} justifyContent="flex-end">
              {favorites.length > 0 && (
                <Button
                  variant="outlined"
                  color="error"
                  startIcon={<ClearAllIcon />}
                  onClick={handleClearAll}
                >
                  Clear All
                </Button>
              )}
              <Button
                variant="contained"
                onClick={() => navigate('/albums')}
                sx={{
                  backgroundColor: '#e53935',
                  '&:hover': { backgroundColor: '#c62828' }
                }}
              >
                <AlbumIcon sx={{ mr: 1 }} />
                Browse More
              </Button>
            </Stack>
          </Grid>
        </Grid>

        {error && (
          <Alert 
            severity="error" 
            sx={{ mt: 2 }} 
            onClose={() => setError('')}
          >
            {error}
          </Alert>
        )}
      </Paper>

      {/* Favorites Content */}
      {favorites.length === 0 ? (
        <Paper sx={{ p: 8, textAlign: 'center' }}>
          <FavoriteIcon sx={{ fontSize: 80, color: '#e0e0e0', mb: 2 }} />
          <Typography variant="h4" gutterBottom>
            No favorites yet
          </Typography>
          <Typography variant="body1" color="text.secondary" sx={{ mb: 3, maxWidth: 400, mx: 'auto' }}>
            You haven't added any albums to your favorites. Start exploring the collection!
          </Typography>
          <Button
            variant="contained"
            size="large"
            onClick={() => navigate('/albums')}
            startIcon={<AlbumIcon />}
            sx={{
              backgroundColor: '#e53935',
              '&:hover': { backgroundColor: '#c62828' }
            }}
          >
            Browse Albums
          </Button>
        </Paper>
      ) : viewMode === 'grid' ? (
        <Grid container spacing={3}>
          {favorites.map((favorite) => (
            <Grid item xs={12} sm={6} md={4} key={favorite.id}>
              <Zoom in={true}>
                <Card
                  sx={{
                    height: '100%',
                    display: 'flex',
                    flexDirection: 'column',
                    position: 'relative',
                    transition: 'transform 0.3s, box-shadow 0.3s',
                    '&:hover': {
                      transform: 'translateY(-8px)',
                      boxShadow: 6
                    }
                  }}
                >
                  <Box sx={{ position: 'relative' }}>
                    <CardMedia
                      component="div"
                      sx={{
                        height: 200,
                        backgroundColor: '#e53935',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        color: 'white',
                        position: 'relative'
                      }}
                    >
                      <MusicNoteIcon sx={{ fontSize: 80, opacity: 0.8 }} />
                      <Chip
                        label="Favorited"
                        size="small"
                        sx={{
                          position: 'absolute',
                          top: 8,
                          left: 8,
                          backgroundColor: '#ff4081',
                          color: 'white',
                          fontWeight: 'bold'
                        }}
                      />
                    </CardMedia>
                  </Box>
                  <CardContent sx={{ flexGrow: 1 }}>
                    <Typography variant="h6" component="h2" gutterBottom>
                      {favorite.tvShow.album}
                    </Typography>
                    <Stack spacing={1}>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <CalendarIcon fontSize="small" color="action" />
                        <Typography variant="body2" color="text.secondary">
                          {favorite.tvShow.year}
                        </Typography>
                      </Box>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <TrendingUpIcon fontSize="small" color="action" />
                        <Chip
                          label={`Chart: ${favorite.tvShow.chartPosition}`}
                          size="small"
                          sx={{
                            backgroundColor: favorite.tvShow.chartPosition === '-' ? '#e0e0e0' : '#e53935',
                            color: favorite.tvShow.chartPosition === '-' ? '#757575' : 'white',
                            fontWeight: 'bold'
                          }}
                        />
                      </Box>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <FavoriteIcon fontSize="small" sx={{ color: '#ff4081' }} />
                        <Typography variant="caption" color="text.secondary">
                          Added {new Date(favorite.addedAt).toLocaleDateString()}
                        </Typography>
                      </Box>
                    </Stack>
                  </CardContent>
                  <CardActions sx={{ p: 2, pt: 0 }}>
                    <Button
                      fullWidth
                      variant="contained"
                      sx={{ mr: 1 }}
                      onClick={() => navigate(`/albums/${favorite.tvShow.id}`)}
                    >
                      View Details
                    </Button>
                    <Tooltip title="Remove from favorites">
                      <IconButton
                        color="error"
                        onClick={() => setDeleteDialog({
                          id: favorite.tvShow.id,
                          title: favorite.tvShow.album
                        })}
                      >
                        <DeleteIcon />
                      </IconButton>
                    </Tooltip>
                  </CardActions>
                </Card>
              </Zoom>
            </Grid>
          ))}
        </Grid>
      ) : (
        /* List View */
        <Stack spacing={2}>
          {favorites.map((favorite) => (
            <Paper
              key={favorite.id}
              elevation={2}
              sx={{
                p: 2,
                display: 'flex',
                alignItems: 'center',
                gap: 2,
                transition: 'transform 0.2s',
                '&:hover': {
                  transform: 'translateX(4px)',
                  boxShadow: 4
                }
              }}
            >
              <Box
                sx={{
                  width: 60,
                  height: 60,
                  backgroundColor: '#e53935',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  borderRadius: 1,
                  color: 'white'
                }}
              >
                <MusicNoteIcon />
              </Box>
              <Box sx={{ flex: 1 }}>
                <Typography variant="h6">{favorite.tvShow.album}</Typography>
                <Stack direction="row" spacing={2} sx={{ mt: 0.5 }}>
                  <Typography variant="body2" color="text.secondary">
                    <CalendarIcon fontSize="small" sx={{ mr: 0.5, verticalAlign: 'middle' }} />
                    {favorite.tvShow.year}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    <TrendingUpIcon fontSize="small" sx={{ mr: 0.5, verticalAlign: 'middle' }} />
                    Chart: {favorite.tvShow.chartPosition}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    <FavoriteIcon fontSize="small" sx={{ mr: 0.5, verticalAlign: 'middle', color: '#ff4081' }} />
                    {new Date(favorite.addedAt).toLocaleDateString()}
                  </Typography>
                </Stack>
              </Box>
              <Stack direction="row" spacing={1}>
                <Button
                  variant="contained"
                  size="small"
                  onClick={() => navigate(`/albums/${favorite.tvShow.id}`)}
                  sx={{
                    backgroundColor: '#e53935',
                    '&:hover': { backgroundColor: '#c62828' }
                  }}
                >
                  View
                </Button>
                <Tooltip title="Remove">
                  <IconButton
                    color="error"
                    onClick={() => setDeleteDialog({
                      id: favorite.tvShow.id,
                      title: favorite.tvShow.album
                    })}
                  >
                    <DeleteIcon />
                  </IconButton>
                </Tooltip>
              </Stack>
            </Paper>
          ))}
        </Stack>
      )}

      {/* Stats */}
      {favorites.length > 0 && (
        <Paper elevation={2} sx={{ p: 3, mt: 4 }}>
          <Typography variant="h6" gutterBottom>
            Collection Stats
          </Typography>
          <Grid container spacing={3}>
            <Grid item xs={6} sm={3}>
              <Box textAlign="center">
                <Typography variant="h4" color="primary">
                  {favorites.length}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Total Albums
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={6} sm={3}>
              <Box textAlign="center">
                <Typography variant="h4" color="primary">
                  {Math.min(...favorites.map(f => f.tvShow.year))}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Earliest Year
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={6} sm={3}>
              <Box textAlign="center">
                <Typography variant="h4" color="primary">
                  {Math.max(...favorites.map(f => f.tvShow.year))}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Latest Year
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={6} sm={3}>
              <Box textAlign="center">
                <Typography variant="h4" color="primary">
                  {favorites.filter(f => f.tvShow.chartPosition !== '-').length}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Charted Albums
                </Typography>
              </Box>
            </Grid>
          </Grid>
        </Paper>
      )}

      {/* Delete Confirmation Dialog */}
      <Dialog
        open={!!deleteDialog}
        onClose={() => setDeleteDialog(null)}
      >
        <DialogTitle>
          <WarningIcon color="error" sx={{ mr: 1, verticalAlign: 'middle' }} />
          Remove Favorite
        </DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to remove "{deleteDialog?.title}" from your favorites?
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialog(null)}>
            Cancel
          </Button>
          <Button
            onClick={() => handleRemoveFavorite(deleteDialog?.id, deleteDialog?.title)}
            color="error"
            variant="contained"
            startIcon={<DeleteIcon />}
          >
            Remove
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default FavoritesPage;