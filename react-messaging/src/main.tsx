import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App.tsx";
import { BrowserRouter, Route } from "react-router";
import { Routes } from "react-router";
import PrivateRoute from "./routes/PrivateRoute.tsx";
import PublicRoute from "./routes/PublicRoute.tsx";
import Login from "./pages/auth/Login.tsx";
import Register from "./pages/auth/Register.tsx";
import "bootstrap/dist/css/bootstrap.min.css";

function Main() {
  return (
    <StrictMode>
      <BrowserRouter>
        <Routes>
          <Route element={<PrivateRoute />}>
            <Route path="/" element={<App />} />
          </Route>
          <Route element={<PublicRoute />}>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </StrictMode>
  );
}

export default Main;

createRoot(document.getElementById("root")!).render(<Main />);
