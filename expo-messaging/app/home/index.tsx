import {
  Image,
  Keyboard,
  Platform,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  TouchableOpacity,
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
import { Link, router } from "expo-router";

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
        {conversations?.map((conversation) => {
          return (
            <Link
              key={conversation.id}
              style={styles.conversation}
              href={{
                pathname: "./conversation",
                params: {
                  id: conversation.id,
                  avatar: conversation.avatar,
                  username: conversation.senderUsername,
                },
              }}
              push={true}
              relativeToDirectory={true}
            >
              <Image
                source={{ uri: conversation.avatar }}
                style={styles.avatar}
              />
              <View>
                <Text style={styles.text}>{conversation.senderUsername}</Text>
                <Text style={styles.text}>{conversation.message}</Text>
                <Text style={styles.text}>{conversation.sentAt}</Text>
              </View>
            </Link>
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
  text: {
    fontFamily: "Poppins",
    fontSize: 12,
    color: Colors.white,
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
  avatar: {
    width: 75,
    height: 75,
    borderRadius: 100,
  },
  conversation: {
    paddingHorizontal: 5,
    paddingVertical: 8,
    flexDirection: "row",
    gap: 10,
  },
});
