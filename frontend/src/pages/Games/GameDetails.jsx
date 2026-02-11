import React, { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import {
  Box,
  Paper,
  Typography,
  CircularProgress,
  Grid,
  Button,
  Divider,
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { gamesAPI } from '../../services/api'
import { toast } from 'react-toastify'

const StatRow = ({ label, homeValue, awayValue }) => (
  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', py: 1.5 }}>
    <Typography variant="h6" sx={{ flex: 1, textAlign: 'right', pr: 2 }}>
      {homeValue}
    </Typography>
    <Typography variant="body1" color="text.secondary" sx={{ flex: 1, textAlign: 'center' }}>
      {label}
    </Typography>
    <Typography variant="h6" sx={{ flex: 1, pl: 2 }}>
      {awayValue}
    </Typography>
  </Box>
)

const GameDetails = () => {
  const { gameId } = useParams()
  const navigate = useNavigate()
  const [game, setGame] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchGameData()
  }, [gameId])

  const fetchGameData = async () => {
    try {
      const response = await gamesAPI.getGameById(gameId)
      setGame(response.data.data)
    } catch (error) {
      toast.error('Failed to load game data')
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

  if (!game) return null

  return (
    <Box>
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/games')}
        sx={{ mb: 3 }}
      >
        Back to Games
      </Button>

      <Paper sx={{ p: 4, mb: 3 }}>
        <Typography variant="body2" color="text.secondary" align="center" gutterBottom>
          {new Date(game.gameDate).toLocaleDateString()} - {game.seasonId}
        </Typography>

        <Grid container spacing={4} alignItems="center" sx={{ mt: 2 }}>
          <Grid item xs={5} textAlign="center">
            <Typography variant="h5" fontWeight="bold" gutterBottom>
              {game.teamNameHome}
            </Typography>
            <Typography variant="h2" fontWeight="bold" color="primary.main">
              {game.ptsHome}
            </Typography>
            <Typography
              variant="h6"
              color={game.wlHome === 'W' ? 'success.main' : 'error.main'}
              sx={{ mt: 1 }}
            >
              {game.wlHome === 'W' ? 'WIN' : 'LOSS'}
            </Typography>
          </Grid>

          <Grid item xs={2} textAlign="center">
            <Typography variant="h6" color="text.secondary">
              VS
            </Typography>
          </Grid>

          <Grid item xs={5} textAlign="center">
            <Typography variant="h5" fontWeight="bold" gutterBottom>
              {game.teamNameAway}
            </Typography>
            <Typography variant="h2" fontWeight="bold" color="primary.main">
              {game.ptsAway}
            </Typography>
            <Typography
              variant="h6"
              color={game.wlAway === 'W' ? 'success.main' : 'error.main'}
              sx={{ mt: 1 }}
            >
              {game.wlAway === 'W' ? 'WIN' : 'LOSS'}
            </Typography>
          </Grid>
        </Grid>
      </Paper>

      <Paper sx={{ p: 4 }}>
        <Typography variant="h6" gutterBottom fontWeight="bold" align="center" sx={{ mb: 3 }}>
          Box Score
        </Typography>

        <Divider sx={{ mb: 2 }} />

        <StatRow
          label="Field Goals Made"
          homeValue={game.fgmHome}
          awayValue={game.fgmAway}
        />
        <StatRow
          label="Field Goal %"
          homeValue={`${(game.fgPctHome * 100).toFixed(1)}%`}
          awayValue={`${(game.fgPctAway * 100).toFixed(1)}%`}
        />
        <Divider sx={{ my: 1 }} />
        <StatRow
          label="3-Pointers Made"
          homeValue={game.fg3mHome}
          awayValue={game.fg3mAway}
        />
        <StatRow
          label="3-Point %"
          homeValue={`${(game.fg3PctHome * 100).toFixed(1)}%`}
          awayValue={`${(game.fg3PctAway * 100).toFixed(1)}%`}
        />
        <Divider sx={{ my: 1 }} />
        <StatRow
          label="Free Throws Made"
          homeValue={game.ftmHome}
          awayValue={game.ftmAway}
        />
        <StatRow
          label="Free Throw %"
          homeValue={`${(game.ftPctHome * 100).toFixed(1)}%`}
          awayValue={`${(game.ftPctAway * 100).toFixed(1)}%`}
        />
        <Divider sx={{ my: 1 }} />
        <StatRow
          label="Rebounds"
          homeValue={game.rebHome}
          awayValue={game.rebAway}
        />
        <StatRow
          label="Assists"
          homeValue={game.astHome}
          awayValue={game.astAway}
        />
        <StatRow
          label="Steals"
          homeValue={game.stlHome}
          awayValue={game.stlAway}
        />
        <StatRow
          label="Blocks"
          homeValue={game.blkHome}
          awayValue={game.blkAway}
        />
        <StatRow
          label="Turnovers"
          homeValue={game.tovHome}
          awayValue={game.tovAway}
        />
      </Paper>
    </Box>
  )
}

export default GameDetails
