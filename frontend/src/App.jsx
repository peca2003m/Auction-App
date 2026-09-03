import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import AuctionsPage from './pages/AuctionsPage';
import AuctionDetailPage from './pages/AuctionDetailPage';
import CreateAuctionPage from './pages/CreateAuctionPage';
import NotificationsPage from './pages/NotificationsPage';
import Navbar from './components/Navbar';
import PreRegisterPage from './pages/PreRegisterPage';
import ProfilePage from './pages/ProfilePage';

function PrivateRoute({ children }) {
  const { token } = useAuth();
  return token ? children : <Navigate to="/login" />;
}

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Navbar />
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/preRegister" element={<PreRegisterPage />} />
          <Route path="/auctions" element={
            <PrivateRoute><AuctionsPage /></PrivateRoute>
          } />
          <Route path="/auctions/:id" element={
            <PrivateRoute><AuctionDetailPage /></PrivateRoute>
          } />
          <Route path="/auctions/create" element={
            <PrivateRoute><CreateAuctionPage /></PrivateRoute>
          } />
          <Route path="/profile" element={
            <PrivateRoute><ProfilePage /></PrivateRoute>
          } />
          <Route path="/notifications" element={
            <PrivateRoute><NotificationsPage /></PrivateRoute>
          } />
          <Route path="/" element={<Navigate to="/auctions" />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;