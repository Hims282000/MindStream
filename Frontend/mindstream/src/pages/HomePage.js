import React from 'react';
import { Link } from 'react-router-dom';
import {
  Container,
  Typography,
  Button,
  Grid,
  Card,
  CardContent,
  Box,
  Paper,
  Stack,
  Chip,
  IconButton,
  alpha,
  useTheme
} from '@mui/material';
import {
  Album as AlbumIcon,
  Favorite as FavoriteIcon,
  TrendingUp as TrendingUpIcon,
  MusicNote as MusicNoteIcon,
  PlayArrow as PlayArrowIcon,
  ArrowForward as ArrowForwardIcon
} from '@mui/icons-material';
import { useAuth } from '../context/AuthContext';
import '../styles/animations.css';

const HomePage = () => {
  const { isAuthenticated } = useAuth();
  const theme = useTheme();

  const features = [
    {
      title: 'Browse Albums',
      description: 'Explore the complete MindStream discography',
      icon: <AlbumIcon sx={{ fontSize: 40 }} />,
      color: theme.palette.primary.main,
      delay: '0s'
    },
    {
      title: 'Track Favorites',
      description: 'Save your favorite albums and create collections',
      icon: <FavoriteIcon sx={{ fontSize: 40 }} />,
      color: '#ec4899', // Pink-500
      delay: '0.1s'
    },
    {
      title: 'Chart Positions',
      description: 'View US chart positions for each album',
      icon: <TrendingUpIcon sx={{ fontSize: 40 }} />,
      color: '#f59e0b', // Amber-500
      delay: '0.2s'
    }
  ];

  const sampleAlbums = [
    { title: 'Elephant', year: 2003, chart: '#6', color: '#e11d48' },
    { title: 'White Blood Cells', year: 2001, chart: '#61', color: '#be123c' },
    { title: 'Icky Thump', year: 2007, chart: '#2', color: '#9f1239' },
    { title: 'Get Behind Me Satan', year: 2005, chart: '#3', color: '#881337' }
  ];

  return (
    <div className="home-page" style={{ overflowX: 'hidden' }}>

      <Box
        sx={{
          background: 'linear-gradient(135deg, #0f172a 0%, #1e293b 100%)',
          color: 'white',
          pt: { xs: 12, md: 20 },
          pb: { xs: 12, md: 16 },
          position: 'relative',
          overflow: 'hidden'
        }}
      >

        <Box sx={{
          position: 'absolute',
          top: '-10%',
          right: '-5%',
          width: '600px',
          height: '600px',
          borderRadius: '50%',
          background: 'radial-gradient(circle, rgba(225,29,72,0.15) 0%, rgba(225,29,72,0) 70%)',
          filter: 'blur(60px)',
          zIndex: 0
        }} />
        <Box sx={{
          position: 'absolute',
          bottom: '-10%',
          left: '-10%',
          width: '500px',
          height: '500px',
          borderRadius: '50%',
          background: 'radial-gradient(circle, rgba(59,130,246,0.1) 0%, rgba(59,130,246,0) 70%)',
          filter: 'blur(60px)',
          zIndex: 0
        }} />

        <Container maxWidth="lg" sx={{ position: 'relative', zIndex: 1, textAlign: 'center' }}>
          <Box className="fade-in-up" sx={{ mb: 4, display: 'inline-flex', alignItems: 'center', bgcolor: 'rgba(255,255,255,0.1)', px: 2, py: 1, borderRadius: '50px', backdropFilter: 'blur(4px)' }}>
            <Chip 
              label="New" 
              size="small" 
              sx={{ bgcolor: theme.palette.primary.main, color: 'white', fontWeight: 'bold', mr: 1.5, height: '24px' }} 
            />
            <Typography variant="body2" sx={{ color: 'rgba(255,255,255,0.9)', fontWeight: 500 }}>
              The Ultimate Collection is here
            </Typography>
          </Box>
          
          <Typography 
            variant="h1" 
            className="fade-in-up" 
            sx={{ 
              mb: 3, 
              background: 'linear-gradient(to right, #ffffff 0%, #cbd5e1 100%)',
              WebkitBackgroundClip: 'text',
              WebkitTextFillColor: 'transparent',
              animationDelay: '0.1s'
            }}
          >
            MINDSTREAM <br/> ALBUM COLLECTION
          </Typography>
          
          <Typography 
            variant="h5" 
            className="fade-in-up"
            sx={{ 
              mb: 6, 
              color: '#94a3b8', 
              maxWidth: '700px', 
              mx: 'auto', 
              fontWeight: 400,
              animationDelay: '0.2s',
              lineHeight: 1.6
            }}
          >
            Explore the complete discography. Track chart positions. <br className="hidden-mobile"/> Save your favorites in a modern, curated interface.
          </Typography>
          
          <Stack 
            direction={{ xs: 'column', sm: 'row' }} 
            spacing={2} 
            justifyContent="center" 
            className="fade-in-up"
            sx={{ animationDelay: '0.3s' }}
          >
            <Button
              component={Link}
              to="/albums"
              variant="contained"
              size="large"
              sx={{
                py: 2,
                px: 5,
                fontSize: '1.1rem',
                borderRadius: '50px',
                boxShadow: '0 4px 14px 0 rgba(225, 29, 72, 0.39)',
              }}
              startIcon={<AlbumIcon />}
            >
              Start Exploring
            </Button>
            {!isAuthenticated && (
              <Button
                component={Link}
                to="/register"
                variant="outlined"
                size="large"
                sx={{
                  py: 2,
                  px: 5,
                  fontSize: '1.1rem',
                  borderRadius: '50px',
                  color: 'white',
                  borderColor: 'rgba(255,255,255,0.3)',
                  backdropFilter: 'blur(4px)',
                  '&:hover': {
                    borderColor: 'white',
                    bgcolor: 'rgba(255,255,255,0.05)'
                  }
                }}
                endIcon={<ArrowForwardIcon />}
              >
                Join Now
              </Button>
            )}
          </Stack>
        </Container>
      </Box>


      <Container sx={{ py: 12 }}>
        <Grid container spacing={4}>
          {features.map((feature, index) => (
            <Grid item xs={12} md={4} key={index}>
              <Card 
                className="fade-in-up"
                sx={{ 
                  height: '100%',
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'flex-start',
                  p: 4,
                  animationDelay: feature.delay,
                  background: 'rgba(255,255,255,0.7)',
                  backdropFilter: 'blur(20px)',
                  border: '1px solid rgba(255,255,255,0.8)'
                }}
              >
                <AvatarWrapper color={feature.color}>
                  {feature.icon}
                </AvatarWrapper>
                <Typography variant="h5" component="h3" gutterBottom sx={{ mt: 3, fontWeight: 700 }}>
                  {feature.title}
                </Typography>
                <Typography variant="body1" color="text.secondary" sx={{ lineHeight: 1.7 }}>
                  {feature.description}
                </Typography>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Container>


      <Box sx={{ bgcolor: 'white', py: 12, position: 'relative' }}>
        <Container>
          <Box sx={{ textAlign: 'center', mb: 8 }} className="fade-in">
            <Typography variant="h3" component="h2" gutterBottom sx={{ color: '#0f172a' }}>
              Featured Collections
            </Typography>
            <Typography variant="body1" color="text.secondary">
              Handpicked albums that defined a generation
            </Typography>
          </Box>
          
          <Grid container spacing={3} justifyContent="center">
            {sampleAlbums.map((album, index) => (
              <Grid item xs={12} sm={6} md={3} key={index}>
                <Card 
                  className="zoom-in"
                  sx={{ 
                    textAlign: 'center',
                    background: 'white',
                    color: 'text.primary',
                    overflow: 'visible',
                    transition: 'transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1)',
                    animationDelay: `${index * 0.1}s`,
                    '&:hover': {
                      transform: 'translateY(-10px)',
                    }
                  }}
                >
                  <Box 
                    sx={{ 
                      height: 240, 
                      borderRadius: '16px 16px 0 0',
                      background: `linear-gradient(135deg, ${album.color} 0%, #000000 120%)`,
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      position: 'relative',
                      boxShadow: 'inset 0 0 20px rgba(0,0,0,0.2)'
                    }}
                  >
                    <MusicNoteIcon sx={{ fontSize: 80, color: 'rgba(255,255,255,0.8)' }} />
                    <Chip 
                      label={album.year} 
                      size="small" 
                      sx={{ 
                        position: 'absolute', 
                        top: 16, 
                        right: 16, 
                        bgcolor: 'rgba(0,0,0,0.6)', 
                        color: 'white',
                        backdropFilter: 'blur(4px)'
                      }} 
                    />
                  </Box>
                  <CardContent sx={{ p: 3 }}>
                    <Typography variant="h6" component="h3" gutterBottom sx={{ fontWeight: 700 }}>
                      {album.title}
                    </Typography>
                    <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 1, mt: 1 }}>
                      <TrendingUpIcon fontSize="small" color="action" />
                      <Typography variant="body2" color="text.secondary" fontWeight={500}>
                        Peaked at #{album.chart === '#' ? '?' : album.chart.replace('#','')}
                      </Typography>
                    </Box>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>

          <Box sx={{ textAlign: 'center', mt: 8 }} className="fade-in-up">
             <Button
                component={Link}
                to={isAuthenticated ? "/albums" : "/register"}
                variant="outlined"
                color="secondary"
                size="large"
                endIcon={<ArrowForwardIcon />}
                sx={{ 
                  borderRadius: '50px', 
                  px: 4, 
                  py: 1.5,
                  borderWidth: '2px',
                  '&:hover': { borderWidth: '2px' }
                }}
              >
                View Complete Discography
              </Button>
          </Box>
        </Container>
      </Box>


      <Box sx={{ py: 12, bgcolor: '#f8fafc', textAlign: 'center' }}>
         <Container maxWidth="md">
            <Typography variant="h3" gutterBottom sx={{ fontWeight: 800 }}>
              Ready to start your collection?
            </Typography>
            <Typography variant="h6" color="text.secondary" sx={{ mb: 4, fontWeight: 400 }}>
              Join MindStream today and track your music journey.
            </Typography>
            <Button
              component={Link}
              to="/register"
              variant="contained"
              size="large"
              sx={{ 
                py: 2, 
                px: 6, 
                fontSize: '1.2rem',
                borderRadius: '50px',
                boxShadow: '0 10px 30px -10px rgba(225, 29, 72, 0.6)'
              }}
            >
              Get Started for Free
            </Button>
         </Container>
      </Box>
    </div>
  );
};


const AvatarWrapper = ({ children, color }) => (
  <Box
    sx={{
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      width: 64,
      height: 64,
      borderRadius: '20px',
      color: color,
      backgroundColor: alpha(color, 0.1),
      mb: 1
    }}
  >
    {children}
  </Box>
);

export default HomePage;