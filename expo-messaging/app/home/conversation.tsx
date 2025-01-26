import { Alert, Platform, StatusBar, StyleSheet } from "react-native";
import React, { useCallback, useEffect, useState } from "react";
import { Colors } from "@/constants/Colors";
import { GiftedChat, IMessage } from "react-native-gifted-chat";
import { useLocalSearchParams } from "expo-router";
import { fetchMessages } from "../service/conversation";

type Params = {
  id: string;
  username: string;
  avatar: string;
};

const Conversation = () => {
  const [messages, setMessages] = useState<IMessage[]>([]);
  const params = useLocalSearchParams<Params>();
  useEffect(() => {
    const fetchPreviousMessages = async () => {
      const messages = await fetchMessages(parseInt(params.id), params.avatar);
      if (!messages) {
        Alert.alert("NO MESSAGES / ERROR");
        return;
      }

      setMessages(messages);
    };

    fetchPreviousMessages();
  }, []);
  const onSend = useCallback((messages: IMessage[] = []) => {
    setMessages((previousMessages) =>
      GiftedChat.append(previousMessages, messages),
    );
  }, []);
  return (
    <GiftedChat
      messages={messages}
      onSend={(messages) => onSend(messages)}
      user={{ _id: "user1" }}
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
