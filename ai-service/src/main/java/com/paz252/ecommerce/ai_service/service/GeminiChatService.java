package com.paz252.ecommerce.ai_service.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

// We inject the Spring AI ChatClient to seamlessly abstract the Google Gemini API communication.

@Service
@RequiredArgsConstructor
public class GeminiChatService {

    private final ChatClient chatClient;

    public String generateShoppingAdvice(String userPrompt){
        String systemInstruction = "You are a helpful e-commerce shopping assistant for the Paz252 platform. " +
                "Keep answers concise, friendly, and focused on products, orders, and shopping advice.";

        return chatClient.prompt()
                .system(systemInstruction)
                .user(userPrompt)
                .call()
                .content();
    }
}
