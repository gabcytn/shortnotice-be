import {
  StyleProp,
  StyleSheet,
  Text,
  TextStyle,
  TouchableOpacity,
  ViewStyle,
} from "react-native";
import React from "react";

type ButtonProps = {
  title: string;
  onPress: () => void;
  styles: {
    pressable: StyleProp<ViewStyle>;
    pressableText: StyleProp<TextStyle>;
  };
  opacity: number;
  disabled: boolean;
};

const Button = ({ title, onPress, styles, opacity, disabled }: ButtonProps) => {
  return (
    <TouchableOpacity
      onPress={onPress}
      style={styles.pressable}
      activeOpacity={opacity}
      disabled={disabled}
    >
      <Text style={styles.pressableText}>{title}</Text>
    </TouchableOpacity>
  );
};

export default Button;

const styles = StyleSheet.create({});
