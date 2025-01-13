import { Colors } from "@/constants/Colors";
import { Stack } from "expo-router";
import React from "react";

const HomeLayout = () => {
  return (
    <Stack
      screenOptions={{
        headerShown: false,
        statusBarBackgroundColor: Colors.background,
        statusBarStyle: "light",
      }}
    >
      <Stack.Screen name="index" />
    </Stack>
  );
};

export default HomeLayout;
