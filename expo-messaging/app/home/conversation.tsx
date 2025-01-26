import { Text, Alert, Platform, StatusBar, StyleSheet } from "react-native";
import React, { useCallback, useEffect, useState } from "react";
import { Colors } from "@/constants/Colors";
import { GiftedChat, IMessage } from "react-native-gifted-chat";
import { useLocalSearchParams } from "expo-router";
import { fetchMessages } from "../service/conversation";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { Params } from "../types/conversation";

const Conversation = () => {
  const [messages, setMessages] = useState<IMessage[]>([]);
  const [userId, setUserId] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const params = useLocalSearchParams<Params>();
  useEffect(() => {
    const fetchPreviousMessages = async () => {
      setIsLoading(true);
      const messages = await fetchMessages(parseInt(params.id), params.avatar);

      if (!messages) {
        Alert.alert("NO MESSAGES / ERROR");
        return;
      }

      setMessages(messages);
      const id = await AsyncStorage.getItem("username");
      setUserId(id);
      setIsLoading(false);
    };

    fetchPreviousMessages();
  }, []);
  const onSend = useCallback((messages: IMessage[] = []) => {
    setMessages((previousMessages) =>
      GiftedChat.append(previousMessages, messages),
    );
  }, []);

  return isLoading || !userId ? (
    <Text>LOADING...</Text>
  ) : (
    <GiftedChat
      messages={messages}
      onSend={(messages) => onSend(messages)}
      user={{ _id: userId }}
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
