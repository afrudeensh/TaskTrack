package com.sha.tasktrack.controller;

import com.sha.tasktrack.dto.Prompt;
import com.sha.tasktrack.service.AssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assist")
public class AssistantController {

    @Autowired
    private AssistantService assistantService;

    @PostMapping("/public/ask")
    public ResponseEntity<String> askAssistant(@RequestBody Prompt prompt) {
        String response = assistantService.askAssistant(prompt);
        return ResponseEntity.ok(response);
    }
}

