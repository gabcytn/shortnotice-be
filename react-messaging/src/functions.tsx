import { Client, Message } from "@stomp/stompjs";

export async function subscribeToStomp(
  stompClient: Client,
  callback: (message: Message) => void,
) {
  try {
    const server = import.meta.env.VITE_SERVER_URL;
    const res = await fetch(`${server}/uuid`, {
      credentials: "include",
    });
    const uuid = await res.text();
    stompClient.onConnect = () => {
      stompClient.subscribe(`/topic/private/${uuid}`, callback);
    };
    stompClient.activate();
  } catch (e: unknown) {
    if (e instanceof Error) {
      console.error("Error subscribing");
      console.error(e.message);
    }
  }
}
