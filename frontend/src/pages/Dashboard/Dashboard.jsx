import React, { useState, useEffect } from 'react'
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  CircularProgress,
  Paper,
} from '@mui/material'
import {
  SportsBasketball,
  People,
  Groups,
  TrendingUp,
} from '@mui/icons-material'
import { dashboardAPI, gamesAPI } from '../../services/api'
import { toast } from 'react-toastify'
import {
  BarChart,
  Bar,
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts'

const StatCard = ({ title, value, icon, color }) => (
  <Card
    sx={{
      height: '100%',
      background: `linear-gradient(135deg, ${color}15 0%, ${color}05 100%)`,
      border: `1px solid ${color}30`,
    }}
  >
    <CardContent>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box>
          <Typography color="text.secondary" gutterBottom variant="body2">
            {title}
          </Typography>
          <Typography variant="h4" component="div" fontWeight="bold">
            {value?.toLocaleString() || '0'}
          </Typography>
        </Box>
        <Box
          sx={{
            backgroundColor: `${color}20`,
            borderRadius: '12px',
            p: 1.5,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}
        >
          {icon}
        </Box>
      </Box>
    </CardContent>
  </Card>
)

const Dashboard = () => {
  const [stats, setStats] = useState(null)
  const [recentGames, setRecentGames] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchDashboardData()
  }, [])

  const fetchDashboardData = async () => {
    try {
      const [statsRes, gamesRes] = await Promise.all([
        dashboardAPI.getStats(),
        gamesAPI.getRecentGames(0, 10),
      ])

      setStats(statsRes.data.data)
      setRecentGames(gamesRes.data.data.content)
    } catch (error) {
      toast.error('Failed to load dashboard data')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }

  // Prepare data for charts
  const gameScoresData = recentGames.slice(0, 7).map((game) => ({
    game: `${game.teamAbbreviationHome} vs ${game.teamAbbreviationAway}`,
    home: game.ptsHome,
    away: game.ptsAway,
  }))

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
        <CircularProgress size={60} />
      </Box>
    )
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom fontWeight="bold" sx={{ mb: 3 }}>
        Dashboard
      </Typography>

      {/* Stats Cards */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Total Games"
            value={stats?.totalGames}
            icon={<SportsBasketball sx={{ fontSize: 40, color: '#1976d2' }} />}
            color="#1976d2"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Total Teams"
            value={stats?.totalTeams}
            icon={<Groups sx={{ fontSize: 40, color: '#f50057' }} />}
            color="#f50057"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Active Players"
            value={stats?.activePlayers}
            icon={<People sx={{ fontSize: 40, color: '#4caf50' }} />}
            color="#4caf50"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Avg Points/Game"
            value={stats?.averagePointsPerGame?.toFixed(1)}
            icon={<TrendingUp sx={{ fontSize: 40, color: '#ff9800' }} />}
            color="#ff9800"
          />
        </Grid>
      </Grid>

      {/* Charts */}
      <Grid container spacing={3}>
        <Grid item xs={12} lg={8}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom fontWeight="bold">
              Recent Game Scores
            </Typography>
            <ResponsiveContainer width="100%" height={350}>
              <BarChart data={gameScoresData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#444" />
                <XAxis 
                  dataKey="game" 
                  angle={-45} 
                  textAnchor="end" 
                  height={100}
                  tick={{ fontSize: 12 }}
                />
                <YAxis />
                <Tooltip 
                  contentStyle={{ 
                    backgroundColor: '#132f4c', 
                    border: '1px solid #1976d2' 
                  }} 
                />
                <Legend />
                <Bar dataKey="home" fill="#1976d2" name="Home Score" />
                <Bar dataKey="away" fill="#f50057" name="Away Score" />
              </BarChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>

        <Grid item xs={12} lg={4}>
          <Paper sx={{ p: 3, height: '100%' }}>
            <Typography variant="h6" gutterBottom fontWeight="bold">
              Platform Statistics
            </Typography>
            <Box sx={{ mt: 3 }}>
              <Box sx={{ mb: 3 }}>
                <Typography variant="body2" color="text.secondary">
                  Current Season
                </Typography>
                <Typography variant="h6">
                  {stats?.currentSeason || 'N/A'}
                </Typography>
              </Box>
              <Box sx={{ mb: 3 }}>
                <Typography variant="body2" color="text.secondary">
                  Total Players
                </Typography>
                <Typography variant="h6">
                  {stats?.totalPlayers?.toLocaleString()}
                </Typography>
              </Box>
              <Box sx={{ mb: 3 }}>
                <Typography variant="body2" color="text.secondary">
                  Play-by-Play Records
                </Typography>
                <Typography variant="h6">
                  {stats?.totalPlayByPlayRecords?.toLocaleString()}
                </Typography>
              </Box>
              <Box>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  Data Coverage
                </Typography>
                <Typography variant="caption" color="text.secondary">
                  Historical data from 1946-47 season to present
                </Typography>
              </Box>
            </Box>
          </Paper>
        </Grid>

        {/* Recent Games Table */}
        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom fontWeight="bold">
              Recent Games
            </Typography>
            <Box sx={{ overflowX: 'auto' }}>
              {recentGames.slice(0, 5).map((game, index) => (
                <Box
                  key={game.gameId || index}
                  sx={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    p: 2,
                    borderBottom: '1px solid',
                    borderColor: 'divider',
                    '&:hover': {
                      backgroundColor: 'rgba(25, 118, 210, 0.08)',
                    },
                  }}
                >
                  <Box sx={{ flex: 1 }}>
                    <Typography variant="body2" color="text.secondary">
                      {new Date(game.gameDate).toLocaleDateString()}
                    </Typography>
                  </Box>
                  <Box sx={{ flex: 2, display: 'flex', alignItems: 'center', gap: 2 }}>
                    <Typography variant="body1" sx={{ flex: 1, textAlign: 'right' }}>
                      {game.teamNameHome}
                    </Typography>
                    <Typography 
                      variant="h6" 
                      fontWeight="bold"
                      color={game.wlHome === 'W' ? 'success.main' : 'text.secondary'}
                    >
                      {game.ptsHome}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">vs</Typography>
                    <Typography 
                      variant="h6" 
                      fontWeight="bold"
                      color={game.wlAway === 'W' ? 'success.main' : 'text.secondary'}
                    >
                      {game.ptsAway}
                    </Typography>
                    <Typography variant="body1" sx={{ flex: 1 }}>
                      {game.teamNameAway}
                    </Typography>
                  </Box>
                </Box>
              ))}
            </Box>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  )
}

export default Dashboard
