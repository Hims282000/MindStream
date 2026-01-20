import React from 'react';
import { Link } from 'react-router-dom';
import {
  Box,
  Container,
  Grid,
  Typography,
  Link as MuiLink,
  IconButton,
  Divider,
  Stack
} from '@mui/material';
import {
  GitHub as GitHubIcon,
  Twitter as TwitterIcon,
  Facebook as FacebookIcon,
  Instagram as InstagramIcon,
  Email as EmailIcon,
  MusicNote as MusicNoteIcon,
  Favorite as FavoriteIcon
} from '@mui/icons-material';

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <Box
      component="footer"
      sx={{
        mt: 'auto',
        py: 6,
        backgroundColor: '#212121',
        color: 'white'
      }}
    >
      <Container maxWidth="lg">
        <Grid container spacing={4}>
          <Grid item xs={12} sm={6} md={3}>
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <MusicNoteIcon sx={{ mr: 1, color: '#e53935' }} />
              <Typography variant="h6" component="div">
                WHITE STRIPES
              </Typography>
            </Box>
            <Typography variant="body2" sx={{ mb: 2, opacity: 0.8 }}>
              The ultimate collection for White Stripes fans. 
              Track albums, chart positions, and build your personal collection.
            </Typography>
            <Stack direction="row" spacing={1}>
              <IconButton
                size="small"
                sx={{ color: 'white', '&:hover': { color: '#e53935' } }}
              >
                <GitHubIcon fontSize="small" />
              </IconButton>
              <IconButton
                size="small"
                sx={{ color: 'white', '&:hover': { color: '#e53935' } }}
              >
                <TwitterIcon fontSize="small" />
              </IconButton>
              <IconButton
                size="small"
                sx={{ color: 'white', '&:hover': { color: '#e53935' } }}
              >
                <FacebookIcon fontSize="small" />
              </IconButton>
              <IconButton
                size="small"
                sx={{ color: 'white', '&:hover': { color: '#e53935' } }}
              >
                <InstagramIcon fontSize="small" />
              </IconButton>
            </Stack>
          </Grid>

          <Grid item xs={12} sm={6} md={3}>
            <Typography variant="h6" gutterBottom>
              Explore
            </Typography>
            <Stack spacing={1}>
              <MuiLink
                component={Link}
                to="/"
                color="inherit"
                underline="hover"
                sx={{ opacity: 0.8, '&:hover': { opacity: 1, color: '#e53935' } }}
              >
                Home
              </MuiLink>
              <MuiLink
                component={Link}
                to="/albums"
                color="inherit"
                underline="hover"
                sx={{ opacity: 0.8, '&:hover': { opacity: 1, color: '#e53935' } }}
              >
                Albums
              </MuiLink>
              <MuiLink
                component={Link}
                to="/favorites"
                color="inherit"
                underline="hover"
                sx={{ opacity: 0.8, '&:hover': { opacity: 1, color: '#e53935' } }}
              >
                Favorites
              </MuiLink>
              <MuiLink
                component={Link}
                to="/dashboard"
                color="inherit"
                underline="hover"
                sx={{ opacity: 0.8, '&:hover': { opacity: 1, color: '#e53935' } }}
              >
                Dashboard
              </MuiLink>
            </Stack>
          </Grid>

          <Grid item xs={12} sm={6} md={3}>
            <Typography variant="h6" gutterBottom>
              Account
            </Typography>
            <Stack spacing={1}>
              <MuiLink
                component={Link}
                to="/login"
                color="inherit"
                underline="hover"
                sx={{ opacity: 0.8, '&:hover': { opacity: 1, color: '#e53935' } }}
              >
                Login
              </MuiLink>
              <MuiLink
                component={Link}
                to="/register"
                color="inherit"
                underline="hover"
                sx={{ opacity: 0.8, '&:hover': { opacity: 1, color: '#e53935' } }}
              >
                Register
              </MuiLink>
              <MuiLink
                component={Link}
                to="/dashboard"
                color="inherit"
                underline="hover"
                sx={{ opacity: 0.8, '&:hover': { opacity: 1, color: '#e53935' } }}
              >
                Profile
              </MuiLink>
            </Stack>
          </Grid>

          <Grid item xs={12} sm={6} md={3}>
            <Typography variant="h6" gutterBottom>
              Contact
            </Typography>
            <Stack spacing={1}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <EmailIcon fontSize="small" />
                <Typography variant="body2" sx={{ opacity: 0.8 }}>
                  support@whitestripes.com
                </Typography>
              </Box>
              <Typography variant="body2" sx={{ opacity: 0.8 }}>
                Made with <FavoriteIcon fontSize="small" sx={{ color: '#e53935', verticalAlign: 'middle', mx: 0.5 }} /> 
                for White Stripes fans
              </Typography>
            </Stack>
          </Grid>
        </Grid>

        <Divider sx={{ my: 4, borderColor: 'rgba(255,255,255,0.1)' }} />

        <Box sx={{ display: 'flex', flexDirection: { xs: 'column', sm: 'row' }, justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="body2" sx={{ opacity: 0.6, mb: { xs: 2, sm: 0 } }}>
            © {currentYear} White Stripes Collection. All rights reserved.
          </Typography>
          <Typography variant="body2" sx={{ opacity: 0.6 }}>
            This site is not affiliated with The White Stripes. Created for educational purposes.
          </Typography>
        </Box>
      </Container>
    </Box>
  );
};

export default Footer;