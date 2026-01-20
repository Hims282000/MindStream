import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Container,
  Grid,
  Paper,
  Typography,
  Box,
  Button,
  Card,
  CardContent,
  CardActions,
  Avatar,
  Chip,
  Divider,
  Stack,
  LinearProgress,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  ListItemSecondaryAction,
  IconButton,
  Tooltip,
  CircularProgress,
  Alert
} from '@mui/material';
import {
  Person as PersonIcon,
  Album as AlbumIcon,
  Favorite as FavoriteIcon,
  TrendingUp as TrendingUpIcon,
  CalendarToday as CalendarIcon,
  MusicNote as MusicNoteIcon,
  Logout as LogoutIcon,
  Settings as SettingsIcon,
  Edit as EditIcon,
  Share as ShareIcon,
  BarChart as BarChartIcon,
  History as HistoryIcon,
  Star as StarIcon,
  Download as DownloadIcon
} from '@mui/icons-material';
import { favoriteService } from '../services/favoriteService';
import { useAuth } from '../context/AuthContext';

const DashboardPage = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  
  const [favorites, setFavorites] = useState([]);
  const [stats, setStats] = useState({
    totalFavorites: 0,
    earliestYear: 0,
    latestYear: 0,
    topChartPosition: '-',
    averageYear: 0
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (user) {
      loadUserData();
    }
  }, [user]);

  const loadUserData = async () => {
    try {
      setLoading(true);
      const response = await favoriteService.getUserFavorites(user.id);
      if (response.data) {
        setFavorites(response.data);
        calculateStats(response.data);
      }
    } catch (err) {
      setError(err.message || 'Failed to load dashboard data');
    } finally {
      setLoading(false);
    }
  };

  const calculateStats = (favs) => {
    if (favs.length === 0) return;

    const years = favs.map(f => f.tvShow.year);
    const chartPositions = favs
      .map(f => f.tvShow.chartPosition)
      .filter(pos => pos !== '-')
      .map(pos => parseInt(pos));

    setStats({
      totalFavorites: favs.length,
      earliestYear: Math.min(...years),
      latestYear: Math.max(...years),
      topChartPosition: chartPositions.length > 0 ? `#${Math.min(...chartPositions)}` : '-',
      averageYear: Math.round(years.reduce((a, b) => a + b, 0) / years.length)
    });
  };

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const getInitials = (name) => {
    return name
      .split(' ')
      .map(word => word[0])
      .join('')
      .toUpperCase()
      .slice(0, 2);
  };

  const getRecentFavorites = () => {
    return [...favorites]
      .sort((a, b) => new Date(b.addedAt) - new Date(a.addedAt))
      .slice(0, 5);
  };

  if (loading) {
    return (
      <Container sx={{ py: 8, textAlign: 'center' }}>
        <CircularProgress size={60} sx={{ color: '#e53935' }} />
        <Typography variant="h6" sx={{ mt: 2 }}>
          Loading your dashboard...
        </Typography>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Header */}
      <Paper elevation={3} sx={{ p: 4, mb: 4 }}>
        <Grid container spacing={3} alignItems="center">
          <Grid item>
            <Avatar
              sx={{
                width: 80,
                height: 80,
                bgcolor: '#e53935',
                fontSize: '2rem',
                fontWeight: 'bold'
              }}
            >
              {getInitials(user?.fullName || user?.username || 'User')}
            </Avatar>
          </Grid>
          <Grid item xs>
            <Typography variant="h3" component="h1" gutterBottom>
              Welcome back, {user?.username}!
            </Typography>
            <Typography variant="h6" color="text.secondary">
              Member since {new Date().getFullYear()}
            </Typography>
          </Grid>
          <Grid item>
            <Stack direction="row" spacing={1}>
              <Tooltip title="Settings">
                <IconButton>
                  <SettingsIcon />
                </IconButton>
              </Tooltip>
              <Tooltip title="Logout">
                <IconButton onClick={handleLogout} color="error">
                  <LogoutIcon />
                </IconButton>
              </Tooltip>
            </Stack>
          </Grid>
        </Grid>
      </Paper>

      {error && (
        <Alert severity="error" sx={{ mb: 3 }} onClose={() => setError('')}>
          {error}
        </Alert>
      )}

      {/* Stats Cards */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Stack direction="row" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography color="text.secondary" gutterBottom>
                    Total Favorites
                  </Typography>
                  <Typography variant="h4">
                    {stats.totalFavorites}
                  </Typography>
                </Box>
                <FavoriteIcon sx={{ fontSize: 40, color: '#ff4081' }} />
              </Stack>
              <LinearProgress 
                variant="determinate" 
                value={Math.min((stats.totalFavorites / 50) * 100, 100)}
                sx={{ mt: 2, height: 8, borderRadius: 4 }}
              />
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Stack direction="row" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography color="text.secondary" gutterBottom>
                    Collection Span
                  </Typography>
                  <Typography variant="h4">
                    {stats.earliestYear}-{stats.latestYear}
                  </Typography>
                </Box>
                <CalendarIcon sx={{ fontSize: 40, color: '#4caf50' }} />
              </Stack>
              <Typography variant="caption" color="text.secondary">
                {stats.latestYear - stats.earliestYear} years
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Stack direction="row" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography color="text.secondary" gutterBottom>
                    Best Chart Position
                  </Typography>
                  <Typography variant="h4">
                    {stats.topChartPosition}
                  </Typography>
                </Box>
                <TrendingUpIcon sx={{ fontSize: 40, color: '#2196f3' }} />
              </Stack>
              <Typography variant="caption" color="text.secondary">
                In your collection
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Stack direction="row" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography color="text.secondary" gutterBottom>
                    Average Year
                  </Typography>
                  <Typography variant="h4">
                    {stats.averageYear || '-'}
                  </Typography>
                </Box>
                <BarChartIcon sx={{ fontSize: 40, color: '#ff9800' }} />
              </Stack>
              <Typography variant="caption" color="text.secondary">
                Mean release year
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <Grid container spacing={3}>
        {/* Recent Activity */}
        <Grid item xs={12} md={8}>
          <Paper elevation={2} sx={{ p: 3, height: '100%' }}>
            <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 3 }}>
              <Typography variant="h5">
                <HistoryIcon sx={{ mr: 1, verticalAlign: 'middle' }} />
                Recent Favorites
              </Typography>
              <Button
                size="small"
                onClick={() => navigate('/favorites')}
              >
                View All
              </Button>
            </Stack>

            {favorites.length === 0 ? (
              <Box textAlign="center" py={4}>
                <MusicNoteIcon sx={{ fontSize: 60, color: '#e0e0e0', mb: 2 }} />
                <Typography variant="h6" gutterBottom>
                  No favorites yet
                </Typography>
                <Typography color="text.secondary" sx={{ mb: 3 }}>
                  Start adding albums to your collection
                </Typography>
                <Button
                  variant="contained"
                  onClick={() => navigate('/albums')}
                  startIcon={<AlbumIcon />}
                  sx={{
                    backgroundColor: '#e53935',
                    '&:hover': { backgroundColor: '#c62828' }
                  }}
                >
                  Browse Albums
                </Button>
              </Box>
            ) : (
              <List>
                {getRecentFavorites().map((favorite, index) => (
                  <React.Fragment key={favorite.id}>
                    <ListItem disablePadding>
                    <ListItemButton
                      onClick={() => navigate(`/albums/${favorite.tvShow.id}`)}
                      sx={{
                        borderRadius: 1,
                        '&:hover': {
                          backgroundColor: 'action.hover'
                        }
                      }}
                    >
                      <ListItemIcon>
                        <Avatar
                          sx={{
                            bgcolor: '#e53935',
                            color: 'white',
                            fontWeight: 'bold'
                          }}
                        >
                          {index + 1}
                        </Avatar>
                      </ListItemIcon>
                      <ListItemText
                        primary={favorite.tvShow.album}
                        secondary={
                          <>
                            <Typography variant="body2" component="span" sx={{ display: 'block' }}>
                              {favorite.tvShow.year} • Chart: {favorite.tvShow.chartPosition}
                            </Typography>
                            <Typography variant="caption" color="text.secondary">
                              Added {new Date(favorite.addedAt).toLocaleDateString()}
                            </Typography>
                          </>
                        }
                      />
                      <ListItemSecondaryAction>
                        <IconButton edge="end">
                          <FavoriteIcon sx={{ color: '#ff4081' }} />
                        </IconButton>
                      </ListItemSecondaryAction>
                    </ListItemButton>
                    </ListItem>
                    {index < getRecentFavorites().length - 1 && <Divider />}
                  </React.Fragment>
                ))}
              </List>
            )}
          </Paper>
        </Grid>

        {/* Quick Actions */}
        <Grid item xs={12} md={4}>
          <Paper elevation={2} sx={{ p: 3, height: '100%' }}>
            <Typography variant="h5" gutterBottom sx={{ mb: 3 }}>
              <StarIcon sx={{ mr: 1, verticalAlign: 'middle' }} />
              Quick Actions
            </Typography>

            <Stack spacing={2}>
              <Button
                fullWidth
                variant="contained"
                size="large"
                startIcon={<AlbumIcon />}
                onClick={() => navigate('/albums')}
                sx={{
                  justifyContent: 'flex-start',
                  py: 1.5,
                  backgroundColor: '#e53935',
                  '&:hover': { backgroundColor: '#c62828' }
                }}
              >
                Browse Albums
              </Button>

              <Button
                fullWidth
                variant="outlined"
                size="large"
                startIcon={<FavoriteIcon />}
                onClick={() => navigate('/favorites')}
                sx={{
                  justifyContent: 'flex-start',
                  py: 1.5,
                  borderColor: '#e53935',
                  color: '#e53935',
                  '&:hover': {
                    borderColor: '#c62828',
                    backgroundColor: 'rgba(229, 57, 53, 0.04)'
                  }
                }}
              >
                My Favorites
              </Button>

              <Button
                fullWidth
                variant="outlined"
                size="large"
                startIcon={<ShareIcon />}
                onClick={() => {
                  navigator.clipboard.writeText(window.location.origin);
                  alert('Website URL copied to clipboard!');
                }}
                sx={{
                  justifyContent: 'flex-start',
                  py: 1.5
                }}
              >
                Share Collection
              </Button>

              <Button
                fullWidth
                variant="outlined"
                size="large"
                startIcon={<EditIcon />}
                onClick={() => {/* TODO: Implement profile edit */}}
                sx={{
                  justifyContent: 'flex-start',
                  py: 1.5
                }}
              >
                Edit Profile
              </Button>

              <Divider sx={{ my: 2 }} />

              <Typography variant="h6" gutterBottom>
                Collection Stats
              </Typography>
              
              <Box>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  Completion Progress
                </Typography>
                <LinearProgress 
                  variant="determinate" 
                  value={Math.min((stats.totalFavorites / 100) * 100, 100)}
                  sx={{ height: 8, borderRadius: 4, mb: 1 }}
                />
                <Typography variant="caption" color="text.secondary">
                  {stats.totalFavorites} of 100 albums collected
                </Typography>
              </Box>

              <Box sx={{ mt: 2 }}>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  Your Top Years
                </Typography>
                {favorites.length > 0 && (
                  <Stack spacing={1}>
                    {Object.entries(
                      favorites.reduce((acc, fav) => {
                        const year = fav.tvShow.year;
                        acc[year] = (acc[year] || 0) + 1;
                        return acc;
                      }, {})
                    )
                      .sort((a, b) => b[1] - a[1])
                      .slice(0, 3)
                      .map(([year, count]) => (
                        <Box key={year} sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                          <Typography variant="body2">{year}</Typography>
                          <LinearProgress 
                            variant="determinate" 
                            value={(count / Math.max(...Object.values(
                              favorites.reduce((acc, fav) => {
                                const year = fav.tvShow.year;
                                acc[year] = (acc[year] || 0) + 1;
                                return acc;
                              }, {})
                            ))) * 100}
                            sx={{ flex: 1, height: 6, borderRadius: 3 }}
                          />
                          <Typography variant="caption">{count}</Typography>
                        </Box>
                      ))}
                  </Stack>
                )}
              </Box>
            </Stack>
          </Paper>
        </Grid>
      </Grid>

      {/* User Info */}
      <Paper elevation={2} sx={{ p: 3, mt: 4 }}>
        <Typography variant="h5" gutterBottom>
          Account Information
        </Typography>
        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            <List>
              <ListItem>
                <ListItemIcon>
                  <PersonIcon color="action" />
                </ListItemIcon>
                <ListItemText 
                  primary="Username" 
                  secondary={user?.username}
                />
              </ListItem>
              <Divider />
              <ListItem>
                <ListItemIcon>
                  <PersonIcon color="action" />
                </ListItemIcon>
                <ListItemText 
                  primary="Full Name" 
                  secondary={user?.fullName || 'Not set'}
                />
              </ListItem>
            </List>
          </Grid>
          <Grid item xs={12} md={6}>
            <List>
              <ListItem>
                <ListItemIcon>
                  <MusicNoteIcon color="action" />
                </ListItemIcon>
                <ListItemText 
                  primary="Member Since" 
                  secondary={new Date().toLocaleDateString()}
                />
              </ListItem>
              <Divider />
              <ListItem>
                <ListItemIcon>
                  <FavoriteIcon color="action" />
                </ListItemIcon>
                <ListItemText 
                  primary="Collection Status" 
                  secondary={
                    <Chip 
                      label={favorites.length > 10 ? "Advanced Collector" : "Beginner Collector"}
                      size="small"
                      color={favorites.length > 10 ? "success" : "default"}
                    />
                  }
                />
              </ListItem>
            </List>
          </Grid>
        </Grid>

        <Box sx={{ mt: 3, textAlign: 'center' }}>
          <Button
            variant="outlined"
            startIcon={<DownloadIcon />}
            onClick={() => {
              const data = {
                user: user,
                favorites: favorites,
                stats: stats,
                exportDate: new Date().toISOString()
              };
              const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
              const url = URL.createObjectURL(blob);
              const a = document.createElement('a');
              a.href = url;
              a.download = `white-stripes-collection-${user?.username}.json`;
              document.body.appendChild(a);
              a.click();
              document.body.removeChild(a);
              URL.revokeObjectURL(url);
            }}
          >
            Export Collection Data
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default DashboardPage;