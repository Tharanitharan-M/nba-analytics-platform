import React, { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import {
  Box,
  Paper,
  Typography,
  CircularProgress,
  Grid,
  Card,
  CardContent,
  Button,
  Chip,
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { playersAPI } from '../../services/api'
import { toast } from 'react-toastify'

const PlayerDetails = () => {
  const { playerId } = useParams()
  const navigate = useNavigate()
  const [player, setPlayer] = useState(null)
  const [playerDetails, setPlayerDetails] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchPlayerData()
  }, [playerId])

  const fetchPlayerData = async () => {
    try {
      const [playerRes, detailsRes] = await Promise.all([
        playersAPI.getPlayerById(playerId),
        playersAPI.getPlayerDetails(playerId),
      ])

      setPlayer(playerRes.data.data)
      setPlayerDetails(detailsRes.data.data)
    } catch (error) {
      toast.error('Failed to load player data')
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
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/players')}
        sx={{ mb: 3 }}
      >
        Back to Players
      </Button>

      <Paper sx={{ p: 4, mb: 3 }}>
        <Box textAlign="center">
          <Typography variant="h3" fontWeight="bold" gutterBottom>
            {player?.fullName}
          </Typography>
          {player?.isActive === 1 && (
            <Chip label="Active Player" color="success" sx={{ mb: 2 }} />
          )}
        </Box>
      </Paper>

      {playerDetails && (
        <>
          <Paper sx={{ p: 3, mb: 3 }}>
            <Typography variant="h6" gutterBottom fontWeight="bold">
              Personal Information
            </Typography>
            <Grid container spacing={3} sx={{ mt: 1 }}>
              <Grid item xs={12} sm={6} md={4}>
                <Typography color="text.secondary" variant="body2">
                  Position
                </Typography>
                <Typography variant="h6">
                  {playerDetails.position || 'N/A'}
                </Typography>
              </Grid>
              <Grid item xs={12} sm={6} md={4}>
                <Typography color="text.secondary" variant="body2">
                  Jersey Number
                </Typography>
                <Typography variant="h6">
                  {playerDetails.jersey || 'N/A'}
                </Typography>
              </Grid>
              <Grid item xs={12} sm={6} md={4}>
                <Typography color="text.secondary" variant="body2">
                  Height
                </Typography>
                <Typography variant="h6">
                  {playerDetails.height || 'N/A'}
                </Typography>
              </Grid>
              <Grid item xs={12} sm={6} md={4}>
                <Typography color="text.secondary" variant="body2">
                  Weight
                </Typography>
                <Typography variant="h6">
                  {playerDetails.weight ? `${playerDetails.weight} lbs` : 'N/A'}
                </Typography>
              </Grid>
              <Grid item xs={12} sm={6} md={4}>
                <Typography color="text.secondary" variant="body2">
                  Country
                </Typography>
                <Typography variant="h6">
                  {playerDetails.country || 'N/A'}
                </Typography>
              </Grid>
              <Grid item xs={12} sm={6} md={4}>
                <Typography color="text.secondary" variant="body2">
                  School
                </Typography>
                <Typography variant="h6">
                  {playerDetails.school || 'N/A'}
                </Typography>
              </Grid>
            </Grid>
          </Paper>

          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom fontWeight="bold">
              Career Information
            </Typography>
            <Grid container spacing={3} sx={{ mt: 1 }}>
              <Grid item xs={12} sm={6} md={4}>
                <Typography color="text.secondary" variant="body2">
                  Current Team
                </Typography>
                <Typography variant="h6">
                  {playerDetails.teamName || 'Free Agent'}
                </Typography>
              </Grid>
              <Grid item xs={12} sm={6} md={4}>
                <Typography color="text.secondary" variant="body2">
                  Years Pro
                </Typography>
                <Typography variant="h6">
                  {playerDetails.seasonExp !== null ? Math.floor(playerDetails.seasonExp) : 'N/A'}
                </Typography>
              </Grid>
              <Grid item xs={12} sm={6} md={4}>
                <Typography color="text.secondary" variant="body2">
                  NBA Career
                </Typography>
                <Typography variant="h6">
                  {playerDetails.fromYear && playerDetails.toYear
                    ? `${Math.floor(playerDetails.fromYear)} - ${Math.floor(playerDetails.toYear)}`
                    : 'N/A'}
                </Typography>
              </Grid>
              {playerDetails.draftYear && (
                <Grid item xs={12} sm={6} md={4}>
                  <Typography color="text.secondary" variant="body2">
                    Draft
                  </Typography>
                  <Typography variant="h6">
                    {playerDetails.draftYear}
                    {playerDetails.draftRound && ` - Round ${playerDetails.draftRound}`}
                    {playerDetails.draftNumber && ` Pick ${playerDetails.draftNumber}`}
                  </Typography>
                </Grid>
              )}
            </Grid>
          </Paper>
        </>
      )}
    </Box>
  )
}

export default PlayerDetails
