import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import Layout from './components/Layout/Layout'
import Login from './pages/Auth/Login'
import Register from './pages/Auth/Register'
import Dashboard from './pages/Dashboard/Dashboard'
import Teams from './pages/Teams/Teams'
import TeamDetails from './pages/Teams/TeamDetails'
import Players from './pages/Players/Players'
import PlayerDetails from './pages/Players/PlayerDetails'
import Games from './pages/Games/Games'
import GameDetails from './pages/Games/GameDetails'
import Compare from './pages/Compare/Compare'
import OnThisDay from './pages/OnThisDay/OnThisDay'
import PrivateRoute from './components/Auth/PrivateRoute'
import { AuthProvider } from './context/AuthContext'

function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        
        <Route path="/" element={<PrivateRoute><Layout /></PrivateRoute>}>
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="teams" element={<Teams />} />
          <Route path="teams/:teamId" element={<TeamDetails />} />
          <Route path="players" element={<Players />} />
          <Route path="players/:playerId" element={<PlayerDetails />} />
          <Route path="games" element={<Games />} />
          <Route path="games/:gameId" element={<GameDetails />} />
          <Route path="compare" element={<Compare />} />
          <Route path="on-this-day" element={<OnThisDay />} />
        </Route>
      </Routes>
    </AuthProvider>
  )
}

export default App
