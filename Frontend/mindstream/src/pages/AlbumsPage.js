import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import {
  Container,
  Grid,
  Card,
  CardContent,
  CardMedia,
  Typography,
  Box,
  TextField,
  InputAdornment,
  Button,
  Chip,
  IconButton,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
  Pagination,
  CircularProgress,
  Alert,
  Paper,
  Stack,
  Tooltip,
  Zoom,
  Fade
} from '@mui/material';
import {
  Search as SearchIcon,
  FilterList as FilterListIcon,
  Sort as SortIcon,
  Favorite as FavoriteIcon,
  FavoriteBorder as FavoriteBorderIcon,
  CalendarToday as CalendarIcon,
  TrendingUp as TrendingUpIcon,
  MusicNote as MusicNoteIcon,
  ViewList as ViewListIcon,
  ViewModule as ViewModuleIcon
} from '@mui/icons-material';
import { albumService } from '../services/albumService';
import { favoriteService } from '../services/favoriteService';
import { useAuth } from '../context/AuthContext';

const AlbumsPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuth();
  
  const [albums, setAlbums] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [filterYear, setFilterYear] = useState('');
  const [sortBy, setSortBy] = useState('year');
  const [viewMode, setViewMode] = useState('grid');
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [favorites, setFavorites] = useState({});
  const itemsPerPage = 12;

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const searchParam = params.get('search');
    if (searchParam) {
      setSearchQuery(searchParam);
    }
    loadAlbums();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [location.search, filterYear]);

  useEffect(() => {
    if (isAuthenticated && user) {
      loadFavorites();
    }
  }, [isAuthenticated, user]);

  const loadAlbums = async () => {
    try {
      setLoading(true);
      const userId = isAuthenticated ? user.id : null;
      let response;
      
      if (searchQuery) {
        response = await albumService.searchAlbums(searchQuery, userId);
      } else if (filterYear) {
        response = await albumService.getAlbumsByYear(parseInt(filterYear), userId);
      } else {
        response = await albumService.getAllAlbums(userId);
      }
      
      if (response.data) {
        const sortedAlbums = sortAlbums(response.data);
        setAlbums(sortedAlbums);
        setTotalPages(Math.ceil(sortedAlbums.length / itemsPerPage));
      }
    } catch (err) {
      setError(err.message || 'Failed to load albums');
    } finally {
      setLoading(false);
    }
  };

  const loadFavorites = async () => {
    try {
      const response = await favoriteService.getUserFavorites(user.id);
      if (response.data) {
        const favMap = {};
        response.data.forEach(fav => {
          favMap[fav.tvShow.id] = true;
        });
        setFavorites(favMap);
      }
    } catch (err) {
      console.error('Failed to load favorites:', err);
    }
  };

  const sortAlbums = (albumsList) => {
    return [...albumsList].sort((a, b) => {
      switch (sortBy) {
        case 'year':
          return b.year - a.year;
        case 'title':
          return a.album.localeCompare(b.album);
        case 'chart':
          const aChart = a.chartPosition === '-' ? 999 : parseInt(a.chartPosition);
          const bChart = b.chartPosition === '-' ? 999 : parseInt(b.chartPosition);
          return aChart - bChart;
        default:
          return 0;
      }
    });
  };

  const handleSearch = (e) => {
    if (e.key === 'Enter' && searchQuery.trim()) {
      navigate(`/albums?search=${encodeURIComponent(searchQuery)}`);
    }
  };

  const handleFilterChange = (e) => {
    setFilterYear(e.target.value);
    setPage(1); // Reset to first page when filter changes
  };

  const handleSortChange = (e) => {
    setSortBy(e.target.value);
    const sorted = sortAlbums(albums);
    setAlbums(sorted);
  };

  const handleToggleFavorite = async (albumId, isCurrentlyFavorite) => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }

    try {
      if (isCurrentlyFavorite) {
        await favoriteService.removeFromFavorites(user.id, albumId);
        setFavorites(prev => ({ ...prev, [albumId]: false }));
      } else {
        await favoriteService.addToFavorites(user.id, albumId);
        setFavorites(prev => ({ ...prev, [albumId]: true }));
      }
    } catch (err) {
      setError(err.message || 'Failed to update favorite');
    }
  };

  const handlePageChange = (event, value) => {
    setPage(value);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const getPaginatedAlbums = () => {
    const startIndex = (page - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    return albums.slice(startIndex, endIndex);
  };

  const getYears = () => {
    const years = albums.map(album => album.year);
    return [...new Set(years)].sort((a, b) => b - a);
  };

  if (loading) {
    return (
      <Container sx={{ py: 8, textAlign: 'center' }}>
        <CircularProgress size={60} sx={{ color: '#e53935' }} />
        <Typography variant="h6" sx={{ mt: 2 }}>
          Loading albums...
        </Typography>
      </Container>
    );
  }

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      {/* Header */}
      <Paper elevation={0} sx={{ p: 3, mb: 4, backgroundColor: 'transparent' }}>
        <Typography variant="h2" component="h1" gutterBottom sx={{ fontWeight: 'bold' }}>
          White Stripes Albums
        </Typography>
        <Typography variant="h6" color="text.secondary" gutterBottom>
          Explore the complete discography ({albums.length} albums)
        </Typography>
      </Paper>

      {/* Search and Filters */}
      <Paper elevation={2} sx={{ p: 3, mb: 4 }}>
        <Grid container spacing={2} alignItems="center">
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              placeholder="Search albums by title..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              onKeyPress={handleSearch}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <SearchIcon />
                  </InputAdornment>
                ),
              }}
            />
          </Grid>
          <Grid item xs={12} md={2}>
            <FormControl fullWidth>
              <InputLabel>Filter by Year</InputLabel>
              <Select
                value={filterYear}
                label="Filter by Year"
                onChange={handleFilterChange}
                startAdornment={
                  <InputAdornment position="start">
                    <FilterListIcon />
                  </InputAdornment>
                }
              >
                <MenuItem value="">All Years</MenuItem>
                {getYears().map(year => (
                  <MenuItem key={year} value={year}>
                    {year}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} md={2}>
            <FormControl fullWidth>
              <InputLabel>Sort By</InputLabel>
              <Select
                value={sortBy}
                label="Sort By"
                onChange={handleSortChange}
                startAdornment={
                  <InputAdornment position="start">
                    <SortIcon />
                  </InputAdornment>
                }
              >
                <MenuItem value="year">Year (Newest)</MenuItem>
                <MenuItem value="title">Title (A-Z)</MenuItem>
                <MenuItem value="chart">Chart Position</MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} md={2}>
            <Stack direction="row" spacing={1} justifyContent="flex-end">
              <Tooltip title="Grid View">
                <IconButton
                  onClick={() => setViewMode('grid')}
                  color={viewMode === 'grid' ? 'primary' : 'default'}
                >
                  <ViewModuleIcon />
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
            </Stack>
          </Grid>
        </Grid>

        {error && (
          <Alert severity="error" sx={{ mt: 2 }} onClose={() => setError('')}>
            {error}
          </Alert>
        )}
      </Paper>

      {/* Album Grid */}
      {viewMode === 'grid' ? (
        <Grid container spacing={3}>
          {getPaginatedAlbums().map((album) => (
            <Grid item xs={12} sm={6} md={4} lg={3} key={album.id}>
              <Zoom in={true}>
                <Card
                  sx={{
                    height: '100%',
                    display: 'flex',
                    flexDirection: 'column',
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
                        color: 'white'
                      }}
                    >
                      <MusicNoteIcon sx={{ fontSize: 80, opacity: 0.8 }} />
                    </CardMedia>
                    <Box
                      sx={{
                        position: 'absolute',
                        top: 8,
                        right: 8,
                        backgroundColor: 'rgba(0,0,0,0.7)',
                        borderRadius: '50%',
                        padding: '4px'
                      }}
                    >
                      <IconButton
                        onClick={() => handleToggleFavorite(album.id, favorites[album.id])}
                        size="small"
                        sx={{ color: favorites[album.id] ? '#ff4081' : 'white' }}
                      >
                        {favorites[album.id] ? <FavoriteIcon /> : <FavoriteBorderIcon />}
                      </IconButton>
                    </Box>
                  </Box>
                  <CardContent sx={{ flexGrow: 1 }}>
                    <Typography variant="h6" component="h2" gutterBottom noWrap>
                      {album.album}
                    </Typography>
                    <Stack spacing={1}>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <CalendarIcon fontSize="small" color="action" />
                        <Typography variant="body2" color="text.secondary">
                          {album.year}
                        </Typography>
                      </Box>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <TrendingUpIcon fontSize="small" color="action" />
                        <Chip
                          label={`Chart: ${album.chartPosition}`}
                          size="small"
                          sx={{
                            backgroundColor: album.chartPosition === '-' ? '#e0e0e0' : '#e53935',
                            color: album.chartPosition === '-' ? '#757575' : 'white',
                            fontWeight: 'bold'
                          }}
                        />
                      </Box>
                    </Stack>
                    <Button
                      fullWidth
                      variant="outlined"
                      sx={{ mt: 2 }}
                      onClick={() => navigate(`/albums/${album.id}`)}
                    >
                      View Details
                    </Button>
                  </CardContent>
                </Card>
              </Zoom>
            </Grid>
          ))}
        </Grid>
      ) : (
        /* List View */
        <Stack spacing={2}>
          {getPaginatedAlbums().map((album) => (
            <Fade in={true} key={album.id}>
              <Paper
                elevation={2}
                sx={{
                  p: 2,
                  display: 'flex',
                  alignItems: 'center',
                  gap: 2,
                  transition: 'transform 0.2s',
                  '&:hover': {
                    transform: 'translateX(4px)'
                  }
                }}
              >
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, flex: 1 }}>
                  <MusicNoteIcon sx={{ fontSize: 40, color: '#e53935' }} />
                  <Box sx={{ flex: 1 }}>
                    <Typography variant="h6">{album.album}</Typography>
                    <Stack direction="row" spacing={2}>
                      <Typography variant="body2" color="text.secondary">
                        <CalendarIcon fontSize="small" sx={{ mr: 0.5, verticalAlign: 'middle' }} />
                        {album.year}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        <TrendingUpIcon fontSize="small" sx={{ mr: 0.5, verticalAlign: 'middle' }} />
                        Chart: {album.chartPosition}
                      </Typography>
                    </Stack>
                  </Box>
                </Box>
                <Stack direction="row" spacing={1}>
                  <IconButton
                    onClick={() => handleToggleFavorite(album.id, favorites[album.id])}
                    color={favorites[album.id] ? 'error' : 'default'}
                  >
                    {favorites[album.id] ? <FavoriteIcon /> : <FavoriteBorderIcon />}
                  </IconButton>
                  <Button
                    variant="contained"
                    size="small"
                    onClick={() => navigate(`/albums/${album.id}`)}
                    sx={{
                      backgroundColor: '#e53935',
                      '&:hover': { backgroundColor: '#c62828' }
                    }}
                  >
                    View
                  </Button>
                </Stack>
              </Paper>
            </Fade>
          ))}
        </Stack>
      )}

      {/* Pagination */}
      {albums.length > 0 && (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4, mb: 4 }}>
          <Pagination
            count={totalPages}
            page={page}
            onChange={handlePageChange}
            color="primary"
            size="large"
            sx={{
              '& .MuiPaginationItem-root': {
                '&.Mui-selected': {
                  backgroundColor: '#e53935',
                  color: 'white',
                  '&:hover': {
                    backgroundColor: '#c62828'
                  }
                }
              }
            }}
          />
        </Box>
      )}

      {albums.length === 0 && !loading && (
        <Paper sx={{ p: 8, textAlign: 'center' }}>
          <MusicNoteIcon sx={{ fontSize: 80, color: '#e0e0e0', mb: 2 }} />
          <Typography variant="h5" gutterBottom>
            No albums found
          </Typography>
          <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
            {searchQuery 
              ? `No results for "${searchQuery}"`
              : 'Try adjusting your filters or search term'
            }
          </Typography>
          <Button
            variant="contained"
            onClick={() => {
              setSearchQuery('');
              setFilterYear('');
              loadAlbums();
            }}
            sx={{
              backgroundColor: '#e53935',
              '&:hover': { backgroundColor: '#c62828' }
            }}
          >
            Clear Filters
          </Button>
        </Paper>
      )}
    </Container>
  );
};

export default AlbumsPage;