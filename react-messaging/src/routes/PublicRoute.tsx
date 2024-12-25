import { Navigate } from "react-router";
import { Outlet } from "react-router";

function PublicRoute() {
  const isLoggedIn = sessionStorage.getItem("isLoggedIn");

  if (!isLoggedIn) return <Outlet />;
  else return <Navigate to={"/"} replace={true} />;
}

export default PublicRoute;
