package com.sha.tasktrack.service;

import com.sha.tasktrack.dto.Prompt;

public interface AssistantService {
    String askAssistant(Prompt prompt);
}