package com.avendum.midsautomate.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Value("${img.path}")
    private String imgPath;

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        logger.info("Received request for image: {}", filename);
        try {
            // Sanitize filename to prevent path traversal
            String sanitizedFilename = filename.replaceAll("[^a-zA-Z0-9.-]", "_");
            Path filePath = Paths.get(imgPath, sanitizedFilename).normalize();

            logger.info("Resolved file path: {}", filePath);

            // Verify the file is within the imgPath directory
            if (!filePath.startsWith(Paths.get(imgPath).normalize())) {
                logger.warn("Invalid file path: {}", filePath);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                logger.warn("Image not found or not readable: {}", filePath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            FileSystemResource resource = new FileSystemResource(filePath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(Files.size(filePath));
            headers.setCacheControl("max-age=86400");

            logger.info("Serving image: {}", filePath);
            return ResponseEntity.ok().headers(headers).body(resource);

        } catch (IOException e) {
            logger.error("Error serving image {}: {}", filename, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}