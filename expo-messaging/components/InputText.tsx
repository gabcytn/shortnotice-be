import {
  StyleProp,
  StyleSheet,
  Text,
  TextInput,
  View,
  ViewStyle,
} from "react-native";
import React, { Dispatch, SetStateAction } from "react";
import { Colors } from "@/constants/Colors";
type InputProps = {
  placeholder: string;
  isSecure: boolean;
  isError?: boolean;
  errorMessage?: string;
  value: string;
  setValue: Dispatch<SetStateAction<string>>;
  styles: {
    inputContainer: StyleProp<ViewStyle>;
  };
};
const InputBox = ({
  placeholder,
  isSecure,
  isError,
  errorMessage,
  styles,
  value,
  setValue,
}: InputProps) => {
  return (
    <>
      <View
        style={[
          styles.inputContainer,
          {
            borderColor: isError ? "red" : Colors.white,
          },
        ]}
      >
        <TextInput
          placeholder={placeholder}
          placeholderTextColor={Colors.greyWhite}
          value={value}
          onChangeText={setValue}
          secureTextEntry={isSecure}
          autoCapitalize="none"
          style={localStyles.input}
        />
      </View>
      {isError ? (
        <Text style={localStyles.errorMessage}>{errorMessage}</Text>
      ) : null}
    </>
  );
};

export default InputBox;

const localStyles = StyleSheet.create({
  errorMessage: {
    textAlign: "left",
    fontFamily: "Poppins",
    fontSize: 10,
    color: "red",
    width: 250,
    marginLeft: 5,
  },
  input: {
    fontFamily: "Poppins",
    color: Colors.white,
  },
});
