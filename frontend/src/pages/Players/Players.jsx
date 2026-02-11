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
  TextField,
  CircularProgress,
  InputAdornment,
  Chip,
} from '@mui/material'
import SearchIcon from '@mui/icons-material/Search'
import { playersAPI } from '../../services/api'
import { toast } from 'react-toastify'

const Players = () => {
  const navigate = useNavigate()
  const [players, setPlayers] = useState([])
  const [searchTerm, setSearchTerm] = useState('')
  const [page, setPage] = useState(0)
  const [rowsPerPage, setRowsPerPage] = useState(20)
  const [totalElements, setTotalElements] = useState(0)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchPlayers()
  }, [page, rowsPerPage])

  useEffect(() => {
    const delaySearch = setTimeout(() => {
      if (searchTerm) {
        searchPlayers()
      } else {
        fetchPlayers()
      }
    }, 500)

    return () => clearTimeout(delaySearch)
  }, [searchTerm])

  const fetchPlayers = async () => {
    setLoading(true)
    try {
      const response = await playersAPI.getActivePlayers(page, rowsPerPage)
      setPlayers(response.data.data.content)
      setTotalElements(response.data.data.totalElements)
    } catch (error) {
      toast.error('Failed to load players')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }

  const searchPlayers = async () => {
    setLoading(true)
    try {
      const response = await playersAPI.searchPlayers(searchTerm, page, rowsPerPage)
      setPlayers(response.data.data.content)
      setTotalElements(response.data.data.totalElements)
    } catch (error) {
      toast.error('Failed to search players')
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
        NBA Players
      </Typography>

      <TextField
        fullWidth
        variant="outlined"
        placeholder="Search players by name..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        sx={{ mb: 3 }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <SearchIcon />
            </InputAdornment>
          ),
        }}
      />

      <Paper>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Player Name</TableCell>
                <TableCell>First Name</TableCell>
                <TableCell>Last Name</TableCell>
                <TableCell align="center">Status</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={4} align="center" sx={{ py: 8 }}>
                    <CircularProgress />
                  </TableCell>
                </TableRow>
              ) : players.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={4} align="center" sx={{ py: 8 }}>
                    <Typography color="text.secondary">
                      No players found
                    </Typography>
                  </TableCell>
                </TableRow>
              ) : (
                players.map((player) => (
                  <TableRow
                    key={player.id}
                    hover
                    onClick={() => navigate(`/players/${player.id}`)}
                    sx={{ cursor: 'pointer' }}
                  >
                    <TableCell>
                      <Typography fontWeight="bold">{player.fullName}</Typography>
                    </TableCell>
                    <TableCell>{player.firstName}</TableCell>
                    <TableCell>{player.lastName}</TableCell>
                    <TableCell align="center">
                      {player.isActive === 1 ? (
                        <Chip label="Active" color="success" size="small" />
                      ) : (
                        <Chip label="Inactive" size="small" />
                      )}
                    </TableCell>
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

export default Players
