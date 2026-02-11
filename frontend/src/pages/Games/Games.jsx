import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Box,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
  Typography,
  CircularProgress,
  Chip,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
} from '@mui/material'
import { gamesAPI } from '../../services/api'
import { toast } from 'react-toastify'

const Games = () => {
  const navigate = useNavigate()
  const [games, setGames] = useState([])
  const [seasons, setSeasons] = useState([])
  const [selectedSeason, setSelectedSeason] = useState('all')
  const [page, setPage] = useState(0)
  const [rowsPerPage, setRowsPerPage] = useState(20)
  const [totalElements, setTotalElements] = useState(0)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchSeasons()
  }, [])

  useEffect(() => {
    fetchGames()
  }, [page, rowsPerPage, selectedSeason])

  const fetchSeasons = async () => {
    try {
      const response = await gamesAPI.getAllSeasons()
      setSeasons(response.data.data)
    } catch (error) {
      console.error('Failed to load seasons:', error)
    }
  }

  const fetchGames = async () => {
    setLoading(true)
    try {
      const response =
        selectedSeason === 'all'
          ? await gamesAPI.getRecentGames(page, rowsPerPage)
          : await gamesAPI.getGamesBySeason(selectedSeason, page, rowsPerPage)

      setGames(response.data.data.content)
      setTotalElements(response.data.data.totalElements)
    } catch (error) {
      toast.error('Failed to load games')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }

  const handleChangePage = (event, newPage) => {
    setPage(newPage)
  }

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10))
    setPage(0)
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom fontWeight="bold" sx={{ mb: 3 }}>
        NBA Games
      </Typography>

      <FormControl sx={{ mb: 3, minWidth: 200 }}>
        <InputLabel>Season</InputLabel>
        <Select
          value={selectedSeason}
          label="Season"
          onChange={(e) => {
            setSelectedSeason(e.target.value)
            setPage(0)
          }}
        >
          <MenuItem value="all">All Seasons</MenuItem>
          {seasons.map((season) => (
            <MenuItem key={season} value={season}>
              {season}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      <Paper>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Date</TableCell>
                <TableCell>Home Team</TableCell>
                <TableCell align="center">Score</TableCell>
                <TableCell>Away Team</TableCell>
                <TableCell align="center">Season</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={5} align="center" sx={{ py: 8 }}>
                    <CircularProgress />
                  </TableCell>
                </TableRow>
              ) : games.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={5} align="center" sx={{ py: 8 }}>
                    <Typography color="text.secondary">No games found</Typography>
                  </TableCell>
                </TableRow>
              ) : (
                games.map((game) => (
                  <TableRow
                    key={game.gameId}
                    hover
                    onClick={() => navigate(`/games/${game.gameId}`)}
                    sx={{ cursor: 'pointer' }}
                  >
                    <TableCell>
                      {new Date(game.gameDate).toLocaleDateString()}
                    </TableCell>
                    <TableCell>
                      <Box display="flex" alignItems="center" gap={1}>
                        <Typography fontWeight="bold">
                          {game.teamNameHome}
                        </Typography>
                        {game.wlHome === 'W' && (
                          <Chip label="W" color="success" size="small" />
                        )}
                      </Box>
                    </TableCell>
                    <TableCell align="center">
                      <Typography fontWeight="bold">
                        {game.ptsHome} - {game.ptsAway}
                      </Typography>
                    </TableCell>
                    <TableCell>
                      <Box display="flex" alignItems="center" gap={1}>
                        <Typography fontWeight="bold">
                          {game.teamNameAway}
                        </Typography>
                        {game.wlAway === 'W' && (
                          <Chip label="W" color="success" size="small" />
                        )}
                      </Box>
                    </TableCell>
                    <TableCell align="center">{game.seasonId}</TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          component="div"
          count={totalElements}
          page={page}
          onPageChange={handleChangePage}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={handleChangeRowsPerPage}
          rowsPerPageOptions={[10, 20, 50, 100]}
        />
      </Paper>
    </Box>
  )
}

export default Games
