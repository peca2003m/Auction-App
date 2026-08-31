import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { auctionService } from '../services/api';

export default function CreateAuctionPage() {

  const navigate = useNavigate();

  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [startingPrice, setStartingPrice] = useState('');
  const [endsAt, setEndsAt] = useState('');

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!title || !startingPrice || !endsAt) {
      setError('Molimo popunite obavezna polja.');
      return;
    }

    try {
      setLoading(true);
      const payload = {
        title,
        description,
        startingPrice: parseFloat(startingPrice),
        endsAt: new Date(endsAt).toISOString(),
      };
      await auctionService.create(payload);
      navigate('/auctions');

    } catch (err) {
      console.error('Greška pri kreiranju aukcije:', err);
      setError(
        err.response?.data?.message || 'Neuspešno kreiranje aukcije. Proverite podatke.'
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6">Create Auction</h1>
      {error && <div className="bg-red-100 text-red-700 p-3 rounded mb-4">{error}</div>}
      <form onSubmit={handleSubmit} className="bg-white rounded-lg shadow p-6">
        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Title *</label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Enter auction title"
          />
        </div>
        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Description</label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="w-full border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
            rows={4}
            placeholder="Enter description"
          />
        </div>
        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Starting Price *</label>
          <input
            type="number"
            value={startingPrice}
            onChange={(e) => setStartingPrice(e.target.value)}
            className="w-full border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="0.00"
            min="0"
            step="0.01"
          />
        </div>
        <div className="mb-6">
          <label className="block text-sm font-medium mb-1">Ends At *</label>
          <input
            type="datetime-local"
            value={endsAt}
            onChange={(e) => setEndsAt(e.target.value)}
            className="w-full border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div className="flex gap-3">
          <button
            type="submit"
            disabled={loading}
            className="flex-1 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50"
          >
            {loading ? 'Creating...' : 'Create Auction'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/auctions')}
            className="flex-1 bg-gray-200 text-gray-700 py-2 rounded-lg hover:bg-gray-300"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  )
}