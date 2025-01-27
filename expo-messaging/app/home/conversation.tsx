import { Text, Alert, Platform, StatusBar, StyleSheet } from "react-native";
import React, { useCallback, useEffect, useState } from "react";
import { Colors } from "@/constants/Colors";
import { GiftedChat, IMessage } from "react-native-gifted-chat";
import { useLocalSearchParams } from "expo-router";
import { fetchMessages } from "../service/conversation";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { Params } from "../types/conversation";
import { client } from "./index";

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

      setUsername(username);
      setIsLoading(false);
    };

    fetchPreviousMessages();
  }, []);

  const onSend = (messages: IMessage[] = []) => {
    if (!client || !client.connected) {
      console.error("Stomp client is not connected");
      return;
    }
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
