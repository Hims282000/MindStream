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
  Avatar,
  Container,
  InputBase,
  alpha,
  styled,
  Badge,
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
  backgroundColor: alpha(theme.palette.common.white, 0.15),
  '&:hover': {
    backgroundColor: alpha(theme.palette.common.white, 0.25),
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
  color: 'inherit',
  '& .MuiInputBase-input': {
    padding: theme.spacing(1, 1, 1, 0),
    paddingLeft: `calc(1em + ${theme.spacing(4)})`,
    transition: theme.transitions.create('width'),
    width: '100%',
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
    <AppBar position="sticky" sx={{ background: 'linear-gradient(45deg, #e53935 30%, #ab000d 90%)' }}>
      <Container maxWidth="xl">
        <Toolbar disableGutters>
          {/* Logo/Brand */}
          <MusicNoteIcon sx={{ display: { xs: 'none', md: 'flex' }, mr: 1 }} />
          <Typography
            variant="h6"
            noWrap
            component={Link}
            to="/"
            sx={{
              mr: 2,
              display: { xs: 'none', md: 'flex' },
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.2rem',
              color: 'inherit',
              textDecoration: 'none',
              '&:hover': {
                opacity: 0.8
              }
            }}
          >
            MINDSTREAM
          </Typography>

          {/* Mobile Menu Icon */}
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
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'left',
              }}
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

          {/* Mobile Logo */}
          <MusicNoteIcon sx={{ display: { xs: 'flex', md: 'none' }, mr: 1 }} />
          <Typography
            variant="h5"
            noWrap
            component={Link}
            to="/"
            sx={{
              mr: 2,
              display: { xs: 'flex', md: 'none' },
              flexGrow: 1,
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.2rem',
              color: 'inherit',
              textDecoration: 'none',
            }}
          >
            MS
          </Typography>

          {/* Desktop Navigation */}
          <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' }, ml: 3 }}>
            <Button
              component={Link}
              to="/"
              sx={{ my: 2, color: 'white', display: 'flex', alignItems: 'center' }}
              startIcon={<HomeIcon />}
            >
              Home
            </Button>
            <Button
              component={Link}
              to="/albums"
              sx={{ my: 2, color: 'white', display: 'flex', alignItems: 'center' }}
              startIcon={<AlbumIcon />}
            >
              Albums
            </Button>
            {isAuthenticated && (
              <Button
                component={Link}
                to="/favorites"
                sx={{ my: 2, color: 'white', display: 'flex', alignItems: 'center' }}
                startIcon={<FavoriteIcon />}
              >
                Favorites
              </Button>
            )}
          </Box>

          {/* Search Bar */}
          <Search>
            <SearchIconWrapper>
              <SearchIcon />
            </SearchIconWrapper>
            <StyledInputBase
              placeholder="Search albums…"
              inputProps={{ 'aria-label': 'search' }}
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              onKeyPress={handleSearch}
            />
          </Search>

          {/* User Actions */}
          <Box sx={{ flexGrow: 0, display: 'flex', alignItems: 'center', gap: 1 }}>
            {isAuthenticated ? (
              <>
                <Tooltip title="Dashboard">
                  <IconButton component={Link} to="/dashboard" sx={{ color: 'white' }}>
                    <PersonIcon />
                  </IconButton>
                </Tooltip>
                <Typography variant="body2" sx={{ display: { xs: 'none', sm: 'block' } }}>
                  Hi, {user?.username}
                </Typography>
                <Tooltip title="Logout">
                  <IconButton onClick={handleLogout} sx={{ color: 'white' }}>
                    <LogoutIcon />
                  </IconButton>
                </Tooltip>
              </>
            ) : (
              <>
                <Button
                  component={Link}
                  to="/login"
                  variant="outlined"
                  sx={{ 
                    color: 'white', 
                    borderColor: 'white',
                    '&:hover': {
                      backgroundColor: 'rgba(255, 255, 255, 0.1)',
                      borderColor: 'white'
                    }
                  }}
                  startIcon={<LoginIcon />}
                >
                  Login
                </Button>
                <Button
                  component={Link}
                  to="/register"
                  variant="contained"
                  sx={{ 
                    ml: 1,
                    backgroundColor: 'white',
                    color: '#e53935',
                    '&:hover': {
                      backgroundColor: '#f5f5f5'
                    }
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