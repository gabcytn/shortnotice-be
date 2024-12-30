import { Client, Message } from "@stomp/stompjs";

export async function subscribeToStomp(
  stompClient: Client,
  callback: (message: Message) => void,
) {
  try {
    const server = import.meta.env.VITE_SERVER_URL;
    const res = await fetch(`${server}/username`, {
      credentials: "include",
    });
    const username = await res.text();
    stompClient.onConnect = () => {
      stompClient.subscribe(`/topic/private/${username}`, callback);
    };
    stompClient.activate();
  } catch (e: unknown) {
    if (e instanceof Error) {
      console.error("Error subscribing");
      console.error(e.message);
    }
  }
}
