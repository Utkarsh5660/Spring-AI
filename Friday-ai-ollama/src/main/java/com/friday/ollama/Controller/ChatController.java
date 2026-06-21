package com.friday.ollama.Controller;

import com.friday.ollama.Service.ModelService;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/chat")
public class ChatController {
    @Autowired private ModelService modelService;

    @GetMapping("/chatQwen")
    public ResponseEntity chatQwen(@RequestParam(value = "id") String id,@RequestParam(value = "prompt") String prompt){
        return ResponseEntity.ok(modelService.generateCode(prompt,id));
    }

    @GetMapping("/chatOllama")
    public ResponseEntity chatOllama(@RequestParam(value = "id") String id,@RequestParam(value = "prompt") String prompt){
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

    @GetMapping(value = "/stream/qwen", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamQwen(@RequestParam(value = "id") String id, @RequestParam(value = "prompt") String prompt){
        return modelService.chatQwenStream(id,prompt);
    }

    @GetMapping(value = "/stream/ollama", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamOllama(@RequestParam(value = "id") String id, @RequestParam(value = "prompt") String prompt){
        return modelService.chatCodellamaStream(id,prompt);
    }
}
