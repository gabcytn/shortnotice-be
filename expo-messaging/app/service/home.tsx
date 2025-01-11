import AsyncStorage from "@react-native-async-storage/async-storage";
import { router } from "expo-router";
export async function checkAuthState(setIsLoading: (v: boolean) => void) {
  setIsLoading(true);
  const authState = await AsyncStorage.getItem("isLoggedIn");
  if (!authState) {
    router.replace("/auth");
  }
  setIsLoading(false);
}
