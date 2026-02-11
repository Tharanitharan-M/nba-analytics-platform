import React, { useState } from 'react'
import {
  Box,
  Paper,
  Typography,
  Grid,
  TextField,
  Autocomplete,
  Button,
  Card,
  CardContent,
  Tabs,
  Tab,
} from '@mui/material'
import CompareArrowsIcon from '@mui/icons-material/CompareArrows'
import { teamsAPI, playersAPI } from '../../services/api'
import { toast } from 'react-toastify'
import {
  RadarChart,
  PolarGrid,
  PolarAngleAxis,
  PolarRadiusAxis,
  Radar,
  Legend,
  ResponsiveContainer,
} from 'recharts'

const Compare = () => {
  const [compareType, setCompareType] = useState(0) // 0 for teams, 1 for players
  const [teams, setTeams] = useState([])
  const [players, setPlayers] = useState([])
  const [selectedItem1, setSelectedItem1] = useState(null)
  const [selectedItem2, setSelectedItem2] = useState(null)
  const [stats1, setStats1] = useState(null)
  const [stats2, setStats2] = useState(null)
  const [loading, setLoading] = useState(false)

  // Fetch teams
  const searchTeams = async (searchTerm) => {
    if (searchTerm.length < 2) return
    try {
      const response = await teamsAPI.searchTeams(searchTerm)
      setTeams(response.data.data)
    } catch (error) {
      console.error('Failed to search teams:', error)
    }
  }

  // Fetch players
  const searchPlayers = async (searchTerm) => {
    if (searchTerm.length < 2) return
    try {
      const response = await playersAPI.searchPlayers(searchTerm, 0, 20)
      setPlayers(response.data.data.content)
    } catch (error) {
      console.error('Failed to search players:', error)
    }
  }

  const handleCompare = async () => {
    if (!selectedItem1 || !selectedItem2) {
      toast.error('Please select two items to compare')
      return
    }

    setLoading(true)
    try {
      if (compareType === 0) {
        // Compare teams - we'll use mock data for demo
        toast.info('Team comparison feature - statistics would be displayed here')
      } else {
        // Compare players - we'll use mock data for demo
        toast.info('Player comparison feature - statistics would be displayed here')
      }
    } catch (error) {
      toast.error('Failed to compare')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom fontWeight="bold" sx={{ mb: 3 }}>
        Compare
      </Typography>

      <Paper sx={{ mb: 3 }}>
        <Tabs
          value={compareType}
          onChange={(e, newValue) => {
            setCompareType(newValue)
            setSelectedItem1(null)
            setSelectedItem2(null)
          }}
          centered
        >
          <Tab label="Compare Teams" />
          <Tab label="Compare Players" />
        </Tabs>
      </Paper>

      <Grid container spacing={3} sx={{ mb: 3 }}>
        <Grid item xs={12} md={5}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              {compareType === 0 ? 'Select Team 1' : 'Select Player 1'}
            </Typography>
            <Autocomplete
              options={compareType === 0 ? teams : players}
              getOptionLabel={(option) => option.fullName}
              value={selectedItem1}
              onChange={(e, newValue) => setSelectedItem1(newValue)}
              onInputChange={(e, value) => {
                if (compareType === 0) {
                  searchTeams(value)
                } else {
                  searchPlayers(value)
                }
              }}
              renderInput={(params) => (
                <TextField
                  {...params}
                  label={compareType === 0 ? 'Search Team' : 'Search Player'}
                  placeholder="Type to search..."
                />
              )}
            />
            {selectedItem1 && (
              <Card sx={{ mt: 3 }}>
                <CardContent>
                  <Typography variant="h6" align="center">
                    {selectedItem1.fullName}
                  </Typography>
                  <Typography variant="body2" color="text.secondary" align="center">
                    {compareType === 0 ? selectedItem1.city : 'Player Details'}
                  </Typography>
                </CardContent>
              </Card>
            )}
          </Paper>
        </Grid>

        <Grid item xs={12} md={2} display="flex" alignItems="center" justifyContent="center">
          <CompareArrowsIcon sx={{ fontSize: 48, color: 'primary.main' }} />
        </Grid>

        <Grid item xs={12} md={5}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              {compareType === 0 ? 'Select Team 2' : 'Select Player 2'}
            </Typography>
            <Autocomplete
              options={compareType === 0 ? teams : players}
              getOptionLabel={(option) => option.fullName}
              value={selectedItem2}
              onChange={(e, newValue) => setSelectedItem2(newValue)}
              onInputChange={(e, value) => {
                if (compareType === 0) {
                  searchTeams(value)
                } else {
                  searchPlayers(value)
                }
              }}
              renderInput={(params) => (
                <TextField
                  {...params}
                  label={compareType === 0 ? 'Search Team' : 'Search Player'}
                  placeholder="Type to search..."
                />
              )}
            />
            {selectedItem2 && (
              <Card sx={{ mt: 3 }}>
                <CardContent>
                  <Typography variant="h6" align="center">
                    {selectedItem2.fullName}
                  </Typography>
                  <Typography variant="body2" color="text.secondary" align="center">
                    {compareType === 0 ? selectedItem2.city : 'Player Details'}
                  </Typography>
                </CardContent>
              </Card>
            )}
          </Paper>
        </Grid>
      </Grid>

      <Box textAlign="center" sx={{ mb: 3 }}>
        <Button
          variant="contained"
          size="large"
          onClick={handleCompare}
          disabled={!selectedItem1 || !selectedItem2 || loading}
          startIcon={<CompareArrowsIcon />}
        >
          Compare
        </Button>
      </Box>

      {selectedItem1 && selectedItem2 && (
        <Paper sx={{ p: 3 }}>
          <Typography variant="h6" gutterBottom align="center">
            Comparison Chart
          </Typography>
          <Typography variant="body2" color="text.secondary" align="center" sx={{ mb: 3 }}>
            Comparing {selectedItem1.fullName} vs {selectedItem2.fullName}
          </Typography>
          <Box sx={{ height: 400, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Typography color="text.secondary">
              Select items and click Compare to view detailed statistics comparison
            </Typography>
          </Box>
        </Paper>
      )}
    </Box>
  )
}

export default Compare
