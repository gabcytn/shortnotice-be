import AsyncStorage from "@react-native-async-storage/async-storage";
import { router } from "expo-router";
import { IMessage } from "react-native-gifted-chat";

const SERVER_URL = process.env.EXPO_PUBLIC_SERVER_URL;

export async function fetchMessages(
  convoID: number,
  avatar: string,
): Promise<IMessage[] | undefined> {
  try {
    const res = await fetch(`${SERVER_URL}/message/history/${convoID}`);
    if (res.status === 403) {
      await AsyncStorage.clear();
      router.replace("/auth");
      return;
    }
    if (!res.ok) throw new Error(`Error status code of ${res.status}`);

    const data = await res.json();
    const messages: IMessage[] = [];

    data.forEach((message: any) => {
      messages.push({
        _id: message.messageId,
        text: message.message,
        createdAt: message.sentAt,
        user: {
          _id: message.sender,
          name: message.sender,
          avatar: avatar,
        },
      });
    });

    return messages;
  } catch (error: unknown) {
    if (error instanceof Error) {
      console.error("Error fetching message history");
      console.error(error.message);
    }
  }
}
