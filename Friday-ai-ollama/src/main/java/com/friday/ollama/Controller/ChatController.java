package com.friday.ollama.Controller;

import com.friday.ollama.Service.ModelService;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

//    private ChatClient chatClient;
//
//    public ChatController(ChatClient.Builder builder){
//        chatClient=builder.build();
//    }
//
//    @GetMapping("/chat")
//    public ResponseEntity<String> chat(
//            @RequestParam(value = "q") String prompt
//    ){
//        String responseContent = this.chatClient
//                .prompt(prompt)
//                .call()
//                .content();
//        return ResponseEntity.ok(responseContent);
//    }

    @Autowired private ModelService modelService;

    @GetMapping("/chatQwen")
    public ResponseEntity chatQwen(@RequestParam(value = "id") String id,@RequestParam(value = "q") String prompt){
        return ResponseEntity.ok(modelService.generateCode(prompt,id));
    }

    @GetMapping("/chatOllama")
    public ResponseEntity chatOllama(@RequestParam(value = "id") String id,@RequestParam(value = "q") String prompt){
        return ResponseEntity.ok(modelService.summarize(prompt,id));
    }

    @GetMapping("/history/getOllamaMessage")
    public List<Message> getOllamaMessage(@RequestParam(value = "id") String id ){
        return modelService.getLlamaMessages(id);
    }

    @GetMapping("/history/getQwenMessage")
    public List<Message> getQwenMessage(@RequestParam(value = "id") String id ){
        return modelService.getQwenHistoryMessage(id);
    }
}
