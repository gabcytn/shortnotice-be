import { useNavigate } from "react-router";
import { handleLogout } from "./pages/auth/functions";
import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import Button from "./components/Button";
import FormInput from "./components/FormInput";

const WEBSOCKET_URL = import.meta.env.VITE_WEBSOCKET_URL;
const stompClient = new Client({
  brokerURL: `${WEBSOCKET_URL}/short-notice`,
  onConnect: () => {
    stompClient.subscribe("/topic/message", (message) => {
      const messageFromServer = JSON.parse(message.body);
      console.log(messageFromServer);
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
      <FormInput
        type="text"
        value={message}
        setValue={setMessage}
        id="message"
        label="Message"
      />
      <Button
        title="Send"
        className="btn-primary"
        type="button"
        onClick={() => {
          stompClient.publish({
            destination: "/app/test",
            body: JSON.stringify({ message: message }),
          });
        }}
      />
      <Button
        title="Logout"
        className="btn-danger mt-3"
        type="button"
        onClick={() => {
          handleLogout(navigate);
        }}
      />
    </div>
  );
}

export default App;
