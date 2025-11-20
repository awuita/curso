package com.yako.ia.service;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class BlogPostTool {
    private final SlugGeneratorService slugGeneratorService;
    private final GeminiService geminiService;
    private final MarkdownWriterService markdownWriterService;

    @Autowired
    public BlogPostTool(SlugGeneratorService slugGeneratorService,
                        GeminiService geminiService,
                        MarkdownWriterService markdownWriterService) {
        this.slugGeneratorService = slugGeneratorService;
        this.geminiService = geminiService;
        this.markdownWriterService = markdownWriterService;
    }

    /**
     * Genera un post de blog usando Gemini y guarda el contenido en posts/content/{slug}.md
     * @param title Título del post
     * @param ricardo Cadena de texto para contexto
     * @return Ruta del archivo generado
     */
    public String generateAndSaveBlogPost(String title, String ricardo) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        String slug = slugGeneratorService.generateSlug(title);
        String[] palabras = ricardo.split("\\s+");
        String content = geminiService.generateContentWithContext(title, slug, palabras);
        try {
            return markdownWriterService.writeMarkdown(title, slug, content, "posts/content");
        } catch (Exception e) {
            throw new RuntimeException("Error writing markdown file: " + e.getMessage(), e);
        }
    }
}
