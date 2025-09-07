package com.sha.tasktrack;

import com.sha.tasktrack.dto.Prompt;
import com.sha.tasktrack.service.AssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TasktrackApplication implements CommandLineRunner {
	@Autowired
	private AssistantService assistantService;
	public static void main(String[] args) {
		SpringApplication.run(TasktrackApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Starting Bedrock test...");

		// Create a test prompt
		Prompt prompt = new Prompt("Say hello in a friendly way.");

		// Call your AssistantService
		String response = assistantService.askAssistant(prompt);
		System.out.println("Response from AI: " + response);
	}
}
