import React, { useState, useEffect } from 'react'
import { useParams } from 'react-router-dom'
import {
  Box,
  Paper,
  Typography,
  CircularProgress,
  Grid,
  Card,
  CardContent,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { useNavigate } from 'react-router-dom'
import { teamsAPI, gamesAPI } from '../../services/api'
import { toast } from 'react-toastify'

const TeamDetails = () => {
  const { teamId } = useParams()
  const navigate = useNavigate()
  const [team, setTeam] = useState(null)
  const [games, setGames] = useState([])
  const [seasons, setSeasons] = useState([])
  const [selectedSeason, setSelectedSeason] = useState('')
  const [teamStats, setTeamStats] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchTeamData()
  }, [teamId])

  useEffect(() => {
    if (selectedSeason) {
      fetchTeamStats()
    }
  }, [selectedSeason])

  const fetchTeamData = async () => {
    try {
      const [teamRes, gamesRes, seasonsRes] = await Promise.all([
        teamsAPI.getTeamById(teamId),
        teamsAPI.getTeamGames(teamId, 0, 10),
        gamesAPI.getAllSeasons(),
      ])

      setTeam(teamRes.data.data)
      setGames(gamesRes.data.data.content)
      setSeasons(seasonsRes.data.data)
      if (seasonsRes.data.data.length > 0) {
        setSelectedSeason(seasonsRes.data.data[0])
      }
    } catch (error) {
      toast.error('Failed to load team data')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }

  const fetchTeamStats = async () => {
    try {
      const response = await teamsAPI.getTeamStats(teamId, selectedSeason)
      setTeamStats(response.data.data)
    } catch (error) {
      console.error('Failed to load team stats:', error)
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
        onClick={() => navigate('/teams')}
        sx={{ mb: 3 }}
      >
        Back to Teams
      </Button>

      <Paper sx={{ p: 4, mb: 3 }}>
        <Box textAlign="center">
          <Typography
            variant="h2"
            fontWeight="bold"
            color="primary.main"
            gutterBottom
          >
            {team?.abbreviation}
          </Typography>
          <Typography variant="h4" gutterBottom>
            {team?.fullName}
          </Typography>
          <Typography variant="body1" color="text.secondary">
            {team?.city}, {team?.state}
          </Typography>
          {team?.yearFounded && (
            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
              Founded: {Math.floor(team.yearFounded)}
            </Typography>
          )}
        </Box>
      </Paper>

      {teamStats && (
        <Paper sx={{ p: 3, mb: 3 }}>
          <Typography variant="h6" gutterBottom fontWeight="bold">
            Season Stats - {selectedSeason}
          </Typography>
          <Grid container spacing={2} sx={{ mt: 2 }}>
            <Grid item xs={6} sm={4} md={2}>
              <Card>
                <CardContent>
                  <Typography color="text.secondary" variant="body2">
                    Record
                  </Typography>
                  <Typography variant="h6">
                    {teamStats.wins}-{teamStats.losses}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={6} sm={4} md={2}>
              <Card>
                <CardContent>
                  <Typography color="text.secondary" variant="body2">
                    Win %
                  </Typography>
                  <Typography variant="h6">
                    {(teamStats.winPercentage * 100).toFixed(1)}%
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={6} sm={4} md={2}>
              <Card>
                <CardContent>
                  <Typography color="text.secondary" variant="body2">
                    PPG
                  </Typography>
                  <Typography variant="h6">
                    {teamStats.pointsPerGame.toFixed(1)}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={6} sm={4} md={2}>
              <Card>
                <CardContent>
                  <Typography color="text.secondary" variant="body2">
                    OPPG
                  </Typography>
                  <Typography variant="h6">
                    {teamStats.pointsAllowedPerGame.toFixed(1)}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={6} sm={4} md={2}>
              <Card>
                <CardContent>
                  <Typography color="text.secondary" variant="body2">
                    FG%
                  </Typography>
                  <Typography variant="h6">
                    {(teamStats.fieldGoalPercentage * 100).toFixed(1)}%
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={6} sm={4} md={2}>
              <Card>
                <CardContent>
                  <Typography color="text.secondary" variant="body2">
                    3P%
                  </Typography>
                  <Typography variant="h6">
                    {(teamStats.threePointPercentage * 100).toFixed(1)}%
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        </Paper>
      )}

      <Paper sx={{ p: 3 }}>
        <Typography variant="h6" gutterBottom fontWeight="bold">
          Recent Games
        </Typography>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Date</TableCell>
                <TableCell>Matchup</TableCell>
                <TableCell align="center">Result</TableCell>
                <TableCell align="center">Score</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {games.map((game, index) => {
                const isHome = game.teamIdHome === teamId
                const result = isHome ? game.wlHome : game.wlAway
                const score = isHome
                  ? `${game.ptsHome} - ${game.ptsAway}`
                  : `${game.ptsAway} - ${game.ptsHome}`
                const opponent = isHome ? game.teamNameAway : game.teamNameHome

                return (
                  <TableRow key={game.gameId || index}>
                    <TableCell>
                      {new Date(game.gameDate).toLocaleDateString()}
                    </TableCell>
                    <TableCell>
                      {isHome ? 'vs' : '@'} {opponent}
                    </TableCell>
                    <TableCell align="center">
                      <Typography
                        color={result === 'W' ? 'success.main' : 'error.main'}
                        fontWeight="bold"
                      >
                        {result}
                      </Typography>
                    </TableCell>
                    <TableCell align="center">{score}</TableCell>
                  </TableRow>
                )
              })}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </Box>
  )
}

export default TeamDetails
