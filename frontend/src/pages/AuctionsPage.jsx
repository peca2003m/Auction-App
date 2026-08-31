import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom";
import { auctionService } from "../services/api";

export default function AuctionsPage() {
  
  const [auctions, setAuctions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const navigate = useNavigate();
  

  useEffect(() => {
    const fetchAuctions = async () => {
      try {
        setLoading(true);
        const response = await auctionService.getAll();
        setAuctions(response.data);
      } catch (err) {
        console.error('Greška pri učitavanju aukcija:', err);
        setError('Neuspešno učitavanje aukcija. Pokušajte ponovo.');
      } finally {
        setLoading(false);
      }
    };

    fetchAuctions();
  }, []);

  
  
  if (loading) return <div className="text-center mt-10">Loading...</div>
  if (error) return <div className="text-center mt-10 text-red-500">{error}</div>

  return (
    <div className="max-w-6xl mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Auctions</h1>
        <button
          onClick={() => navigate('/auctions/create')}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
        >
          + Create Auction
        </button>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {auctions.map(auction => (
          <div
            key={auction.id}
            className="bg-white rounded-lg shadow p-4 cursor-pointer hover:shadow-md transition"
            onClick={() => navigate(`/auctions/${auction.id}`)}
          >
            <h2 className="text-xl font-semibold mb-2">{auction.title}</h2>
            <p className="text-gray-500 text-sm mb-3">{auction.description}</p>
            <div className="flex justify-between items-center">
              <span className="text-green-600 font-bold">${auction.currentPrice}</span>
              <span className={`text-sm px-2 py-1 rounded ${
                auction.status === 'ACTIVE'
                  ? 'bg-green-100 text-green-700'
                  : 'bg-red-100 text-red-700'
              }`}>
                {auction.status}
              </span>
            </div>
            <p className="text-gray-400 text-xs mt-2">
              Ends: {new Date(auction.endsAt).toLocaleString()}
            </p>
          </div>
        ))}
      </div>
    </div>
  )

}