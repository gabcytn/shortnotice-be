import {
  Platform,
  StatusBar,
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableWithoutFeedback,
  Keyboard,
  Pressable,
  View,
} from "react-native";
import { Link, router } from "expo-router";
import AsyncStorage from "@react-native-async-storage/async-storage";
import React, { useEffect, useState } from "react";
import { Colors } from "@/constants/Colors";
import Button from "@/components/Button";
import InputText from "@/components/InputText";

const Register = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [isUsernameError, setIsUsernameError] = useState(false);
  const [isPasswordError, setIsPasswordError] = useState(false);
  const [isConfirmPasswordError, setIsConfirmPasswordError] = useState(false);
  useEffect(() => {
    const checkLoginStatus = async () => {
      const isLoggedIn = await AsyncStorage.getItem("isLoggedIn");
      if (isLoggedIn) {
        router.replace("/home");
      }
    };

    checkLoginStatus();
  }, []);

  useEffect(() => {
    if (username.length < 3 && username) setIsUsernameError(true);
    else setIsUsernameError(false);
  }, [username]);
  useEffect(() => {
    if ((confirmPassword != password && confirmPassword) || isPasswordError) {
      setIsConfirmPasswordError(true);
    } else {
      setIsConfirmPasswordError(false);
    }
  }, [confirmPassword]);
  useEffect(() => {
    if (password.length < 8 && password) {
      setIsPasswordError(true);
    } else {
      setIsPasswordError(false);
    }
  }, [password]);
  return (
    <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
      <SafeAreaView style={styles.container}>
        <Text style={styles.mainText}>
          Register to{" "}
          <Text style={{ color: Colors.mainColor }}>ShortNotice</Text>
        </Text>
        <Text style={styles.subTitle}>Notified in an instant</Text>
        <InputText
          placeholder="Username"
          isSecure={false}
          styles={styles}
          value={username}
          setValue={setUsername}
          isError={isUsernameError}
          errorMessage="Username too short"
        />
        <InputText
          placeholder="Password"
          isSecure={true}
          styles={styles}
          value={password}
          setValue={setPassword}
          isError={isPasswordError}
          errorMessage="Password too short"
        />
        <InputText
          placeholder="Confirm Password"
          isSecure={true}
          styles={styles}
          value={confirmPassword}
          setValue={setConfirmPassword}
          isError={isConfirmPasswordError}
          errorMessage="Passwords do not match"
        />
        <Button
          title="Create"
          styles={styles}
          opacity={0.7}
          onPress={() => {
            console.log("clicked register");
          }}
          disabled={false}
        />
        <View style={{ flexDirection: "row", marginTop: 8 }}>
          <Text style={styles.haveAnAccount}>Already have an account? </Text>
          <Pressable
            onPress={() => {
              router.back();
            }}
          >
            <Text style={styles.signIn}>Sign in.</Text>
          </Pressable>
        </View>
      </SafeAreaView>
    </TouchableWithoutFeedback>
  );
};

export default Register;

const styles = StyleSheet.create({
  container: {
    paddingTop: Platform.OS === "android" ? StatusBar.currentHeight : 0,
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: Colors.background,
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
  inputContainer: {
    borderWidth: 1,
    borderRadius: 5,
    width: 250,
    paddingHorizontal: 5,
    paddingVertical: 3,
    marginVertical: 5,
  },
  haveAnAccount: {
    fontFamily: "Poppins",
    fontSize: 12,
    color: Colors.white,
  },
  signIn: {
    fontFamily: "Poppins",
    color: Colors.mainColor,
    fontSize: 12,
  },
  pressable: {
    marginTop: 5,
    paddingHorizontal: 10,
    paddingVertical: 8,
    width: 100,
    backgroundColor: Colors.mainColor,
    borderRadius: 7,
  },
  pressableText: {
    textAlign: "center",
    fontFamily: "Poppins",
    textTransform: "uppercase",
    color: Colors.white,
  },
});
