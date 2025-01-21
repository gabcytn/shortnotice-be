import AsyncStorage from "@react-native-async-storage/async-storage";
import { router } from "expo-router";

const SERVER_URL = process.env.EXPO_PUBLIC_SERVER_URL;

export async function startup(
  setLoading: (v: boolean) => void,
  setConversations: (v: []) => void,
) {
  try {
    setLoading(true);
    const loggedIn = await isLoggedIn();
    if (!loggedIn) {
      await AsyncStorage.clear();
      router.replace("/auth");
      return;
    }
    await fetchCredentials();
    const conversations = await fetchConversations();
    setConversations(conversations);
  } catch (e: unknown) {
    if (e instanceof Error) console.error(e.message);
  } finally {
    setLoading(false);
  }
}

export async function isLoggedIn(): Promise<boolean> {
  const localAuthStatus = await AsyncStorage.getItem("isLoggedIn");
  const res = await fetch(`${SERVER_URL}/auth/status`, {
    method: "GET",
    credentials: "include",
  });
  const text = await res.text();
  const serverAuthStatus = text === "true";
  if (!localAuthStatus || !serverAuthStatus) {
    return false;
  }

  return true;
}

export async function fetchCredentials() {
  try {
    const res = await fetch(`${SERVER_URL}/credentials`, {
      method: "GET",
      credentials: "include",
    });

    if (!res.ok) throw new Error(`Error status code of ${res.status}`);

    const data = await res.json();
    await AsyncStorage.setItem("id", data.id);
    await AsyncStorage.setItem("username", data.username);
  } catch (e: unknown) {
    if (e instanceof Error) {
      console.error(e.message);
    }
  }
}

export async function fetchConversations() {
  try {
    const res = await fetch(`${SERVER_URL}/conversation/list`, {
      method: "GET",
      credentials: "include",
    });

    if (!res.ok) throw new Error(`Error status code of ${res.status}`);

    const data = await res.json();
    return data;
  } catch (e: unknown) {
    if (e instanceof Error) {
      console.error(e.message);
    }
  }
}
