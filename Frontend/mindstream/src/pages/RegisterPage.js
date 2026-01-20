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
  Grid,
  Stepper,
  Step,
  StepLabel
} from '@mui/material';
import {
  Person as PersonIcon,
  Email as EmailIcon,
  Lock as LockIcon,
  Visibility as VisibilityIcon,
  VisibilityOff as VisibilityOffIcon,
  HowToReg as HowToRegIcon,
  ArrowBack as ArrowBackIcon,
  MusicNote as MusicNoteIcon
} from '@mui/icons-material';
import { authService } from '../services/authService';

const steps = ['Account Details', 'Personal Info', 'Complete'];

const RegisterPage = () => {
  const [activeStep, setActiveStep] = useState(0);
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    fullName: ''
  });
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
    if (error) setError('');
  };

  const validateStep = () => {
    if (activeStep === 0) {
      if (!formData.username || !formData.email || !formData.password) {
        setError('Please fill all required fields');
        return false;
      }
      if (formData.password.length < 6) {
        setError('Password must be at least 6 characters');
        return false;
      }
    }
    if (activeStep === 1) {
      if (!formData.fullName) {
        setError('Full name is required');
        return false;
      }
      if (formData.password !== formData.confirmPassword) {
        setError('Passwords do not match');
        return false;
      }
    }
    return true;
  };

  const handleNext = () => {
    if (validateStep()) {
      setActiveStep((prevStep) => prevStep + 1);
      setError('');
    }
  };

  const handleBack = () => {
    setActiveStep((prevStep) => prevStep - 1);
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateStep()) return;

    setLoading(true);
    setError('');

    try {
      const userData = {
        username: formData.username,
        email: formData.email,
        password: formData.password,
        fullName: formData.fullName
      };

      await authService.register(userData);
      setSuccess('Registration successful! Redirecting to login...');
      
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (err) {
      setError(err.message || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const getStepContent = (step) => {
    switch (step) {
      case 0:
        return (
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Username *"
                name="username"
                value={formData.username}
                onChange={handleChange}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <PersonIcon color="action" />
                    </InputAdornment>
                  ),
                }}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Email *"
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <EmailIcon color="action" />
                    </InputAdornment>
                  ),
                }}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Password *"
                name="password"
                type={showPassword ? 'text' : 'password'}
                value={formData.password}
                onChange={handleChange}
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
              />
              <Typography variant="caption" color="text.secondary">
                Must be at least 6 characters
              </Typography>
            </Grid>
          </Grid>
        );
      case 1:
        return (
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Full Name *"
                name="fullName"
                value={formData.fullName}
                onChange={handleChange}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <PersonIcon color="action" />
                    </InputAdornment>
                  ),
                }}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Confirm Password *"
                name="confirmPassword"
                type={showConfirmPassword ? 'text' : 'password'}
                value={formData.confirmPassword}
                onChange={handleChange}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <LockIcon color="action" />
                    </InputAdornment>
                  ),
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                        edge="end"
                      >
                        {showConfirmPassword ? <VisibilityOffIcon /> : <VisibilityIcon />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />
            </Grid>
          </Grid>
        );
      case 2:
        return (
          <Box textAlign="center">
            <HowToRegIcon sx={{ fontSize: 60, color: '#4caf50', mb: 2 }} />
            <Typography variant="h6" gutterBottom>
              Ready to join the collection!
            </Typography>
            <Typography variant="body2" color="text.secondary" paragraph>
              You'll be able to:
            </Typography>
            <Box textAlign="left" sx={{ mb: 3 }}>
              <Typography variant="body2" color="text.secondary">
                • Browse White Stripes albums
              </Typography>
              <Typography variant="body2" color="text.secondary">
                • Save your favorite albums
              </Typography>
              <Typography variant="body2" color="text.secondary">
                • Track chart positions
              </Typography>
              <Typography variant="body2" color="text.secondary">
                • Create personal collections
              </Typography>
            </Box>
          </Box>
        );
      default:
        return 'Unknown step';
    }
  };

  return (
    <Container maxWidth="md" sx={{ py: 8 }}>
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
          Join the Collection
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Create your White Stripes album collection account
        </Typography>
      </Box>

      <Paper elevation={3} sx={{ p: 4 }}>
        <Stepper activeStep={activeStep} sx={{ mb: 4 }}>
          {steps.map((label) => (
            <Step key={label}>
              <StepLabel>{label}</StepLabel>
            </Step>
          ))}
        </Stepper>

        {(error || success) && (
          <Alert 
            severity={success ? "success" : "error"} 
            sx={{ mb: 3 }}
            onClose={() => {
              setError('');
              setSuccess('');
            }}
          >
            {error || success}
          </Alert>
        )}

        <form onSubmit={activeStep === steps.length - 1 ? handleSubmit : undefined}>
          <Box sx={{ mt: 2, mb: 4 }}>
            {getStepContent(activeStep)}
          </Box>

          <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
            <Button
              disabled={activeStep === 0}
              onClick={handleBack}
              sx={{ mr: 1 }}
            >
              Back
            </Button>
            <Box>
              {activeStep === steps.length - 1 ? (
                <Button
                  type="submit"
                  variant="contained"
                  disabled={loading}
                  startIcon={loading ? <CircularProgress size={20} color="inherit" /> : <HowToRegIcon />}
                  sx={{
                    backgroundColor: '#e53935',
                    '&:hover': {
                      backgroundColor: '#c62828'
                    }
                  }}
                >
                  {loading ? 'Creating Account...' : 'Create Account'}
                </Button>
              ) : (
                <Button
                  variant="contained"
                  onClick={handleNext}
                  sx={{
                    backgroundColor: '#e53935',
                    '&:hover': {
                      backgroundColor: '#c62828'
                    }
                  }}
                >
                  Next
                </Button>
              )}
            </Box>
          </Box>
        </form>

        <Divider sx={{ my: 3 }}>
          <Typography variant="body2" color="text.secondary">
            Already have an account?
          </Typography>
        </Divider>

        <Button
          fullWidth
          component={Link}
          to="/login"
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
          Sign In Instead
        </Button>
      </Paper>
    </Container>
  );
};

export default RegisterPage;