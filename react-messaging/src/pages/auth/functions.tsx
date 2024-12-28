import { FormEvent } from "react";

const SERVER_URL = import.meta.env.VITE_SERVER_URL;

export async function handleLoginSubmit(
  e: FormEvent,
  username: string,
  password: string,
  setIsLoading: (val: boolean) => void,
  navigate: (path: string) => void,
) {
  e.preventDefault();
  setIsLoading(true);
  try {
    const res = await fetch(`${SERVER_URL}/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify({
        username: username,
        password: password,
      }),
    });
    if (res.status === 200) {
      sessionStorage.setItem("isLoggedIn", "true");
      navigate("/");
      return;
    }
    throw new Error(`Error status code of ${res.status}`);
  } catch (e: unknown) {
    if (e instanceof Error) {
      console.error(e.message);
    }
  } finally {
    setIsLoading(false);
  }
}

export async function handleLogout(navigate: (path: string) => void) {
  const res = await fetch(`${SERVER_URL}/logout`, {
    method: "POST",
    credentials: "include",
  });
  if (res.status === 200) {
    sessionStorage.clear();
    navigate("/login");
  }
}

export async function handleRegisterSubmit(
  e: FormEvent,
  username: string,
  password: string,
  confirmPassword: string,
  setIsLoading: (val: boolean) => void,
  navigate: (path: string) => void,
) {
  e.preventDefault();
  if (password !== confirmPassword) {
    alert("Passwords do not match!");
    return;
  }

  try {
    setIsLoading(true);
    const res = await fetch(`${SERVER_URL}/register`, {
      method: "POST",
      credentials: "include",
      body: JSON.stringify({
        username: username,
        password: password,
      }),
    });

    if (!res.ok) throw new Error(`Error status code of ${res.status}`);

    navigate("/login");
  } catch (e) {
    if (e instanceof Error) {
      console.error("Error registering user");
      console.error(e.message);
    }
  } finally {
    setIsLoading(false);
  }
}
