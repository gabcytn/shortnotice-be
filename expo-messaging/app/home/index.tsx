import { Button, StyleSheet, Text, View } from "react-native";
import React, { useEffect, useState } from "react";
import { logout } from "../service/auth";
import { startup } from "../service/home";

const Home = () => {
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    async function effect() {
      await startup(setIsLoading);
    }

    effect();
  }, []);

  if (isLoading) return <Text>LOADING...</Text>;

  return (
    <View>
      <Text>Home</Text>
      <Button title="Logout" onPress={logout} />
    </View>
  );
};

export default Home;

const styles = StyleSheet.create({});
