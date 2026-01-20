import React, { useState, useMemo, createContext } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import CircularProgress from '@mui/material/CircularProgress';
import Box from '@mui/material/Box';
import { AuthProvider, useAuth } from './context/AuthContext';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import AlbumsPage from './pages/AlbumsPage';
import AlbumDetailPage from './pages/AlbumDetailPage';
import FavoritesPage from './pages/FavoritesPage';
import DashboardPage from './pages/DashboardPage';
import NotFoundPage from './pages/NotFoundPage';
import Navbar from './components/Layout/Navbar';
import Footer from './components/Layout/Footer';

export const ColorModeContext = createContext({ toggleColorMode: () => {} });

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();
  
  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh' }}>
        <CircularProgress sx={{ color: '#e53935' }} />
      </Box>
    );
  }
  
  return isAuthenticated ? children : <Navigate to="/login" />;
};

function App() {
  const [mode, setMode] = useState('light');

  const colorMode = useMemo(
    () => ({
      toggleColorMode: () => {
        setMode((prevMode) => (prevMode === 'light' ? 'dark' : 'light'));
      },
    }),
    [],
  );

  const theme = useMemo(
    () =>
      createTheme({
        palette: {
          mode,
          ...(mode === 'light'
            ? {
                primary: {
                  main: '#e11d48',
                  light: '#fb7185',
                  dark: '#be123c',
                  contrastText: '#ffffff',
                },
                secondary: {
                  main: '#0f172a',
                  light: '#334155',
                  dark: '#020617',
                },
                background: {
                  default: '#f8fafc',
                  paper: '#ffffff',
                },
                text: {
                  primary: '#0f172a',
                  secondary: '#64748b',
                },
              }
            : {
                primary: {
                  main: '#f43f5e',
                  light: '#fb7185',
                  dark: '#e11d48',
                  contrastText: '#ffffff',
                },
                secondary: {
                  main: '#f8fafc',
                  light: '#f1f5f9',
                  dark: '#cbd5e1',
                },
                background: {
                  default: '#0f172a',
                  paper: '#1e293b',
                },
                text: {
                  primary: '#f1f5f9',
                  secondary: '#94a3b8',
                },
              }),
        },
        typography: {
          fontFamily: '"Inter", "Roboto", "Helvetica", "Arial", sans-serif',
          h1: {
            fontFamily: '"Poppins", sans-serif',
            fontWeight: 800,
            fontSize: '3.5rem',
            letterSpacing: '-0.02em',
            lineHeight: 1.2,
          },
          h2: {
            fontFamily: '"Poppins", sans-serif',
            fontWeight: 700,
            letterSpacing: '-0.01em',
          },
          h3: {
            fontFamily: '"Poppins", sans-serif',
            fontWeight: 700,
          },
          h4: {
            fontFamily: '"Poppins", sans-serif',
            fontWeight: 600,
          },
          h5: {
            fontFamily: '"Poppins", sans-serif',
            fontWeight: 600,
          },
          h6: {
            fontFamily: '"Poppins", sans-serif',
            fontWeight: 600,
          },
          button: {
            fontFamily: '"Poppins", sans-serif',
            fontWeight: 600,
            textTransform: 'none',
          },
        },
        shape: {
          borderRadius: 16,
        },
        components: {
          MuiButton: {
            styleOverrides: {
              root: {
                borderRadius: '12px',
                padding: '10px 24px',
                boxShadow: 'none',
                fontSize: '0.95rem',
                transition: 'all 0.2s ease-in-out',
                '&:hover': {
                  transform: 'translateY(-2px)',
                  boxShadow: '0 4px 12px rgba(225, 29, 72, 0.2)',
                },
              },
              containedPrimary: {
                background: 'linear-gradient(135deg, #e11d48 0%, #be123c 100%)',
              },
            },
          },
          MuiCard: {
            styleOverrides: {
              root: {
                borderRadius: '20px',
                boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03)',
                transition: 'all 0.3s ease',
                '&:hover': {
                  transform: 'translateY(-5px)',
                  boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)',
                },
              },
            },
          },
          MuiPaper: {
            styleOverrides: {
              root: {
                backgroundImage: 'none',
              },
              rounded: {
                borderRadius: '20px',
              },
              elevation1: {
                boxShadow: '0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)',
              },
              elevation2: {
                boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)',
              },
              elevation3: {
                boxShadow: '0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)',
              },
            },
          },
          MuiAppBar: {
            styleOverrides: {
              root: {
                background: mode === 'light' ? 'rgba(255, 255, 255, 0.8)' : 'rgba(15, 23, 42, 0.8)',
                backdropFilter: 'blur(12px)',
                borderBottom: `1px solid ${mode === 'light' ? 'rgba(0,0,0,0.05)' : 'rgba(255,255,255,0.05)'}`,
                color: mode === 'light' ? '#0f172a' : '#f1f5f9',
                boxShadow: 'none',
              },
            },
          },
          MuiCssBaseline: {
            styleOverrides: {
              body: {
                scrollbarColor: mode === 'dark' ? '#6b7280 #1e293b' : '#94a3b8 #f1f5f9',
                '&::-webkit-scrollbar, & *::-webkit-scrollbar': {
                  backgroundColor: mode === 'dark' ? '#1e293b' : '#f1f5f9',
                },
                '&::-webkit-scrollbar-thumb, & *::-webkit-scrollbar-thumb': {
                  borderRadius: 8,
                  backgroundColor: mode === 'dark' ? '#6b7280' : '#94a3b8',
                  minHeight: 24,
                  border: `3px solid ${mode === 'dark' ? '#1e293b' : '#f1f5f9'}`,
                },
                '&::-webkit-scrollbar-thumb:focus, & *::-webkit-scrollbar-thumb:focus': {
                  backgroundColor: mode === 'dark' ? '#9ca3af' : '#64748b',
                },
                '&::-webkit-scrollbar-thumb:active, & *::-webkit-scrollbar-thumb:active': {
                  backgroundColor: mode === 'dark' ? '#9ca3af' : '#64748b',
                },
                '&::-webkit-scrollbar-thumb:hover, & *::-webkit-scrollbar-thumb:hover': {
                  backgroundColor: mode === 'dark' ? '#9ca3af' : '#64748b',
                },
                '&::-webkit-scrollbar-corner, & *::-webkit-scrollbar-corner': {
                  backgroundColor: mode === 'dark' ? '#1e293b' : '#f1f5f9',
                },
              },
            },
          },
        },
      }),
    [mode],
  );

  return (
    <ColorModeContext.Provider value={colorMode}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <AuthProvider>
          <Router>
            <div className="App" style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh', background: theme.palette.background.default }}>
              <Navbar />
              <main style={{ minHeight: '80vh', flex: 1 }}>
                <Routes>
                  <Route path="/" element={<HomePage />} />
                  <Route path="/login" element={<LoginPage />} />
                  <Route path="/register" element={<RegisterPage />} />
                  <Route path="/albums" element={<AlbumsPage />} />
                  <Route path="/albums/:id" element={<AlbumDetailPage />} />
                  <Route 
                    path="/favorites" 
                    element={
                      <ProtectedRoute>
                        <FavoritesPage />
                      </ProtectedRoute>
                    } 
                  />
                  <Route 
                    path="/dashboard" 
                    element={
                      <ProtectedRoute>
                        <DashboardPage />
                      </ProtectedRoute>
                    } 
                  />
                  <Route path="*" element={<NotFoundPage />} />
                </Routes>
              </main>
              <Footer />
            </div>
          </Router>
        </AuthProvider>
      </ThemeProvider>
    </ColorModeContext.Provider>
  );
}

export default App;