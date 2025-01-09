import { Stack } from "expo-router";
import React from "react";

export default function AuthLayout() {
  return (
    <Stack
      screenOptions={{
        headerShown: false,
        statusBarTranslucent: true,
        statusBarStyle: "dark",
      }}
    >
      <Stack.Screen name="index" />
      <Stack.Screen name="register" />
    </Stack>
  );
}
