import { Text, Alert, Platform, StatusBar, StyleSheet } from "react-native";
import React, { useCallback, useEffect, useState } from "react";
import { Colors } from "@/constants/Colors";
import { GiftedChat, IMessage } from "react-native-gifted-chat";
import { useLocalSearchParams } from "expo-router";
import { fetchMessages } from "../service/conversation";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { IncomingMessage, Params } from "../types/conversation";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const SOCKET_URL = "http://192.168.68.106:8080/short-notice"; // Replace with your server URL

const client = new Client({
  webSocketFactory: () => new SockJS(SOCKET_URL),
  debug: (str) => {
    console.log("STOMP Debug:", str);
  },
});

const Conversation = () => {
  const [messages, setMessages] = useState<IMessage[]>([]);
  const [username, setUsername] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const params = useLocalSearchParams<Params>();

  useEffect(() => {
    const fetchPreviousMessages = async () => {
      setIsLoading(true);
      const messages = await fetchMessages(
        parseInt(params.convoId),
        params.convoAvatar,
      );

      if (!messages) {
        Alert.alert("NO MESSAGES / ERROR");
        return;
      }

      setMessages(messages);
      const id = await AsyncStorage.getItem("id");
      const username = await AsyncStorage.getItem("username");

      client.onConnect = () => {
        client.subscribe(`/topic/private/${username}`, (message) => {
          const socketResponse = message.body.body;
          console.log(socketResponse);
          Alert.alert("MESSAGE RECEIVED: ", message.body);
          console.log(message);
        });
      };

      client.activate();

      setUsername(username);
      setIsLoading(false);
    };

    fetchPreviousMessages();
    return () => {
      client.deactivate();
    };
  }, []);

  const onSend = (messages: IMessage[] = []) => {
    if (client && client.connected) {
      client.publish({
        destination: `/app/private/${params.convoId}`,
        body: JSON.stringify({
          content: messages[0].text,
          recipient: params.convoUsername,
        }),
      });
      setMessages((previousMessages) =>
        GiftedChat.append(previousMessages, messages),
      );
    } else {
      console.error("Stomp client is not connected");
    }
  };

  return isLoading || !username ? (
    <Text>LOADING...</Text>
  ) : (
    <GiftedChat
      messages={messages}
      onSend={(messages) => onSend(messages)}
      user={{ _id: username }}
    />
  );
};

export default Conversation;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingTop: Platform.OS === "android" ? StatusBar.currentHeight : 0,
    paddingHorizontal: 10,
    backgroundColor: Colors.background,
  },
});
