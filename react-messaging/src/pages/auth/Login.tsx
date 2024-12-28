import { FormEvent, useState } from "react";
import { useNavigate } from "react-router";
import { handleLoginSubmit } from "./functions";
import Button from "../../components/Button";
import FormInput from "../../components/FormInput";

function Login() {
  const navigate = useNavigate();
  const handleSubmit = async (e: FormEvent) => {
    handleLoginSubmit(e, username, password, setIsLoading, navigate);
  };
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState<boolean>(false);
  return (
    <div className="container">
      <div className="row">
        <div className="col-12">
          <h3 className="text-center">Login Page</h3>
          <form onSubmit={handleSubmit}>
            <FormInput
              label="Username"
              id="username"
              type="text"
              value={username}
              setValue={setUsername}
              disabled={isLoading}
            />
            <FormInput
              label="Password"
              id="password"
              type="password"
              value={password}
              setValue={setPassword}
              disabled={isLoading}
            />
            <Button
              title="Submit"
              type="submit"
              className="btn-primary mt-3"
              disabled={isLoading}
            />
          </form>
        </div>
      </div>
    </div>
  );
}

export default Login;
