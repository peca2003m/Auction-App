import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

let stompClient = null;

export const connectWebSocket = (auctionId, onBidUpdate) => {
  stompClient = new Client({
    webSocketFactory: () => new SockJS('http://localhost:8082/ws'),
    onConnect: () => {
      stompClient.subscribe(`/topic/auctions/${auctionId}`, (message) => {
        onBidUpdate(JSON.parse(message.body));
      });
    },
    debug: () => {},
  });

  stompClient.activate();
};

export const disconnectWebSocket = () => {
  if (stompClient) {
    stompClient.deactivate();
    stompClient = null;
  }
};