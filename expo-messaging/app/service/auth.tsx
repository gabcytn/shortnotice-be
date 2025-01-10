import AsyncStorage from "@react-native-async-storage/async-storage";
import { router } from "expo-router";
import { Alert } from "react-native";

const SERVER_URL = process.env.EXPO_PUBLIC_SERVER_URL;

export async function login(username: string, password: string) {
  if ([username, password].includes("")) {
    Alert.alert("Incomplete fields");
    return;
  }

  const requestObject = {
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
