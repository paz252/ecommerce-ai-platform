package com.paz252.ecommerce.ai_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paz252.ecommerce.ai_service.dto.ChatRequest;
import com.paz252.ecommerce.ai_service.dto.ChatResponse;
import com.paz252.ecommerce.ai_service.service.GeminiChatService;

import lombok.RequiredArgsConstructor;

// Expose the chatbot capabilities to the ecosystem.

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class AiChatController {

    private final GeminiChatService chatService;

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> askAssistant(@RequestBody ChatRequest request){
        String reply = chatService.generateShoppingAdvice(request.prompt());
        return ResponseEntity.ok(new ChatResponse(reply));
    }
}
