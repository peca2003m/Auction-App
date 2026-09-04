import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { auctionService } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { connectWebSocket, disconnectWebSocket } from '../services/websocket';

export default function AuctionDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [auction, setAuction] = useState(null);
  const [bidAmount, setBidAmount] = useState('');
  const [loading, setLoading] = useState(true);
  const [bidLoading, setBidLoading] = useState(false);
  const [error, setError] = useState('');
  const [bidError, setBidError] = useState('');
  const [bidSuccess, setBidSuccess] = useState('');

  const fetchAuction = async () => {
  try {
    const response = await auctionService.getById(id);
    //console.log("Auction Response Data:", response.data);
    setAuction(response.data);
  } catch (err) {
    setError('Error loading auction.');
  } finally {
    setLoading(false);
  }
};

  useEffect(() => {
  fetchAuction();
}, [id]);

useEffect(() => {
  connectWebSocket(id, (updatedBid) => {
    setAuction((prev) => {
      if (!prev) return prev;
      return {
        ...prev,
        currentPrice: updatedBid.amount,
        lastBidderId: updatedBid.bidderId,
      };
    });
  });

  return () => disconnectWebSocket();
}, [id]);
  const handleBid = async (e) => {
    e.preventDefault();
    setBidError('');
    setBidSuccess('');
    try {
      setBidLoading(true);
      await auctionService.placeBid(id, parseFloat(bidAmount));
      setBidSuccess('Bid placed successfully!');
      await fetchAuction();
      setBidAmount('');
    } catch (err) {
      setBidError(err.response?.data || 'Bid failed. Please try again.');
    } finally {
      setBidLoading(false);
    }
  };

  if (loading) return <div className="text-center mt-10">Loading...</div>;
  if (error) return <div className="text-center mt-10 text-red-500">{error}</div>;
  if (!auction) return null;

  const isSeller = user?.keycloakId === auction.sellerId;
  const isHighestBidder = user?.keycloakId === auction.lastBidderId;
  const canBid = auction.status === 'ACTIVE' && !isSeller && !isHighestBidder;

  return (
    <div className="max-w-4xl mx-auto p-6">
      <button
        onClick={() => navigate('/auctions')}
        className="text-blue-600 mb-4 hover:underline"
      >
        ← Back to Auctions
      </button>

      <div className="bg-white rounded-lg shadow p-6 mb-6">
        <div className="flex justify-between items-start mb-4">
          <h1 className="text-3xl font-bold">{auction.title}</h1>
          <span className={`px-3 py-1 rounded text-sm font-medium ${
            auction.status === 'ACTIVE'
              ? 'bg-green-100 text-green-700'
              : 'bg-red-100 text-red-700'
          }`}>
            {auction.status}
          </span>
        </div>

        <p className="text-gray-600 mb-6">{auction.description}</p>

      {auction.imageUrls && auction.imageUrls.length > 0 && (
        <div className="flex gap-2 mb-4 flex-wrap">
          {auction.imageUrls.map((url, i) => (
            <img
              key={i}
              src={url}
              alt={`auction ${i + 1}`}
              className="w-32 h-32 object-cover rounded border"
            />
          ))}
        </div>
      )}

        <div className="grid grid-cols-2 gap-4 mb-6">
          <div className="bg-gray-50 rounded p-4">
            <p className="text-sm text-gray-500">Starting Price</p>
            <p className="text-xl font-bold">${auction.startingPrice}</p>
          </div>
          <div className="bg-gray-50 rounded p-4">
            <p className="text-sm text-gray-500">Current Price</p>
            <p className="text-xl font-bold text-green-600">${auction.currentPrice}</p>
          </div>
        </div>

        <p className="text-gray-400 text-sm mb-2">
          Ends: {new Date(auction.endsAt).toLocaleString()}
        </p>

        {isSeller && (
          <div className="bg-yellow-50 border border-yellow-200 text-yellow-700 px-4 py-2 rounded text-sm">
            This is your auction
          </div>
        )}
      </div>

      {auction.status === 'ACTIVE' && (
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-xl font-bold mb-4">Place a Bid</h2>

          {bidError && (
            <div className="bg-red-100 text-red-700 p-3 rounded mb-4">{bidError}</div>
          )}
          {bidSuccess && (
            <div className="bg-green-100 text-green-700 p-3 rounded mb-4">{bidSuccess}</div>
          )}

          {isHighestBidder && (
            <div className="bg-blue-50 border border-blue-200 text-blue-700 px-4 py-2 rounded text-sm mb-4">
              You are currently the highest bidder!
            </div>
          )}

          <form onSubmit={handleBid} className="flex gap-3">
            <input
              type="number"
              value={bidAmount}
              onChange={(e) => setBidAmount(e.target.value)}
              disabled={!canBid}
              className="flex-1 border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
              placeholder={`More than $${auction.currentPrice}`}
              min={auction.currentPrice}
              step="0.01"
            />
            <button
              type="submit"
              disabled={bidLoading || !canBid}
              className={`px-6 py-2 rounded-lg text-white font-medium ${
                !canBid
                  ? 'bg-gray-400 cursor-not-allowed'
                  : 'bg-blue-600 hover:bg-blue-700'
              }`}
            >
              {isSeller
                ? 'Your Auction'
                : isHighestBidder
                ? 'Highest Bidder'
                : bidLoading
                ? 'Bidding...'
                : 'Place Bid'}
            </button>
          </form>
        </div>
      )}

      {auction.status === 'CLOSED' && (
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-xl font-bold mb-2">Auction Closed</h2>
          <p className="text-gray-600">
            Final price: <span className="font-bold text-green-600">${auction.currentPrice}</span>
          </p>
        </div>
      )}
    </div>
  );
}