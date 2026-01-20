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
  Stack,
  useTheme
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
  const theme = useTheme();

  return (
    <Box
      component="footer"
      sx={{
        mt: 'auto',
        py: 8,
        background: theme.palette.mode === 'light' 
          ? `linear-gradient(to top, #020617 0%, #0f172a 100%)`
          : `linear-gradient(to top, #020617 0%, #1e293b 100%)`,
        color: 'white',
        borderTop: '1px solid rgba(255,255,255,0.05)'
      }}
    >
      <Container maxWidth="lg">
        <Grid container spacing={6}>
          <Grid item xs={12} sm={6} md={3}>
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
              <MusicNoteIcon sx={{ mr: 1, color: 'primary.main', fontSize: 32 }} />
              <Typography variant="h6" component="div" sx={{ fontWeight: 700, letterSpacing: '0.05em' }}>
                MINDSTREAM
              </Typography>
            </Box>
            <Typography variant="body2" sx={{ mb: 3, opacity: 0.7, lineHeight: 1.8 }}>
              The ultimate collection for MindStream fans. 
              Track albums, chart positions, and build your personal collection.
            </Typography>
            <Stack direction="row" spacing={1}>
              <IconButton
                size="small"
                sx={{ 
                  color: 'white', 
                  bgcolor: 'rgba(255,255,255,0.05)',
                  '&:hover': { bgcolor: 'primary.main', color: 'white' } 
                }}
              >
                <GitHubIcon fontSize="small" />
              </IconButton>
              <IconButton
                size="small"
                sx={{ 
                  color: 'white', 
                  bgcolor: 'rgba(255,255,255,0.05)',
                  '&:hover': { bgcolor: 'primary.main', color: 'white' } 
                }}
              >
                <TwitterIcon fontSize="small" />
              </IconButton>
              <IconButton
                size="small"
                sx={{ 
                  color: 'white', 
                  bgcolor: 'rgba(255,255,255,0.05)',
                  '&:hover': { bgcolor: 'primary.main', color: 'white' } 
                }}
              >
                <FacebookIcon fontSize="small" />
              </IconButton>
              <IconButton
                size="small"
                sx={{ 
                  color: 'white', 
                  bgcolor: 'rgba(255,255,255,0.05)',
                  '&:hover': { bgcolor: 'primary.main', color: 'white' } 
                }}
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
                  iamhims738@gmail.com
                </Typography>
              </Box>
              <Typography variant="body2" sx={{ opacity: 0.8 }}>
                Made with <FavoriteIcon fontSize="small" sx={{ color: '#e53935', verticalAlign: 'middle', mx: 0.5 }} /> 
                for MindStream fans
              </Typography>
            </Stack>
          </Grid>
        </Grid>

        <Divider sx={{ my: 4, borderColor: 'rgba(255,255,255,0.1)' }} />

        <Box sx={{ display: 'flex', flexDirection: { xs: 'column', sm: 'row' }, justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="body2" sx={{ opacity: 0.6, mb: { xs: 2, sm: 0 } }}>
            © {currentYear} MindStream Collection. All rights reserved.
          </Typography>
          <Typography variant="body2" sx={{ opacity: 0.6 }}>
            This is a MindStream project. Created for educational purposes.
          </Typography>
        </Box>
      </Container>
    </Box>
  );
};

export default Footer;