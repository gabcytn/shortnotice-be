import { Button, StyleSheet, Text, View } from "react-native";
import React, { useEffect, useState } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { router } from "expo-router";

const SERVER_URL = process.env.EXPO_PUBLIC_SERVER_URL;

const Home = () => {
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    setIsLoading(true);
    const checkAuthState = async () => {
      const authState = await AsyncStorage.getItem("isLoggedIn");
      if (!authState) {
        router.replace("/auth");
      }
    };
    checkAuthState();
    setIsLoading(false);
  }, []);

  if (isLoading) return <Text>LOADING...</Text>;

  return (
    <View>
      <Text>Home</Text>
      <Button
        title="Logout"
        onPress={async () => {
          await fetch(`${SERVER_URL}/logout`, {
            method: "POST",
            credentials: "include",
          });
          AsyncStorage.removeItem("isLoggedIn");
          router.replace("/auth");
        }}
      />
    </View>
  );
};

export default Home;

const styles = StyleSheet.create({});
