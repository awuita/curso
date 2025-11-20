package com.yako.ia.controller;

import com.yako.ia.dto.BlogPostRequest;
import com.yako.ia.dto.BlogPostResponse;
import com.yako.ia.service.SlugGeneratorService;
import com.yako.ia.service.GeminiService;
import com.yako.ia.service.MarkdownWriterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class BlogPostController {
    private final SlugGeneratorService slugGeneratorService;
    private final GeminiService geminiService;
    private final MarkdownWriterService markdownWriterService;
    @Value("${blogpost.storage.path:posts}")
    private String storagePath;

    public BlogPostController(SlugGeneratorService slugGeneratorService,
                              GeminiService geminiService,
                              MarkdownWriterService markdownWriterService) {
        this.slugGeneratorService = slugGeneratorService;
        this.geminiService = geminiService;
        this.markdownWriterService = markdownWriterService;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BlogPostController.class);

    @PostMapping
    public ResponseEntity<?> generateBlogPost(@RequestBody BlogPostRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            log.warn("Blog post generation failed: title is missing");
            return ResponseEntity.badRequest().body("Title is required");
        }
        String slug = slugGeneratorService.generateSlug(request.getTitle());
        String content;
        try {
            content = geminiService.generateContent(request.getTitle(), slug);
        } catch (Exception e) {
            log.error("Gemini generation failed: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Gemini generation failed");
        }
        String filePath;
        try {
            filePath = markdownWriterService.writeMarkdown(request.getTitle(), slug, content, storagePath);
        } catch (Exception e) {
            log.error("File write failed: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("File write failed");
        }
        String summary = content.length() > 200 ? content.substring(0, 200) : content;
        BlogPostResponse response = new BlogPostResponse(request.getTitle(), slug, filePath, summary);
        log.info("Blog post generated: title={}, slug={}, filePath={}", request.getTitle(), slug, filePath);
        return ResponseEntity.ok(response);
    }
}
