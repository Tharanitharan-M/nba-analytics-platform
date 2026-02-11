import React from 'react'
import { AppBar, Toolbar, Typography, IconButton, Box, Button } from '@mui/material'
import MenuIcon from '@mui/icons-material/Menu'
import SportsBasketballIcon from '@mui/icons-material/SportsBasketball'
import LogoutIcon from '@mui/icons-material/Logout'
import { useAuth } from '../../context/AuthContext'
import { useNavigate } from 'react-router-dom'

const Navbar = ({ drawerWidth, handleDrawerToggle }) => {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <AppBar
      position="fixed"
      sx={{
        width: { sm: `calc(100% - ${drawerWidth}px)` },
        ml: { sm: `${drawerWidth}px` },
        background: 'linear-gradient(135deg, #1976d2 0%, #1565c0 100%)',
      }}
    >
      <Toolbar>
        <IconButton
          color="inherit"
          aria-label="open drawer"
          edge="start"
          onClick={handleDrawerToggle}
          sx={{ mr: 2, display: { sm: 'none' } }}
        >
          <MenuIcon />
        </IconButton>
        
        <SportsBasketballIcon sx={{ mr: 2, display: { xs: 'none', sm: 'block' } }} />
        
        <Typography variant="h6" noWrap component="div" sx={{ flexGrow: 1 }}>
          NBA Analytics Platform
        </Typography>
        
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Typography variant="body2">
            Welcome, {user?.username}
          </Typography>
          <Button
            color="inherit"
            startIcon={<LogoutIcon />}
            onClick={handleLogout}
          >
            Logout
          </Button>
        </Box>
      </Toolbar>
    </AppBar>
  )
}

export default Navbar
