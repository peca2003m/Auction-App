import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="bg-white shadow mb-6">
      <div className="max-w-6xl mx-auto px-6 py-4 flex justify-between items-center">
        <Link to="/auctions" className="text-xl font-bold text-blue-600">
          AuctionApp
        </Link>
        <div className="flex items-center gap-4">
          {user && (
            <>
              <Link to="/auctions" className="text-gray-600 hover:text-blue-600">
                Auctions
              </Link>
              <Link to="/notifications" className="text-gray-600 hover:text-blue-600">
                Notifications
              </Link>
              <span className="text-gray-500 text-sm">
                {user.username}
              </span>
              <button
                onClick={handleLogout}
                className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 text-sm"
              >
                Logout
              </button>
            </>
          )}
        </div>
      </div>
    </nav>
  )
}