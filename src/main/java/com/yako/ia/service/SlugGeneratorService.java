package com.yako.ia.service;

import org.springframework.stereotype.Service;

@Service
public class SlugGeneratorService {
    public String generateSlug(String title) {
        return title.toLowerCase()
            .replaceAll("[^a-z0-9\s]", "")
            .replaceAll("\s+", "-")
            .replaceAll("-+", "-");
    }
}
