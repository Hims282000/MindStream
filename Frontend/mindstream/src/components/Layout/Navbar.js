import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  IconButton,
  Menu,
  MenuItem,
  Box,
  Container,
  InputBase,
  alpha,
  styled,
  Tooltip
} from '@mui/material';
import {
  Search as SearchIcon,
  Home as HomeIcon,
  Album as AlbumIcon,
  Favorite as FavoriteIcon,
  Person as PersonIcon,
  Login as LoginIcon,
  Logout as LogoutIcon,
  Menu as MenuIcon,
  MusicNote as MusicNoteIcon
} from '@mui/icons-material';
import { useAuth } from '../../context/AuthContext';

const Search = styled('div')(({ theme }) => ({
  position: 'relative',
  borderRadius: theme.shape.borderRadius,
  backgroundColor: 'rgba(0,0,0,0.04)',
  '&:hover': {
    backgroundColor: 'rgba(0,0,0,0.08)',
  },
  marginRight: theme.spacing(2),
  marginLeft: 0,
  width: '100%',
  [theme.breakpoints.up('sm')]: {
    marginLeft: theme.spacing(3),
    width: 'auto',
  },
}));

const SearchIconWrapper = styled('div')(({ theme }) => ({
  padding: theme.spacing(0, 2),
  height: '100%',
  position: 'absolute',
  pointerEvents: 'none',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
}));

const StyledInputBase = styled(InputBase)(({ theme }) => ({
  color: '#1e293b',
  width: '100%',
  '& .MuiInputBase-input': {
    padding: theme.spacing(1, 1, 1, 0),
    paddingLeft: `calc(1em + ${theme.spacing(4)})`,
    transition: theme.transitions.create('width'),
    width: '100%',
    color: '#1e293b',
    '&::-webkit-input-placeholder': {
      color: '#64748b',
      opacity: 1,
    },
    '&::-moz-placeholder': {
      color: '#64748b',
      opacity: 1,
    },
    '&:-ms-input-placeholder': {
      color: '#64748b',
      opacity: 1,
    },
    '&::placeholder': {
      color: '#64748b',
      opacity: 1,
    },
    [theme.breakpoints.up('md')]: {
      width: '20ch',
    },
  },
}));

const Navbar = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');

  const handleMenu = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    logout();
    handleClose();
    navigate('/login');
  };

  const handleSearch = (e) => {
    if (e.key === 'Enter' && searchQuery.trim()) {
      navigate(`/albums?search=${encodeURIComponent(searchQuery)}`);
      setSearchQuery('');
    }
  };

  return (
    <AppBar position="sticky">
      <Container maxWidth="xl">
        <Toolbar disableGutters>

          <MusicNoteIcon sx={{ display: { xs: 'none', md: 'flex' }, mr: 1, color: 'primary.main' }} />
          <Typography
            variant="h6"
            noWrap
            component={Link}
            to="/"
            sx={{
              mr: 2,
              display: { xs: 'none', md: 'flex' },
              fontFamily: 'Poppins',
              fontWeight: 700,
              letterSpacing: '.1rem',
              color: 'text.primary',
              textDecoration: 'none',
              '&:hover': {
                color: 'primary.main',
                transition: 'color 0.2s'
              }
            }}
          >
            MINDSTREAM
          </Typography>


          <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
            <IconButton
              size="large"
              aria-label="menu"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleMenu}
              color="inherit"
            >
              <MenuIcon />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorEl}
              anchorOrigin={{ vertical: 'bottom', horizontal: 'left' }}
              keepMounted
              transformOrigin={{ vertical: 'top', horizontal: 'left' }}
              open={Boolean(anchorEl)}
              onClose={handleClose}
              sx={{ display: { xs: 'block', md: 'none' } }}
            >
              <MenuItem component={Link} to="/" onClick={handleClose}>
                <HomeIcon sx={{ mr: 1 }} /> Home
              </MenuItem>
              <MenuItem component={Link} to="/albums" onClick={handleClose}>
                <AlbumIcon sx={{ mr: 1 }} /> Albums
              </MenuItem>
              {isAuthenticated && (
                <MenuItem component={Link} to="/favorites" onClick={handleClose}>
                  <FavoriteIcon sx={{ mr: 1 }} /> Favorites
                </MenuItem>
              )}
            </Menu>
          </Box>


          <MusicNoteIcon sx={{ display: { xs: 'flex', md: 'none' }, mr: 1, color: 'primary.main' }} />
          <Typography
            variant="h5"
            noWrap
            component={Link}
            to="/"
            sx={{
              mr: 2,
              display: { xs: 'flex', md: 'none' },
              flexGrow: 1,
              fontFamily: 'Poppins',
              fontWeight: 700,
              letterSpacing: '.1rem',
              color: 'text.primary',
              textDecoration: 'none',
            }}
          >
            MS
          </Typography>


          <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' }, ml: 3 }}>
            <Button
              component={Link}
              to="/"
              sx={{ my: 2, color: 'text.secondary', display: 'flex', alignItems: 'center', '&:hover': { color: 'primary.main', background: 'transparent' } }}
              startIcon={<HomeIcon />}
            >
              Home
            </Button>
            <Button
              component={Link}
              to="/albums"
              sx={{ my: 2, color: 'text.secondary', display: 'flex', alignItems: 'center', '&:hover': { color: 'primary.main', background: 'transparent' } }}
              startIcon={<AlbumIcon />}
            >
              Albums
            </Button>
            {isAuthenticated && (
              <Button
                component={Link}
                to="/favorites"
                sx={{ my: 2, color: 'text.secondary', display: 'flex', alignItems: 'center', '&:hover': { color: 'primary.main', background: 'transparent' } }}
                startIcon={<FavoriteIcon />}
              >
                Favorites
              </Button>
            )}
          </Box>


          <Search>
            <SearchIconWrapper>
              <SearchIcon color="action" />
            </SearchIconWrapper>
            <StyledInputBase
              placeholder="Search albums…"
              inputProps={{ 'aria-label': 'search' }}
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              onKeyPress={handleSearch}
            />
          </Search>


          <Box sx={{ flexGrow: 0, display: 'flex', alignItems: 'center', gap: 1, ml: 1 }}>
            {isAuthenticated ? (
              <>
                <Tooltip title="Dashboard">
                  <IconButton component={Link} to="/dashboard" color="primary">
                    <PersonIcon />
                  </IconButton>
                </Tooltip>
                <Typography variant="body2" sx={{ display: { xs: 'none', sm: 'block' }, color: 'text.primary', fontWeight: 600 }}>
                  Hi, {user?.username}
                </Typography>
                <Tooltip title="Logout">
                  <IconButton onClick={handleLogout} sx={{ color: 'text.secondary', '&:hover': { color: 'error.main' } }}>
                    <LogoutIcon />
                  </IconButton>
                </Tooltip>
              </>
            ) : (
              <>
                <Button
                  component={Link}
                  to="/login"
                  variant="text"
                  sx={{ 
                    color: 'text.primary',
                    '&:hover': { color: 'primary.main', background: 'transparent' }
                  }}
                  startIcon={<LoginIcon />}
                >
                  Login
                </Button>
                <Button
                  component={Link}
                  to="/register"
                  variant="contained"
                  color="primary"
                  sx={{ 
                    ml: 1,
                    boxShadow: '0 4px 14px 0 rgba(225, 29, 72, 0.39)',
                  }}
                >
                  Register
                </Button>
              </>
            )}
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
};

export default Navbar;