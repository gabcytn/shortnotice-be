import {
  Platform,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
} from "react-native";
import React from "react";
import { Colors } from "@/constants/Colors";

const Conversation = () => {
  return (
    <ScrollView style={styles.container}>
      <Text>Conversation</Text>
    </ScrollView>
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
