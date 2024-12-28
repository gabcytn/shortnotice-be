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
      setIsLoading(false);
      navigate("/");
      return;
    }
    throw new Error(`Error status code of ${res.status}`);
  } catch (e: unknown) {
    setIsLoading(false);
    if (e instanceof Error) {
      console.error(e.message);
    }
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
