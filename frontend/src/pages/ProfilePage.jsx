import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { auctionService } from '../services/api';

export default function ProfilePage() {
  const { user } = useAuth();
  const [myAuctions, setMyAuctions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMyAuctions = async () => {
      try {
        const response = await auctionService.getAll();
        const filtered = response.data.filter(a => a.sellerId === user?.keycloakId);
        setMyAuctions(filtered);
      } catch (err) {
        console.error('Error loading auctions:', err);
      } finally {
        setLoading(false);
      }
    };

    if (user) fetchMyAuctions();
  }, [user]);

  if (loading) return <div className="text-center mt-10">Loading...</div>;

  return (
    <div className="max-w-4xl mx-auto p-6">
      <div className="bg-white rounded-lg shadow p-6 mb-6">
        <h1 className="text-3xl font-bold mb-4">My Profile</h1>
        <div className="grid grid-cols-2 gap-4">
          <div>
            <p className="text-sm text-gray-500">Username</p>
            <p className="font-medium">{user?.username}</p>
          </div>
          <div>
            <p className="text-sm text-gray-500">Email</p>
            <p className="font-medium">{user?.email}</p>
          </div>
          <div>
            <p className="text-sm text-gray-500">First Name</p>
            <p className="font-medium">{user?.firstName}</p>
          </div>
          <div>
            <p className="text-sm text-gray-500">Last Name</p>
            <p className="font-medium">{user?.lastName}</p>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow p-6">
        <h2 className="text-xl font-bold mb-4">My Auctions ({myAuctions.length})</h2>
        {myAuctions.length === 0 ? (
          <p className="text-gray-500 text-center py-4">You haven't created any auctions yet.</p>
        ) : (
          <div className="flex flex-col gap-3">
            {myAuctions.map(auction => (
              <div key={auction.id} className="border rounded-lg p-4 flex justify-between items-center">
                <div>
                  <h3 className="font-semibold">{auction.title}</h3>
                  <p className="text-sm text-gray-500">
                    Current price: <span className="text-green-600 font-medium">${auction.currentPrice}</span>
                  </p>
                  <p className="text-xs text-gray-400">
                    Ends: {new Date(auction.endsAt).toLocaleString()}
                  </p>
                </div>
                <span className={`px-3 py-1 rounded text-sm font-medium ${
                  auction.status === 'ACTIVE'
                    ? 'bg-green-100 text-green-700'
                    : 'bg-red-100 text-red-700'
                }`}>
                  {auction.status}
                </span>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}