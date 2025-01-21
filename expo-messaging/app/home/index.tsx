import {
  Keyboard,
  Platform,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  TouchableWithoutFeedback,
  View,
} from "react-native";
import React, { useEffect, useState } from "react";
import { logout } from "../service/auth";
import { startup } from "../service/home";
import { Colors } from "@/constants/Colors";
import Button from "@/components/Button";
import InputBox from "@/components/InputText";
import { Conversation } from "../types/home";

const Home = () => {
  const [isLoading, setIsLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [conversations, setConversations] = useState<Conversation[]>([]);

  useEffect(() => {
    async function effect() {
      await startup(setIsLoading, setConversations);
    }
    effect();
  }, []);

  if (isLoading) return <Text>LOADING...</Text>;

  return (
    <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
      <ScrollView style={styles.container}>
        <InputBox
          placeholder="Search"
          isSecure={false}
          value={search}
          setValue={setSearch}
          styles={styles}
        />
        {conversations.map((conversation) => {
          return (
            <View key={conversation.id}>
              <Text>{conversation.senderUsername}</Text>
              <Text>{conversation.message}</Text>
              <Text>{conversation.sentAt}</Text>
            </View>
          );
        })}
        <Button
          title="Logout"
          onPress={logout}
          styles={styles}
          disabled={isLoading}
          opacity={0.7}
        />
      </ScrollView>
    </TouchableWithoutFeedback>
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
  inputContainer: {
    borderWidth: 1,
    borderRadius: 5,
    width: "100%",
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
