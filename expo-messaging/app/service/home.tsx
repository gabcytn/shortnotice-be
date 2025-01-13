import AsyncStorage from "@react-native-async-storage/async-storage";
import { router } from "expo-router";

const SERVER_URL = process.env.EXPO_PUBLIC_SERVER_URL;

export async function startup(setLoading: (v: boolean) => void) {
  try {
    setLoading(true);
    const loggedIn = await isLoggedIn();
    if (!loggedIn) return;
    await fetchCredentials();
    await fetchConversations();
  } catch (e: unknown) {
    if (e instanceof Error) console.error(e.message);
  } finally {
    setLoading(false);
  }
}

export async function isLoggedIn(): Promise<boolean> {
  const authState = await AsyncStorage.getItem("isLoggedIn");
  if (!authState) {
    router.replace("/auth");
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
    console.log(data);
  } catch (e: unknown) {
    if (e instanceof Error) {
      console.error(e.message);
    }
  }
}
