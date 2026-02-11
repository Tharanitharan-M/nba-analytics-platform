import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor to handle errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// Auth API
export const authAPI = {
  login: (credentials) => api.post('/auth/login', credentials),
  register: (userData) => api.post('/auth/register', userData),
}

// Dashboard API
export const dashboardAPI = {
  getStats: () => api.get('/dashboard/stats'),
}

// Teams API
export const teamsAPI = {
  getAllTeams: () => api.get('/teams'),
  getTeamById: (teamId) => api.get(`/teams/${teamId}`),
  getTeamByAbbreviation: (abbr) => api.get(`/teams/abbreviation/${abbr}`),
  searchTeams: (query) => api.get(`/teams/search?q=${query}`),
  getTeamGames: (teamId, page = 0, size = 20) => 
    api.get(`/teams/${teamId}/games?page=${page}&size=${size}`),
  getTeamStats: (teamId, seasonId) => 
    api.get(`/teams/${teamId}/stats/${seasonId}`),
}

// Players API
export const playersAPI = {
  getAllPlayers: (page = 0, size = 20) => 
    api.get(`/players?page=${page}&size=${size}`),
  getActivePlayers: (page = 0, size = 20) => 
    api.get(`/players/active?page=${page}&size=${size}`),
  searchPlayers: (query, page = 0, size = 20) => 
    api.get(`/players/search?q=${query}&page=${page}&size=${size}`),
  getPlayerById: (playerId) => api.get(`/players/${playerId}`),
  getPlayerDetails: (playerId) => api.get(`/players/${playerId}/details`),
  getPlayersByTeam: (teamId) => api.get(`/players/team/${teamId}`),
  getAllPositions: () => api.get('/players/positions'),
  getAllCountries: () => api.get('/players/countries'),
}

// Games API
export const gamesAPI = {
  getRecentGames: (page = 0, size = 20) => 
    api.get(`/games?page=${page}&size=${size}`),
  getGameById: (gameId) => api.get(`/games/${gameId}`),
  getGamesBySeason: (seasonId, page = 0, size = 20) => 
    api.get(`/games/season/${seasonId}?page=${page}&size=${size}`),
  getGamesByTeam: (teamId, page = 0, size = 20) => 
    api.get(`/games/team/${teamId}?page=${page}&size=${size}`),
  getGamesByDateRange: (startDate, endDate, page = 0, size = 20) => 
    api.get(`/games/date-range?startDate=${startDate}&endDate=${endDate}&page=${page}&size=${size}`),
  getGamesOnThisDay: (month, day) => 
    api.get(`/games/on-this-day?month=${month}&day=${day}`),
  getAllSeasons: () => api.get('/games/seasons'),
  getHighScoringGames: (limit = 10) => 
    api.get(`/games/high-scoring?limit=${limit}`),
}

export default api
