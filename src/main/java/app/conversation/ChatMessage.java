package app.conversation;

public record ChatMessage(
        Role role,
        String content,
        String toolName // null unless role == TOOL
) {

    public static ChatMessage user(String text) {
        return new ChatMessage(Role.USER, text, null);
    }

    public static ChatMessage assistant(String text) {
        return new ChatMessage(Role.ASSISTANT, text, null);
    }
    public static ChatMessage toolResult(String toolName, String result) {
        return new ChatMessage(Role.TOOL, result, toolName);
    }
}
