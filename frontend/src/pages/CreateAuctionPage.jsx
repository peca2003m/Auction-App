import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { auctionService } from '../services/api';
import { categoryService } from '../services/api';

const CLOUDINARY_CLOUD_NAME = 'defhuvc3m';
const CLOUDINARY_PRESET = 'AuctionApp';

export default function CreateAuctionPage() {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [startingPrice, setStartingPrice] = useState('');
  const [endsAt, setEndsAt] = useState('');
  const [images, setImages] = useState([]);
  const [imageUrls, setImageUrls] = useState([]);
  const [uploading, setUploading] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  
  const [categories, setCategories] = useState([]);
  const [categoryId, setCategoryId] = useState('');

  useEffect(() => {
  categoryService.getAll().then(res => setCategories(res.data));
  }, []);

  const handleImageChange = async (e) => {
    const files = Array.from(e.target.files);
    setImages(files);
    setUploading(true);

    try {
      const urls = await Promise.all(files.map(async (file) => {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('upload_preset', CLOUDINARY_PRESET);

        const response = await fetch(
          `https://api.cloudinary.com/v1_1/${CLOUDINARY_CLOUD_NAME}/image/upload`,
          { method: 'POST', body: formData }
        );
        const data = await response.json();
        return data.secure_url;
      }));

      setImageUrls(urls);
    } catch (err) {
      setError('Image upload failed.');
    } finally {
      setUploading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!title || !startingPrice || !endsAt) {
      setError('Please fill in all required fields.');
      return;
    }

    try {
      setLoading(true);
      const payload = {
        title,
        description,
        startingPrice: parseFloat(startingPrice),
        endsAt: endsAt + ':00',
        imageUrls,
        categoryId: categoryId || null,
      };
      await auctionService.create(payload);
      navigate('/auctions');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create auction.');
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
          <label className="block text-sm font-medium mb-1">Category</label>
          <select
            value={categoryId}
            onChange={(e) => setCategoryId(e.target.value)}
            className="w-full border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Select category</option>
            {categories.map(cat => (
              <option key={cat.id} value={cat.id}>{cat.name}</option>
            ))}
          </select>
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

        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Ends At *</label>
          <input
            type="datetime-local"
            value={endsAt}
            onChange={(e) => setEndsAt(e.target.value)}
            className="w-full border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div className="mb-6">
          <label className="block text-sm font-medium mb-1">Images</label>
          <input
            type="file"
            multiple
            accept="image/*"
            onChange={handleImageChange}
            className="w-full border rounded-lg p-2"
          />
          {uploading && <p className="text-sm text-blue-500 mt-1">Uploading images...</p>}
          {imageUrls.length > 0 && (
            <div className="flex gap-2 mt-2 flex-wrap">
              {imageUrls.map((url, i) => (
                <img key={i} src={url} alt={`preview ${i}`} className="w-20 h-20 object-cover rounded" />
              ))}
            </div>
          )}
        </div>

        <div className="flex gap-3">
          <button
            type="submit"
            disabled={loading || uploading}
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
  );
}