export type Params = {
  convoId: string;
  convoUsername: string;
  convoAvatar: string;
};

export type IncomingMessage = {
  conversationId: number;
  sender: string;
  message: string;
  messageId: number;
  isRequest: boolean;
  sentAt: Date;
};
