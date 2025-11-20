package com.yako.ia.controller;

import com.yako.ia.dto.BlogPostRequest;
import com.yako.ia.dto.BlogPostResponse;
import com.yako.ia.service.SlugGeneratorService;
import com.yako.ia.service.GeminiService;
import com.yako.ia.service.MarkdownWriterService;
import com.yako.ia.service.CarlosAiTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/posts")
public class BlogPostController {
    private final SlugGeneratorService slugGeneratorService;
    private final GeminiService geminiService;
    private final MarkdownWriterService markdownWriterService;
    private final CarlosAiTool carlosAiTool;

    public BlogPostController(SlugGeneratorService slugGeneratorService,
                              GeminiService geminiService,
                              MarkdownWriterService markdownWriterService,
                              CarlosAiTool carlosAiTool) {
        this.slugGeneratorService = slugGeneratorService;
        this.geminiService = geminiService;
        this.markdownWriterService = markdownWriterService;
        this.carlosAiTool = carlosAiTool;
    }
    @PostMapping("/javi")
    public ResponseEntity<?> generateBlogPostWithWikipedia(@RequestParam String title, @RequestParam String topic) {
        if (title == null || title.trim().isEmpty()) {
            log.warn("Blog post generation failed: title is missing");
            return ResponseEntity.badRequest().body("Title is required");
        }
        if (topic == null || topic.trim().isEmpty()) {
            log.warn("Blog post generation failed: topic is missing");
            return ResponseEntity.badRequest().body("Topic is required");
        }
        String slug = slugGeneratorService.generateSlug(title);
        String wikipediaSummary = carlosAiTool.fetchWikipediaSummary(topic);
        String promptText = "Escribe un post de blog en español sobre: '" + title + "'. El slug es '" + slug + "'. El texto debe ser informativo y bien redactado. Usa este contexto de Wikipedia: " + wikipediaSummary;
        try {
            String content = geminiService.generateContent(title, slug, promptText);
            String filePath = markdownWriterService.writeMarkdown(title, slug, content, "posts/content");
            BlogPostResponse response = new BlogPostResponse(title, slug, filePath, content.length() > 200 ? content.substring(0, 200) : content);
            log.info("Blog post generated: title={}, slug={}, filePath={}", title, slug, filePath);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error generating blog post");
        }
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BlogPostController.class);

    @PostMapping("/content")
    public ResponseEntity<?> generateBlogPostWithContext(@RequestParam String title, @RequestParam String ricardo) {
        if (title == null || title.trim().isEmpty()) {
            log.warn("Blog post generation failed: title is missing");
            return ResponseEntity.badRequest().body("Title is required");
        }
        String slug = slugGeneratorService.generateSlug(title);
        String[] palabras = ricardo.split("\\s+");
        try {
            String content = geminiService.generateContentWithContext(title, slug, palabras);
            String filePath = markdownWriterService.writeMarkdown(title, slug, content, "posts/content");
            BlogPostResponse response = new BlogPostResponse(title, slug, filePath, content.length() > 200 ? content.substring(0, 200) : content);
            log.info("Blog post generated: title={}, slug={}, filePath={}", title, slug, filePath);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error generating blog post");
        }
    }
}
