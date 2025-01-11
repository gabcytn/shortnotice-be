import AsyncStorage from "@react-native-async-storage/async-storage";
import { router } from "expo-router";
import { Alert } from "react-native";

const SERVER_URL = process.env.EXPO_PUBLIC_SERVER_URL;

export async function login(username: string, password: string) {
  if ([username, password].includes("")) {
    Alert.alert("Incomplete fields");
    return;
  }

  const requestObject = getRequestObject(username, password);

  try {
    const res = await fetch(`${SERVER_URL}/login`, requestObject);
    if (!res.ok) throw new Error(`Error status code of ${res.status}`);
    AsyncStorage.setItem("isLoggedIn", "true");
    router.replace("/home");
  } catch (e: unknown) {
    if (e instanceof Error) {
      Alert.alert(e.message);
    }
  }
}

export async function register(
  username: string,
  password: string,
  conPass: string,
) {
  if ([username, password, conPass].includes("")) {
    Alert.alert("Incomplete fields");
    return;
  }
  if (password !== conPass) {
    Alert.alert("Passwords do not match");
    return;
  }

  const requestObject = getRequestObject(username, password);

  try {
    const res = await fetch(`${SERVER_URL}/register`, requestObject);
    if (!res.ok) throw new Error(`Error status code of ${res.status}`);
    router.replace("/auth");
  } catch (e: unknown) {
    if (e instanceof Error) {
      Alert.alert(e.message);
    }
  }
}

function getRequestObject(username: string, password: string) {
  return {
    method: "POST",
    credentials: "include" as RequestCredentials,
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      username: username,
      password: password,
    }),
  };
}

export async function logout() {
  try {
    const res = await fetch(`${SERVER_URL}/logout`, {
      method: "POST",
      credentials: "include",
    });

    if (!res.ok) throw new Error(`Error status code of ${res.status}`);
    AsyncStorage.removeItem("isLoggedIn");
    router.replace("/auth");
  } catch (e: unknown) {
    if (e instanceof Error) {
      console.error(e.message);
      console.error("Error logging out");
    }
  }
}
