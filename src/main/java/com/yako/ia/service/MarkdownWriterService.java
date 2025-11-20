package com.yako.ia.service;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class MarkdownWriterService {
    public String writeMarkdown(String title, String slug, String content, String storagePath) throws IOException {
        String fileName = slug + ".md";
        File dir = new File(storagePath);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("# " + title + "\n\n");
            writer.write("Slug: " + slug + "\n\n");
            writer.write(content);
        }
        return file.getAbsolutePath();
    }
}
