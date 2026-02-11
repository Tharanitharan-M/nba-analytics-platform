import React from 'react'
import { Drawer, Box, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Divider, Toolbar } from '@mui/material'
import { useNavigate, useLocation } from 'react-router-dom'
import DashboardIcon from '@mui/icons-material/Dashboard'
import GroupsIcon from '@mui/icons-material/Groups'
import PersonIcon from '@mui/icons-material/Person'
import SportsBasketballIcon from '@mui/icons-material/SportsBasketball'
import CompareArrowsIcon from '@mui/icons-material/CompareArrows'
import HistoryIcon from '@mui/icons-material/History'

const menuItems = [
  { text: 'Dashboard', icon: <DashboardIcon />, path: '/dashboard' },
  { text: 'Teams', icon: <GroupsIcon />, path: '/teams' },
  { text: 'Players', icon: <PersonIcon />, path: '/players' },
  { text: 'Games', icon: <SportsBasketballIcon />, path: '/games' },
  { text: 'Compare', icon: <CompareArrowsIcon />, path: '/compare' },
  { text: 'On This Day', icon: <HistoryIcon />, path: '/on-this-day' },
]

const Sidebar = ({ drawerWidth, mobileOpen, handleDrawerToggle }) => {
  const navigate = useNavigate()
  const location = useLocation()

  const drawer = (
    <Box>
      <Toolbar sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
        <SportsBasketballIcon sx={{ mr: 1, fontSize: 32, color: 'primary.main' }} />
      </Toolbar>
      <Divider />
      <List>
        {menuItems.map((item) => (
          <ListItem key={item.text} disablePadding>
            <ListItemButton
              selected={location.pathname === item.path}
              onClick={() => {
                navigate(item.path)
                if (mobileOpen) handleDrawerToggle()
              }}
              sx={{
                '&.Mui-selected': {
                  backgroundColor: 'rgba(25, 118, 210, 0.2)',
                  '&:hover': {
                    backgroundColor: 'rgba(25, 118, 210, 0.3)',
                  },
                },
              }}
            >
              <ListItemIcon sx={{ color: location.pathname === item.path ? 'primary.main' : 'inherit' }}>
                {item.icon}
              </ListItemIcon>
              <ListItemText primary={item.text} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Box>
  )

  return (
    <Box
      component="nav"
      sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}
    >
      {/* Mobile drawer */}
      <Drawer
        variant="temporary"
        open={mobileOpen}
        onClose={handleDrawerToggle}
        ModalProps={{ keepMounted: true }}
        sx={{
          display: { xs: 'block', sm: 'none' },
          '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
        }}
      >
        {drawer}
      </Drawer>
      
      {/* Desktop drawer */}
      <Drawer
        variant="permanent"
        sx={{
          display: { xs: 'none', sm: 'block' },
          '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
        }}
        open
      >
        {drawer}
      </Drawer>
    </Box>
  )
}

export default Sidebar
