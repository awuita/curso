package com.yako.ia.service;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {
    private final VertexAiGeminiChatModel chatModel;

    public GeminiService(VertexAiGeminiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String generateContent(String title, String slug) {
        String promptText = "Escribe un post de blog en español sobre: '" + title + "'. El slug es '" + slug + "'. El texto debe ser informativo y bien redactado.";
        Prompt prompt = new Prompt(promptText);
        try {
            var result = chatModel.call(prompt).getResult();
            var output = result.getOutput();
            if (output != null) {
                try {
                    var method = output.getClass().getMethod("getContent");
                    Object content = method.invoke(output);
                    if (content != null) {
                        return content.toString();
                    }
                } catch (Exception e) {
                    // Si no existe getContent(), usar toString()
                    return output.toString();
                }
            }
            return "Gemini no devolvió contenido.";
        } catch (Exception e) {
            System.err.println("Error al generar contenido con Gemini: " + e.getMessage());
            return "Gemini generation failed";
        }
    }
}
