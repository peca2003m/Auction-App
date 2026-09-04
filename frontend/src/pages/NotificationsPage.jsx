import { useEffect, useState } from 'react';
import { notificationService } from '../services/api';

export default function NotificationsPage() {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchNotifications = async () => {
    try {
      const response = await notificationService.getMyNotifications();
      const sorted = [...response.data].sort((a, b) =>
        new Date(b.createdAt) - new Date(a.createdAt)
      );
      setNotifications(sorted);
    } catch (err) {
      setError('Error loading notifications.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchNotifications();
  }, []);

  const handleMarkAsRead = async (id) => {
    try {
      await notificationService.markAsRead(id);
      setNotifications(prev =>
        prev.map(n => n.id === id ? { ...n, isRead: true } : n)
      );
    } catch (err) {
      console.error('Error marking as read:', err);
    }
  };

  const unreadCount = notifications.filter(n => !n.isRead).length;

  if (loading) return <div className="text-center mt-10">Loading...</div>;
  if (error) return <div className="text-center mt-10 text-red-500">{error}</div>;

  return (
    <div className="max-w-3xl mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Notifications</h1>
        {unreadCount > 0 && (
          <span className="bg-red-500 text-white text-sm px-3 py-1 rounded-full">
            {unreadCount} unread
          </span>
        )}
      </div>

      {notifications.length === 0 ? (
        <div className="bg-white rounded-lg shadow p-8 text-center text-gray-500">
          No notifications yet.
        </div>
      ) : (
        <div className="flex flex-col gap-3">
          {notifications.map(notification => (
            <div
              key={notification.id}
              className={`bg-white rounded-lg shadow p-4 flex justify-between items-start ${
                !notification.isRead ? 'border-l-4 border-blue-500' : ''
              }`}
            >
              <div>
                <div className="flex items-center gap-2 mb-1">
                  <span className={`text-xs px-2 py-1 rounded font-medium ${
                    notification.type === 'AUCTION WON'
                      ? 'bg-green-100 text-green-700'
                      : notification.type === 'OUTBID'
                      ? 'bg-red-100 text-red-700'
                      : 'bg-gray-100 text-gray-700'
                  }`}>
                    {notification.type}
                  </span>
                  {!notification.isRead && (
                    <span className="text-xs text-blue-500 font-medium">NEW</span>
                  )}
                </div>
                <p className="text-gray-700">{notification.message}</p>
                {notification.createdAt && (
                  <p className="text-gray-400 text-xs mt-1">
                    {new Date(notification.createdAt).toLocaleString()}
                  </p>
                )}
              </div>
              {!notification.isRead && (
                <button
                  onClick={() => handleMarkAsRead(notification.id)}
                  className="text-sm text-blue-600 hover:underline ml-4 whitespace-nowrap"
                >
                  Mark as read
                </button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}