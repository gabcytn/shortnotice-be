import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App.tsx";

function Main() {
  return (
    <StrictMode>
      <App />
    </StrictMode>
  );
}

export default Main;

createRoot(document.getElementById("root")!).render(<Main />);
