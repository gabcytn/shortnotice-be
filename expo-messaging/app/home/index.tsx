import {
  Alert,
  FlatList,
  Image,
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
import { Link } from "expo-router";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { IncomingMessage } from "../types/conversation";

const SOCKET_URL = `${process.env.EXPO_PUBLIC_SERVER_URL}/short-notice`;

export const client = new Client({
  webSocketFactory: () => new SockJS(SOCKET_URL),
  debug: (str) => {
    console.log("STOMP Debug:", str);
  },
});

const Home = () => {
  const [isLoading, setIsLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [conversations, setConversations] = useState<Conversation[]>([]);

  useEffect(() => {
    async function effect() {
      await startup(setIsLoading, setConversations);
      const username = await AsyncStorage.getItem("username");
      client.onConnect = () => {
        client.subscribe(`/topic/private/${username}`, (message) => {
          const socketResponse = JSON.parse(message.body);
          const body = socketResponse.body as IncomingMessage;
          console.log(body.message);
          Alert.alert("MESSAGE RECEIVED: ", body.message);
        });
      };
      client.activate();
    }
    effect();
    return () => {
      client.deactivate();
    };
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
        <FlatList
          data={conversations}
          renderItem={(conversation) => (
            <Link
              style={styles.conversation}
              href={{
                pathname: "./conversation",
                params: {
                  convoId: conversation.item.id,
                  convoAvatar: conversation.item.avatar,
                  convoUsername: conversation.item.senderUsername,
                },
              }}
              push={true}
              relativeToDirectory={true}
            >
              <Image
                source={{ uri: conversation.item.avatar }}
                style={styles.avatar}
              />
              <View>
                <Text style={styles.text}>
                  {conversation.item.senderUsername}
                </Text>
                <Text style={styles.text}>{conversation.item.message}</Text>
                <Text style={styles.text}>{conversation.item.sentAt}</Text>
              </View>
            </Link>
          )}
          keyExtractor={(conversation) => conversation.id.toString()}
        />
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
