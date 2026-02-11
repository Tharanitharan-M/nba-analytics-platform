import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Box,
  Grid,
  Card,
  CardContent,
  CardActionArea,
  Typography,
  TextField,
  CircularProgress,
  InputAdornment,
} from '@mui/material'
import SearchIcon from '@mui/icons-material/Search'
import { teamsAPI } from '../../services/api'
import { toast } from 'react-toastify'

const Teams = () => {
  const navigate = useNavigate()
  const [teams, setTeams] = useState([])
  const [filteredTeams, setFilteredTeams] = useState([])
  const [searchTerm, setSearchTerm] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchTeams()
  }, [])

  useEffect(() => {
    if (searchTerm) {
      const filtered = teams.filter((team) =>
        team.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        team.abbreviation.toLowerCase().includes(searchTerm.toLowerCase()) ||
        team.city.toLowerCase().includes(searchTerm.toLowerCase())
      )
      setFilteredTeams(filtered)
    } else {
      setFilteredTeams(teams)
    }
  }, [searchTerm, teams])

  const fetchTeams = async () => {
    try {
      const response = await teamsAPI.getAllTeams()
      setTeams(response.data.data)
      setFilteredTeams(response.data.data)
    } catch (error) {
      toast.error('Failed to load teams')
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
      <Typography variant="h4" gutterBottom fontWeight="bold" sx={{ mb: 3 }}>
        NBA Teams
      </Typography>

      <TextField
        fullWidth
        variant="outlined"
        placeholder="Search teams by name, city, or abbreviation..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        sx={{ mb: 4 }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <SearchIcon />
            </InputAdornment>
          ),
        }}
      />

      <Grid container spacing={3}>
        {filteredTeams.map((team) => (
          <Grid item xs={12} sm={6} md={4} lg={3} key={team.id}>
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
              <CardActionArea onClick={() => navigate(`/teams/${team.id}`)}>
                <CardContent>
                  <Box
                    sx={{
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      height: 80,
                      mb: 2,
                      fontSize: 48,
                      fontWeight: 'bold',
                      color: 'primary.main',
                    }}
                  >
                    {team.abbreviation}
                  </Box>
                  <Typography
                    variant="h6"
                    component="div"
                    align="center"
                    fontWeight="bold"
                    gutterBottom
                  >
                    {team.fullName}
                  </Typography>
                  <Typography
                    variant="body2"
                    color="text.secondary"
                    align="center"
                  >
                    {team.city}, {team.state}
                  </Typography>
                  {team.yearFounded && (
                    <Typography
                      variant="caption"
                      color="text.secondary"
                      align="center"
                      display="block"
                      sx={{ mt: 1 }}
                    >
                      Founded: {Math.floor(team.yearFounded)}
                    </Typography>
                  )}
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>
        ))}
      </Grid>

      {filteredTeams.length === 0 && (
        <Box textAlign="center" py={8}>
          <Typography variant="h6" color="text.secondary">
            No teams found
          </Typography>
        </Box>
      )}
    </Box>
  )
}

export default Teams
