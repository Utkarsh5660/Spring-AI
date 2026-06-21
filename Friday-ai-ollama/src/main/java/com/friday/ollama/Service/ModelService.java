package com.friday.ollama.Service;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class ModelService {
    private final ChatClient qwenClient;
    private final ChatClient codellamaCLient;
    private final ChatMemory chatMemoryQwen;
    private final ChatMemory chatMemoryCodellama;

    public ModelService(
            @Qualifier("qwenChatModel") OllamaChatModel qwenModel,
            @Qualifier("codellamaChatModel") OllamaChatModel mistralModel,
            ChatMemory chatMemoryQwen,
            ChatMemory chatMemoryCodellama
            ) {
        this.chatMemoryQwen = chatMemoryQwen;
        this.chatMemoryCodellama = chatMemoryCodellama;
        this.qwenClient = ChatClient
                .builder(qwenModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemoryQwen).build())
                .build();
        this.codellamaCLient = ChatClient
                .builder(mistralModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemoryCodellama).build())
                .build();
    }
    public String generateCode(String prompt,String conversationID){
        return qwenClient
                .prompt()
                .system("Your are a coding assistant , and all your replies should be keeping this thing in mind")
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,conversationID))
                .call()
                .content();
    }

    public String summarize(String prompt,String conversationID){
        return codellamaCLient
                .prompt()
                .system("Your are a general purpose assistant and all your answers should be keeping this point in consideration")
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,conversationID))
                .call()
                .content();
    }
    // NEW: streaming version
    public Flux<String> chatQwenStream(String conversationID,String userMessage){
        return qwenClient.prompt()
                .system("Your are a coding assistant , and all your replies should be keeping this thing in mind")
                .user(userMessage)
                .advisors(advisorSpec->advisorSpec.param(ChatMemory.CONVERSATION_ID,conversationID))
                .stream()
                .content();
    }

    public Flux<String> chatCodellamaStream(String id, String userMessage){
        return codellamaCLient.prompt()
                .system("Your are a general purpose assistant and all your answers should be keeping this point in consideration")
                .user(userMessage)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,id))
                .stream()
                .content();
    }
    public List<Message> getQwenHistoryMessage(String id){
        return chatMemoryQwen.get(id);
    }
    public List<Message> getLlamaMessages(String id){
        return chatMemoryCodellama.get(id);
    }
}
