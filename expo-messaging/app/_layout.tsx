import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { useFonts } from "expo-font";
import { SplashScreen, Stack } from "expo-router";
import { useEffect } from "react";

SplashScreen.preventAutoHideAsync();
const queryClient = new QueryClient();

export default function RootLayout() {
  const [loaded] = useFonts({
    Poppins: require("@/assets/fonts/Poppins.ttf"),
    PoppinsBold: require("@/assets/fonts/Poppins-SemiBold.ttf"),
  });

  useEffect(() => {
    if (loaded) SplashScreen.hideAsync();
  }, [loaded]);

  if (!loaded) return null;
  return (
    <QueryClientProvider client={queryClient}>
      <Stack
        screenOptions={{
          statusBarTranslucent: true,
          headerShown: false,
          statusBarStyle: "dark",
        }}
      >
        <Stack.Screen name="index" />
        <Stack.Screen name="auth" />
        <Stack.Screen name="home" />
      </Stack>
    </QueryClientProvider>
  );
}
