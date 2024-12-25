import { Navigate } from "react-router";
import { Outlet } from "react-router";

function PrivateRoute() {
  const isLoggedIn = sessionStorage.getItem("isLoggedIn");
  if (isLoggedIn) return <Outlet />;
  return <Navigate to={"/login"} replace={true} />;
}

export default PrivateRoute;
