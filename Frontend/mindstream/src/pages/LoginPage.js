import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Box,
  Alert,
  IconButton,
  InputAdornment,
  CircularProgress,
  Divider,
  Grid
} from '@mui/material';
import {
  Lock as LockIcon,
  Person as PersonIcon,
  Visibility as VisibilityIcon,
  VisibilityOff as VisibilityOffIcon,
  Login as LoginIcon,
  ArrowBack as ArrowBackIcon,
  MusicNote as MusicNoteIcon
} from '@mui/icons-material';
import { useAuth } from '../context/AuthContext';

const LoginPage = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const { login, error } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await login(formData.username, formData.password);
      navigate('/dashboard');
    } catch (err) {
      // Error is handled in AuthContext
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="sm" sx={{ py: 8 }}>
      <Box sx={{ mb: 4, textAlign: 'center' }}>
        <Button
          component={Link}
          to="/"
          startIcon={<ArrowBackIcon />}
          sx={{ mb: 2 }}
        >
          Back to Home
        </Button>
        <MusicNoteIcon sx={{ fontSize: 60, color: '#e53935', mb: 2 }} />
        <Typography variant="h3" component="h1" gutterBottom>
          Welcome Back
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Sign in to your MindStream collection
        </Typography>
      </Box>

      <Paper elevation={3} sx={{ p: 4 }}>
        {error && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {error}
          </Alert>
        )}

        <form onSubmit={handleSubmit}>
          <TextField
            fullWidth
            label="Username"
            name="username"
            value={formData.username}
            onChange={handleChange}
            margin="normal"
            variant="outlined"
            required
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <PersonIcon color="action" />
                </InputAdornment>
              ),
            }}
            disabled={loading}
          />

          <TextField
            fullWidth
            label="Password"
            name="password"
            type={showPassword ? 'text' : 'password'}
            value={formData.password}
            onChange={handleChange}
            margin="normal"
            variant="outlined"
            required
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <LockIcon color="action" />
                </InputAdornment>
              ),
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton
                    onClick={() => setShowPassword(!showPassword)}
                    edge="end"
                  >
                    {showPassword ? <VisibilityOffIcon /> : <VisibilityIcon />}
                  </IconButton>
                </InputAdornment>
              ),
            }}
            disabled={loading}
          />

          <Button
            fullWidth
            type="submit"
            variant="contained"
            size="large"
            disabled={loading}
            sx={{
              mt: 3,
              mb: 2,
              py: 1.5,
              backgroundColor: '#e53935',
              '&:hover': {
                backgroundColor: '#c62828'
              }
            }}
            startIcon={loading ? <CircularProgress size={20} color="inherit" /> : <LoginIcon />}
          >
            {loading ? 'Signing In...' : 'Sign In'}
          </Button>
        </form>

        <Divider sx={{ my: 3 }}>
          <Typography variant="body2" color="text.secondary">
            New to the collection?
          </Typography>
        </Divider>

        <Grid container spacing={2}>
          <Grid item xs={12}>
            <Button
              fullWidth
              component={Link}
              to="/register"
              variant="outlined"
              sx={{
                borderColor: '#e53935',
                color: '#e53935',
                '&:hover': {
                  borderColor: '#c62828',
                  backgroundColor: 'rgba(229, 57, 53, 0.04)'
                }
              }}
            >
              Create New Account
            </Button>
          </Grid>
        </Grid>

        <Box sx={{ mt: 3, textAlign: 'center' }}>
          <Typography variant="body2" color="text.secondary">
            Demo Credentials:
          </Typography>
          <Typography variant="body2" color="text.secondary" fontFamily="monospace">
            Username: testuser | Password: password123
          </Typography>
        </Box>
      </Paper>
    </Container>
  );
};

export default LoginPage;