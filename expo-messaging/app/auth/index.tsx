import {
  Keyboard,
  Platform,
  StatusBar,
  StyleSheet,
  Text,
  SafeAreaView,
  TouchableWithoutFeedback,
  Pressable,
  View,
} from "react-native";
import React, { useEffect, useState } from "react";
import { Colors } from "@/constants/Colors";
import InputText from "@/components/InputText";
import Button from "@/components/Button";
import { Link, router } from "expo-router";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { login } from "../service/auth";

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const checkLoginStatus = async () => {
      const isLoggedIn = await AsyncStorage.getItem("isLoggedIn");
      if (isLoggedIn) {
        router.replace("/home");
      }
    };

    checkLoginStatus();
  }, []);

  return (
    <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
      <SafeAreaView style={styles.constainer}>
        <Text style={styles.mainText}>
          Welcome to{" "}
          <Text style={{ color: Colors.mainColor }}>ShortNotice</Text>
        </Text>
        <Text style={styles.subTitle}>Notified in an instant</Text>
        <InputText
          placeholder="Username"
          isSecure={false}
          value={username}
          setValue={setUsername}
          styles={styles}
        />
        <InputText
          placeholder="Password"
          isSecure={true}
          value={password}
          setValue={setPassword}
          styles={styles}
        />
        <Button
          title="Sign In"
          onPress={() => {
            setIsLoading(true);
            login(username, password);
            setIsLoading(false);
          }}
          styles={styles}
          opacity={0.7}
          disabled={isLoading}
        />
        <View style={{ flexDirection: "row", marginTop: 8 }}>
          <Text style={styles.noAccount}>Don't have an account? </Text>
          <Pressable>
            <Link href={"/auth/register"} style={styles.createAccount}>
              Create one.
            </Link>
          </Pressable>
        </View>
      </SafeAreaView>
    </TouchableWithoutFeedback>
  );
};

export default Login;

const styles = StyleSheet.create({
  constainer: {
    flex: 1,
    paddingTop: Platform.OS === "android" ? StatusBar.currentHeight : 0,
    paddingHorizontal: 30,
    backgroundColor: Colors.background,
    justifyContent: "center",
    alignItems: "center",
  },
  mainText: {
    fontFamily: "Poppins",
    fontSize: 21,
    color: Colors.white,
  },
  subTitle: {
    fontFamily: "Poppins",
    fontSize: 12,
    marginTop: -5,
    color: Colors.white,
  },
  noAccount: {
    fontFamily: "Poppins",
    color: Colors.white,
    fontSize: 12,
  },
  createAccount: {
    fontFamily: "Poppins",
    color: Colors.mainColor,
    fontSize: 12,
  },
  inputContainer: {
    borderWidth: 1,
    borderRadius: 5,
    width: 250,
    paddingHorizontal: 5,
    paddingVertical: 3,
    marginVertical: 5,
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
