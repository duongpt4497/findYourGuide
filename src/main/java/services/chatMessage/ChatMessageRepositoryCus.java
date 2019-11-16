package services.chatMessage;

import entities.ChatMessage;

import java.util.List;

public interface ChatMessageRepositoryCus {
    public void save(ChatMessage chatMessage);
    public List<ChatMessage> get(String user, String receiver, int firstElement, int lastElement);
}
