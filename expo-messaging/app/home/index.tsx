import { Button, StyleSheet, Text, View } from "react-native";
import React, { useEffect, useState } from "react";
import { logout } from "../service/auth";
import { isLoggedIn, startup } from "../service/home";

const Home = () => {
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setIsLoading(true);

    const effect = async () => {
      const loggedIn = await isLoggedIn();
      if (!loggedIn) return;
      startup();
    };

    effect();
    setIsLoading(false);
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
