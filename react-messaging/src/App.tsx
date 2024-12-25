import { useNavigate } from "react-router";
import { handleLogout } from "./pages/auth/functions";
import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";

const WEBSOCKET_URL = import.meta.env.VITE_WEBSOCKET_URL;
const stompClient = new Client({
  brokerURL: `${WEBSOCKET_URL}/short-notice`,
  onConnect: () => {
    stompClient.subscribe("/topic/message", (message) => {
      const messageFromServer = JSON.parse(message.body);
      console.log(messageFromServer.message);
    });
  },
});

stompClient.onStompError = (frame) => {
  console.error("Broker reported error: " + frame.headers["message"]);
  console.error("Additional details: " + frame.body);
};
stompClient.onWebSocketError = (error) => {
  console.error("Error with websocket", error);
};

function App() {
  const navigate = useNavigate();
  const [message, setMessage] = useState("");
  useEffect(() => {
    stompClient.activate();
    console.log("client activated");
    return () => {
      stompClient.deactivate();
    };
  }, []);
  return (
    <div className="container">
      <h2 className="text-center">Messaging app</h2>
      <input
        className="form-control"
        type="text"
        value={message}
        onChange={(e) => {
          setMessage(e.target.value);
        }}
      />
      <button
        className="btn btn-primary"
        onClick={() => {
          stompClient.publish({
            destination: "/app/test",
            body: JSON.stringify({ message: message }),
          });
        }}
      >
        Send
      </button>
      <button
        className="btn btn-danger mt-3"
        onClick={() => {
          handleLogout(navigate);
        }}
      >
        Logout
      </button>
    </div>
  );
}

export default App;
