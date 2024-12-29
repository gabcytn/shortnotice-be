import { Client } from "@stomp/stompjs";

export async function subscribeToStomp(stompClient: Client) {
  const server = import.meta.env.VITE_SERVER_URL;
  const res = await fetch(`${server}/username`, {
    credentials: "include",
  });
  const username = await res.text();
  stompClient.onConnect = () => {
    stompClient.subscribe(`/topic/private/${username}`, (message) => {
      const messageFromServer = JSON.parse(message.body);
      console.log(messageFromServer);
    });
  };
  stompClient.activate();
}
