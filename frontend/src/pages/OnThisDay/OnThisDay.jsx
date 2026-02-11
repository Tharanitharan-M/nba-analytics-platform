import React, { useState, useEffect } from 'react'
import {
  Box,
  Paper,
  Typography,
  CircularProgress,
  Card,
  CardContent,
  Grid,
  Button,
} from '@mui/material'
import CalendarTodayIcon from '@mui/icons-material/CalendarToday'
import SportsBasketballIcon from '@mui/icons-material/SportsBasketball'
import { gamesAPI } from '../../services/api'
import { toast } from 'react-toastify'

const OnThisDay = () => {
  const [games, setGames] = useState([])
  const [loading, setLoading] = useState(true)
  const today = new Date()
  const month = today.getMonth() + 1
  const day = today.getDate()

  useEffect(() => {
    fetchGamesOnThisDay()
  }, [])

  const fetchGamesOnThisDay = async () => {
    try {
      const response = await gamesAPI.getGamesOnThisDay(month, day)
      setGames(response.data.data)
    } catch (error) {
      toast.error('Failed to load historical games')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
        <CircularProgress size={60} />
      </Box>
    )
  }

  return (
    <Box>
      <Paper
        sx={{
          p: 4,
          mb: 4,
          background: 'linear-gradient(135deg, rgba(25, 118, 210, 0.1) 0%, rgba(21, 101, 192, 0.1) 100%)',
        }}
      >
        <Box textAlign="center">
          <CalendarTodayIcon sx={{ fontSize: 60, color: 'primary.main', mb: 2 }} />
          <Typography variant="h3" gutterBottom fontWeight="bold">
            On This Day in NBA History
          </Typography>
          <Typography variant="h5" color="text.secondary">
            {today.toLocaleDateString('en-US', { month: 'long', day: 'numeric' })}
          </Typography>
        </Box>
      </Paper>

      {games.length === 0 ? (
        <Paper sx={{ p: 8, textAlign: 'center' }}>
          <SportsBasketballIcon sx={{ fontSize: 80, color: 'text.secondary', mb: 2 }} />
          <Typography variant="h6" color="text.secondary">
            No games found for this date
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
            Check back another day to see historical NBA games!
          </Typography>
        </Paper>
      ) : (
        <>
          <Typography variant="h6" gutterBottom sx={{ mb: 3 }}>
            {games.length} game{games.length !== 1 ? 's' : ''} played on this day throughout NBA history
          </Typography>

          <Grid container spacing={3}>
            {games.map((game, index) => (
              <Grid item xs={12} md={6} lg={4} key={game.gameId || index}>
                <Card
                  sx={{
                    height: '100%',
                    transition: 'all 0.3s',
                    '&:hover': {
                      transform: 'translateY(-4px)',
                      boxShadow: 6,
                    },
                  }}
                >
                  <CardContent>
                    <Typography variant="caption" color="text.secondary" gutterBottom display="block">
                      {new Date(game.gameDate).getFullYear()} - {game.seasonId}
                    </Typography>

                    <Box sx={{ my: 2 }}>
                      <Box
                        sx={{
                          display: 'flex',
                          justifyContent: 'space-between',
                          alignItems: 'center',
                          mb: 1,
                        }}
                      >
                        <Typography variant="body1" fontWeight="bold">
                          {game.teamNameHome}
                        </Typography>
                        <Typography
                          variant="h6"
                          fontWeight="bold"
                          color={game.wlHome === 'W' ? 'success.main' : 'text.primary'}
                        >
                          {game.ptsHome}
                        </Typography>
                      </Box>

                      <Box
                        sx={{
                          display: 'flex',
                          justifyContent: 'space-between',
                          alignItems: 'center',
                        }}
                      >
                        <Typography variant="body1" fontWeight="bold">
                          {game.teamNameAway}
                        </Typography>
                        <Typography
                          variant="h6"
                          fontWeight="bold"
                          color={game.wlAway === 'W' ? 'success.main' : 'text.primary'}
                        >
                          {game.ptsAway}
                        </Typography>
                      </Box>
                    </Box>

                    <Typography variant="caption" color="text.secondary">
                      {game.wlHome === 'W'
                        ? `${game.teamAbbreviationHome} won by ${Math.abs(
                            game.ptsHome - game.ptsAway
                          )} points`
                        : `${game.teamAbbreviationAway} won by ${Math.abs(
                            game.ptsAway - game.ptsHome
                          )} points`}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </>
      )}
    </Box>
  )
}

export default OnThisDay
