import {
  Platform,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
} from "react-native";
import React, { useEffect, useState } from "react";
import { logout } from "../service/auth";
import { startup } from "../service/home";
import { Colors } from "@/constants/Colors";
import Button from "@/components/Button";

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
    <ScrollView style={styles.container}>
      <Button
        title="Logout"
        onPress={logout}
        styles={styles}
        disabled={isLoading}
        opacity={0.7}
      />
    </ScrollView>
  );
};

export default Home;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingTop: Platform.OS === "android" ? StatusBar.currentHeight : 0,
    paddingHorizontal: 10,
    backgroundColor: Colors.background,
  },
  pressable: {
    backgroundColor: Colors.mainColor,
    marginTop: 5,
    paddingHorizontal: 10,
    paddingVertical: 8,
    width: 100,
    borderRadius: 7,
  },
  pressableText: {
    fontFamily: "Poppins",
    textAlign: "center",
    textTransform: "uppercase",
    color: Colors.white,
  },
});
