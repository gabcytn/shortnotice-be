export type Params = {
  id: string;
  username: string;
  avatar: string;
};

export type IncomingMessage = {
  conversationId: number;
  sender: string;
  message: string;
  messageId: number;
  isRequest: boolean;
  sentAt: Date;
};
