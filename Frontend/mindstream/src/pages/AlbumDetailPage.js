import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import {
  Container,
  Grid,
  Paper,
  Typography,
  Box,
  Button,
  Chip,
  IconButton,
  CircularProgress,
  Alert,
  Divider,
  Stack,
  Card,
  CardContent,
  Tooltip,
  Breadcrumbs,
  Fade
} from '@mui/material';
import {
  ArrowBack as ArrowBackIcon,
  Favorite as FavoriteIcon,
  FavoriteBorder as FavoriteBorderIcon,
  CalendarToday as CalendarIcon,
  TrendingUp as TrendingUpIcon,
  MusicNote as MusicNoteIcon,
  Share as ShareIcon,
  Album as AlbumIcon,
  NavigateBefore as NavigateBeforeIcon,
  NavigateNext as NavigateNextIcon,
  Home as HomeIcon
} from '@mui/icons-material';
import { albumService } from '../services/albumService';
import { favoriteService } from '../services/favoriteService';
import { useAuth } from '../context/AuthContext';

const AlbumDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuth();
  
  const [album, setAlbum] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [isFavorite, setIsFavorite] = useState(false);
  const [similarAlbums, setSimilarAlbums] = useState([]);
  const [currentAlbumIndex, setCurrentAlbumIndex] = useState(0);
  const [allAlbums, setAllAlbums] = useState([]);

  useEffect(() => {
    loadAlbum();
    loadAllAlbums();
  }, [id]);

  useEffect(() => {
    if (isAuthenticated && user && album) {
      checkFavoriteStatus();
    }
  }, [isAuthenticated, user, album]);

  const loadAlbum = async () => {
    try {
      setLoading(true);
      const userId = isAuthenticated ? user.id : null;
      const response = await albumService.getAlbumById(id, userId);
      setAlbum(response.data);
    } catch (err) {
      setError(err.message || 'Failed to load album details');
    } finally {
      setLoading(false);
    }
  };

  const loadAllAlbums = async () => {
    try {
      const response = await albumService.getAllAlbums();
      if (response.data) {
        setAllAlbums(response.data);
        const index = response.data.findIndex(a => a.id === parseInt(id));
        if (index !== -1) {
          setCurrentAlbumIndex(index);
          loadSimilarAlbums(response.data, index);
        }
      }
    } catch (err) {
      console.error('Failed to load all albums:', err);
    }
  };

  const loadSimilarAlbums = (albums, currentIndex) => {
    // Get albums from same year or nearby years
    const currentAlbum = albums[currentIndex];
    const similar = albums
      .filter((a, index) => index !== currentIndex && Math.abs(a.year - currentAlbum.year) <= 2)
      .slice(0, 4);
    setSimilarAlbums(similar);
  };

  const checkFavoriteStatus = async () => {
    try {
      const response = await favoriteService.checkFavorite(user.id, id);
      setIsFavorite(response.isFavorite);
    } catch (err) {
      console.error('Failed to check favorite status:', err);
    }
  };

  const handleToggleFavorite = async () => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }

    try {
      if (isFavorite) {
        await favoriteService.removeFromFavorites(user.id, id);
        setIsFavorite(false);
      } else {
        await favoriteService.addToFavorites(user.id, id);
        setIsFavorite(true);
      }
    } catch (err) {
      setError(err.message || 'Failed to update favorite');
    }
  };

  const handleShare = () => {
    if (navigator.share) {
      navigator.share({
        title: album.album,
        text: `Check out ${album.album} on MindStream`,
        url: window.location.href,
      });
    } else {
      navigator.clipboard.writeText(window.location.href);
      alert('Link copied to clipboard!');
    }
  };

  const handleNavigate = (direction) => {
    if (direction === 'prev' && currentAlbumIndex > 0) {
      navigate(`/albums/${allAlbums[currentAlbumIndex - 1].id}`);
    } else if (direction === 'next' && currentAlbumIndex < allAlbums.length - 1) {
      navigate(`/albums/${allAlbums[currentAlbumIndex + 1].id}`);
    }
  };

  if (loading) {
    return (
      <Container sx={{ py: 8, textAlign: 'center' }}>
        <CircularProgress size={60} sx={{ color: '#e53935' }} />
        <Typography variant="h6" sx={{ mt: 2 }}>
          Loading album details...
        </Typography>
      </Container>
    );
  }

  if (error) {
    return (
      <Container sx={{ py: 8 }}>
        <Alert severity="error" sx={{ mb: 3 }}>{error}</Alert>
        <Button
          component={Link}
          to="/albums"
          startIcon={<ArrowBackIcon />}
          variant="contained"
        >
          Back to Albums
        </Button>
      </Container>
    );
  }

  if (!album) {
    return (
      <Container sx={{ py: 8, textAlign: 'center' }}>
        <MusicNoteIcon sx={{ fontSize: 80, color: '#e0e0e0', mb: 2 }} />
        <Typography variant="h4" gutterBottom>
          Album not found
        </Typography>
        <Button
          component={Link}
          to="/albums"
          variant="contained"
          startIcon={<ArrowBackIcon />}
          sx={{ mt: 2 }}
        >
          Browse All Albums
        </Button>
      </Container>
    );
  }

  const getChartColor = () => {
    if (album.chartPosition === '-') return '#757575';
    const position = parseInt(album.chartPosition);
    if (position <= 10) return '#4caf50';
    if (position <= 50) return '#ff9800';
    return '#f44336';
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Breadcrumbs */}
      <Breadcrumbs aria-label="breadcrumb" sx={{ mb: 3 }}>
        <Button
          component={Link}
          to="/"
          startIcon={<HomeIcon />}
          sx={{ color: 'inherit' }}
        >
          Home
        </Button>
        <Button
          component={Link}
          to="/albums"
          startIcon={<AlbumIcon />}
          sx={{ color: 'inherit' }}
        >
          Albums
        </Button>
        <Typography color="text.primary">{album.album}</Typography>
      </Breadcrumbs>

      {/* Album Header */}
      <Paper elevation={3} sx={{ p: 4, mb: 4 }}>
        <Grid container spacing={4}>
          <Grid item xs={12} md={4}>
            <Fade in={true}>
              <Paper
                elevation={6}
                sx={{
                  height: 300,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  backgroundColor: '#e53935',
                  color: 'white',
                  position: 'relative'
                }}
              >
                <MusicNoteIcon sx={{ fontSize: 120, opacity: 0.9 }} />
                <Chip
                  label={`#${currentAlbumIndex + 1}`}
                  sx={{
                    position: 'absolute',
                    top: 16,
                    left: 16,
                    backgroundColor: 'rgba(0,0,0,0.7)',
                    color: 'white',
                    fontWeight: 'bold'
                  }}
                />
              </Paper>
            </Fade>
          </Grid>
          
          <Grid item xs={12} md={8}>
            <Stack spacing={3}>
              <Box>
                <Typography variant="h2" component="h1" gutterBottom>
                  {album.album}
                </Typography>
                <Typography variant="h5" color="text.secondary">
                  MindStream Collection
                </Typography>
              </Box>

              <Divider />

              <Grid container spacing={3}>
                <Grid item xs={6} sm={3}>
                  <Paper sx={{ p: 2, textAlign: 'center' }}>
                    <CalendarIcon color="action" sx={{ fontSize: 40, mb: 1 }} />
                    <Typography variant="h6">Year</Typography>
                    <Typography variant="h4" color="primary">
                      {album.year}
                    </Typography>
                  </Paper>
                </Grid>
                <Grid item xs={6} sm={3}>
                  <Paper sx={{ p: 2, textAlign: 'center' }}>
                    <TrendingUpIcon color="action" sx={{ fontSize: 40, mb: 1 }} />
                    <Typography variant="h6">Chart Peak</Typography>
                    <Typography 
                      variant="h4" 
                      sx={{ 
                        color: getChartColor(),
                        fontWeight: 'bold'
                      }}
                    >
                      {album.chartPosition}
                    </Typography>
                  </Paper>
                </Grid>
                <Grid item xs={6} sm={3}>
                  <Paper sx={{ p: 2, textAlign: 'center' }}>
                    <AlbumIcon color="action" sx={{ fontSize: 40, mb: 1 }} />
                    <Typography variant="h6">Type</Typography>
                    <Typography variant="body1">
                      {album.year < 2000 ? 'Early' : album.year < 2010 ? 'Classic' : 'Modern'}
                    </Typography>
                  </Paper>
                </Grid>
                <Grid item xs={6} sm={3}>
                  <Paper sx={{ p: 2, textAlign: 'center' }}>
                    <FavoriteIcon color="action" sx={{ fontSize: 40, mb: 1 }} />
                    <Typography variant="h6">Favorite</Typography>
                    <Chip
                      label={isFavorite ? 'Yes' : 'No'}
                      color={isFavorite ? 'error' : 'default'}
                      size="small"
                    />
                  </Paper>
                </Grid>
              </Grid>

              <Stack direction="row" spacing={2} sx={{ mt: 2 }}>
                <Button
                  variant="contained"
                  size="large"
                  startIcon={isFavorite ? <FavoriteIcon /> : <FavoriteBorderIcon />}
                  onClick={handleToggleFavorite}
                  sx={{
                    flex: 1,
                    backgroundColor: isFavorite ? '#ff4081' : '#e53935',
                    '&:hover': {
                      backgroundColor: isFavorite ? '#f50057' : '#c62828'
                    }
                  }}
                >
                  {isFavorite ? 'Remove Favorite' : 'Add to Favorites'}
                </Button>
                <Tooltip title="Share">
                  <IconButton
                    size="large"
                    onClick={handleShare}
                    sx={{
                      border: '1px solid',
                      borderColor: 'divider'
                    }}
                  >
                    <ShareIcon />
                  </IconButton>
                </Tooltip>
              </Stack>
            </Stack>
          </Grid>
        </Grid>
      </Paper>

      {/* Navigation */}
      <Paper elevation={2} sx={{ p: 2, mb: 4 }}>
        <Grid container alignItems="center" justifyContent="space-between">
          <Grid item>
            <Button
              startIcon={<ArrowBackIcon />}
              onClick={() => navigate('/albums')}
            >
              Back to Albums
            </Button>
          </Grid>
          <Grid item>
            <Stack direction="row" spacing={1}>
              <Tooltip title="Previous Album">
                <span>
                  <IconButton
                    onClick={() => handleNavigate('prev')}
                    disabled={currentAlbumIndex === 0}
                  >
                    <NavigateBeforeIcon />
                  </IconButton>
                </span>
              </Tooltip>
              <Chip
                label={`${currentAlbumIndex + 1} of ${allAlbums.length}`}
                variant="outlined"
              />
              <Tooltip title="Next Album">
                <span>
                  <IconButton
                    onClick={() => handleNavigate('next')}
                    disabled={currentAlbumIndex === allAlbums.length - 1}
                  >
                    <NavigateNextIcon />
                  </IconButton>
                </span>
              </Tooltip>
            </Stack>
          </Grid>
        </Grid>
      </Paper>

      {/* Similar Albums */}
      {similarAlbums.length > 0 && (
        <Box sx={{ mb: 4 }}>
          <Typography variant="h5" gutterBottom sx={{ mb: 3 }}>
            Similar Albums
          </Typography>
          <Grid container spacing={2}>
            {similarAlbums.map((similarAlbum) => (
              <Grid item xs={6} sm={3} key={similarAlbum.id}>
                <Card
                  sx={{
                    transition: 'transform 0.2s',
                    '&:hover': {
                      transform: 'translateY(-4px)',
                      cursor: 'pointer'
                    }
                  }}
                  onClick={() => navigate(`/albums/${similarAlbum.id}`)}
                >
                  <CardContent>
                    <Typography variant="h6" noWrap>
                      {similarAlbum.album}
                    </Typography>
                    <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mt: 1 }}>
                      <Typography variant="body2" color="text.secondary">
                        {similarAlbum.year}
                      </Typography>
                      <Chip
                        label={`#${similarAlbum.chartPosition}`}
                        size="small"
                        sx={{
                          backgroundColor: '#e53935',
                          color: 'white'
                        }}
                      />
                    </Stack>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Box>
      )}

      {/* Album Info */}
      <Paper elevation={2} sx={{ p: 4 }}>
        <Typography variant="h5" gutterBottom>
          About This Album
        </Typography>
        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            <Typography variant="body1" paragraph>
              Released in {album.year}, "{album.album}" represents an important 
              chapter in MindStream's discography. With a peak chart position 
              of #{album.chartPosition === '-' ? 'N/A' : album.chartPosition} on 
              the US charts, this album showcases the band's distinctive sound and 
              musical evolution.
            </Typography>
            <Typography variant="body1" paragraph>
              MindStream, known for its curated collection of albums, 
              consistently pushed musical boundaries throughout their career. Each 
              album tells a unique story of artistic growth and creative expression.
            </Typography>
          </Grid>
          <Grid item xs={12} md={6}>
            <Card variant="outlined">
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  Quick Facts
                </Typography>
                <Stack spacing={1}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Typography variant="body2">Release Decade:</Typography>
                    <Typography variant="body2" fontWeight="bold">
                      {Math.floor(album.year / 10) * 10}s
                    </Typography>
                  </Box>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Typography variant="body2">Chart Success:</Typography>
                    <Typography variant="body2" fontWeight="bold">
                      {album.chartPosition === '-' 
                        ? 'Not Charted' 
                        : album.chartPosition <= 10 
                          ? 'Top 10 Hit' 
                          : album.chartPosition <= 50 
                            ? 'Charted' 
                            : 'Moderate Success'
                      }
                    </Typography>
                  </Box>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Typography variant="body2">Era:</Typography>
                    <Typography variant="body2" fontWeight="bold">
                      {album.year < 2002 ? 'Early Years' 
                       : album.year < 2007 ? 'Peak Years' 
                       : 'Later Years'}
                    </Typography>
                  </Box>
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Paper>
    </Container>
  );
};

export default AlbumDetailPage;