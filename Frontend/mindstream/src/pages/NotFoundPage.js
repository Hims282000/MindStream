import React from 'react';
import { Link } from 'react-router-dom';
import {
  Container,
  Paper,
  Typography,
  Button,
  Box,
  Stack
} from '@mui/material';
import {
  Home as HomeIcon,
  Search as SearchIcon,
  Album as AlbumIcon,
  SentimentDissatisfied as SentimentDissatisfiedIcon
} from '@mui/icons-material';

const NotFoundPage = () => {
  return (
    <Container maxWidth="md" sx={{ py: 8 }}>
      <Paper
        elevation={3}
        sx={{
          p: 6,
          textAlign: 'center',
          borderRadius: 2
        }}
      >
        <SentimentDissatisfiedIcon sx={{ fontSize: 80, color: '#e53935', mb: 3 }} />
        
        <Typography variant="h1" component="h1" gutterBottom sx={{ fontWeight: 'bold' }}>
          404
        </Typography>
        
        <Typography variant="h4" gutterBottom>
          Page Not Found
        </Typography>
        
        <Typography variant="body1" color="text.secondary" sx={{ mb: 4, maxWidth: 600, mx: 'auto' }}>
          Oops! The page you're looking for doesn't exist. It might have been moved, 
          deleted, or perhaps you typed the wrong URL.
        </Typography>

        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} justifyContent="center">
          <Button
            component={Link}
            to="/"
            variant="contained"
            size="large"
            startIcon={<HomeIcon />}
            sx={{
              backgroundColor: '#e53935',
              '&:hover': {
                backgroundColor: '#c62828'
              }
            }}
          >
            Go Home
          </Button>
          
          <Button
            component={Link}
            to="/albums"
            variant="outlined"
            size="large"
            startIcon={<AlbumIcon />}
            sx={{
              borderColor: '#e53935',
              color: '#e53935',
              '&:hover': {
                borderColor: '#c62828',
                backgroundColor: 'rgba(229, 57, 53, 0.04)'
              }
            }}
          >
            Browse Albums
          </Button>
        </Stack>

        <Box sx={{ mt: 6, p: 3, backgroundColor: '#f5f5f5', borderRadius: 2 }}>
          <Typography variant="h6" gutterBottom>
            Try one of these pages instead:
          </Typography>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} justifyContent="center" sx={{ mt: 2 }}>
            <Button
              component={Link}
              to="/albums"
              variant="text"
              startIcon={<AlbumIcon />}
            >
              All Albums
            </Button>
            <Button
              component={Link}
              to="/favorites"
              variant="text"
              startIcon={<SearchIcon />}
            >
              Favorites
            </Button>
            <Button
              component={Link}
              to="/dashboard"
              variant="text"
              startIcon={<HomeIcon />}
            >
              Dashboard
            </Button>
          </Stack>
        </Box>
      </Paper>
    </Container>
  );
};

export default NotFoundPage;