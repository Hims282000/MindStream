import React from 'react';
import { Link } from 'react-router-dom';
import {
  Container,
  Typography,
  Button,
  Grid,
  Card,
  CardContent,
  CardMedia,
  Box,
  Paper,
  Stack,
  Chip,
  Divider
} from '@mui/material';
import {
  Album as AlbumIcon,
  Favorite as FavoriteIcon,
  Search as SearchIcon,
  TrendingUp as TrendingUpIcon,
  MusicNote as MusicNoteIcon,
  PlayArrow as PlayArrowIcon,
  Star as StarIcon
} from '@mui/icons-material';
import { useAuth } from '../context/AuthContext';
import './HomePage.css';

const HomePage = () => {
  const { isAuthenticated } = useAuth();

  const features = [
    {
      title: 'Browse Albums',
      description: 'Explore the complete MindStream discography',
      icon: <AlbumIcon sx={{ fontSize: 40 }} />,
      color: '#e53935'
    },
    {
      title: 'Track Favorites',
      description: 'Save your favorite albums and create collections',
      icon: <FavoriteIcon sx={{ fontSize: 40 }} />,
      color: '#ab000d'
    },
    {
      title: 'Chart Positions',
      description: 'View US chart positions for each album',
      icon: <TrendingUpIcon sx={{ fontSize: 40 }} />,
      color: '#ff6f60'
    }
  ];

  const sampleAlbums = [
    { title: 'Elephant', year: 2003, chart: '#6', color: '#e53935' },
    { title: 'White Blood Cells', year: 2001, chart: '#61', color: '#f44336' },
    { title: 'Icky Thump', year: 2007, chart: '#2', color: '#ef5350' },
    { title: 'Get Behind Me Satan', year: 2005, chart: '#3', color: '#e57373' }
  ];

  return (
    <div className="home-page">
      {/* Hero Section */}
      <Box
        sx={{
          background: 'linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)), url(https://images.unsplash.com/photo-1511379938547-c1f69419868d?auto=format&fit=crop&w=1920&q=80)',
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          color: 'white',
          py: 10,
          textAlign: 'center'
        }}
      >
        <Container maxWidth="md">
          <MusicNoteIcon sx={{ fontSize: 60, mb: 3, color: '#e53935' }} />
          <Typography variant="h1" component="h1" gutterBottom sx={{ fontWeight: 'bold' }}>
            MINDSTREAM ALBUM COLLECTION
          </Typography>
          <Typography variant="h5" component="p" gutterBottom sx={{ mb: 4, opacity: 0.9 }}>
            Explore the complete discography. Track chart positions. Save your favorites.
          </Typography>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} justifyContent="center">
            <Button
              component={Link}
              to="/albums"
              variant="contained"
              size="large"
              startIcon={<AlbumIcon />}
              sx={{
                backgroundColor: '#e53935',
                '&:hover': {
                  backgroundColor: '#c62828'
                }
              }}
            >
              Browse Albums
            </Button>
            {!isAuthenticated && (
              <Button
                component={Link}
                to="/register"
                variant="outlined"
                size="large"
                startIcon={<StarIcon />}
                sx={{
                  color: 'white',
                  borderColor: 'white',
                  '&:hover': {
                    backgroundColor: 'rgba(255, 255, 255, 0.1)'
                  }
                }}
              >
                Start Collecting
              </Button>
            )}
          </Stack>
        </Container>
      </Box>

      {/* Features Section */}
      <Container sx={{ py: 8 }}>
        <Typography variant="h3" component="h2" align="center" gutterBottom sx={{ mb: 6 }}>
          Why Use Our Collection?
        </Typography>
        <Grid container spacing={4}>
          {features.map((feature, index) => (
            <Grid item xs={12} md={4} key={index}>
              <Card sx={{ 
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                textAlign: 'center',
                p: 3,
                transition: 'transform 0.3s',
                '&:hover': {
                  transform: 'translateY(-8px)'
                }
              }}>
                <Box sx={{ color: feature.color, mb: 2 }}>
                  {feature.icon}
                </Box>
                <Typography variant="h5" component="h3" gutterBottom>
                  {feature.title}
                </Typography>
                <Typography variant="body1" color="text.secondary">
                  {feature.description}
                </Typography>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Container>

      {/* Sample Albums */}
      <Paper sx={{ backgroundColor: '#f5f5f5', py: 8 }}>
        <Container>
          <Typography variant="h3" component="h2" align="center" gutterBottom sx={{ mb: 6 }}>
            Featured Albums
          </Typography>
          <Grid container spacing={3} justifyContent="center">
            {sampleAlbums.map((album, index) => (
              <Grid item xs={6} sm={3} key={index}>
                <Card sx={{ 
                  textAlign: 'center',
                  backgroundColor: album.color,
                  color: 'white',
                  '&:hover': {
                    boxShadow: 6
                  }
                }}>
                  <CardContent>
                    <Typography variant="h6" component="h3" gutterBottom>
                      {album.title}
                    </Typography>
                    <Divider sx={{ backgroundColor: 'white', my: 1 }} />
                    <Typography variant="body2" sx={{ mb: 1 }}>
                      {album.year}
                    </Typography>
                    <Chip 
                      label={`Chart: ${album.chart}`}
                      size="small"
                      sx={{ 
                        backgroundColor: 'white',
                        color: album.color,
                        fontWeight: 'bold'
                      }}
                    />
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Container>
      </Paper>

      {/* Call to Action */}
      <Container sx={{ py: 8, textAlign: 'center' }}>
        <Typography variant="h4" component="h2" gutterBottom>
          Ready to explore?
        </Typography>
        <Typography variant="body1" color="text.secondary" sx={{ mb: 4, maxWidth: 600, mx: 'auto' }}>
          Join thousands of fans who are tracking and collecting MindStream albums.
        </Typography>
        <Button
          component={Link}
          to={isAuthenticated ? "/albums" : "/register"}
          variant="contained"
          size="large"
          startIcon={isAuthenticated ? <PlayArrowIcon /> : <StarIcon />}
          sx={{
            backgroundColor: '#212121',
            '&:hover': {
              backgroundColor: '#000000'
            }
          }}
        >
          {isAuthenticated ? 'Continue Exploring' : 'Get Started Free'}
        </Button>
      </Container>
    </div>
  );
};

export default HomePage;