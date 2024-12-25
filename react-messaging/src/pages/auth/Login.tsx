import { FormEvent, useState } from "react";
import { useNavigate } from "react-router";
import { handleLoginSubmit } from "./functions";

function Login() {
  const navigate = useNavigate();
  const handleSubmit = async (e: FormEvent) => {
    handleLoginSubmit(e, username, password, navigate);
  };
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  return (
    <div className="container">
      <div className="row">
        <div className="col-12">
          <h3 className="text-center">Login Page</h3>
          <form onSubmit={handleSubmit}>
            <div className="form">
              <label htmlFor="" className="form-label">
                Username
              </label>
              <input
                type="text"
                className="form-control"
                value={username}
                onChange={(e) => {
                  setUsername(e.target.value);
                }}
              />
            </div>
            <div className="form">
              <label htmlFor="" className="form-label">
                Password
              </label>
              <input
                type="password"
                className="form-control"
                value={password}
                onChange={(e) => {
                  setPassword(e.target.value);
                }}
              />
            </div>
            <button type="submit" className="btn btn-primary mt-3">
              Submit
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Login;
