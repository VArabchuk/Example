package com.example.demo.controller;

import com.example.demo.service.EntityService;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/entity")
public class EntityController {
    private final EntityService service;


    @GetMapping
    public ResponseEntity<InputStreamResource> downloadFile() {
        service.getAll();
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream("Example.json");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDisposition(
                ContentDisposition.builder("attachment").filename("Example.json").build()
        );

        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
